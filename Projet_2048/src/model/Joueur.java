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
    //private String mdp ; //peut être utile pour récupérer le mdp en cas d'oubli de l'utilisateur
    private int scoreMax;
    
    public Joueur(String _nom, int score_max) {
        nom=_nom;
        scoreMax=score_max;
    }

    public String getNom(){
        return nom;
    }

    public void setNom(String _nom){
        nom=_nom;
    }
    
    public int getScoreMax(){
        return scoreMax;
    }

    public void setScoreMax(int score_max){
        if (this.scoreMax<score_max) {
           scoreMax=score_max;
        }
    }
    
    /*
    public String getMdp(){
        return mdp;
    }

    public void setMdp(String _mdp){
        mdp=_mdp;
    }
    */
    
}