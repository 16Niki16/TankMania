package projectAccess;


import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class SignUpMenu extends JFrame {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SignUpMenu frame = new SignUpMenu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public SignUpMenu() { //JFrame constructor
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 332);
		setTitle("Sing up");
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setBackground(Color.MAGENTA);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(29, 11, 374, 271);
		contentPane.add(panel);
		panel.setLayout(null);

		JTextField textName = new JTextField();
		textName.setBounds(188, 24, 136, 20);
		panel.add(textName);
		textName.setColumns(10);

		JTextField textFamilyName = new JTextField();
		textFamilyName.setBounds(188, 55, 136, 20);
		panel.add(textFamilyName);
		textFamilyName.setColumns(10);

		JTextField textUsername = new JTextField();
		textUsername.setBounds(188, 86, 136, 20);
		panel.add(textUsername);
		textUsername.setColumns(10);

		JTextField textPassword = new JTextField();
		textPassword.setBounds(188, 117, 136, 20);
		panel.add(textPassword);
		textPassword.setColumns(10);

		int n;
		String[] Gender = { "Male", "Female" };
		JComboBox comboGender = new JComboBox(Gender);
		comboGender.setSelectedIndex(1);
		comboGender.setBounds(188, 206, 136, 22);
		panel.add(comboGender);
		String content = comboGender.getSelectedItem().toString();
		if(content.equals("Male")) {
			n = 1;
		}else {
			n=2;
		}

		JTextField textTelNumber = new JTextField();
		textTelNumber.setBounds(188, 148, 136, 20);
		panel.add(textTelNumber);
		textTelNumber.setColumns(10);

		JTextField textEmail = new JTextField();
		textEmail.setBounds(188, 175, 136, 20);
		panel.add(textEmail);
		textEmail.setColumns(10);

		JLabel lblName = new JLabel("Name");
		lblName.setBounds(27, 24, 136, 20);
		panel.add(lblName);

		JLabel lblFamilyName = new JLabel("Family name");
		lblFamilyName.setBounds(27, 58, 136, 20);
		panel.add(lblFamilyName);

		JLabel lblUserName = new JLabel("Username");
		lblUserName.setBounds(27, 86, 136, 20);
		panel.add(lblUserName);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(27, 120, 136, 20);
		panel.add(lblPassword);

		JLabel lblTelNumber = new JLabel("Telephone number");
		lblTelNumber.setBounds(27, 151, 136, 20);
		panel.add(lblTelNumber);

		JLabel lblEmail = new JLabel("Email");
		lblEmail.setBounds(27, 178, 136, 20);
		panel.add(lblEmail);

		JLabel lblGender = new JLabel("Gender");
		lblGender.setBounds(27, 207, 136, 20);
		panel.add(lblGender);

		JButton btnContinue = new JButton("Create your profile!");
		btnContinue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (textName.getText().equals("") || textFamilyName.getText().equals("") || textUsername.getText().equals("") ||
						textPassword.getText().equals("") || textEmail.getText().equals("") || textTelNumber.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Invalid account");
				} else {
					sqlConnection sqlQuery= new sqlConnection();
					try {
						ResultSet r = sqlQuery.getQuery("SELECT COUNT(*) AS rowcount FROM UserData WHERE Username='"+ textUsername.getText()+ "'");
						r.next();
						int count = r.getInt("rowcount");
						r.close();
						if(count == 0) {
							if(Pattern.matches("\\d{10}",textTelNumber.getText())&& Pattern.matches("[a-zA-Z0-9]+[@][a-zA-Z0-9.]+", textEmail.getText())){
								String query = "INSERT INTO UserData ([RealName], [RealFamilyName],[Username],[Password],[Email], [TelephoneNumber], " +
										"[Gender], [History]) VALUES('" + textName.getText() + "','" + textFamilyName.getText() + "','" + textUsername.getText()
										+ "','" + textPassword.getText() + "','" + textEmail.getText() + "','" + textTelNumber.getText() +  "','" + n + "','"+ 0 +"')";
								sqlQuery.updateQuery(query);
								dispose();
							}else{
								JOptionPane.showMessageDialog(null, "Non valid phone number or email");
							}
						}else {
							JOptionPane.showMessageDialog(null, "Username already exists");
						}
					} catch (Exception ex) {
						System.out.println(ex.getMessage());
					}
				}
			}
		});
		btnContinue.setBounds(105, 239, 117, 23);
		panel.add(btnContinue);
	}
}
