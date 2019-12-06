/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;

/**
 * Case caractérisée par sa valeur, ses coordonnées et un id.
 * 
 * @author nKBlaZy
 * @author TomWyso
 */
public class Case implements Parametres, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int valeur;
    private int oldValeur;
    private int x, y, z; // x: largeur, y: hauteur, z: profondeur
    private final int id; // identifiant de la case
    private Cube cube;

    /**
     * Constructeur de Case.
     * 
     * @param _v Valeur de la Case.
     * @param _x Coordonnée x.
     * @param _y Coordonnée y.
     * @param _z Coordonnée z.
     * @param _id id unique de la Case.
     * @param _c Cube dans lequel est la Case.
     * 
     * @see Cube
     */
    public Case(int _v, int _x, int _y, int _z, int _id, Cube _c) {
        valeur = _v;
        oldValeur = _v;
        x = _x;
        y = _y;
        z = _z;
        id = _id;
        cube = _c;
    }
    
    /**
     * Retourne le voisin le plus proche dans une direction donnée.
     * 
     * @param _direction Direction dans laquelle chercher le voisin.
     * @return Case voisine, <code>null</code> sinon.
     */
    public Case getVoisinDirect(int _direction){
        switch (_direction) {
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
    
    /**
     * Test si la Case a la même valeur que la Case passée en paramètre.
     * 
     * @param _c Case à comparer.
     * @return <code>true</code> si la Case a la même valeur, <code>false</code> sinon.
     */
    public boolean valeurEgale(Case _c) {
        if (_c != null) {
            return this.valeur == _c.valeur;
        } else {
            return false;
        }
    }

    /**
     * Retourne la Case sous la forme d'une chaîne de caractères.
     * La chaîne fait toujours 4 caractères et contient la valeur de la Case.
     * 
     * @return La Case sous la forme d'une chaîne de caractère.
     */
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

    /**
     * Retourne la valeur de la Case.
     * 
     * @return La valeur de la Case.
     */
    public int getValeur() {
        return valeur;
    }

    /**
     * Retourne l'ancienne valeur de la Case.
     * 
     * @return L'ancienne valeur de la Case.
     */
    public int getOldValeur() {
        return oldValeur;
    }

    /**
     * Retourne la coordonnée x de la Case.
     * 
     * @return La coordonnée x de la Case.
     */
    public int getX() {
        return x;
    }

    /**
     * Retourne la coordonnée y de la Case.
     * 
     * @return La coordonnée y de la Case.
     */
    public int getY() {
        return y;
    }

    /**
     * Retourne la coordonnée z de la Case.
     * 
     * @return La coordonnée z de la Case.
     */
    public int getZ() {
        return z;
    }

    /**
     * Retourne l'id de la Case.
     * 
     * @return L'id de la Case.
     */
    public int getId() {
        return id;
    }

    /**
     * Change la valeur de la Case.
     * L'ancienne valeur est enregistrée dans <code>oldValeur</code> avant.
     * 
     * @param _valeur Nouvelle valeur de la Case.
     */
    public void setValeur(int _valeur) {
        this.oldValeur = this.valeur;
        this.valeur = _valeur;
    }

    /**
     * Change la coordonnée x de la Case.
     * 
     * @param _x Nouvelle coordonnée x de la Case.
     */
    public void setX(int _x) {
        this.x = _x;
    }

    /**
     * Change la coordonnée y de la Case.
     * 
     * @param _y Nouvelle coordonnée y de la Case.
     */
    public void setY(int _y) {
        this.y = _y;
    }

    /**
     * Change la coordonnée z de la Case.
     * 
     * @param _z Nouvelle coordonnée z de la Case.
     */
    public void setZ(int _z) {
        this.z = _z;
    }
}
