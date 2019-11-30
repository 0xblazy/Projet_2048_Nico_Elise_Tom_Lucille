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
    
    public Joueur(String _nom) {
        nom=_nom;
    }

    public String getNom(){
        return nom;
    }  
}