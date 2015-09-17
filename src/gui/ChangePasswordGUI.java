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
import exceptions.InvalidPassword;
import exceptions.InvalidToken;
import exceptions.NoDigestAlgorithm;
import exceptions.PasswordsDontMatch;
import exceptions.RemoteDesyncException;

public class ChangePasswordGUI extends JFrame {

	private static final long serialVersionUID = 2294281513788541004L;
	private JPanel contentPane;
	private JPasswordField passwordField;
	private JPasswordField passwordField1;
    private JPasswordField passwordField2;

    private FacadeInterface facade;
    private List<Byte> token;

    /**
     * Create the frame.
     */
    public ChangePasswordGUI(List<Byte> tkn) {
        final JFrame reference = this;

    	this.facade = LoginGUI.getBusinessLogic();
    	this.token = tkn;

        setTitle("Change password");
        setResizable(false);

        setBounds(100, 100, 420, 211);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        JLabel lblPassword = new JLabel("Current password:");
        lblPassword.setBounds(79, 29, 123, 15);
        JLabel lblPassword1 = new JLabel("New password:");
        lblPassword1.setBounds(79, 62, 110, 15);
        JLabel lblPassword2 = new JLabel("New password:");
        lblPassword2.setBounds(79, 93, 110, 15);

		passwordField1 = new JPasswordField();
		passwordField1.setBounds(225, 60, 154, 19);
		passwordField2 = new JPasswordField();
		passwordField2.setBounds(225, 91, 154, 19);

		JButton btnChange = new JButton("Change");
		btnChange.setBounds(104, 137, 109, 25);
		btnChange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String tmpPassword = new String(passwordField.getPassword());
				String tmpPassword1 = new String(passwordField1.getPassword());
				String tmpPassword2 = new String(passwordField2.getPassword());

				if (tmpPassword1.equals("")) return;

				passwordField.setText("");
				passwordField1.setText("");
				passwordField2.setText("");

				try {
					if (!tmpPassword1.equals(tmpPassword2))
						throw new PasswordsDontMatch();
					facade.changePassword(token, tmpPassword, tmpPassword1);
					JOptionPane.showMessageDialog(null,
							"Password succesfully changed.", "Password changed",
							JOptionPane.INFORMATION_MESSAGE);
					reference.dispatchEvent(new WindowEvent(reference,
							WindowEvent.WINDOW_CLOSING));
				} catch (PasswordsDontMatch | RemoteException
						| RemoteDesyncException | InvalidToken
						| NoDigestAlgorithm | InvalidPassword e) {
					ExceptionHandler.handle(e);
				}
			}
		});



        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBounds(219, 137, 110, 25);
        btnCancel.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		reference.dispatchEvent(new WindowEvent(reference, WindowEvent.WINDOW_CLOSING));
        	}
        });

        passwordField = new JPasswordField();
        passwordField.setBounds(225, 27, 154, 19);
        contentPane.setLayout(null);
        contentPane.add(btnChange);
        contentPane.add(btnCancel);
        contentPane.add(lblPassword);
        contentPane.add(lblPassword1);
        contentPane.add(passwordField1);
        contentPane.add(lblPassword2);
        contentPane.add(passwordField2);
        contentPane.add(passwordField);
    }
}
