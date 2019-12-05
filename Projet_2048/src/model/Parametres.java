/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 * Interface contenant les paramètres partagés (directions, codes..).
 * 
 * @author TomWyso
 * @author nKBlaZy
 */
public interface Parametres {
    
    /**
     * Direction HAUT, {@value #HAUT}.
     */
    static final int HAUT = 1;  //coordonnées x
    /**
     * Direction BAS, {@value #BAS}.
     */
    static final int BAS = -1;
    
    /**
     * Direction GAUCHE, {@value #GAUCHE}.
     */
    static final int GAUCHE = 2;  //coordonnées y
    /**
     * Direction DROITE, {@value #DROITE}.
     */
    static final int DROITE = -2;
    
    /**
     * Direction AVANT, {@value #AVANT}.
     */
    static final int AVANT = 3;  //coordonnées z
    /**
     * Direction ARRIERE, {@value #ARRIERE}.
     */
    static final int ARRIERE = -3;
    
    /**
     * Taille des grilles, {@value #TAILLE}.
     */
    static final int TAILLE = 3;
    /**
     * Objectif pour gagner une partie, {@value #OBJECTIF}.
     */
    static final int OBJECTIF = 2048;
    
    /**
     * Code lorsque les données correspondent (nom et mot de passe), {@value #CORRECT_DATA}.
     */
    static final int CORRECT_DATA = -121;
    /**
     * Code lorsque les données ont été mises à jour, {@value #UPDATED}.
     */
    static final int UPDATED = -122;
    /**
     * Code erreur lorsque le joueur n'existe pas, {@value #ERROR_NOPLAYER}.
     */
    static final int ERROR_NOPLAYER = -123;
    /**
     * Code erreur lorsque le mot de passe est incorrect, {@value #ERROR_WRONG_PASS}.
     */
    static final int ERROR_WRONG_PASS = -124;
    /**
     * Code erreur lorsqu'il y a un problème avec la requête SQL, {@value #ERROR_SQL}.
     */
    static final int ERROR_SQL = -125;
    /**
     * Code erreur lorsque la requête SQL n'a pas mis à jour la table, {@value #NO_UPDATE}.
     */
    static final int NO_UPDATE = -126;
}
