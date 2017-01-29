package Police.Techno.DB;

/**
 * Created by kubri on 1/28/2017.
 */
import Police.Techno.Post.Post;
import java.sql.*;

public class DBManagement {
    public static void main(String[] args) {

    }

    public static Connection connect(String path) throws Exception {
        Connection c = null;
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:"+path+".db");
        System.out.println("Opened database successfully");
        return c;
    }

    public static void closeConnection(Connection c) {
        try {
            c.close();
        }
        catch (SQLException e) {
            System.err.println("SQL EXCEPTION!");
            System.exit(-1);
        }
        System.out.println("Connection closed");
    }

    public  static void createTable(Connection c) throws SQLException {
        Statement stmt = null;
        stmt = c.createStatement();
        String sql = "CREATE TABLE POSTS " +
                "(ID INTEGER PRIMARY KEY    AUTOINCREMENT, " +
                "TITLE TEXT NOT NULL, " +
                "POST TEXT  NOT NULL, " +
                "AUTHOR TEXT    NOT NULL)";
        stmt.executeUpdate(sql);
        stmt.close();
        System.out.println("Table created successfully");
    }

    public static void addPost(Connection c, String title, String post, String author) {
        try {
            Statement stmt = c.createStatement();
            String sql = "INSERT INTO POSTS (TITLE,POST,AUTHOR) " +
                    "VALUES ('" +
                    title + "', '" +
                    post + "', '" +
                    author + "');";
            stmt.executeUpdate(sql);
            stmt.close();
        }
        catch (SQLException e){
            System.err.println("SQL EXCEPTION!");
            System.exit(-1);
        }
    }

    public static int getMaxId(Connection c) {
        Statement stmt;
        int max = 0;
        try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM POSTS ORDER BY ID DESC LIMIT 1;");
            max = rs.getInt("ID");
        }
        catch (SQLException e) {
            System.err.println("SQL EXCEPTION!");
            System.exit(-1);
        }
        return max;
    }

    public static Post getPost(Connection c, int id) {
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = c.createStatement();
            rs = stmt.executeQuery("SELECT * FROM POSTS WHERE ID = "+
                    Integer.toString(id)+";");
        }
        catch (SQLException e) {
            System.err.println("SQL EXCEPTION!");
            System.exit(-1);
        }
        Post post = null;
        try {
            post = new Post(rs.getString("TITLE"), rs.getString("POST"), rs.getString("AUTHOR"));
        }
        catch (SQLException e) {
            System.err.println("SQL EXCEPTION!");
            System.exit(-1);
        }
        return post;
    }
}
