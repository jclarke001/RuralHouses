package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import businessLogic.FacadeInterface;
import domain.UserType;
import exceptions.InvalidToken;
import exceptions.NoDigestAlgorithm;
import exceptions.PasswordsDontMatch;
import exceptions.UsernameExists;

public class CreateUserGUI extends JFrame {

	private static final long serialVersionUID = 2294281513788541004L;
	private JPanel contentPane;
	private JTextField txtUsername;
	private JPasswordField passwordField1;
	private JPasswordField passwordField2;
	private JTextField txtName;

	private FacadeInterface facade;
	private List<Byte> token;
	private UserType type;

	public CreateUserGUI(List<Byte> tkn, UserType type1) {
		this.facade = LoginGUI.getBusinessLogic();
		this.token = tkn;
		this.type = type1;

		JButton btnCreate;
		switch (type) {
		case OWNER:
			setTitle("Create owner account");
			btnCreate = new JButton("Create");
			break;
		case CUSTOMER:
			setTitle("Sign in");
			btnCreate = new JButton("Sign in");
			break;
		default:
			setTitle("Create account");
			btnCreate = new JButton("Create");
			break;
		}

		setResizable(false);
		setBounds(100, 100, 420, 252);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(84, 27, 77, 15);
		JLabel lblPassword1 = new JLabel("Password:");
		lblPassword1.setBounds(84, 58, 75, 15);
		JLabel lblPassword2 = new JLabel("Password:");
		lblPassword2.setBounds(84, 89, 75, 15);

		txtUsername = new JTextField();
		txtUsername.setBounds(179, 25, 165, 19);
		txtUsername.setColumns(10);

		passwordField1 = new JPasswordField();
		passwordField1.setBounds(179, 56, 165, 19);
		passwordField2 = new JPasswordField();
		passwordField2.setBounds(179, 87, 165, 19);


		btnCreate.setBounds(93, 176, 109, 25);
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String tmpUsername = txtUsername.getText();
				String tmpPassword1 = new String(passwordField1.getPassword());
				String tmpPassword2 = new String(passwordField2.getPassword());
				String tmpName = txtName.getText();

				if (tmpUsername.equals("") || tmpPassword1.equals("")
						|| tmpName.equals("")) {
					JOptionPane.showMessageDialog(null,
							"A username and a password are required.",
							"Invalid values", JOptionPane.WARNING_MESSAGE);
					return;
				}

				passwordField1.setText("");
				passwordField2.setText("");

				try {
					if (!tmpPassword1.equals(tmpPassword2))
						throw new PasswordsDontMatch();
					facade.createAccount(token, type, tmpUsername,
							tmpPassword1, tmpName);
					txtUsername.setText("");
					txtName.setText("");
					JOptionPane.showMessageDialog(null,
							"Account created succesfully.", "Account created",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (PasswordsDontMatch | RemoteException | UsernameExists
						| NoDigestAlgorithm | InvalidToken e) {
					ExceptionHandler.handle(e);
				}
			}
		});

		final JFrame reference = this;
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(208, 176, 110, 25);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				reference.dispatchEvent(new WindowEvent(reference,
						WindowEvent.WINDOW_CLOSING));
			}
		});

		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(84, 118, 45, 15);

		txtName = new JTextField();
		txtName.setBounds(179, 116, 165, 19);
		txtName.setColumns(10);

		contentPane.setLayout(null);
		contentPane.add(btnCreate);
		contentPane.add(btnCancel);
		contentPane.add(lblUsername);
		contentPane.add(lblPassword1);
		contentPane.add(passwordField1);
		contentPane.add(txtUsername);
		contentPane.add(lblPassword2);
		contentPane.add(lblName);
		contentPane.add(txtName);
		contentPane.add(passwordField2);
	}
}
