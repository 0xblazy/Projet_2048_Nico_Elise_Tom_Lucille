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
public class Cube implements Parametres {

    private Case[][][] cube; //[z][y][x]
    private Partie partie;
    private int valeurMax;
    private boolean deplacement;

    public Cube(Partie _p) {
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
            if (Math.random() < 0.66) {
                cube[c[2]][c[1]][c[0]] = new Case(2, c[0], c[1], c[2], this);
            } else {
                cube[c[2]][c[1]][c[0]] = new Case(4, c[0], c[1], c[2], this);
            }

            // Change la valeurMax du cube si la case générée est plus grande que la valeurMax actuelle
            if (valeurMax < cube[c[2]][c[1]][c[0]].getValeur()) {
                valeurMax = cube[c[2]][c[1]][c[0]].getValeur();
            }
        }
    }

    // Retourne la liste des cases libres
    private List<int[]> casesLibres() {
        ArrayList<int[]> casesLibres = new ArrayList<>();

        for (int k = 0; k < TAILLE; k++) {
            for (int j = 0; j < TAILLE; j++) {
                for (int i = 0; i < TAILLE; i++) {
                    if (cube[k][j][i] == null) {
                        casesLibres.add(new int[]{i, j, k});
                    }
                }
            }
        }

        return casesLibres;
    }

    private void deplacerCase(Case[] casesAuBord, int direction, int rangee, int compteur) {
       if (casesAuBord[rangee] != null) {
            if ((direction == HAUT && casesAuBord[rangee].getY() != compteur)
                    || (direction == BAS && casesAuBord[rangee].getY() != TAILLE - 1 - compteur)
                    || (direction == GAUCHE && casesAuBord[rangee].getX() != compteur)
                    || (direction == DROITE && casesAuBord[rangee].getX() != TAILLE - 1 - compteur)
                    || (direction == AVANT && casesAuBord[rangee].getZ() != compteur)
                    || (direction == ARRIERE && casesAuBord[rangee].getZ() != TAILLE - 1 - compteur)) {
                this.cube[casesAuBord[rangee].getZ()][casesAuBord[rangee].getY()][casesAuBord[rangee].getX()]=null;
                switch (direction) {
                    case HAUT:
                        casesAuBord[rangee].setY(compteur);
                        break;
                    case BAS:
                        casesAuBord[rangee].setY(TAILLE - 1 - compteur);
                        break;
                    case GAUCHE:
                        casesAuBord[rangee].setX(compteur);
                        break;
                    case DROITE:
                        casesAuBord[rangee].setX(TAILLE - 1 - compteur);
                        break;
                    case AVANT:
                        casesAuBord[rangee].setZ(compteur);
                        break;
                    case ARRIERE:
                        casesAuBord[rangee].setZ(TAILLE - 1 - compteur);
                        break;
                }
                this.cube[casesAuBord[rangee].getZ()][casesAuBord[rangee].getY()][casesAuBord[rangee].getX()]=casesAuBord[rangee];
                deplacement = true;
            }
            Case voisin = casesAuBord[rangee].getVoisinDirect(-direction);
            if (voisin != null) {
                if (casesAuBord[rangee].valeurEgale(voisin)) {
                    this.fusion(casesAuBord[rangee]);
                    casesAuBord[rangee] = voisin.getVoisinDirect(-direction);
                     this.cube[voisin.getZ()][voisin.getY()][voisin.getX()]=null;
                    this.deplacerCase(casesAuBord, rangee, direction, compteur + 1);
                } else {
                    casesAuBord[rangee] = voisin;
                    this.deplacerCase(casesAuBord, rangee, direction, compteur + 1);
                }
            }
        }
    }

    public Case[] getCasesAuBord(int direction) { //retourne les cases au bord d'une direction donnée
        Case[] cAuBord = new Case[9];
        for (int i = 0; i < TAILLE * TAILLE; i++) { //i = coord Z, la couche
            for (int j = 0; j < TAILLE * TAILLE; j++) { //j = coord Y, représentation en ligne
                for (int k = 0; k < TAILLE * TAILLE; k++) { //k = coord X, représentation en colonne
                    Case c = cube[i][j][k];
                    switch (direction) {
                        case HAUT:
                            if ((cAuBord[k+3*i] == null) || (cAuBord[k+3*i].getY() > j)) { 
                                cAuBord[k+3*i] = c;
                            }
                            break;
                        case BAS:
                            if ((cAuBord[k+3*i] == null) || (cAuBord[k+3*i].getY() < j)) {
                                cAuBord[k+3*i] = c;
                            }
                            break;
                        case GAUCHE:
                            if ((cAuBord[j+3*i] == null) || (cAuBord[j+3*i].getX() > k)) {
                                cAuBord[j+3*i] = c;
                            }
                            break;
                        case DROITE:
                            if ((cAuBord[j+3*i] == null) || (cAuBord[j+3*i].getX() < k)) {
                                cAuBord[j+3*i] = c;
                            }
                            break;
                        case AVANT:
                            if ((cAuBord[k+3*j] == null) || (cAuBord[k+3*j].getZ() > i)) {
                                cAuBord[k+3*j] = c;
                            }
                            break;
                        case ARRIERE:
                            if ((cAuBord[k+3*j] == null) || (cAuBord[k+3*j].getZ() < i)) {
                                cAuBord[k+3*j] = c;
                            }
                            break;
                    }
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
