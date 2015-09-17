package gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Vector;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import businessLogic.FacadeInterface;
import domain.Booking;
import domain.BookingStatus;
import domain.Offer;
import domain.Owner;
import domain.User;
import exceptions.InvalidToken;
import exceptions.RemoteDesyncException;

public class ManageOffersGUI extends JFrame {

	private static final long serialVersionUID = 5948321468143511867L;

	private JPanel contentPane;

	private DefaultTableModel tableModel;
	private JTable table;
	private JCheckBox chckbxShowOnlyPending;
	private List<Offer> offers;
	private String[] columnNames = new String[] {
		"Offer number",
		"First day",
		"Last day",
		"Price",
		"Rural house",
		"Booking status",
		"Customer",
		"Telephone"
	};

	private List<Byte> token;
	private FacadeInterface businessLogic;
	private Owner owner;

	public ManageOffersGUI(List<Byte> tkn) {
	    this.token = tkn;
	    this.businessLogic = LoginGUI.getBusinessLogic();
	    try {
			User user = businessLogic.getUserByToken(token);
			if (!(user instanceof Owner))
				throw new InvalidToken();
			this.owner = (Owner) user;
		} catch (RemoteException | InvalidToken e1) {
			ExceptionHandler.handle(e1);
		}

		setTitle("Manage offers - " + owner.getName());
		setResizable(false);
		setBounds(100, 100, 750, 478);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		table = new JTable();
		tableModel = new DefaultTableModel(null, columnNames);
		table.setModel(tableModel);

		JLabel lblOffers = new JLabel("Offers");
		lblOffers.setFont(new Font("Dialog", Font.BOLD, 14));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(table);

		JButton btnAddOffer = new JButton("Add offer");
		btnAddOffer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFrame frame = new CreateOffer(token);
				frame.setVisible(true);
			}
		});

		JButton btnDeleteOffer = new JButton("Delete offer");
		btnDeleteOffer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteOffer();
			}
		});

		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateList();
			}
		});

		final JButton btnAccept = new JButton("Accept");
		btnAccept.setEnabled(false);
		btnAccept.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				acceptBooking();
			}
		});

		final JButton btnDeny = new JButton("Deny");
		btnDeny.setEnabled(false);
		btnDeny.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				denyBooking();
			}
		});

		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent arg0) {
						int i = table.getSelectedRow();
						if (i == -1 || offers.get(i).getBooking() == null) {
							btnAccept.setEnabled(false);
							btnDeny.setEnabled(false);
						} else {
							if (offers.get(i).getBooking().getStatus() == BookingStatus.ACCEPTED)
								btnDeny.setText("Restore");
							else
								btnDeny.setText("Deny");
							btnAccept.setEnabled(true);
							btnDeny.setEnabled(true);
						}
					}
				});

		chckbxShowOnlyPending = new JCheckBox("Show only pending");
		chckbxShowOnlyPending.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				updateList();
			}
		});

		updateList();

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
							.addGap(345)
							.addComponent(lblOffers)
							.addPreferredGap(ComponentPlacement.RELATED, 200, Short.MAX_VALUE)
							.addComponent(chckbxShowOnlyPending))
						.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
							.addGap(58)
							.addComponent(btnAddOffer, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnDeleteOffer, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnUpdate, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnAccept, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnDeny, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 712, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(12)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblOffers)
						.addComponent(chckbxShowOnlyPending))
					.addGap(9)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 298, GroupLayout.PREFERRED_SIZE)
					.addGap(31)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnUpdate)
						.addComponent(btnDeleteOffer)
						.addComponent(btnAddOffer)
						.addComponent(btnAccept)
						.addComponent(btnDeny)))
		);
		contentPane.setLayout(gl_contentPane);
	}

	private void updateList() {
		while (tableModel.getRowCount() != 0)
			tableModel.removeRow(0);

		try {
			offers = businessLogic.getOffers(token);
		} catch (RemoteException | RemoteDesyncException | InvalidToken e1) {
			ExceptionHandler.handle(e1);
			return;
		}

		Vector<Object> row;
		for (Offer offer : offers) {
			Booking b = offer.getBooking();
			if (!chckbxShowOnlyPending.isSelected()
					|| (b != null && b.getStatus() == BookingStatus.PENDING)) {
				row = new Vector<Object>();

				row.add(offer.getId());
				row.add(offer.getFirstDay());
				row.add(offer.getLastDay());
				row.add(offer.getPrice());
				row.add(offer.getRuralHouse());
				if (b != null) {
					row.add(b.getStatus());
					if (b.getCustomer() == null)
						row.add("Anonymous");
					else
						row.add(b.getCustomer());
					row.add(b.getTelephone());
				} else {
					row.add("FREE");
					row.add("");
					row.add("");
				}
				tableModel.addRow(row);
			}
		}
	}

    private boolean askConfirmationOffer() {
        String[] messages = { "Yes", "No" };
        int result = JOptionPane.showOptionDialog(null,
                "Are you sure you want to delete the selected offer?",
                "Delete offer", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, messages, 0);
        return (result == 0);
    }

    private boolean askConfirmationDenyBooking() {
        String[] messages = { "Yes", "No" };
        int result = JOptionPane.showOptionDialog(null,
                "Are you sure you want to deny the selected booking?",
                "Cancel booking", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, messages, 0);
        return (result == 0);
    }

    private boolean askConfirmationRestoreBooking() {
        String[] messages = { "Yes", "No" };
        int result = JOptionPane.showOptionDialog(null,
                "Are you sure you want to restore the selected booking?",
                "Restore booking", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, messages, 0);
        return (result == 0);
    }

	private void deleteOffer() {
		int i = table.getSelectedRow();
		if (i != -1 && askConfirmationOffer()) {
			try {
				String telephone = businessLogic.removeOffer(token,
						offers.get(i));
				if (telephone != null) {
					String msg = "Notify to this telephone the"
							+ " cancellation of the offer: \n\n";
					msg += "    " + telephone + "\n";

					JOptionPane.showMessageDialog(null, msg,
							"Notify customers", JOptionPane.WARNING_MESSAGE);
				}
			} catch (RemoteException | InvalidToken | RemoteDesyncException e1) {
				ExceptionHandler.handle(e1);
			}
			updateList();
		}
	}

	private void denyBooking() {
		int i = table.getSelectedRow();
		if (i != -1) {
			Booking b = offers.get(i).getBooking();
			try {
				if (b.getStatus() == BookingStatus.ACCEPTED) {
					if (askConfirmationRestoreBooking())
						businessLogic.restoreBooking(token, b);
				} else {
					if (askConfirmationDenyBooking())
						businessLogic.denyBooking(token, b);
				}
			} catch (RemoteException | InvalidToken | RemoteDesyncException e1) {
				ExceptionHandler.handle(e1);
			}
			updateList();
		}
	}

	private void acceptBooking() {
		int i = table.getSelectedRow();
		if (i != -1 && offers.get(i).getBooking() != null) {
			try {
				businessLogic.acceptBooking(token, offers.get(i).getBooking());
			} catch (RemoteException | InvalidToken | RemoteDesyncException e1) {
				ExceptionHandler.handle(e1);
			}
			updateList();
		}
	}
}
