/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bdd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author nKBlaZy
 */
public class BaseDeDonnees {
    private String connectUrl = "jdbc:mysql://localhost/projet2048elnt";
    private String username = "projet2048elnt";
    private String password = "projet2048";
    private Connection connection = null;
    
    // Essaye de se connecter à la base de données, retourne true si la connection est établie
    public boolean connection() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            return false;
        }
        try {
            connection = DriverManager.getConnection(connectUrl, username, password);
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }
    
}
