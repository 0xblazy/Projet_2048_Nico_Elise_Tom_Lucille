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
 * Permet la communication avec la base de données.<br>
 * La classe s'utilise de la manière suivante:<br>
 * <pre>{@code
 * BaseDeDonnees bdd = BaseDeDonnes.getInstance();
 * if (bdd.connection()) {
 *     ...
 *     bdd.deconnection();
 * }
 * }</pre>
 * La base de données a la structure suivante :<br>
 * <pre> {@code
 * Joueur(nom PK, mdp, meilleur_deplacements, meilleur_temps, score_max)
 * Partie(id PK, joueur FK(Joueur.nom), deplacements, temps, score, valeur_max)
 * }</pre>
 * 
 * @author nKBlaZy
 * @author elise-per
 */
public class BaseDeDonnees implements Parametres {
    private String connectUrl = "jdbc:mysql://localhost/projet2048elnt";
    private String username = "projet2048elnt";
    private String password = "projet2048";
    private Connection connection = null;
    private Statement stmt;
    private static final BaseDeDonnees INSTANCE = new BaseDeDonnees();
    
    private BaseDeDonnees () {}
    
    /**
     * Retourne l'instance de <code>BaseDeDonnees</code>.
     * Permet de suivre le patron de conception Singleton.
     * 
     * @return L'intance de <code>BaseDeDonnees</code>.
     */
    public static BaseDeDonnees getInstance() {
        return INSTANCE;
    }
    
    /**
     * Essaye de se connecter à la base de données.
     * 
     * @return <code>true</code> si la connexion est établie, <code>false</code> sinon.
     */
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
    
