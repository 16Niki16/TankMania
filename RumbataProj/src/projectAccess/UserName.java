package projectAccess;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class UserName extends JFrame {

	private JPanel contentPane;
	private JTextField TextUser;
	private JTextField TextPassword;
	private JTextField TextUser1;
	private JTextField TextPassword1;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UserName frame = new UserName();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public UserName() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 468, 314);
		setTitle("Sing in");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setBackground(Color.green);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(5, 5, 468, 309);
		contentPane.add(panel);
		panel.setBackground(Color.pink);
		panel.setLayout(null);

		JLabel lblUser = new JLabel("Username1");
		//lblUser.setForeground(Color.BLACK);
		lblUser.setBounds(32, 24, 89, 14);
		panel.add(lblUser);

		JLabel lblPassword = new JLabel("Password1");
		//lblPassword.setForeground(Color.BLACK);
		lblPassword.setBounds(32, 67, 89, 14);
		panel.add(lblPassword);

		TextUser = new JTextField();
		TextUser.setBounds(172, 21, 133, 20);
		panel.add(TextUser);
		TextUser.setColumns(10);

		TextPassword = new JTextField();
		TextPassword.setBounds(172, 64, 133, 20);
		panel.add(TextPassword);
		TextPassword.setColumns(10);

		sqlConnection sqlQuery = new sqlConnection();
		JButton btnDeleteMenu = new JButton("Delete profile");
		btnDeleteMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ResultSet r = sqlQuery.getQuery("SELECT COUNT(*) AS rowcount FROM UserData WHERE Username='"
									+ TextUser.getText()+"' AND Password='"+TextPassword.getText()+"'");
					r.next();
					int count = r.getInt("rowcount");
					r.close();
					if(count==1){
						new Thread() {
							public void run() {
								DeleteProfile.main(new String[] {TextUser.getText()});
							}
						}.start();
					}else{
						
						JOptionPane.showMessageDialog(null, "Invalid account");
						/*lblUser.setForeground(Color.RED);
						lblPassword.setForeground(Color.RED);
						panel.add(lblUser);*/
					}
				} catch (SQLException ex) {ex.printStackTrace();}
			}
		});
		btnDeleteMenu.setBounds(313, 21, 128, 23);
		panel.add(btnDeleteMenu);


		JLabel lblUser1 = new JLabel("Username2");
		lblUser1.setBounds(32, 124, 89, 14);
		panel.add(lblUser1);

		JLabel lblPassword1 = new JLabel("Password2");
		lblPassword1.setBounds(32, 167, 89, 14);
		panel.add(lblPassword1);

		TextUser1 = new JTextField();
		TextUser1.setBounds(172, 121, 133, 20);
		panel.add(TextUser1);
		TextUser1.setColumns(10);

		TextPassword1 = new JTextField();
		TextPassword1.setBounds(172, 164, 133, 20);
		panel.add(TextPassword1);
		TextPassword1.setColumns(10);

		JButton btnDeleteMenu1 = new JButton("Delete profile");
		btnDeleteMenu1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ResultSet r = sqlQuery.getQuery("SELECT COUNT(*) AS rowcount FROM UserData WHERE Username='"
							+ TextUser1.getText()+"' AND Password='"+TextPassword1.getText()+"'");
					r.next();
					int count = r.getInt("rowcount");
					r.close();
					if(count==1){
						new Thread() {
							public void run() {
								DeleteProfile.main(new String[] {TextUser1.getText()});
							}
						}.start();
					}else{
						JOptionPane.showMessageDialog(null, "Invalid account");
					}
				} catch (SQLException ex) {ex.printStackTrace();}
			}
		});
		btnDeleteMenu1.setBounds(313, 121, 128, 23);
		panel.add(btnDeleteMenu1);


		JButton btnSignIn = new JButton("Sign Up");
		btnSignIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread() {
					public void run() {
						SignUpMenu.main(new String[] {});
					}
				}.start();
			}
		});
		btnSignIn.setBounds(207, 205, 121, 23);
		panel.add(btnSignIn);

		JButton AddButton = new JButton("Play");
		AddButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (TextUser.getText().equals("")||TextPassword.getText().equals("")||TextUser1.getText().equals("")||
						TextPassword1.getText().equals("")||TextUser.getText().equals(TextUser1.getText())) {
					JOptionPane.showMessageDialog(null, "Invalid or same accounts");
				} else {
					try {
						ResultSet r = sqlQuery.getQuery("SELECT COUNT(*) AS rowcount FROM UserData WHERE Username='"
								+ TextUser.getText()+"' AND Password='"+TextPassword.getText()+"'");
						r.next();
						int count = r.getInt("rowcount");
						r.close();
						ResultSet r2 = sqlQuery.getQuery("SELECT COUNT(*) AS rowcount FROM UserData WHERE Username='"
								+ TextUser1.getText()+"' AND Password='"+TextPassword1.getText()+"'");
						r2.next();
						int count2 = r2.getInt("rowcount");
						r2.close();
						if(count == 1 && count2==1) {
							new Thread() {
								public void run() {
									Menu.main(new String[] {TextUser.getText(), TextUser1.getText()});
								}
							}.start();
							dispose();
						}else {
							JOptionPane.showMessageDialog(null, "Invalid accounts");
						}
					} catch (Exception ex) {
						System.out.println(ex.getMessage());
					}
				}
			}
		});
		AddButton.setBounds(32, 205, 121, 23);
		panel.add(AddButton);
	}
}
