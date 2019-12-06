/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;

/**
 *  Joueur caractérisé par son nom.
 * 
 * @author elise-per
 */

public class Joueur implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String nom ;
    
    /**
     * Constructeur.
     * 
     * @param _nom Nom du Joueur.
     */
    public Joueur(String _nom) {
        nom=_nom;
    }

    /**
     * Retourne le nom du Joueur.
     * 
     * @return Le nom du Joueur.
     */
    public String getNom(){
        return nom;
    }  
}