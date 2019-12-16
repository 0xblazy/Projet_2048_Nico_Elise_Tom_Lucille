/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;

/**
 * Joueur caractérisé par son nom, son meilleur déplacement, son meilleur temps (en minutes) et son meilleur score.
 * Utilisé dans le controlleur pour la Partie et pour le classement.
 * 
 * @author elise-per
 * @author nKBlaZy
 */

public class Joueur implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String nom ;
    private transient int deplacements;
    private transient double temps;
    private transient int score;
    
    /**
     * Constructeur.
     * 
     * @param _nom Nom du Joueur.
     * @param _deplacements Meilleur déplacement du Joueur.
     * @param _temps Meilleur temps du Joueur.
     * @param _score Meilleur score du Joueur.
     */
    public Joueur(String _nom, int _deplacements, double _temps, int _score) {
        nom = _nom;
        deplacements = _deplacements;
        temps = _temps;
        score = _score;
    }
    
    /**
     * Constructeur utilisé dans le Controlleur. Les deplacements, temps et score ne sont pas nécessaires.
     * 
     * @param _nom Nom du Joueur.
     */
    public Joueur(String _nom) {
        this(_nom, 0, 0, 0);
    }

    /**
     * Retourne le nom du Joueur.
     * 
     * @return Nom du Joueur.
     */
    public String getNom(){
        return nom;
    }  

    /**
     * Retourne le meilleur déplacement du Joueur.
     * 
     * @return Meilleur déplacement du Joueur. 
     */
    public int getDeplacements() {
        return deplacements;
    }

    /**
     * Retourne le meilleur temps du Joueur.
     * 
     * @return Meilleur temps du Joueur.
     */
    public double getTemps() {
        return temps;
    }

    /**
     * Retourne le meilleur score du Joueur.
     * 
     * @return Meilleur score du Joueur.
     */
    public int getScore() {
        return score;
    }
}