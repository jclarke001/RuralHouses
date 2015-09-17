package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import businessLogic.FacadeInterface;
import domain.Offer;
import domain.Owner;
import domain.User;
import exceptions.InvalidToken;
import exceptions.RemoteDesyncException;

public class OwnerMenu extends JFrame {

	private static final long serialVersionUID = -7529983209802403455L;
	private JPanel contentPane;

    private List<Byte> token;
    private FacadeInterface businessLogic;
    private Owner owner;

    public OwnerMenu(List<Byte> tkn) throws RemoteException, InvalidToken {
    	this.token = tkn;
    	this.businessLogic = LoginGUI.getBusinessLogic();

    	User user;
		try {
			user = businessLogic.getUserByToken(token);
			if (!(user instanceof Owner))
	    		throw new InvalidToken();
	        this.owner = (Owner) user;
		} catch (RemoteException | InvalidToken e2) {
			logout();
			throw e2;
		}

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
            	logout();
            }

            public void windowOpened(WindowEvent e) {
            	removeOutDatedOffers();
            	notifyBankAccount();
            }
        });

        setTitle("Owner: " + owner.getName());
        setResizable(false);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new GridLayout(2, 1, 0, 0));

        JButton btnManageOffers = new JButton("Manage offers and bookings");
        btnManageOffers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new ManageOffersGUI(token);
                frame.setVisible(true);
            }
        });
        contentPane.add(btnManageOffers);

        JButton btnEditProfile = new JButton("Edit profile");
        btnEditProfile.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		JFrame frame = new EditProfileOwner(token);
        		frame.setVisible(true);
        	}
        });
        contentPane.add(btnEditProfile);
    }


    private void removeOutDatedOffers() {
    	Date now = new Date(System.currentTimeMillis());
    	FacadeInterface facade = LoginGUI.getBusinessLogic();
    	try {
			List<Offer> offers = facade.getOutDatedOffers(owner, now);
			if (!offers.isEmpty()) {
				String msg = "The next offers have expired:\n\n";
				for (Offer o : offers)
					msg += o.toString() + "\n";
				msg += "\nDo you whant to remove them automatically?";
				if (askConfirmation(msg)) {
					facade.removeOutDatedOffers(token, now);
				}
			}
		} catch (InvalidToken | RemoteException | RemoteDesyncException e) {
			ExceptionHandler.handle(e);
		}

    }

    private void notifyBankAccount() {
    	if (!owner.hasBankAccount() || owner.getBankAccount().equals("")) {
    		String msg = "You don't have a bank account set up so no one will be able to book "
    			+ "your offers. Make sure to set it up before setting up any offer.\n\n"
    			+ "You can do this by going into Edit profile in the main owner menu.";
			JOptionPane.showMessageDialog(null, msg, "Bank account missing",
					JOptionPane.WARNING_MESSAGE);
    	}
    }

	private boolean askConfirmation(String msg) {
		String[] messages = { "Yes", "No" };
		int result = JOptionPane.showOptionDialog(null, msg, "Delete",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				messages, 0);
		return (result == 0);
    }

	private void logout() {
        try {
            LoginGUI.getBusinessLogic().logout(token);
        } catch (RemoteException e1) {
            ExceptionHandler.handle(e1);
        }
	}
}
