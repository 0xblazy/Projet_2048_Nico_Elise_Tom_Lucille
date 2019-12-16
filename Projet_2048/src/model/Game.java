/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 * Game caractérisée par un score, un nombre de déplacement, un temps (en minutes) et une valeur maximale atteinte.
 * Utilisée pour l'historique des parties d'un Joueur.
 * Nommée ainsi pour ne pas confondre avec la Partie.
 * 
 * @author nKBlaZy
 */
public class Game {
    
    private int score;
    private int deplacements;
    private double temps;
    private int valMax;
    
    /**
     * Constructeur.
     * 
     * @param _score Score de la Game.
     * @param _deplacements Nombre de déplacements de la Game.
     * @param _temps Temps de la Game en minutes.
     * @param _valMax Valeur maximale de la Game.
     */
    public Game(int _score, int _deplacements, double _temps, int _valMax) {
        score = _score;
        deplacements = _deplacements;
        temps = _temps;
        valMax = _valMax;
    }

    /**
     * Retourne le score de la Game.
     * 
     * @return Score de la Game.
     */
    public int getScore() {
        return score;
    }

    /**
     * Retourne le nombre de déplacements de la Game.
     * 
     * @return Nombre de déplacements de la Game.
     */
    public int getDeplacements() {
        return deplacements;
    }

    /**
     * Retourne le temps de la Game en minutes.
     * 
     * @return Temps de la Game en minutes. 
     */
    public double getTemps() {
        return temps;
    }

    /**
     * Retourne la valeur maximale de la Game.
     * 
     * @return Valeur maximale de la Game. 
     */
    public int getValMax() {
        return valMax;
    }    
}
