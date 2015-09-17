package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import businessLogic.FacadeInterface;
import domain.District;
import domain.Owner;
import domain.RuralHouse;
import exceptions.RemoteDesyncException;

public class SearchHouseGUI extends JFrame {

	private static final long serialVersionUID = -3127468027645285695L;
	private JPanel contentPane;
	private JTextField txtBedrooms;
	private JTextField txtBathrooms;
	private JTextField txtKitchens;
	private JTextField txtDinningrooms;
	private JTextField txtParkingspaces;
	private JTextField txtDescription;
	private JComboBox<Owner> comboBoxOwner;
	private JComboBox<District> comboBoxDistrict;
	private Vector<JTextField> numeric;

	private JButton btnSearch;

	private DefaultComboBoxModel<Owner> cboxModelOwner;
	private DefaultComboBoxModel<District> cboxModelDistrict;

	private FacadeInterface businessLogic;
	private JLabel lblAnswer;
	private JCheckBox cbOwner;
	private JCheckBox cbDistrict;

	public SearchHouseGUI(final List<Byte> tkn) {
		setResizable(false);
		this.businessLogic = LoginGUI.getBusinessLogic();

		setBounds(100, 100, 514, 340);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		contentPane.setLayout(null);

		lblAnswer = new JLabel("");
		lblAnswer.setBounds(65, 282, 300, 15);
		lblAnswer.setForeground(Color.BLUE);
		contentPane.add(lblAnswer);

		cboxModelOwner = new DefaultComboBoxModel<Owner>();
		comboBoxOwner = new JComboBox<Owner>();
		comboBoxOwner.setBounds(224, 49, 166, 24);
		comboBoxOwner.setModel(cboxModelOwner);
		comboBoxOwner.setEnabled(false);
		contentPane.add(comboBoxOwner);

		JLabel lblDistrict = new JLabel("District:");
		lblDistrict.setBounds(65, 82, 56, 15);
		contentPane.add(lblDistrict);

		cboxModelDistrict = new DefaultComboBoxModel<District>(
				District.values());
		comboBoxDistrict = new JComboBox<District>();
		comboBoxDistrict.setBounds(224, 78, 166, 24);
		comboBoxDistrict.setModel(cboxModelDistrict);
		comboBoxDistrict.setEnabled(false);
		contentPane.add(comboBoxDistrict);

		JLabel lblBedrooms = new JLabel("Bedrooms:");
		lblBedrooms.setBounds(65, 109, 76, 15);
		contentPane.add(lblBedrooms);

		txtBedrooms = new JTextField();
		txtBedrooms.setBounds(224, 107, 35, 19);
		txtBedrooms.setColumns(10);
		contentPane.add(txtBedrooms);

		JLabel lblBathrooms = new JLabel("Bathrooms:");
		lblBathrooms.setBounds(65, 133, 82, 15);
		contentPane.add(lblBathrooms);

		txtBathrooms = new JTextField();
		txtBathrooms.setBounds(224, 131, 35, 19);
		txtBathrooms.setColumns(10);
		contentPane.add(txtBathrooms);

		JLabel lblKitchens = new JLabel("Kitchens:");
		lblKitchens.setBounds(65, 157, 66, 15);
		contentPane.add(lblKitchens);

		txtKitchens = new JTextField();
		txtKitchens.setBounds(224, 155, 35, 19);
		txtKitchens.setColumns(10);
		contentPane.add(txtKitchens);

		JLabel lblDiningRooms = new JLabel("Dinning rooms:");
		lblDiningRooms.setBounds(65, 179, 107, 15);
		contentPane.add(lblDiningRooms);

		txtDinningrooms = new JTextField();
		txtDinningrooms.setBounds(224, 179, 35, 19);
		txtDinningrooms.setColumns(10);
		contentPane.add(txtDinningrooms);

		JLabel lblParkingSpaces = new JLabel("Parking spaces:");
		lblParkingSpaces.setBounds(65, 203, 113, 15);
		contentPane.add(lblParkingSpaces);

		txtParkingspaces = new JTextField();
		txtParkingspaces.setBounds(224, 203, 35, 19);
		txtParkingspaces.setColumns(10);
		contentPane.add(txtParkingspaces);

		JLabel lblDescription = new JLabel("Description:");
		lblDescription.setBounds(65, 227, 86, 15);
		contentPane.add(lblDescription);

		txtDescription = new JTextField();
		txtDescription.setBounds(224, 227, 254, 19);
		txtDescription.setColumns(10);
		contentPane.add(txtDescription);

		btnSearch = new JButton("Search");
		btnSearch.setBounds(395, 272, 83, 25);
		contentPane.add(btnSearch);

		cbOwner = new JCheckBox("");
		cbOwner.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (cbOwner.isSelected())
					comboBoxOwner.setEnabled(true);
				else
					comboBoxOwner.setEnabled(false);
			}
		});
		cbOwner.setBounds(398, 50, 23, 23);
		contentPane.add(cbOwner);

		cbDistrict = new JCheckBox("");
		cbDistrict.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (cbDistrict.isSelected())
					comboBoxDistrict.setEnabled(true);
				else
					comboBoxDistrict.setEnabled(false);
			}
		});
		cbDistrict.setBounds(398, 78, 23, 23);
		contentPane.add(cbDistrict);

		JLabel lblOwner = new JLabel("Owner");
		lblOwner.setBounds(65, 54, 70, 15);
		contentPane.add(lblOwner);

		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for (JTextField txt : numeric) {
					if (txt.getText().equals(""))
						txt.setText("0");
				}
				try {
					Owner o = null;
					if (cbOwner.isSelected())
						o = (Owner) comboBoxOwner.getSelectedItem();

					District dist = null;
					if (cbDistrict.isSelected())
						dist = (District) comboBoxDistrict.getSelectedItem();
					String des = txtDescription.getText();

					int bed = Integer.parseInt(txtBedrooms.getText());
					int bath = Integer.parseInt(txtBathrooms.getText());
					int kit = Integer.parseInt(txtKitchens.getText());
					int din = Integer.parseInt(txtDinningrooms.getText());
					int park = Integer.parseInt(txtParkingspaces.getText());

					List<RuralHouse> answer = businessLogic.searchRuralHouses(
							o, des, dist, bed, bath, kit, din, park);
					if (!answer.isEmpty()) {
						JFrame frame = new SearchResultGUI(tkn, answer);
						frame.setVisible(true);
					} else {
						JOptionPane	.showMessageDialog(	null,
								"Not results were found for the given arguments",
								"No results",
								JOptionPane.INFORMATION_MESSAGE);
					}
				} catch (NumberFormatException e) {
					showError("You have to introduce numbers in numeric fields, or leave them empty");
				} catch (RemoteException | RemoteDesyncException e) {
					ExceptionHandler.handle(e);
				}
			}
		});

		numeric = new Vector<JTextField>();
		numeric.add(txtBedrooms);
		numeric.add(txtBathrooms);
		numeric.add(txtDinningrooms);
		numeric.add(txtKitchens);
		numeric.add(txtParkingspaces);

		final JFrame reference = this;
		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				try {
					initialize();
				} catch (RemoteException | RemoteDesyncException e1) {
					ExceptionHandler.handle(e1);

					// Close window
					reference.dispatchEvent(new WindowEvent(reference,
							WindowEvent.WINDOW_CLOSING));
				}
			}
		});

	}

	private void initialize() throws RemoteException, RemoteDesyncException {
		for (Owner o : businessLogic.getOwners())
			cboxModelOwner.addElement(o);
	}

	private static void showError(String errorMessage) {
		JOptionPane.showMessageDialog(null, errorMessage, "Error",
				JOptionPane.ERROR_MESSAGE);
	}
}
