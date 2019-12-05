/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import application.FXMLDocumentController;
import bdd.BaseDeDonnees;
import java.io.Serializable;
import java.util.Scanner;

/**
 *
 * @author nKBlaZy
 */
public class Partie extends Thread implements Parametres, Serializable {

    private static final long serialVersionUID = 1L;
    
    private int score, move;
    private Cube cube;
    private transient BaseDeDonnees bdd;
    private transient int direction;
    private transient FXMLDocumentController controller;
    private Joueur joueur;
    private boolean reload;
    private long temps;
    private transient long debut;

    public Partie(FXMLDocumentController _controller, Joueur _joueur) {
        bdd = BaseDeDonnees.getInstance();
        cube = new Cube();
        score = 0;
        move = 0;
        controller = _controller;
        joueur = _joueur;
        reload = false;
        temps = 0;
    }

    private void initCube() {
        cube.nouvelleCase();
        cube.nouvelleCase();
    }

    private void afficherCube() {
        System.out.println(cube + "\n");
    }

    public void run() {
        if (!reload) {
            initCube();
        }
        controller.updatePanes();
        Scanner sc = new Scanner(System.in);
        boolean nouvelleCase;
        debut = System.currentTimeMillis();
        // Boucle de jeu
        while (!cube.partieFinie() && cube.getValeurMax() < OBJECTIF) {
            // Affichage
            System.out.println("Score : " + score + " Max : " + cube.getValeurMax());
            afficherCube();
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            // Déplacements
            boolean deplacement = cube.lanceurDeplacerCases(direction);
            // Score
            score += cube.getScoreTour();
            // Génération d'une nouvelle case si déplacement
            if (deplacement) {
                move++;
                nouvelleCase = cube.nouvelleCase();
                controller.updatePanes();
                if (!nouvelleCase) {
                    cube.gameOver();
                }
            }
        }
        if (!isInterrupted()) {
            temps += System.currentTimeMillis() - debut;
            if (joueur != null) {
                if (bdd.connection()) {
                    bdd.insertionPartie(joueur.getNom(), move, temps, score, cube.getValeurMax());
                    if (cube.getValeurMax() >= OBJECTIF) {
                        if (bdd.getMeilleurDeplacements(joueur.getNom()) > move || bdd.getMeilleurDeplacements(joueur.getNom()) == 0) {
                            bdd.setMeilleurDeplacements(joueur.getNom(), move);
                        }
                        if (bdd.getMeilleurTemps(joueur.getNom()) > temps || bdd.getMeilleurTemps(joueur.getNom()) == 0) {
                            bdd.setMeilleurTemps(joueur.getNom(), temps);
                        }
                        if (bdd.getScoreMax(joueur.getNom()) < score) {
                            bdd.setScoreMax(joueur.getNom(), score);
                        }
                    }
                    bdd.deconnection();
                }
            }
            afficherCube();
            if (cube.getValeurMax() >= OBJECTIF) {
                cube.victory();
                controller.victory();
            } else {
                cube.gameOver();
                controller.gameOver();
            }
        } else {
            System.out.println("Partie interrompue");
        }
    }
    
    public void sauvegardeTemps() {
        temps += System.currentTimeMillis() - debut;
    }

    public int getScore() {
        return score;
    }

    public int getMove() {
        return move;
    }

    public Cube getCube() {
        return cube;
    }

    public void setDirection(int _direction) {
        this.direction = _direction;
    }

    public void setJoueur(Joueur _joueur) {
        this.joueur = _joueur;
    }

    public void setController(FXMLDocumentController _controller) {
        this.controller = _controller;
    }

    public void setBdd(BaseDeDonnees _bdd) {
        this.bdd = _bdd;
    }

    public void setReload(boolean _reload) {
        this.reload = _reload;
    }
}
