package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import domain.UserType;

public class AnonymousMenu extends JFrame {

	private static final long serialVersionUID = 1026093323740695946L;
	private JPanel contentPane;

    /**
     * Create the frame.
     */
    public AnonymousMenu() {

        setTitle("Menu");
        setResizable(false);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new GridLayout(3, 1, 0, 0));

        JButton btnQueryAvailability = new JButton("Query Availability");
        btnQueryAvailability.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new QueryAvailabilityGUI(null);
                frame.setVisible(true);
            }
        });
        contentPane.add(btnQueryAvailability);

		JButton btnSearchHouse = new JButton("Search house");
		btnSearchHouse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFrame frame = new SearchHouseGUI(null);
				frame.setVisible(true);
			}
		});
		contentPane.add(btnSearchHouse);

        JButton btnSignIn = new JButton("Sign in");
        btnSignIn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new CreateUserGUI(null, UserType.CUSTOMER);
                frame.setVisible(true);
            }
        });
        contentPane.add(btnSignIn);
    }

}
