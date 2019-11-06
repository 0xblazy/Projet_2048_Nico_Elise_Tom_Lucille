/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Scanner;

/**
 *
 * @author nKBlaZy
 */
public class Partie extends Thread implements Parametres {
    private int score;
    private Cube cube;

    public Partie() {
        cube = new Cube(this);
        score = 0;
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
        Scanner sc = new Scanner(System.in);
        boolean nouvelleCase;
        // Boucle de jeu
        while (!cube.partieFinie() && cube.getValeurMax() < OBJECTIF) {
            // Affichage
            System.out.println("Score : " + score + " Max : " + cube.getValeurMax());
            afficherCube();
            // Demande de direction
            System.out.println("Déplacer vers la Gauche (q), Droite (d), Haut (z), Bas (s), Avant (r), Arrière (f)");
            String s = sc.nextLine();
            while (!(s.equals("q") || s.equals("d")  || s.equals("z") || s.equals("s") || s.equals("r") || s.equals("f"))) {
                System.out.println("Saisie incorrecte");
                s = sc.nextLine();
            }
            // Définition de la direction
            int direction;
            switch (s) {
                case "q":
                    direction = GAUCHE;
                    break;
                case "d":
                    direction = DROITE;
                    break;
                case "z":
                    direction = HAUT;
                    break;
                case "s":
                    direction = BAS;
                    break;
                case "r":
                    direction = AVANT;
                    break;
                default:
                    direction = ARRIERE;
                    break;
            }
            // Déplacements
            boolean deplacement = cube.lanceurDeplacerCases(direction);
            // Score
            score += cube.getScoreTour();
            // Génération d'une nouvelle case si déplacement
            if (deplacement) {
                nouvelleCase = cube.nouvelleCase();
                if (!nouvelleCase) cube.gameOver();
            }
        }
        afficherCube();
        if (cube.getValeurMax() >= OBJECTIF) {
            cube.victory();
        } else {
            cube.gameOver();
        }
    }

    public int getScore() {
        return score;
    }
    
    public Cube getCube() {
        return cube;
    }
    
}
