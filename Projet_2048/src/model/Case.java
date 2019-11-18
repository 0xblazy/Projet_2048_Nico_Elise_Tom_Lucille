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
public class Case implements Parametres {
    private int valeur;
    private int oldValeur;
    private int x, y, z; // x: largeur, y: hauteur, z: profondeur
    private int oldX, oldY, oldZ; // anciennes valeurs de x, y et z
    private final int id; // identifiant de la case
    private Cube cube;

    public Case(int _v, int _x, int _y, int _z, int _id, Cube _c) {
        valeur = _v;
        oldValeur = _v;
        x = _x;
        oldX = _x;
        y = _y;
        oldY = _y;
        z = _z;
        oldZ = _z;
        id = _id;
        cube = _c;
    }
    
    public Case getVoisinDirect(int direction){
        switch (direction) {
            case HAUT:
                if (y == 0) {
                    return null;
                } else {
                    for (int j = y - 1 ; j >= 0 ; j--) {
                        if (cube.getCube()[z][j][x] != null) {
                            return cube.getCube()[z][j][x];
                        }
                    }
                }
                break;
            case BAS:
                if (y == TAILLE - 1) {
                    return null;
                } else {
                    for (int j = y + 1 ; j < TAILLE ; j++) {
                        if (cube.getCube()[z][j][x] != null) {
                            return cube.getCube()[z][j][x];
                        }
                    }
                }
                break;
            case GAUCHE:
                if (x == 0) {
                    return null;
                } else {
                    for (int i = x - 1 ; i >= 0 ; i--) {
                        if (cube.getCube()[z][y][i] != null) {
                            return cube.getCube()[z][y][i];
                        }
                    }
                }
                break;
            case DROITE:
                if (x == TAILLE - 1) {
                    return null;
                } else {
                    for (int i = x + 1 ; i < TAILLE ; i++) {
                        if (cube.getCube()[z][y][i] != null) {
                            return cube.getCube()[z][y][i];
                        }
                    }
                }
                break;
            case AVANT:
                if (z == 0) {
                    return null;
                } else {
                    for (int k = z - 1 ; k >= 0 ; k--) {
                        if (cube.getCube()[k][y][x] != null) {
                            return cube.getCube()[k][y][x];
                        }
                    }
                }
                break;
            case ARRIERE:
                if (z == TAILLE - 1) {
                    return null;
                } else {
                    for (int k = z + 1 ; k < TAILLE ; k++) {
                        if (cube.getCube()[k][y][x] != null) {
                            return cube.getCube()[k][y][x];
                        }
                    }
                }
                break;
        }
        return null;
    }
    
    public boolean valeurEgale(Case c) {
        if (c != null) {
            return this.valeur == c.valeur;
        } else {
            return false;
        }
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

    public int getOldValeur() {
        return oldValeur;
    }

    public int getX() {
        return x;
    }

    public int getOldX() {
        return oldX;
    }

    public int getY() {
        return y;
    }

    public int getOldY() {
        return oldY;
    }

    public int getZ() {
        return z;
    }

    public int getOldZ() {
        return oldZ;
    }

    public int getId() {
        return id;
    }

    public void setValeur(int valeur) {
        this.oldValeur = this.valeur;
        this.valeur = valeur;
    }

    public void setX(int x) {
        this.oldX = this.x;
        this.x = x;
    }

    public void setY(int y) {
        this.oldY = this.y;
        this.y = y;
    }

    public void setZ(int z) {
        this.oldZ = this.z;
        this.z = z;
    }
}
