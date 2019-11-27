/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bdd;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Parametres;

/**
 *
 * @author nKBlaZy
 */
public class BaseDeDonnees implements Parametres {
    private String connectUrl = "jdbc:mysql://localhost/projet2048elnt";
    private String username = "projet2048elnt";
    private String password = "projet2048";
    private Connection connection = null;
    private Statement stmt;
    private static final BaseDeDonnees INSTANCE = new BaseDeDonnees();
    
    private BaseDeDonnees () {}
    
    public static BaseDeDonnees getInstance() {
        return INSTANCE;
    }
    
    // Essaye de se connecter à la base de données, retourne true si la connection est établie
    public boolean connection() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            System.out.println("Impossible de trouver le Driver");
            return false;
        }
        try {
            connection = DriverManager.getConnection(connectUrl, username, password);
            stmt = connection.createStatement();
        } catch (SQLException ex) {
            System.out.println("Connection à la base impossible : " + ex.getMessage());
            return false;
        }
        return true;
    }
    
    // Se déconnecte de la base de données
    public void deconnection() {
        try {
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(BaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Créer un nouveau joueur, retourne un code en fonction de la situation
    public int creationJoueur (String _nom, String _mdp) {  
        try {
            int result = stmt.executeUpdate("INSERT INTO Joueur(nom, mdp) VALUES ('" + _nom + "', '" + hashMdp(_mdp) +"')");
            if (result < 1) {
                return NO_UPDATE;
            } else {
                return UPDATED;
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
            return ERROR_SQL;
        }
    }
    
    // Vérifie le nom et le mot de passe et retourne retourne un code en fonction de la situation
    public int connectJoueur(String _nom, String _mdp) {
        try {
            ResultSet rs = stmt.executeQuery("SELECT nom, mdp FROM Joueur WHERE nom = '" + _nom + "'");
            if (rs.first()) {
                if (rs.getString("mdp").equals(hashMdp(_mdp))) {
                    return CORRECT_DATA;
                } else {
                    return ERROR_WRONG_PASS;
                }
            } else {
                return ERROR_NOPLAYER;
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
            return ERROR_SQL;
        }
    }
    
    // Retourne le scoreMax d'un joueur, un code erreur sinon
    public int getScoreMax(String _nom) {
        try {
            ResultSet rs = stmt.executeQuery("SELECT score_max FROM Joueur WHERE nom = '" + _nom + "'");
            if (rs.first()) {
                return rs.getInt("score_max");
            } else {
                return ERROR_NOPLAYER;
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
            return ERROR_SQL;
        }
    }
    
    // Retourne le meilleurDeplacments d'un joueur, un code erreur sinon
    public int getMeilleurDeplacements(String _nom) {
        try {
            ResultSet rs = stmt.executeQuery("SELECT meilleur_deplacements FROM Joueur WHERE nom = '" + _nom + "'");
            if (rs.first()) {
                return rs.getInt("meilleur_deplacements");
            } else {
                return ERROR_NOPLAYER;
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
            return ERROR_SQL;
        }
    }
    
    // Retourne le meilleurTemps d'un joueur, un code erreur sinon
    public int getMeilleurTemps(String _nom) {
        try {
            ResultSet rs = stmt.executeQuery("SELECT meilleur_temps FROM Joueur WHERE nom = '" + _nom + "'");
            if (rs.first()) {
                return rs.getInt("meilleur_temps");
            } else {
                return ERROR_NOPLAYER;
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
            return ERROR_SQL;
        }
    }
    
    // Met à jour le scoreMax d'un joueur et retourne un code en fonction de la situation
    public int setScoreMax(String _nom, int _scoreMax) {
        try {
            int result = stmt.executeUpdate("UPDATE Joueur SET score_max = '" + _scoreMax + "' WHERE nom = '" + _nom + "'");
            if (result < 1) {
                return NO_UPDATE;
            } else {
                return UPDATED;
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
            return ERROR_SQL;
        }
    }
    
    // Met à jour le meilleurDeplacement d'un joueur et retourne un code en fonction de la situation
    public int setMeilleurDeplacements(String _nom, int _meilleurDeplacement) {
        try {
            int result = stmt.executeUpdate("UPDATE Joueur SET meilleur_deplacements = '" + _meilleurDeplacement + "' WHERE nom = '" + _nom + "'");
            if (result < 1) {
                return NO_UPDATE;
            } else {
                return UPDATED;
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
            return ERROR_SQL;
        }
    }
    
    // Met à jour le meilleurTemps d'un joueur et retourne un code en fonction de la situation
    public int setMeilleurTemps(String _nom, int _meilleurTemps) {
        try {
            int result = stmt.executeUpdate("UPDATE Joueur SET meilleur_temps = '" + _meilleurTemps + "' WHERE nom = '" + _nom + "'");
            if (result < 1) {
                return NO_UPDATE;
            } else {
                return UPDATED;
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
            return ERROR_SQL;
        }
    }
    
    // Retourne le classement des joueurs trié par score
    public List<Object[]> getClassementScore() {
        List<Object[]> list = new ArrayList<>();
        try {
            ResultSet rs = stmt.executeQuery("SELECT nom, meilleur_deplacements, meilleur_temps, score_max FROM Joueur ORDER BY score_max DESC");
            while (rs.next()) {
                list.add(new Object[]{rs.getString("nom"), rs.getInt("meilleur_deplacements"), rs.getInt("meilleur_temps"), rs.getInt("score_max")});
            }
            return list;
        } catch (SQLException ex) {
            Logger.getLogger(BaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    // Retourne le classement des joueurs trié par nombre de déplacement
    public List<Object[]> getClassementDeplacements() {
        List<Object[]> list = new ArrayList<>();
        try {
            ResultSet rs = stmt.executeQuery("SELECT nom, meilleur_deplacements, meilleur_temps, score_max FROM Joueur ORDER BY meilleur_deplacements DESC");
            while (rs.next()) {
                list.add(new Object[]{rs.getString("nom"), rs.getInt("meilleur_deplacements"), rs.getInt("meilleur_temps"), rs.getInt("score_max")});
            }
            return list;
        } catch (SQLException ex) {
            Logger.getLogger(BaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    // Retourne le classement des joueurs trié par meilleur temps
    public List<Object[]> getClassementTemps() {
        List<Object[]> list = new ArrayList<>();
        try {
            ResultSet rs = stmt.executeQuery("SELECT nom, meilleur_deplacements, meilleur_temps, score_max FROM Joueur ORDER BY meilleur_temps DESC");
            while (rs.next()) {
                list.add(new Object[]{rs.getString("nom"), rs.getInt("meilleur_deplacements"), rs.getInt("meilleur_temps"), rs.getInt("score_max")});
            }
            return list;
        } catch (SQLException ex) {
            Logger.getLogger(BaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
   
    // Ajoute une partie, retourne un code en fonction de la situation
    public int insertionPartie (String _nom, int _deplacement, int _temps, int _score, int _val_max){
         try {
            int result = stmt.executeUpdate("INSERT INTO Partie(joueur, deplacements, temps, score, valeur_max) VALUES ('" + _nom  + "', '" + _deplacement + "', '" + _temps + "', '" + _score  + "', '" + _val_max +"')");
            if (result < 1) {
                return NO_UPDATE;
            } else {
                return UPDATED;
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
            return ERROR_SQL;
        }
    }
    
    // Retourne l'historique des parties d'un joueur
    public List<int[]> getHistorique(String _joueur) {
        List<int[]> list = new ArrayList<>();
        try {
            ResultSet rs = stmt.executeQuery("SELECT score, deplacements, temps, valeur_max FROM Partie WHERE joueur= '" + _joueur +"'");
            while (rs.next()) {
                list.add(new int[]{rs.getInt("score"), rs.getInt("deplacements"), rs.getInt("temps"), rs.getInt("valeur_max")});
            }
            return list;
        } catch (SQLException ex) {
            Logger.getLogger(BaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    // Retourne le nombre de parties d'un joueur
    public int getNbParties(String joueur){
        try {
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM Partie WHERE joueur= '" + joueur + "'");
            rs.next();
            return rs.getInt("COUNT(*)");
        } catch (SQLException ex) {
            Logger.getLogger(BaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
     }
    
    
    // Hash le mot de passe passé en paramètre avec l'algorithme SHA-256
    private String hashMdp(String _mdp) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte [] bytes = md.digest(_mdp.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Impossible de trouver l'algorithme SHA-256");
        }
        return null;
    }
}
