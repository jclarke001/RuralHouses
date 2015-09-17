package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.LinkedList;
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
import domain.RuralHouse;
import exceptions.InvalidToken;
import exceptions.RemoteDesyncException;

public class ManagePropertiesGUI extends JFrame {

	private static final long serialVersionUID = -4729334298789236721L;
	private JPanel contentPane;

    private DefaultTableModel tableModel;
    private JTable table;
    private String[] columnNames = new String[] { "Number", "District",
            "Owner", "Bedthrooms", "Bathrooms", "Diningrooms", "Kitchens",
            "Parking spaces", "Description" };
    private List<RuralHouse> ruralHouses;

	private FacadeInterface businessLogic;
	private List<Byte> token;

	/**
	 * Create the frame.
	 */
	public ManagePropertiesGUI(List<Byte> t) {
		this.businessLogic = LoginGUI.getBusinessLogic();
		this.token = t;

		setTitle("Manage properties");
		setResizable(false);
		setBounds(100, 100, 600, 522);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JButton btnAdd = new JButton("Add");
		btnAdd.setBounds(43, 17, 91, 25);
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					JFrame frame = new EditRuralHouseGUI(token, null);
					frame.setVisible(true);
				} catch (RemoteException | RemoteDesyncException e) {
					ExceptionHandler.handle(e);
				}
			}
		});

		JButton btnEdit = new JButton("Edit");
		btnEdit.setBounds(146, 17, 87, 25);
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			    int i = table.getSelectedRow();
			    if (i != -1) {
			        RuralHouse rh = ruralHouses.get(i);
					try {
						JFrame frame = new EditRuralHouseGUI(token, rh);
						frame.setVisible(true);
					} catch (RemoteException | RemoteDesyncException e) {
						ExceptionHandler.handle(e);
					}
				}
			}
		});

		JButton btnInspect = new JButton("Inspect");
		btnInspect.setBounds(245, 17, 92, 25);
		btnInspect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			    int i = table.getSelectedRow();
				if (i != -1) {
				    RuralHouse rh = ruralHouses.get(i);
					try {
						JFrame frame = new EditRuralHouseGUI(null, rh);
						frame.setVisible(true);
					} catch (RemoteException | RemoteDesyncException e) {
						ExceptionHandler.handle(e);
					}
				}
			}
		});

		JButton btnWithdraw = new JButton("Withdraw");
		btnWithdraw.setBounds(349, 17, 103, 25);
		btnWithdraw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			    int i = table.getSelectedRow();
				if (i != -1 && askConfirmation()) {
				    RuralHouse rh = ruralHouses.get(i);
					List<String> telephones = new LinkedList<String>();
					try {
						telephones = businessLogic.removeRuralHouse(token, rh);
						notifyCustomers(telephones);
					} catch (RemoteException | InvalidToken
							| RemoteDesyncException e) {
						ExceptionHandler.handle(e);
					}
					updateList();
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
		btnUpdate.setBounds(464, 17, 86, 25);
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				updateList();
			}
		});
		contentPane.setLayout(null);
		contentPane.add(btnAdd);
		contentPane.add(btnEdit);
		contentPane.add(btnInspect);
		contentPane.add(btnWithdraw);
		contentPane.add(btnUpdate);
		contentPane.add(scrollPane);

		this.updateList();
	}

	private void updateList() {
        while (tableModel.getRowCount() != 0)
            tableModel.removeRow(0);

		try {
		    ruralHouses = businessLogic.getRuralHouses(token);
		    Vector<Object> row;
            for (RuralHouse rh : ruralHouses) {
                row = new Vector<Object>();
                row.add(rh.getId());
                row.add(rh.getDistrict());
                row.add(rh.getOwner());
                row.add(rh.getNBedrooms());
                row.add(rh.getNBathrooms());
                row.add(rh.getNDiningrooms());
                row.add(rh.getNKitchens());
                row.add(rh.getNParkingSpaces());
                row.add(rh.getDescription());
                tableModel.addRow(row);
	        }
		} catch (RemoteException | InvalidToken e) {
			ExceptionHandler.handle(e);
		}
	}

	private boolean askConfirmation() {
		String[] messages = { "Yes", "No" };
		int result = JOptionPane.showOptionDialog(null,
				"Are you sure you want to delete the selected property?",
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
