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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import domain.RuralHouse;
import exceptions.RemoteDesyncException;

public class SearchResultGUI extends JFrame {

	private static final long serialVersionUID = 5948321468143511867L;

	private JPanel contentPane;

	private DefaultTableModel tableModel;
	private JTable table;
	private String[] columnNames = new String[] { "Number", "District",
			"Owner", "Bedthrooms", "Bathrooms", "Diningrooms", "Kitchens",
			"Parking spaces", "Description" };

	/**
	 * Create the frame.
	 */
	public SearchResultGUI(final List<Byte> tkn,
			final List<RuralHouse> ruralHouses) {

		setTitle("Search result");
		setResizable(false);
		setBounds(100, 100, 750, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		table = new JTable();
		tableModel = new DefaultTableModel(null, columnNames);
		table.setModel(tableModel);

		JButton btnInspect = new JButton("Inspect selected house");
		btnInspect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int i = table.getSelectedRow();
				if (i != -1) {
					try {
						JFrame frame = new EditRuralHouseGUI(null, ruralHouses
								.get(i));
						frame.setVisible(true);
					} catch (RemoteException | RemoteDesyncException e1) {
						ExceptionHandler.handle(e1);
					}
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(table);

		JButton btnBook = new JButton("Query availability");
		btnBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int i = table.getSelectedRow();
				if (i != -1) {
					JFrame frame = new QueryAvailabilityGUI(tkn, ruralHouses
							.get(i));
					frame.setVisible(true);
				}
			}
		});

		JLabel lblRuralHouses = new JLabel("Rural houses");
		lblRuralHouses.setFont(new Font("Dialog", Font.BOLD, 14));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane
				.setHorizontalGroup(gl_contentPane
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_contentPane
										.createSequentialGroup()
										.addGroup(
												gl_contentPane
														.createParallelGroup(
																Alignment.LEADING)
														.addGroup(
																gl_contentPane
																		.createSequentialGroup()
																		.addGap(318)
																		.addComponent(
																				lblRuralHouses))
														.addGroup(
																gl_contentPane
																		.createSequentialGroup()
																		.addGap(137)
																		.addComponent(
																				btnInspect,
																				GroupLayout.PREFERRED_SIZE,
																				230,
																				GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addComponent(
																				btnBook,
																				GroupLayout.PREFERRED_SIZE,
																				230,
																				GroupLayout.PREFERRED_SIZE)))
										.addGap(12))
						.addGroup(
								gl_contentPane
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(scrollPane,
												GroupLayout.DEFAULT_SIZE, 712,
												Short.MAX_VALUE)
										.addContainerGap()));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(
				Alignment.LEADING)
				.addGroup(
						gl_contentPane
								.createSequentialGroup()
								.addGap(15)
								.addComponent(lblRuralHouses)
								.addGap(18)
								.addComponent(scrollPane,
										GroupLayout.PREFERRED_SIZE, 311,
										GroupLayout.PREFERRED_SIZE)
								.addGap(12)
								.addGroup(
										gl_contentPane
												.createParallelGroup(
														Alignment.BASELINE)
												.addComponent(btnInspect)
												.addComponent(btnBook))));
		contentPane.setLayout(gl_contentPane);

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
	}
}
