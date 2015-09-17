package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import domain.Customer;
import domain.User;
import exceptions.CorruptedCache;
import exceptions.CorruptedDatabase;
import exceptions.InvalidToken;
import exceptions.RemoteDesyncException;

import businessLogic.FacadeInterface;

public class EditProfileCustomer extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldName;

	private List<Byte> token;
	private FacadeInterface businessLogic;
	private Customer customer;

	public EditProfileCustomer(List<Byte> tkn) {
		this.token = tkn;
		this.businessLogic = LoginGUI.getBusinessLogic();
		try {
			User user = businessLogic.getUserByToken(token);
			if (!(user instanceof Customer))
				throw new InvalidToken();
			this.customer = (Customer) user;
		} catch (RemoteException | InvalidToken e1) {
			ExceptionHandler.handle(e1);
		}

		setTitle("Edit profile - " + customer.getName());
		setResizable(false);
		setBounds(100, 100, 440, 127);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JLabel lblName = new JLabel("Name:");

		textFieldName = new JTextField();
		textFieldName.setColumns(10);
		textFieldName.setText(customer.getName());

		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					businessLogic.editProfileCustomer(token, textFieldName.getText());
				} catch (RemoteException | InvalidToken | CorruptedDatabase
						| RemoteDesyncException | CorruptedCache e) {
					ExceptionHandler.handle(e);
				}
			}
		});

		JButton btnChangePassword = new JButton("Change password");
		btnChangePassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new ChangePasswordGUI(token);
				frame.setVisible(true);
			}
		});

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(45)
							.addComponent(lblName)
							.addGap(30)
							.addComponent(textFieldName, GroupLayout.PREFERRED_SIZE, 261, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(101)
							.addComponent(btnSubmit)
							.addGap(18)
							.addComponent(btnChangePassword)))
					.addContainerGap(49, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(textFieldName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblName))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnSubmit)
						.addComponent(btnChangePassword))
					.addGap(60))
		);
		contentPane.setLayout(gl_contentPane);
	}
}
