package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    Connection con;

    public Connection getConnection() {
        try {
            String access = "jdbc:ucanaccess://ventas.accdb";
            //String access = "jdbc:ucanaccess://D:/ventas.accdb";   Ruta Absoluta miPC
            //String myBD = "jdbc:mysql://localhost:3306/sistemaventa?serverTimezone=UTC"; Ruta LocalHost
            con = DriverManager.getConnection(access);
            return con;
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return null;
    }
}
