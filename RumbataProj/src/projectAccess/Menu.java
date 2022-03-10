package projectAccess;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import Game.Main;
public class Menu extends JFrame {

	private static String[] username;
	public static void main(String[] args) {
		username = args;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Menu frame = new Menu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Menu() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 577, 432);
		setTitle("Lobby");

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setBackground(Color.pink);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 541, 371);
		contentPane.add(panel);
		panel.setBackground(Color.green);
		panel.setLayout(null);

		JButton btnNewGame = new JButton("Start game");
		btnNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread() {
					public void run() {
						Main.main(username);
					}
				}.start();
				dispose();
			}
		});
		btnNewGame.setBounds(185, 280, 148, 23);
		panel.add(btnNewGame);

		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnExit.setBounds(185, 334, 148, 23);
		panel.add(btnExit);
		
		sqlConnection sqlQuery = new sqlConnection();
		
		int UserID1 = sqlQuery.getFirstID("SELECT ID FROM UserData WHERE Username='"+ username[0] + "'");
		int UserID2 = sqlQuery.getFirstID("SELECT ID FROM UserData WHERE Username='"+ username[1] + "'");
		int count = 0;
		try {
			ResultSet r = sqlQuery.getQuery("SELECT COUNT(*) AS rowcount FROM History WHERE [Winner-ID]='"
					+ UserID1 +"'");
			r.next();
			count = r.getInt("rowcount");
			r.close();
		} catch (SQLException ex) {ex.printStackTrace();}
		
		int count1 = 0;
		try {
			ResultSet r = sqlQuery.getQuery("SELECT COUNT(*) AS rowcount FROM History WHERE [Winner-ID]='"
					+ UserID2 +"'");
			r.next();
			count = r.getInt("rowcount");
			r.close();
		} catch (SQLException ex) {ex.printStackTrace();}
		
		ResultSet score1 = sqlQuery.getQuery("SELECT History FROM UserData WHERE Username='"+ username[0] + "'");
		String score = "";
		try {
			score1.next();
			score = score1.getString("History");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JLabel lblUser1 = new JLabel("User1:" + username[0] + " " + score + " "+ "Victories: " + count);
		lblUser1.setBounds(80, 70, 196, 147);
		panel.add(lblUser1);
		
		ResultSet score2 = sqlQuery.getQuery("SELECT History FROM UserData WHERE Username='"+ username[1] + "'");
		String score3 = "";
		try {
			score2.next();
			score3 = score2.getString("History");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		JLabel lblUser2 = new JLabel("User2: " + username[1] + " " + score3 + " " + "Victories: " + count1);
		lblUser2.setBounds(80, 110, 181, 124);
		panel.add(lblUser2);

		JLabel lblTankMania = new JLabel("Tank Mania");
		lblTankMania.setFont(new Font("Tahoma", Font.PLAIN, 35));
		lblTankMania.setBounds(168, 20, 184, 55);
		panel.add(lblTankMania);
	}
}