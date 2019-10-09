/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 *
 * @author nKBlaZy
 */
public class Cube implements Parametres{
    private Case[][][] cube; //[z][y][x]
    private Partie partie;
    private int valeurMax;
    
    public Cube (Partie _p) {
        partie = _p;
        cube = new Case[TAILLE][TAILLE][TAILLE];
        valeurMax = 0;
    }
    
    // Génère une nouvelle case
    public void nouvelleCase() {
        List<int[]> casesLibres = casesLibres();
        Random ra = new Random();
        
        // Si il y a des cases libres
        if (casesLibres.size() > 0) {
            // Récupère les coordonnées d'une case libre aléatoire
            int[] c = casesLibres.get(ra.nextInt(casesLibres.size()));
            // System.out.println(Arrays.toString(c));
            
            // Créé la case
            cube[c[2]][c[1]][c[0]] = new Case((1 + ra.nextInt(2)) * 2, c[0], c[1], c[2], this);
            
            // Change la valeurMax du cube si la case générée est plus grande que la valeurMax actuelle
            if (valeurMax < cube[c[2]][c[1]][c[0]].getValeur()) valeurMax = cube[c[2]][c[1]][c[0]].getValeur();
        }
    } 
    
    // Retourne la liste des cases libres
    private List<int[]> casesLibres() {
        ArrayList<int[]> casesLibres = new ArrayList<>();
        
        for (int k = 0 ; k < TAILLE ; k++) {
            for (int j = 0 ; j < TAILLE ; j++) {
                for (int i = 0 ; i < TAILLE ; i++) {
                    if (cube[k][j][i] == null) {
                        casesLibres.add(new int[]{i,j,k});
                    }
                }
            }
        }
        
        return casesLibres;
    }
    
    private void DeplacerCase(Case[] casesAuBord, int direction, int rangee){
        
    }
    
    public Case[] getCasesAuBord(int direction) { //retourne les cases au bord d'une direction donnée
        Case[] cAuBord = new Case[9];
        for(int i = 0; i<TAILLE*TAILLE; i++){ //i = coord Z, la couche
            for(int j = 0; j<TAILLE*TAILLE; j++){ //j = coord Y, représentation en ligne
                for(int k = 0; k<TAILLE*TAILLE; k++){ //k = coord X, représentation en colonne
                    
                }
            }
        }
        return cAuBord;
    }

    @Override
    public String toString() {
        return Arrays.toString(cube[0][0]) + Arrays.toString(cube[1][0]) 
                + Arrays.toString(cube[2][0]) + "\n" + Arrays.toString(cube[0][1])
                + Arrays.toString(cube[1][1]) + Arrays.toString(cube[2][1]) + "\n" 
                + Arrays.toString(cube[0][2]) + Arrays.toString(cube[1][2]) 
                + Arrays.toString(cube[2][2]);
    }

    public Case[][][] getCube() {
        return cube;
    }
    
    public void victory() {
        System.out.println("Victoire ! Vous avez atteint le score de" + this.valeurMax);
        // System.exit(0);
    }

    public void gameOver() {
        System.out.println("Plus de déplacements possibles! Votre score est " + this.valeurMax);
        // System.exit(1);
    }
}
