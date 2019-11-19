/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author elise
 */

public class Joueur implements Parametres {
    private String nom ;
    private String mdp ;
    private int scoreMax;
    private Statement stmt;
    private Joueur j = new Joueur(nom,mdp,scoreMax);
    
    public Joueur(String _nom, String _mdp, int score_max) {
        nom=_nom;
        mdp=_mdp;
        scoreMax=score_max;
    }

   
   public int Score_max(int score_max) {
       if (j.scoreMax<score_max) {
           return score_max;
       } else {
           return scoreMax;
       }
   }
   
   public String sauvegarde(String nom, int _score) throws SQLException {
        Score_max(scoreMax);
        if (_score < scoreMax ) {
            stmt.executeUpdate("UPDATE Joueur SET score = '" + _score + "' WHERE nom = '" + nom + "'");
            return "score enregistré";
        } else {
            stmt.executeUpdate("UPDATE Joueur SET score_max = '" + _score + "' WHERE nom = '" + nom + "'");
            return "score max enregistré";
        }
    }
   
    public String getNom(){
        return nom;
    }

    public void setNom(String _nom){
        nom=_nom;
    }

    public String getMdp(){
        return mdp;
    }

    public void setMdp(String _mdp){
        mdp=_mdp;
    }
    
    public int getScoreMax(){
        return scoreMax;
    }

    public void setScoreMax(int score_max){
        scoreMax=score_max;
    }
 
    
}