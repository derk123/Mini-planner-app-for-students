package com.example.login;

import java.sql.*;

public class ConnexioDB {
static String user="postgres";
static String password="332468";
static String url="jdbc:postgresql://localhost:5433/myapplication";
static String driver="org.postgresql.Driver";

public static Connection getConnexion(){
    Connection connection= null;
    try{
    Class.forName(driver);
        try {
            connection=DriverManager.getConnection(url,user,password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }catch (ClassNotFoundException e){
        throw new RuntimeException(e);
    }
    return connection;
}

}