    /**
     * Déconnecte de la base de données.
     */
    public void deconnection() {
        try {
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(BaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Créer un nouveau joueur dans la base de données.
     * 
     * @param _nom Nom du joueur.
     * @param _mdp Mot de passe du joueur.
     * @return <code>UPDATED</code> si le joueur a été créé.<br>
     * <code>NO_UPDATE</code> si le joueur n'a pas été crée (déjà existant dans la base de données).<br>
     * <code>ERROR_SQL</code> si il y a un problème avec la requête SQL.
     * 
     * @see model.Parametres
     */
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
    
    /**
     * Vérifie si un joueur existe déjà dans la base de données.
     * 
     * @param _nom Nom du joueur à vérifier.
     * @return <code>true</code> si il existe, <code>false</code> sinon.
     */
    public boolean joueurExiste(String _nom) {
        try {
            ResultSet rs = stmt.executeQuery("SELECT nom FROM Joueur WHERE nom = '" + _nom + "'");
            if (rs.first()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Vérifie le nom et le mot de passe afin de connecter le joueur.
     * 
     * @param _nom Nom du joueur.
     * @param _mdp Mot de passe du joueur.
     * @return <code>CORRECT_DATA</code> si le nom et le mot de passe sont corrects.<br>
     * <code>ERROR_WRONG_PASS</code> si le mot de passe est incorrect.<br>
     * <code>ERROR_NOPLAYER</code> si le joueur n'existe pas.<br>
     * <code>ERROR_SQL</code> si il y a un problème avec la requête SQL.
     * 
     * @see model.Parametres
     */
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
    
    /**
     * Retourne le score maximal d'un joueur.
     * 
     * @param _nom Nom du joueur.
     * @return Le score maximal du joueur si il n'y a pas d'erreur.<br>
     * <code>ERROR_NOPLAYER</code> si le joueur n'existe pas.<br>
     * <code>ERROR_SQL</code> si il y a un problème avec la requête SQL.
     * 
     * @see model.Parametres
     */
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
    
    /**
     * Retourne le meilleur déplacements d'un joueur.
     * 
     * @param _nom Nom du joueur.
     * @return Le meilleur déplacements du joueur si il n'y a pas d'erreur.<br>
     * <code>ERROR_NOPLAYER</code> si le joueur n'existe pas.<br>
     * <code>ERROR_SQL</code> si il y a un problème avec la requête SQL.
     * 
     * @see model.Parametres
     */
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
    
    /**
     * Retourne le meilleur temps d'un joueur.
     * 
     * @param _nom Nom du joueur.
     * @return Le temps déplacements du joueur si il n'y a pas d'erreur.<br>
     * <code>ERROR_NOPLAYER</code> si le joueur n'existe pas.<br>
     * <code>ERROR_SQL</code> si il y a un problème avec la requête SQL.
     * 
     * @see model.Parametres
     */
    public long getMeilleurTemps(String _nom) {
        try {
            ResultSet rs = stmt.executeQuery("SELECT meilleur_temps FROM Joueur WHERE nom = '" + _nom + "'");
            if (rs.first()) {
                return rs.getLong("meilleur_temps");
            } else {
                return ERROR_NOPLAYER;
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
            return ERROR_SQL;
        }
    }

    /**
     * Met à jour le score maximal d'un joueur.
     * 
     * @param _nom Nom du joueur.
     * @param _scoreMax Nouveau score maximal.
     * @return <code>UPDATED</code> si le score maximal a été mis à jour.<br>
     * <code>NO_UPDATE</code> si le score maximal n'a pas été mis à jour.<br>
     * <code>ERROR_SQL</code> si il y a un problème avec la requête SQL.
     */
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
    
    /**
     * Met à jour le meilleur déplacements d'un joueur.
     * 
     * @param _nom Nom du joueur.
     * @param _meilleurDeplacement Nouveau meilleur déplacements.
     * @return <code>UPDATED</code> si le meilleur déplacements a été mis à jour.<br>
     * <code>NO_UPDATE</code> si le meilleur déplacements n'a pas été mis à jour.<br>
     * <code>ERROR_SQL</code> si il y a un problème avec la requête SQL.
     */
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
    
    /**
     * Met à jour le meilleur temps d'un joueur.
     * 
     * @param _nom Nom du joueur.
     * @param _meilleurTemps Nouveau meilleur temps.
     * @return <code>UPDATED</code> si le meilleur temps a été mis à jour.<br>
     * <code>NO_UPDATE</code> si le meilleur temps n'a pas été mis à jour.<br>
     * <code>ERROR_SQL</code> si il y a un problème avec la requête SQL.
     */
    public int setMeilleurTemps(String _nom, long _meilleurTemps) {
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
    
    /**
     * Retourne le classement des joueurs triés par score.
     * 
     * @return Une liste de tableaux d'objets <code>{nom, meilleur déplacements, meilleur temps, score maximal}</code>, <code>null</code> sinon.
     */
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
    
    /**
     * Retourne le classement des joueurs triés par nombre de déplacements.
     * 
     * @return Une liste de tableaux d'objets <code>{nom, meilleur déplacements, meilleur temps, score maximal}</code>, <code>null</code> sinon.
     */
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
    
     /**
     * Retourne le classement des joueurs triés par temps.
     * 
     * @return Une liste de tableaux d'objets <code>{nom, meilleur déplacements, meilleur temps, score maximal}</code>, <code>null</code> sinon.
     */
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
   
    /**
     * Ajoute une partie dans la base de données.
     * 
     * @param _nom Nom du joueur.
     * @param _deplacement Nombre de déplacements.
     * @param _temps Temps pour finir la partie.
     * @param _score Score de la partie.
     * @param _val_max Valeur maximale atteinte (2048 = Victoire).
     * @return <code>UPDATED</code> si la partie a été ajoutée.<br>
     * <code>NO_UPDATE</code> si la partie n'a pas été ajoutée.<br>
     * <code>ERROR_SQL</code> si il y a un problème avec la requête SQL.
     */
    public int insertionPartie (String _nom, int _deplacement, long _temps, int _score, int _val_max){
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
    
    /**
     * Retourne l'historique des partie d'un joueur.
     * 
     * @param _joueur Nom du joueur.
     * @return Une liste de tableaux d'entier <code>{score, déplacements, temps, valeur maximale}</code>, <code>null</code> sinon.
     */
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
    
    /**
     * Retourne le nombre de parties d'un joueur.
     * 
     * @param joueur Nom du joueur.
     * @return Le nombre de parties du joueur.
     */
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
    
    /**
     * Chiffre le mot de passe avec l'algorithme SHA-256.
     * 
     * @param _mdp Mot de passe en clair.
     * @return Le mot de passe chiffré.
     */
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
