package projectAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class sqlConnection {
   // private String connection="jdbc:ucanaccess://DataBase/Database8.accdb";
    private String connectionMySQL = "jdbc:mysql://localhost:3306/project";

    public sqlConnection() {}

    
    public ResultSet getQuery(String query){ //returns query result
        try {
            Connection conn = DriverManager.getConnection(connectionMySQL, "root", "root");
            Statement st = conn.createStatement();
            ResultSet result = st.executeQuery(query);
            //conn.close();
            return result;
        }catch (Exception ex) {
            System.out.println(ex);
            return null;
        }
    }
    public void updateQuery(String query){ //updates the database
        try {
            Connection conn = DriverManager.getConnection(connectionMySQL, "root", "root");
            Statement st = conn.createStatement();
            st.executeUpdate(query);
            conn.close();
        }catch (Exception ex) {
            System.out.println(ex);
        }
    }
    

    public int getFirstID(String query){ //returns the ID of the user with that name, returns -1 if it crashes
        ResultSet r = getQuery(query);
        try {
            r.next();
            int m = r.getInt("ID");
            r.close();
            return  m;
        }catch (Exception ex) {
            System.out.println(ex.getMessage());
            return -1;
        }
    }
    
}
