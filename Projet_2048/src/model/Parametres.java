/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author TomWyso
 */
public interface Parametres {
    
    static final int HAUT = 1;  //coordonnées x
    static final int BAS = -1;
    
    static final int GAUCHE = 2;  //coordonnées y
    static final int DROITE = -2;
    
    static final int AVANT = 3;  //coordonnées z
    static final int ARRIERE = -3;
    
    static final int TAILLE = 3;
    static final int OBJECTIF = 2048;
    
    static final int CORRECT_DATA = -121;
    static final int UPDATED = -122;
    static final int ERROR_NOPLAYER = -123;
    static final int ERROR_WRONG_PASS = -124;
    static final int ERROR_SQL = -125;
    static final int NO_UPDATE = -126;
    
    //********PARAMETRES GRAPHIQUES********
    //static final int SIZE_GRID = 0;
}
