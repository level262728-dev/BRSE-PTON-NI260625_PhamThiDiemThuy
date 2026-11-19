package structure.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/db_library_management?useSSL=false&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";

    public static Connection openConnection() {
        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("Lỗi Driver");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối không thành công !!");
            e.printStackTrace();
        }
        return null;
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Lỗi đóng kết nối");
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Connection conn = openConnection();
        if (conn != null) {
            System.out.println("Kết nối thành công tới db_library_management !!");
        }
        closeConnection(conn);
    }
}