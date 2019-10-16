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
    private int scoreTour;

    public Cube(Partie _p) {
        partie = _p;
        cube = new Case[TAILLE][TAILLE][TAILLE];
        valeurMax = 0;
    }
    
    public int getScoreTour(){
        return scoreTour;
    }

    public int getValeurMax(){
        return valeurMax;
    }
    
    // Génère une nouvelle case
    public boolean nouvelleCase() {
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
            return true;
        }
        return false;
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

    public boolean lanceurDeplacerCases(int direction) {
        Case[] casesAuBord = this.getCasesAuBord(direction); //casesAuBord = tableau de 9 cases au bord d'une direction donnée par l'utilisateur
        //System.out.println("Cases au bord " + Arrays.toString(casesAuBord));
        deplacement = false; // pour vérifier si on a bougé au moins une case après le déplacement, avant d'en rajouter une nouvelle
        scoreTour = 0; //Initialisation du score en début de chaque tour
        for (int i = 0; i < TAILLE*TAILLE; i++) { //On parcours les 3 grilles de jeux
            this.deplacerCase(casesAuBord, direction, i, 0); 
        }
        return deplacement;
    }
    
    private void deplacerCase(Case[] casesAuBord, int direction, int rangee, int compteur) {
        if (casesAuBord[rangee] != null) {
            if ((direction == HAUT && casesAuBord[rangee].getY() != compteur)
                    || (direction == BAS && casesAuBord[rangee].getY() != TAILLE - 1 - compteur)
                    || (direction == GAUCHE && casesAuBord[rangee].getX() != compteur)
                    || (direction == DROITE && casesAuBord[rangee].getX() != TAILLE - 1 - compteur)
                    || (direction == AVANT && casesAuBord[rangee].getZ() != compteur)
                    || (direction == ARRIERE && casesAuBord[rangee].getZ() != TAILLE - 1 - compteur)) {
                this.cube[casesAuBord[rangee].getZ()][casesAuBord[rangee].getY()][casesAuBord[rangee].getX()] = null; //réinitialisation à null pour la case
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
                this.cube[casesAuBord[rangee].getZ()][casesAuBord[rangee].getY()][casesAuBord[rangee].getX()] = casesAuBord[rangee]; //ajout de la case ses nouvelles coordonnées
                deplacement = true;
            }
            
            Case voisin = casesAuBord[rangee].getVoisinDirect(-direction);
            if (voisin != null) {
                //System.out.println("Case : " + casesAuBord[rangee]);
                //System.out.println("Voisin : " + voisin);
                if (casesAuBord[rangee].valeurEgale(voisin)) {
                    this.scoreTour += this.fusion(casesAuBord[rangee]); //On incrémente le score du tour
                    //System.out.println("FUSSIONNNNN!!!!");
                    casesAuBord[rangee] = voisin.getVoisinDirect(-direction);
                    this.cube[voisin.getZ()][voisin.getY()][voisin.getX()] = null;
                    this.deplacerCase(casesAuBord, direction, rangee, compteur + 1);
                } else {
                    casesAuBord[rangee] = voisin;
                    this.deplacerCase(casesAuBord, direction, rangee, compteur + 1);
                }
            } else {
                casesAuBord[rangee] = null;
            }
        }
    }
    
    private int fusion(Case c) {
        c.setValeur(c.getValeur() * 2);
        if (this.valeurMax < c.getValeur()) { //On vérifie si la nouvelle case générée n'est pas une valeur max des 3 grilles
            this.valeurMax = c.getValeur(); //Si elle l'est, la valeur max des grilles est mise à jour
        }
        deplacement = true;
        return c.getValeur();
    }

    public Case[] getCasesAuBord(int direction) { //retourne les cases au bord d'une direction donnée
        Case[] cAuBord = new Case[TAILLE * TAILLE];
        for (int i = 0; i < TAILLE; i++) { //i = coord Z, la couche
            for (int j = 0; j < TAILLE; j++) { //j = coord Y, représentation en ligne
                for (int k = 0; k < TAILLE; k++) { //k = coord X, représentation en colonne
                    Case c = cube[i][j][k];
                    if (c != null) {
                        switch (direction) {
                            case HAUT:
                                if ((cAuBord[k+3*i] == null) || (cAuBord[k+3*i].getY() > c.getY())) { 
                                    cAuBord[k+3*i] = c;
                                    // k+3*i = coordonnée x à laquelle on ajoute 3*i selon le tableau dans lequel on est (exemple: tableau 1, i=0 donc k=1, par contre si tableau 2, i=1 donc k=4...)
                                }
                                break;
                            case BAS:
                                if ((cAuBord[k+3*i] == null) || (cAuBord[k+3*i].getY() < c.getY())) {
                                    cAuBord[k+3*i] = c;
                                }
                                break;
                            case GAUCHE:
                                if ((cAuBord[j+3*i] == null) || (cAuBord[j+3*i].getX() > c.getX())) {
                                    cAuBord[j+3*i] = c;
                                    // j+3*i = coordonnée j à laquelle on ajoute 3*i selon le tableau dans lequel on est (exemple: si j=1 et tableau 2, i=1 donc j=4...)
                                }
                                break;
                            case DROITE:
                                if ((cAuBord[j+3*i] == null) || (cAuBord[j+3*i].getX() < c.getX())) {
                                    cAuBord[j+3*i] = c;
                                }
                                break;
                            case AVANT:
                                if ((cAuBord[k+3*j] == null) || (cAuBord[k+3*j].getZ() > c.getZ())) {
                                    cAuBord[k+3*j] = c;
                                    //k+3*j = coordonnée k à laquelle on ajoute 3*j selon la ligne dans laquelle on est (exemple: si j=1 et k=1, alors c=4 et est en deuxième ligne première colonne)
                                }
                                break;
                            case ARRIERE:
                                if ((cAuBord[k+3*j] == null) || (cAuBord[k+3*j].getZ() < c.getZ())) {
                                    cAuBord[k+3*j] = c;
                                }
                                break;
                        }
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
