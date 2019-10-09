/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author nKBlaZy
 */
public class Cube {
    private Case[][][] cube; //[z][y][x]
    private Partie partie;
    private int valeurMax;
    
    public Cube (Partie _p) {
        partie = _p;
        cube = new Case[3][3][3];
        valeurMax = 0;
    }
    
    public void nouvelleCase() {
        List<int[]> casesLibres = casesLibres();
        Random ra = new Random();
        
        if (casesLibres.size() > 0) {
            int[] c = casesLibres.get(ra.nextInt(casesLibres.size()));
            System.out.println(Arrays.toString(c));
            cube[c[2]][c[1]][c[0]] = new Case((1 + ra.nextInt(2)) * 2, c[0], c[1], c[2]);
            
            if (valeurMax < cube[c[2]][c[1]][c[0]].getValeur()) valeurMax = cube[c[2]][c[1]][c[0]].getValeur();
        }
    } 
    
    private List<int[]> casesLibres() {
        ArrayList<int[]> casesLibres = new ArrayList<>();
        
        for (int k = 0 ; k < 3 ; k++) {
            for (int j = 0 ; j < 3 ; j++) {
                for (int i = 0 ; i < 3 ; i++) {
                    if (cube[k][j][i] == null) {
                        casesLibres.add(new int[]{i,j,k});
                    }
                }
            }
        }
        
        return casesLibres;
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
