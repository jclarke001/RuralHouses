package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import javax.swing.JButton;

public class RegisterPropertyGUI extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldName;
	private JTextField textFieldDistrict;
	private JTextField textFieldBedrooms;
	private JTextField textFieldBathrooms;
	private JTextField textFieldKitchens;
	private JTextField textFieldDinningrooms;
	private JTextField textFieldParkingspaces;
	private JTextField textFieldDescription;
	private JTextField textFieldAccount1;
	private JTextField textFieldAccount2;
	private JTextField textFieldAccount3;
	private JTextField textFieldAccount4;

	/**
	 * Create the frame.
	 */
	public RegisterPropertyGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JLabel lblName = new JLabel("Name:");

		JLabel lblDistrict = new JLabel("District:");

		JLabel lblBedrooms = new JLabel("Bedrooms:");

		JLabel lblBathrooms = new JLabel("Bathrooms:");

		JLabel lblKitchens = new JLabel("Kitchens:");

		JLabel lblDiningRooms = new JLabel("Dinning rooms:");

		JLabel lblNewLabel = new JLabel("Parking spaces:");

		JLabel lblDescription = new JLabel("Description:");

		JLabel lblAccountNumber = new JLabel("Account number:");

		textFieldName = new JTextField();
		textFieldName.setColumns(10);

		textFieldDistrict = new JTextField();
		textFieldDistrict.setColumns(10);

		textFieldBedrooms = new JTextField();
		textFieldBedrooms.setColumns(10);

		textFieldBathrooms = new JTextField();
		textFieldBathrooms.setColumns(10);

		textFieldKitchens = new JTextField();
		textFieldKitchens.setColumns(10);

		textFieldDinningrooms = new JTextField();
		textFieldDinningrooms.setColumns(10);

		textFieldParkingspaces = new JTextField();
		textFieldParkingspaces.setColumns(10);

		textFieldDescription = new JTextField();
		textFieldDescription.setColumns(10);

		textFieldAccount1 = new JTextField();
		textFieldAccount1.setColumns(10);

		textFieldAccount2 = new JTextField();
		textFieldAccount2.setColumns(10);

		textFieldAccount3 = new JTextField();
		textFieldAccount3.setColumns(10);

		textFieldAccount4 = new JTextField();
		textFieldAccount4.setColumns(10);

		JButton btnSubmit = new JButton("Submit");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(65)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblBathrooms)
						.addComponent(lblBedrooms)
						.addComponent(lblDistrict)
						.addComponent(lblName)
						.addComponent(lblKitchens)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
							.addComponent(lblDiningRooms)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblDescription)
								.addComponent(lblNewLabel)
								.addComponent(lblAccountNumber))))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(textFieldBedrooms, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
						.addComponent(textFieldBathrooms, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
						.addComponent(textFieldKitchens, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
						.addComponent(textFieldDinningrooms, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
						.addComponent(textFieldParkingspaces, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(textFieldAccount1, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(textFieldAccount2, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(textFieldAccount3, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(textFieldAccount4, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
							.addComponent(textFieldName, Alignment.LEADING)
							.addComponent(textFieldDistrict, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE))
						.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
							.addComponent(btnSubmit)
							.addComponent(textFieldDescription, GroupLayout.PREFERRED_SIZE, 275, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(42, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblName)
						.addComponent(textFieldName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDistrict)
						.addComponent(textFieldDistrict, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblBedrooms)
						.addComponent(textFieldBedrooms, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblBathrooms)
						.addComponent(textFieldBathrooms, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(textFieldKitchens, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblKitchens))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDiningRooms)
						.addComponent(textFieldDinningrooms, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(textFieldParkingspaces, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblDescription)
						.addComponent(textFieldDescription, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblAccountNumber)
						.addComponent(textFieldAccount1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(textFieldAccount2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(textFieldAccount3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(textFieldAccount4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(52)
					.addComponent(btnSubmit)
					.addGap(50))
		);
		contentPane.setLayout(gl_contentPane);
	}
}
