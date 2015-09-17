package gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Vector;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import businessLogic.FacadeInterface;
import domain.Booking;
import exceptions.InvalidToken;
import exceptions.RemoteDesyncException;

public class ManageBookingsGUI extends JFrame {

	private static final long serialVersionUID = -967819446887329739L;

	private JPanel contentPane;

	private DefaultTableModel tableModel;
	private JTable table;
	private List<Booking> bookings;
	private String[] columnNames = new String[] {
		"Offer number",
		"First day",
		"Last day",
		"Price",
		"Rural house",
		"Booking status"
	};

	private FacadeInterface businessLogic;

	private List<Byte> token;

	/**
	 * Create the frame.
	 */
	public ManageBookingsGUI(List<Byte> tkn) {
	    this.token = tkn;
	    businessLogic = LoginGUI.getBusinessLogic();

		setTitle("Manage bookings");
		setResizable(false);
		setBounds(100, 100, 662, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		table = new JTable();
		tableModel = new DefaultTableModel(null, columnNames);
		table.setModel(tableModel);

		updateList();

		JLabel lblBookings = new JLabel("Bookings");
		lblBookings.setFont(new Font("Dialog", Font.BOLD, 14));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(table);

		JButton btnNewBooking = new JButton("New booking");
		btnNewBooking.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFrame frame = new QueryAvailabilityGUI(token);
				frame.setVisible(true);
			}
		});

		JButton btnCancelBooking = new JButton("Cancel booking");
		btnCancelBooking.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelBooking();
			}
		});

		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateList();
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(290)
							.addComponent(lblBookings))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(12)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 615, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(95)
							.addComponent(btnNewBooking, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnCancelBooking, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnUpdate, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)))
					.addGap(21))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(12)
					.addComponent(lblBookings)
					.addGap(9)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 298, GroupLayout.PREFERRED_SIZE)
					.addGap(31)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnCancelBooking)
						.addComponent(btnNewBooking)
						.addComponent(btnUpdate)))
		);
		contentPane.setLayout(gl_contentPane);
	}

	private void updateList() {
		while (tableModel.getRowCount() != 0)
			tableModel.removeRow(0);

		Vector<Object> row;

        try {
            bookings = businessLogic.getBookings(token);
            if (bookings != null) {
                for (Booking b : bookings) {
                    if (b.getOffer().getFirstDay().getTime() > System
                            .currentTimeMillis()) {
            			row = new Vector<Object>();

	        			row.add(b.getOffer().getId());
	        			row.add(b.getOffer().getFirstDay());
	        			row.add(b.getOffer().getLastDay());
	        			row.add(b.getOffer().getPrice());
	        			row.add(b.getOffer().getRuralHouse());
        				row.add(b.getStatus());
	        			tableModel.addRow(row);
                    }
                }
            }
        } catch (RemoteException | InvalidToken e) {
        	ExceptionHandler.handle(e);
        }
	}

    private boolean askConfirmation() {
        String[] messages = { "Yes", "No" };
        int result = JOptionPane.showOptionDialog(null,
                "Are you sure you want to cancel the selected booking?",
                "Delete", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, messages, 0);
        return (result == 0);
    }

	private void cancelBooking() {
		int i = table.getSelectedRow();
		if (i != -1 && askConfirmation()) {
            try {
                businessLogic.cancelBooking(token, bookings.get(i));
			} catch (RemoteException | InvalidToken
					| RemoteDesyncException e) {
				ExceptionHandler.handle(e);
			}
            updateList();
		}
	}
}
