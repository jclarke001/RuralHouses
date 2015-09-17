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
import javax.swing.border.EmptyBorder;

import businessLogic.FacadeInterface;
import domain.Owner;
import exceptions.InvalidToken;
import exceptions.NoDigestAlgorithm;
import exceptions.PasswordsDontMatch;
import exceptions.RemoteDesyncException;

public class ChangePasswordAdminGUI extends JFrame {

	private static final long serialVersionUID = 2294281513788541004L;
	private JPanel contentPane;
	private JPasswordField passwordField1;
	private JPasswordField passwordField2;

	private FacadeInterface facade;
	private List<Byte> token;

    public ChangePasswordAdminGUI(List<Byte> tkn, final Owner owner) {
        this.facade = LoginGUI.getBusinessLogic();
        this.token = tkn;

		setTitle("Change password - Admin");
		setResizable(false);

		setBounds(100, 100, 420, 133);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JLabel lblPassword1 = new JLabel("New password:");
		lblPassword1.setBounds(79, 12, 110, 15);
		JLabel lblPassword2 = new JLabel("New password:");
		lblPassword2.setBounds(79, 39, 110, 15);

		passwordField1 = new JPasswordField();
		passwordField1.setBounds(219, 10, 154, 19);
		passwordField2 = new JPasswordField();
		passwordField2.setBounds(219, 35, 154, 19);

		JButton btnChange = new JButton("Change");
		btnChange.setBounds(109, 66, 109, 25);
		btnChange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String tmpPassword1 = new String(passwordField1.getPassword());
				String tmpPassword2 = new String(passwordField2.getPassword());

				if (tmpPassword1.equals(""))
					return;

				passwordField1.setText("");
				passwordField2.setText("");

				try {
					if (!tmpPassword1.equals(tmpPassword2))
						throw new PasswordsDontMatch();
					facade.changePasswordAdmin(token, owner, tmpPassword1);
					JOptionPane
							.showMessageDialog(null,
									"Password succesfully changed.",
									"Password changed",
									JOptionPane.INFORMATION_MESSAGE);
				} catch (PasswordsDontMatch | RemoteException | InvalidToken
						| RemoteDesyncException | NoDigestAlgorithm e) {
					ExceptionHandler.handle(e);
				}
			}
		});

		final JFrame reference = this;
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(230, 66, 110, 25);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				reference.dispatchEvent(new WindowEvent(reference,
						WindowEvent.WINDOW_CLOSING));
			}
		});
		contentPane.setLayout(null);
		contentPane.add(btnChange);
		contentPane.add(btnCancel);
		contentPane.add(lblPassword1);
		contentPane.add(passwordField1);
		contentPane.add(lblPassword2);
		contentPane.add(passwordField2);
	}
}
