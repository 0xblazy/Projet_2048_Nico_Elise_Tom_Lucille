/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Cube caractérisé par un tableau en trois dimensions de Case.
 * 
 * @author nKBlaZy
 * @author Lixye
 * @author TomWyso
 * @author elise-per
 * 
 * @see Case
 */
public class Cube implements Parametres, Serializable {
    
    private static final long serialVersionUID = 1L;

    private Case[][][] cube; //[z][y][x]

    private int valeurMax;
    private boolean deplacement;
    private int scoreTour;
    private int idCases;

    /**
     * Constructeur.
     */
    public Cube() {
        cube = new Case[TAILLE][TAILLE][TAILLE];
        valeurMax = 0;
        idCases = 0;
    }
    
    /**
     * Génère une nouvelle Case.
     * 
     * @return <code>true</code> si une Case est générée, <code>false</code> sinon.
     * 
     * @see Case
     */
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
                cube[c[2]][c[1]][c[0]] = new Case(2, c[0], c[1], c[2], idCases, this);
            } else {
                cube[c[2]][c[1]][c[0]] = new Case(4, c[0], c[1], c[2], idCases, this);
            }
            idCases++;

            // Change la valeurMax du cube si la case générée est plus grande que la valeurMax actuelle
            if (valeurMax < cube[c[2]][c[1]][c[0]].getValeur()) {
                valeurMax = cube[c[2]][c[1]][c[0]].getValeur();
            }
            return true;
        }
        return false;
    }

    /**
     * Cherches les Cases libres.
     * 
     * @return La liste des Cases  libres, données sous la forme d'un tableau <code>{x,y,z}</code>.
     * 
     * @see Case
     */
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

    /**
     * Lance le déplacement dans une direction donnée.
     * 
     * @param direction Direction dans laquelle lancer le déplacement.
     * @return <code>true</code> si au moins une Case a été déplacée, <code>false</code> sinon.
     */
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
    
    /**
     * Déplace les Cases dans une direction donnée, de manière récursive.
     * 
     * @param casesAuBord Cases au bord de la grille.
     * @param direction Direction dans laquelle déplacer les Cases.
     * @param rangee Rangée dans laquelle le déplacement s'effectue.
     * @param compteur Compteur pour l'appel itératif, mettre 0 pour le premier appel.
     */
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
    
    /**
     * Fusionne deux Cases.
     * 
     * @param c Case sur laquelle faire la fusion (la deuxième disparait).
     * @return La valeur de la Case une fois fusionnée.
     */
    private int fusion(Case c) {
        c.setValeur(c.getValeur() * 2);
        if (this.valeurMax < c.getValeur()) { //On vérifie si la nouvelle case générée n'est pas une valeur max des 3 grilles
            this.valeurMax = c.getValeur(); //Si elle l'est, la valeur max des grilles est mise à jour
        }
        deplacement = true;
        return c.getValeur();
    }

    /**
     * Cherche les Cases les plus au bord dans une direction donnée.
     * 
     * @param direction Direction dans laquelle chercher les Cases les plus au bord.
     * @return Le tableau contenant les Cases les plus au bord.<br>
     * Certains index du tableau peuvent être vides si il n'y a pas de Case dans la rangée/colonne correspondante.<br>
     * <code>{Grille1Case1,Grille1Case2,....,GrilleNCaseN}</code>
     * 
     * @see Case
     */
    private Case[] getCasesAuBord(int direction) { //retourne les cases au bord d'une direction donnée
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

    /**
     * Retourne le Cube sous forme d'une chaîne de caractères.
     * La chaîne se forme avec la concaténation de la n ligne de chaque grille, un retourne à la ligne, puis avec la n+1 ligne, etc.
     * 
     * @return Le Cube sous forme d'une chaîne de caractères.
     */
    @Override
    public String toString() {
        return Arrays.toString(cube[0][0]) + Arrays.toString(cube[1][0])
                + Arrays.toString(cube[2][0]) + "\n" + Arrays.toString(cube[0][1])
                + Arrays.toString(cube[1][1]) + Arrays.toString(cube[2][1]) + "\n"
                + Arrays.toString(cube[0][2]) + Arrays.toString(cube[1][2])
                + Arrays.toString(cube[2][2]);
    }

    /**
     * Affiche un message de victoire dans la console.
     */
    public void victory() {
        System.out.println("Victoire ! Vous avez atteint le score de " + this.valeurMax);
        // System.exit(0);
    }

    /**
     * Affiche un message de défaite dans la console.
     */
    public void gameOver() {
        System.out.println("Plus de déplacements possibles! Votre score est " + this.valeurMax);
        // System.exit(1);
    }
        
    /**
     * Vérifie si la partie est finie.
     * La partie est finie si il n'y plus de Case libre et si aucune Case n'a de voisin avec la même valeur.
     * 
     * @return <code>true</code> si la partie est finie, <code>false</code> sinon. 
     */
    public boolean partieFinie(){
        if (this.casesLibres().size() > 0){
            return false;
        } else {
            for (int i=0; i<TAILLE; i++) {
                for (int j=0; j<TAILLE; j++) {
                    for (int k=0; k<TAILLE; k++) {
                       Case c = this.cube[i][j][k];
                       for (int d = 1 ; d <=3 ; d++) {
                           Case voisin = c.getVoisinDirect(d);
                           if (voisin != null) {
                               if (c.valeurEgale(voisin)) {
                                   return false;
                               }
                           }
                        }
                    }  
                }
            }
        }
        return true;
    }
    
    /**
     * Retourne le Cube.
     * 
     * @return Le Cube.
     */
    public Case[][][] getCube() {
        return cube;
    }
    
    /**
     * Cherche la Case avec un id donné.
     * 
     * @param _id id de la Case à chercher.
     * @return La Case si elle a été trouvée, <code>null</code> sinon.
     * 
     * @see Case
     */
    public Case getCase(int _id) {
        for (int k = 0 ; k < TAILLE ; k++) {
            for (int j = 0 ; j < TAILLE ; j++) {
                for (int i = 0 ; i < TAILLE ; i++) {
                    Case c = cube[k][j][i];
                    if (c != null) {
                        if (c.getId() == _id) {
                            return c;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * Retourne le score pour le tour actuel (derniers déplacements).
     * 
     * @return Le score pour le tour actuel. 
     */
    public int getScoreTour(){
        return scoreTour;
    }

    /**
     * Retourne la valeur maximale du Cube.
     * 
     * @return La valeur maximale du Cube.
     */
    public int getValeurMax(){
        return valeurMax;
    }
}
