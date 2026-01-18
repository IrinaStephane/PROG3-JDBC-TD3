import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private final String url;
    private final String username;
    private final String password;

    public DBConnection() {
        this.url = System.getenv("DB_URL");
        this.username = System.getenv("DB_USERNAME");
        this.password = System.getenv("DB_PASSWORD");
    }

    public Connection getDBConnection(){
        try {
            return DriverManager.getConnection(url,username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
