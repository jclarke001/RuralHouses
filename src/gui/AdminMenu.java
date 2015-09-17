package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
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

import exceptions.InvalidPassword;
import exceptions.InvalidToken;
import exceptions.NoDigestAlgorithm;

public class AdminMenu extends JFrame {

	private static final long serialVersionUID = 2694058342688595187L;
	private JPanel contentPane;

	private List<Byte> token;

	public AdminMenu(List<Byte> tkn) {
		this.token = tkn;

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					LoginGUI.getBusinessLogic().logout(token);
				} catch (RemoteException e1) {
					ExceptionHandler.handle(e1);
				}
			}
		});

		setTitle("Administrator");
		setResizable(false);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(4, 1, 0, 0));

		JButton btnManageProperties = new JButton("Manage properties");
		btnManageProperties.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new ManagePropertiesGUI(token);
				frame.setVisible(true);
			}
		});
		contentPane.add(btnManageProperties);

		JButton btnManageOwners = new JButton("Manage owners");
		btnManageOwners.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFrame frame = new ManageOwnersGUI(token);
				frame.setVisible(true);
			}
		});
		contentPane.add(btnManageOwners);

		JButton btnChangePass = new JButton("Change password");
		btnChangePass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFrame frame = new ChangePasswordGUI(token);
				frame.setVisible(true);
			}
		});
		contentPane.add(btnChangePass);

		JButton btnResetDB = new JButton("Reset database");
		btnResetDB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String password = askPassword();
				if (password != null) {
					String text = askConfirmation();
					if (text != null && text.equals("RESET")) {
						try {
							LoginGUI.getBusinessLogic()
									.resetDB(token, password);
							JOptionPane.showMessageDialog(null,
									"Closing the application in order to ensure"
											+ "correct data synchronization",
									"Closing...",
									JOptionPane.INFORMATION_MESSAGE);
							LoginGUI.getBusinessLogic().close();
							System.exit(0);
						} catch (RemoteException | InvalidToken
								| NoDigestAlgorithm | InvalidPassword e) {
							ExceptionHandler.handle(e);
						}
					}
				}
			}
		});
		contentPane.add(btnResetDB);
	}

	private String askPassword() {
		String password = null;

		JPanel panel = new JPanel();
		JLabel label = new JLabel("Enter the password:");
		JPasswordField pass = new JPasswordField(10);
		panel.add(label);
		panel.add(pass);
		String[] options = new String[] { "OK", "Cancel" };
		int option = JOptionPane.showOptionDialog(null, panel,
				"Reset database", JOptionPane.NO_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, options, options[1]);
		if (option == 0) {
			password = new String(pass.getPassword());
		}
		return password;
	}

	private String askConfirmation() {
		String text = null;

		JPanel panel = new JPanel();
		JLabel label = new JLabel(
				"Enter reset in upercase to delete all the database\n");
		JTextField txt = new JTextField();
		txt.setColumns(10);
		panel.add(label);
		panel.add(txt);
		String[] options = new String[] { "OK", "Cancel" };
		int option = JOptionPane.showOptionDialog(null, panel,
				"Reset database", JOptionPane.NO_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, options, options[1]);
		if (option == 0) {
			text = txt.getText();
		}
		return text;
	}
}
