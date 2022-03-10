package projectAccess;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DeleteProfile extends JFrame {

	private JPanel contentPane;
	static String user;

	public static void main(String[] args) {
		user = args[0];
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DeleteProfile frame = new DeleteProfile();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public DeleteProfile() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 264, 180);
		setTitle("Delete Profile");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setBackground(Color.green);
		contentPane.setLayout(null);

		JButton btnBackToTheLobby = new JButton("close window");
		btnBackToTheLobby.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnBackToTheLobby.setBounds(29, 45, 185, 23);
		contentPane.add(btnBackToTheLobby);

		sqlConnection sqlQuery = new sqlConnection();
		JButton btnDeleteAccount = new JButton("Delete account");
		btnDeleteAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					sqlQuery.updateQuery("DELETE FROM UserData WHERE Username ='" + user + "'");
					dispose();
			}
		});
		btnDeleteAccount.setBounds(29, 11, 185, 23);
		contentPane.add(btnDeleteAccount);
	}
}
