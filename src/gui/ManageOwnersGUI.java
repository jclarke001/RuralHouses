package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import businessLogic.FacadeInterface;
import domain.Owner;
import domain.UserType;
import exceptions.CorruptedCache;
import exceptions.CorruptedDatabase;
import exceptions.InvalidToken;
import exceptions.RemoteDesyncException;

public class ManageOwnersGUI extends JFrame {

	private static final long serialVersionUID = -4729334298789236721L;
	private JPanel contentPane;

	private DefaultTableModel tableModel;
	private JTable table;
	private String[] columnNames = new String[] { "Id", "Name",
			"Account number" };
	private List<Owner> owners;

	private FacadeInterface businessLogic;
	private List<Byte> token;

	public ManageOwnersGUI(List<Byte> tkn) {
		businessLogic = LoginGUI.getBusinessLogic();
		this.token = tkn;

		setTitle("Manage owners");
		setResizable(false);
		setBounds(100, 100, 600, 512);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JButton btnAdd = new JButton("Add");
		btnAdd.setBounds(17, 17, 91, 25);
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFrame frame = new CreateUserGUI(token, UserType.OWNER);
				frame.setVisible(true);
			}
		});

		JButton btnEditPassword = new JButton("Edit password");
		btnEditPassword.setBounds(126, 17, 135, 25);
		btnEditPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int i = table.getSelectedRow();
				if (i != -1) {
					Owner owner = owners.get(i);
					JFrame frame = new ChangePasswordAdminGUI(token, owner);
					frame.setVisible(true);
				}
			}
		});

		JButton btnRemove = new JButton("Remove account");
		btnRemove.setBounds(279, 17, 149, 25);
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int i = table.getSelectedRow();
				if (i != -1 && askConfirmation()) {
					Owner owner = owners.get(i);
					try {
						notifyCustomers(businessLogic.removeOwner(token, owner));
						updateList();
					} catch (RemoteException | InvalidToken
							| RemoteDesyncException | CorruptedDatabase
							| CorruptedCache e) {
						ExceptionHandler.handle(e);
					}
				}
			}
		});

		table = new JTable();
		tableModel = new DefaultTableModel(null, columnNames);
		table.setModel(tableModel);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(17, 48, 566, 398);
		scrollPane.setViewportView(table);

		JButton btnUpdate = new JButton("Update");
		btnUpdate.setBounds(446, 17, 86, 25);
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				updateList();
			}
		});
		contentPane.setLayout(null);
		contentPane.add(btnAdd);
		contentPane.add(btnEditPassword);
		contentPane.add(btnRemove);
		contentPane.add(btnUpdate);
		contentPane.add(scrollPane);

		this.updateList();
	}

	private void updateList() {
		while (tableModel.getRowCount() != 0)
			tableModel.removeRow(0);
		try {
			owners = businessLogic.getOwners();
			Vector<Object> row;
			for (Owner o : owners) {
				row = new Vector<Object>();
				row.add(o.getId());
				row.add(o.getName());
				if (o.hasBankAccount())
					row.add(o.getBankAccount());
				else
					row.add("Not set");
				tableModel.addRow(row);
			}
		} catch (RemoteException e) {
			ExceptionHandler.handle(e);
		}
	}

	private boolean askConfirmation() {
		String[] messages = { "Yes", "No" };
		int result = JOptionPane.showOptionDialog(null,
				"Are you sure you want to delete the selected owner account?",
				"Delete", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, messages, 0);
		return (result == 0);
	}

	private void notifyCustomers(List<String> telephones) {
		if (telephones.size() > 0) {
			String msg = "Notify to these telephones about the cancellation of their bookings:\n\n";
			for (String t : telephones) {
				if (t != null)
					msg += "    " + t + "\n";
			}

			JOptionPane
					.showMessageDialog(null, msg,
							"Notify customers",
							JOptionPane.WARNING_MESSAGE);
		}
	}
}
