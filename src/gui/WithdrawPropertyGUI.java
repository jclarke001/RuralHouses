package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JScrollPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.util.Vector;

import javax.swing.ListSelectionModel;

import domain.Owner;
import domain.RuralHouse;
import businessLogic.ApplicationFacadeInterface;

public class WithdrawPropertyGUI extends JFrame {

    private JPanel contentPane;
    private DefaultListModel lstPropertiesInfo = new DefaultListModel();
    private JList lstProperties = new JList(lstPropertiesInfo);

    private Owner owner;

    /**
     * Create the frame.
     */
    public WithdrawPropertyGUI(Owner owner) {
        this.owner = owner;
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        JButton btnWithdrawProperty = new JButton("Withdraw property");
        btnWithdrawProperty.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                RuralHouse rh = (RuralHouse) lstProperties.getSelectedValue();

                if (rh != null) {
                    boolean confirmed = askConfirmation();
                    if (confirmed) {
                        ApplicationFacadeInterface facade = StartWindow.getBusinessLogic();
                        try {
                            facade.removeRuralHouse(rh);
                        } catch (RemoteException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        updateList();
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane();
        GroupLayout gl_contentPane = new GroupLayout(contentPane);
        gl_contentPane.setHorizontalGroup(
            gl_contentPane.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_contentPane.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_contentPane.createSequentialGroup()
                            .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE)
                            .addContainerGap())
                        .addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
                            .addComponent(btnWithdrawProperty)
                            .addGap(131))))
        );
        gl_contentPane.setVerticalGroup(
            gl_contentPane.createParallelGroup(Alignment.TRAILING)
                .addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.UNRELATED)
                    .addComponent(btnWithdrawProperty)
                    .addContainerGap(16, Short.MAX_VALUE))
        );
        lstProperties.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        scrollPane.setViewportView(lstProperties);
        contentPane.setLayout(gl_contentPane);

        this.updateList();
    }

    private void updateList() {
        this.lstPropertiesInfo.clear();

        //Obtain the business logic from a StartWindow class (local or remote)
        ApplicationFacadeInterface facade = StartWindow.getBusinessLogic();

        Vector<RuralHouse> result = null;
        try {
            result = facade.getRuralHouses(this.owner);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (result != null) {
            for (RuralHouse rh : result) {
                this.lstPropertiesInfo.addElement(rh);
            }
        }
    }

    private boolean askConfirmation() {
        String[] messages = { "Yes", "No" };
        int result = JOptionPane.showOptionDialog(null,
                "Are you sure you want to delete the selected property?",
                "Delete", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, messages, 0);
        return (result == 0);
    }
}
