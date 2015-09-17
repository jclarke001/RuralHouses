package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import businessLogic.FacadeInterface;

import domain.Customer;
import domain.User;
import exceptions.CorruptedCache;
import exceptions.CorruptedDatabase;
import exceptions.InvalidPassword;
import exceptions.InvalidToken;
import exceptions.NoDigestAlgorithm;
import exceptions.RemoteDesyncException;

public class CustomerMenu extends JFrame {

	private static final long serialVersionUID = -3211037229280698267L;
	private JPanel contentPane;

	private List<Byte> token;
	private FacadeInterface businessLogic;
	private Customer customer;

	/**
	 * Create the frame.
	 */
	public CustomerMenu(List<Byte> tkn) {
		this.token = tkn;
		this.businessLogic = LoginGUI.getBusinessLogic();

		User user;
		try {
			user = businessLogic.getUserByToken(token);
			if (!(user instanceof Customer))
				throw new InvalidToken();
			this.customer = (Customer) user;
		} catch (RemoteException | InvalidToken e2) {
			ExceptionHandler.handle(e2);
		}

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					LoginGUI.getBusinessLogic().logout(token);
				} catch (RemoteException e1) {
					ExceptionHandler.handle(e1);
				}
			}
		});

		setTitle("Customer: " + customer.getName());
		setResizable(false);
		setBounds(100, 100, 450, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(5, 1, 0, 0));

		JButton btnQueryAvailability = new JButton("Query Availability");
		btnQueryAvailability.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new QueryAvailabilityGUI(token);
				frame.setVisible(true);
			}
		});
		contentPane.add(btnQueryAvailability);

		JButton btnCancelBooking = new JButton("Manage bookings");
		btnCancelBooking.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFrame frame = new ManageBookingsGUI(token);
				frame.setVisible(true);
			}
		});
		contentPane.add(btnCancelBooking);

		JButton btnSearchHouse = new JButton("Search house");
		btnSearchHouse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFrame frame = new SearchHouseGUI(token);
				frame.setVisible(true);
			}
		});
		contentPane.add(btnSearchHouse);

		JButton btnEditProfile = new JButton("Edit profile");
		btnEditProfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFrame frame = new EditProfileCustomer(token);
				frame.setVisible(true);
			}
		});
		contentPane.add(btnEditProfile);

		final JFrame reference = this;
		JButton btnRemoveAccount = new JButton("Remove account");
		btnRemoveAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JPanel panel = new JPanel();
				JLabel label = new JLabel(
						"Enter the password to remove the account:");
				JPasswordField pass = new JPasswordField(10);
				panel.add(label);
				panel.add(pass);
				String[] options = new String[] { "OK", "Cancel" };
				int option = JOptionPane.showOptionDialog(null, panel,
						"Remove account", JOptionPane.NO_OPTION,
						JOptionPane.PLAIN_MESSAGE, null, options, options[1]);
				if (option == 0) {
					String password = new String(pass.getPassword());
					try {
						LoginGUI.getBusinessLogic().removeCustomer(token,
								password);
						reference.dispatchEvent(new WindowEvent(reference,
								WindowEvent.WINDOW_CLOSING));
					} catch (RemoteException | RemoteDesyncException
							| InvalidToken | InvalidPassword
							| NoDigestAlgorithm | CorruptedDatabase
							| CorruptedCache e) {
						ExceptionHandler.handle(e);
					}
				}
			}
		});
		contentPane.add(btnRemoveAccount);
	}
}
