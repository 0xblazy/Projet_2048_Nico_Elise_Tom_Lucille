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
public class Case {
    private int valeur;
    private int x, y, z; // x: largeur, y: hauteur, z: profondeur
    private Cube cube;

    public Case(int _v, int _x, int _y, int _z, Cube _c) {
        valeur = _v;
        x = _x;
        y = _y;
        z = _z;
        cube = _c;
    }

    @Override
    public String toString() {
        if (valeur < 10) {
            return "  " + valeur + " ";
        }
        if (valeur < 100) {
            return " " + valeur + " ";
        }
        if (valeur < 1000) {
            return " " + valeur;
        }
        return Integer.toString(valeur);
    }

    public int getValeur() {
        return valeur;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public void setValeur(int valeur) {
        this.valeur = valeur;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    } 
}
