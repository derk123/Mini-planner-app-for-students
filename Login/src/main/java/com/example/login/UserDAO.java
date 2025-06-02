package com.example.login;

import java.sql.*;
public class UserDAO {

        public  static User getUser(String username, String password) {
            String query="SELECT * FROM users WHERE username=? AND password=?";

            try(Connection con =ConnexioDB.getConnexion(); PreparedStatement stmt=con.prepareStatement(query)){
                stmt.setString(1,username);
                stmt.setString(2,password);
                ResultSet rs=stmt.executeQuery();

                if(rs.next()){
                    return new User(rs.getString("username"),rs.getString("password"),rs.getInt("id"));
                }
            }catch (SQLException e) {
                throw new RuntimeException("Error retrieving user",e);
            }

            return null;
        }


        public  static boolean createUser(String username, String password) {
            String query="INSERT INTO users (username,password) VALUES (?,?)";
            try(Connection con=ConnexioDB.getConnexion();PreparedStatement stmt=con.prepareStatement(query)){
                stmt.setString(1,username);
                stmt.setString(2,password);
                stmt.executeUpdate();

                return true;
            }catch (SQLException e) {
                return false;
            }
        }
}
