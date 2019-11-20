/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import application.FXMLDocumentController;
import bdd.BaseDeDonnees;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nKBlaZy
 */
public class Partie extends Thread implements Parametres {

    private int score, move;
    private Cube cube;
    private BaseDeDonnees bdd;
    private int direction;
    private FXMLDocumentController controller;

    public Partie(FXMLDocumentController _controller) {
        bdd = BaseDeDonnees.getInstance();
        cube = new Cube(this);
        score = 0;
        move = 0;
        controller = _controller;
    }

    private void initCube() {
        cube.nouvelleCase();
        cube.nouvelleCase();
    }

    private void afficherCube() {
        System.out.println(cube + "\n");
    }

    public void run() {
        initCube();
        controller.updatePanes();
        Scanner sc = new Scanner(System.in);
        boolean nouvelleCase;
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
}
