/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author nKBlaZy
 */
public class Partie implements Parametres {
    private int score;
    private Cube cube;

    public Partie() {
        cube = new Cube(this);
        score = 0;
    }
    
    public void initCube() {
        cube.nouvelleCase();
        cube.nouvelleCase();
    }

    public void afficherCube() {
        System.out.println(cube + "\n");
    }

    public int getScore() {
        return score;
    }
    
}
