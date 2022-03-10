package Game;

import javax.swing.JFrame;

public class Main extends JFrame {
	private static String[] usernames;
	public Main() {
		Gameplay gg=new Gameplay(this);
		gg.userNames=usernames;
		add(gg);
		setTitle("Board");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000,1000);
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);
	}
	public static void main(String[] args) {
		usernames =args;
		new Main();
	}
}
