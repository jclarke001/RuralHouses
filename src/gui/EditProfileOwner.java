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
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import businessLogic.FacadeInterface;
import domain.Owner;
import domain.User;
import exceptions.CorruptedCache;
import exceptions.CorruptedDatabase;
import exceptions.InvalidToken;
import exceptions.RemoteDesyncException;

public class EditProfileOwner extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldName;

	private List<Byte> token;
	private FacadeInterface businessLogic;
	private Owner owner;
	private JTextField textFieldBankAccount;

	public EditProfileOwner(List<Byte> tkn) {
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

		setTitle("Edit profile - " + owner.getName());
		setResizable(false);
		setBounds(100, 100, 440, 185);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JLabel lblName = new JLabel("Name:");

		textFieldName = new JTextField();
		textFieldName.setColumns(10);
		textFieldName.setText(owner.getName());

		JLabel lblBankAccount = new JLabel("Bank account:");

		textFieldBankAccount = new JTextField();
		textFieldBankAccount.setColumns(10);
		textFieldBankAccount.setText(owner.getBankAccount());

		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					businessLogic.editProfileOwner(token, textFieldName.getText(), textFieldBankAccount.getText());
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
							.addGap(106)
							.addComponent(btnSubmit)
							.addGap(18)
							.addComponent(btnChangePassword))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(45)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblBankAccount)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(textFieldBankAccount))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblName)
									.addGap(72)
									.addComponent(textFieldName, GroupLayout.PREFERRED_SIZE, 219, GroupLayout.PREFERRED_SIZE)))))
					.addContainerGap(49, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(textFieldName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblName))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblBankAccount)
						.addComponent(textFieldBankAccount, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(38)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnSubmit)
						.addComponent(btnChangePassword))
					.addGap(20))
		);
		contentPane.setLayout(gl_contentPane);
	}
}
