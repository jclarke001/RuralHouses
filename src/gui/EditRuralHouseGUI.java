package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
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
import exceptions.InvalidToken;
import exceptions.RegisterPropertyValidationError;
import exceptions.RemoteDesyncException;

public class EditRuralHouseGUI extends JFrame {

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

	private JButton btnSubmit;
	private JLabel lblOwner;

	private DefaultComboBoxModel<Owner> cboxModelOwner;
	private DefaultComboBoxModel<District> cboxModelDistrict;

	private RuralHouse ruralHouse;
	private List<Byte> token;
	private FacadeInterface businessLogic;
	private JLabel lblAnswer;

	public EditRuralHouseGUI(List<Byte> tkn, RuralHouse rh)
			throws RemoteException, RemoteDesyncException {
		setResizable(false);
		this.businessLogic = LoginGUI.getBusinessLogic();
		this.token = tkn;
		this.ruralHouse = rh;

		setBounds(100, 100, 514, 430);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		contentPane.setLayout(null);

		lblAnswer = new JLabel("");
		lblAnswer.setBounds(65, 339, 300, 15);
		lblAnswer.setForeground(Color.BLUE);
		contentPane.add(lblAnswer);

		lblOwner = new JLabel("Owner:");
		lblOwner.setBounds(65, 53, 52, 15);
		contentPane.add(lblOwner);

		cboxModelOwner = new DefaultComboBoxModel<Owner>();
		comboBoxOwner = new JComboBox<Owner>();
		comboBoxOwner.setBounds(224, 49, 166, 24);
		comboBoxOwner.setModel(cboxModelOwner);
		contentPane.add(comboBoxOwner);

		JLabel lblDistrict = new JLabel("District:");
		lblDistrict.setBounds(65, 82, 56, 15);
		contentPane.add(lblDistrict);

		cboxModelDistrict = new DefaultComboBoxModel<District>(
				District.values());
		comboBoxDistrict = new JComboBox<District>();
		comboBoxDistrict.setBounds(224, 78, 166, 24);
		comboBoxDistrict.setModel(cboxModelDistrict);
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

		btnSubmit = new JButton("Submit");
		btnSubmit.setBounds(395, 339, 83, 25);
		contentPane.add(btnSubmit);

		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Owner o = (Owner) comboBoxOwner.getSelectedItem();
					String des = txtDescription.getText();
					District dist = (District) comboBoxDistrict
							.getSelectedItem();
					int bed = Integer.parseInt(txtBedrooms.getText());
					int bath = Integer.parseInt(txtBathrooms.getText());
					int kit = Integer.parseInt(txtKitchens.getText());
					int din = Integer.parseInt(txtDinningrooms.getText());
					int park = Integer.parseInt(txtParkingspaces.getText());

					if (ruralHouse == null) {
						businessLogic.addRuralHouse(token, o, des, dist, bed,
								bath, kit, din, park);
						lblAnswer.setText("Rural House added correctly");
					} else {
						businessLogic.editRuralHouse(token, ruralHouse, o, des,
								dist, bed, bath, kit, din, park);
						lblAnswer.setText("Rural House edited correctly");
					}
				} catch (RegisterPropertyValidationError e) {
					showError("The rural house must have at least 1 kitchen, 3 bedrooms and 2 bathrooms.");
				} catch (NumberFormatException e) {
					showError("You have to introduce numbers in numeric fields");
				} catch (RemoteException | InvalidToken | RemoteDesyncException e) {
					ExceptionHandler.handle(e);
				}
			}
		});

		initialize();
	}

	private void initialize() throws RemoteException, RemoteDesyncException {
		for (Owner o : businessLogic.getOwners())
			cboxModelOwner.addElement(o);

		if (cboxModelOwner.getSize() == 0) {
			btnSubmit.setEnabled(false);
		}

		if (ruralHouse != null && token != null) { // Edit Property
			setTitle("Edit property");
			this.ruralHouse = businessLogic.getRuralHouse(ruralHouse);
		} else if (ruralHouse == null && token != null) { // Register Property
			setTitle("Register property");
		} else if (ruralHouse != null && token == null) { // Inspect Property
			setTitle("Inspect property");
			this.ruralHouse = businessLogic.getRuralHouse(ruralHouse);
			btnSubmit.setVisible(false);
		}

		// If editing or inspecting, initialize text boxes with the previous
		// values
		if (ruralHouse != null) {
			try {
				cboxModelDistrict.setSelectedItem(ruralHouse.getDistrict());
				cboxModelOwner.setSelectedItem(ruralHouse.getOwner());
				txtBedrooms
						.setText(Integer.toString(ruralHouse.getNBedrooms()));
				txtBathrooms.setText(Integer.toString(ruralHouse
						.getNBathrooms()));
				txtKitchens
						.setText(Integer.toString(ruralHouse.getNKitchens()));
				txtDinningrooms.setText(Integer.toString(ruralHouse
						.getNDiningrooms()));
				txtParkingspaces.setText(Integer.toString(ruralHouse
						.getNParkingSpaces()));
				txtDescription.setText(ruralHouse.getDescription());
			} catch (NullPointerException | StringIndexOutOfBoundsException e) {
			}
		}

		// If inspecting, set all boxes as read only
		if (token == null) {
			comboBoxOwner.setEnabled(false);
			comboBoxDistrict.setEnabled(false);
			txtBedrooms.setEditable(false);
			txtBathrooms.setEditable(false);
			txtKitchens.setEditable(false);
			txtDinningrooms.setEditable(false);
			txtParkingspaces.setEditable(false);
			txtDescription.setEditable(false);
		}
	}

	private static void showError(String errorMessage) {
		JOptionPane.showMessageDialog(null, errorMessage, "Error",
				JOptionPane.ERROR_MESSAGE);
	}
}
