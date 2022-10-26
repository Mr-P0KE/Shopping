package Test;

import java.sql.*;

public class FindP {
    public FindP() throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
    }

    public static Connection getConnection() throws SQLException {
        Connection c  = DriverManager.getConnection("jdbc:mysql://localhost:3306/tmall?CharacterEncoding = UTF-8","root","admin");
        return c;
    }
    public static void Check(String username,String password,Connection c){
        if(username ==null || password ==null){
            System.out.println("null!!!");
        }else {
            try (PreparedStatement ps = c.prepareStatement("select *from user where name = ? and password = ?")){
                ps.setString(1,username);
                ps.setString(2,password);

                ResultSet b = ps.executeQuery();
                if(b.next()){
                    System.out.println("True");
                }else
                    System.out.println("Error");

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) throws SQLException {
        Connection c = getConnection();
        Check("","123",c);
    }
}
