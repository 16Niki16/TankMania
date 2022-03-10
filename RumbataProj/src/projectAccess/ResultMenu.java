package projectAccess;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Game.Main;

import javax.swing.JTextField;
import javax.swing.JButton;

public class ResultMenu extends JFrame {

	private JPanel contentPane;

	static String[] users;
	static int WinnerScore;

	public static void main(String[] args, int score) {
		users = args;
		WinnerScore = score;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ResultMenu frame = new ResultMenu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ResultMenu() { //constructor
		executeUpdateQueries();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setTitle("Results");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setBackground(Color.magenta);
		contentPane.setLayout(null);

		JLabel results = new JLabel("winner " + users[0] + " " + "score: " + WinnerScore);
		results.setBounds(10, 59, 206, 92);
		contentPane.add(results);

		JLabel results1 = new JLabel("loser " + users[1] + " " + "score: 0");
		results1.setBounds(10, 124, 328, 23);
		contentPane.add(results1);

		JButton btnNewGame = new JButton("New game");
		btnNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread() {
					public void run() {
						Main.main(users);
					}
				}.start();
				dispose();
			}
		});
		btnNewGame.setBounds(10, 199, 111, 23);
		contentPane.add(btnNewGame);

		JButton btnBackToLobby = new JButton("Back to lobby");
		btnBackToLobby.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread() {
					public void run() {
						Menu.main(users);
					}
				}.start();
				dispose();
			}
		});
		btnBackToLobby.setBounds(152, 199, 123, 23);
		contentPane.add(btnBackToLobby);

		JButton btnExitGame = new JButton("Exit game");
		btnExitGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		btnExitGame.setBounds(303, 199, 111, 23);
		contentPane.add(btnExitGame);
	}
	private void executeUpdateQueries(){ // updates user score, adds a game in history,
		sqlConnection sqlQuery = new sqlConnection();
		String updateWinnerScore = "UPDATE UserData SET UserData.History =" + WinnerScore
				+ "+ UserData.History WHERE Username='" + users[0] + "'";
		sqlQuery.updateQuery(updateWinnerScore);

		int winnerID = sqlQuery.getFirstID("SELECT ID FROM UserData WHERE Username='"+ users[0] + "'");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime now = LocalDateTime.now();
		String AddHistoryRecord = "INSERT INTO History ([DateMatchMaking], [Winner-ID]) VALUES('" + dtf.format(now) + "','" + winnerID + "')";
		sqlQuery.updateQuery(AddHistoryRecord);

		int matchID = sqlQuery.getFirstID("SELECT * FROM History ORDER BY ID DESC LIMIT 1");
		String addConnection = "INSERT INTO Connect ([User-ID], [Match-ID]) VALUES('"+ winnerID + "','" + matchID + "')";
		sqlQuery.updateQuery(addConnection);

		int loserID = sqlQuery.getFirstID("SELECT ID FROM UserData WHERE Username='"+ users[1] + "'");
		addConnection = "INSERT INTO Connect ([User-ID], [Match-ID]) VALUES('"+ loserID + "','" + matchID + "')";
		sqlQuery.updateQuery(addConnection);

	}
}
