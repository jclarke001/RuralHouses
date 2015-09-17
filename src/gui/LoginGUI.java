package gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import businessLogic.FacadeImplementation;
import businessLogic.FacadeInterface;
import configuration.ConfigXML;
import exceptions.AuthenticationError;
import exceptions.CorruptedCache;
import exceptions.InvalidToken;
import exceptions.NoDigestAlgorithm;

public class LoginGUI extends JFrame {

	private static final long serialVersionUID = -465646312032476568L;
	private JPanel contentPane;
	private JTextField txtUsername;
	private JPasswordField passwordField;

	private static configuration.ConfigXML c;

	public static FacadeInterface facadeInterface;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				LoginGUI frame = new LoginGUI();
				frame.setVisible(true);

				try {
					c = ConfigXML.getInstance();

					System.setProperty("java.security.policy",
							c.getJavaPolicyPath());
					System.setSecurityManager(new RMISecurityManager());
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());

					c = configuration.ConfigXML.getInstance();
					if (c.isBusinessLogicLocal())
						facadeInterface = new FacadeImplementation();
					else {
						final String businessLogicNode = c
								.getBusinessLogicNode();
						// Remote service name
						String serviceName = "/" + c.getServiceRMI();
						// RMI server port number
						int portNumber = Integer.parseInt(c.getPortRMI());
						// RMI server host IP IP
						facadeInterface = (FacadeInterface) Naming
								.lookup("rmi://" + businessLogicNode + ":"
										+ portNumber + serviceName);
					}
				} catch (RemoteException | ClassNotFoundException
						| InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException
						| NoDigestAlgorithm | MalformedURLException | NotBoundException
						| com.db4o.ext.DatabaseFileLockedException e) {
					ExceptionHandler.handle(e);
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoginGUI() {
		setResizable(false);
		setTitle("Rural Houses");

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				FacadeInterface facade = LoginGUI.facadeInterface;
				try {
					if (c.isBusinessLogicLocal())
						facade.close();
				} catch (Exception e1) {
					System.out
							.println("Error: "
									+ e1.toString()
									+ " , probably problems with Business Logic or Database");
				}
				System.exit(0);
			}
		});
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); unnecessary as
		// windowClosing calls System.exit();
		setBounds(100, 100, 420, 193);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JLabel lblUsername = new JLabel("Username:");

		JLabel lblPassword = new JLabel("Password:");

		txtUsername = new JTextField();
		txtUsername.setColumns(10);

		passwordField = new JPasswordField();

		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String tmpUsername = txtUsername.getText();
				String tmpPassword = new String(passwordField.getPassword());
				txtUsername.setText("");
				passwordField.setText("");
				JFrame frame;
				try {
					List<Byte> token = facadeInterface.login(tmpUsername,
							tmpPassword);
					switch (facadeInterface.getUserType(token)) {
					case OWNER:
						frame = new OwnerMenu(token);
						frame.setVisible(true);
						break;
					case ADMINISTRATOR:
						frame = new AdminMenu(token);
						frame.setVisible(true);
						break;
					case CUSTOMER:
						frame = new CustomerMenu(token);
						frame.setVisible(true);
						break;
					}
				} catch (RemoteException | AuthenticationError
						| NoDigestAlgorithm | InvalidToken | CorruptedCache e) {
					ExceptionHandler.handle(e);
				}
			}
		});

		JButton btnAnonymous = new JButton("Anonymous");
		btnAnonymous.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtUsername.setText("");
				passwordField.setText("");
				JFrame frame = new AnonymousMenu();
				frame.setVisible(true);
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane
				.setHorizontalGroup(gl_contentPane
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								Alignment.TRAILING,
								gl_contentPane
										.createSequentialGroup()
										.addGroup(
												gl_contentPane
														.createParallelGroup(
																Alignment.TRAILING)
														.addGroup(
																Alignment.LEADING,
																gl_contentPane
																		.createSequentialGroup()
																		.addGap(92)
																		.addComponent(
																				btnLogin,
																				GroupLayout.PREFERRED_SIZE,
																				109,
																				GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addComponent(
																				btnAnonymous))
														.addGroup(
																gl_contentPane
																		.createSequentialGroup()
																		.addGap(78)
																		.addGroup(
																				gl_contentPane
																						.createParallelGroup(
																								Alignment.LEADING)
																						.addComponent(
																								lblUsername)
																						.addComponent(
																								lblPassword))
																		.addPreferredGap(
																				ComponentPlacement.UNRELATED)
																		.addGroup(
																				gl_contentPane
																						.createParallelGroup(
																								Alignment.LEADING)
																						.addComponent(
																								passwordField,
																								GroupLayout.DEFAULT_SIZE,
																								165,
																								Short.MAX_VALUE)
																						.addComponent(
																								txtUsername,
																								GroupLayout.DEFAULT_SIZE,
																								165,
																								Short.MAX_VALUE))))
										.addGap(68)));
		gl_contentPane
				.setVerticalGroup(gl_contentPane
						.createParallelGroup(Alignment.TRAILING)
						.addGroup(
								Alignment.LEADING,
								gl_contentPane
										.createSequentialGroup()
										.addGap(38)
										.addGroup(
												gl_contentPane
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																lblUsername)
														.addComponent(
																txtUsername,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												ComponentPlacement.UNRELATED)
										.addGroup(
												gl_contentPane
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																lblPassword)
														.addComponent(
																passwordField,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE))
										.addGap(26)
										.addGroup(
												gl_contentPane
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																btnAnonymous)
														.addComponent(btnLogin))
										.addContainerGap(66, Short.MAX_VALUE)));
		contentPane.setLayout(gl_contentPane);
	}

	public static FacadeInterface getBusinessLogic() {
		return facadeInterface;
	}
}
