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
import javafx.application.Platform;

/**
 * Thread de la Partie, caractérisée par un Cube, un score, un nombre de déplacement et un temps.
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
    private boolean inConsole;
    private boolean reload;
    private long temps;
    private transient long debut;

    /**
     * Constructeur.
     * 
     * @param _controller Controller de la Partie.
     * @param _joueur Joueur de la Partie.
     * 
     * @see application.FXMLDocumentController
     * @see Joueur
     */
    public Partie(FXMLDocumentController _controller, Joueur _joueur, boolean _inConsole) {
        bdd = BaseDeDonnees.getInstance();
        cube = new Cube();
        score = 0;
        move = 0;
        controller = _controller;
        joueur = _joueur;
        inConsole = _inConsole;
        reload = false;
        temps = 0;
    }

    /**
     * Initialise le Cube en générant deux Cases.
     * 
     * @see Cube
     */
    private void initCube() {
        cube.nouvelleCase();
        cube.nouvelleCase();
    }

    /**
     * Affiche le Cube dans la console.
     * 
     * @see Cube
     */
    private void afficherCube() {
        System.out.println(cube + "\n");
    }

    /**
     * Lance le Thread de la Partie.
     * Si c'est le premier lancement de la Partie, on commence par initaliser le Cube.
     */
    public void run() {
        if (!reload) {
            initCube();
        }
        if (inConsole) {
            System.out.println("Déplacements : " + move + " Score : " + score + " Max : " + cube.getValeurMax());
            afficherCube();
        } else {
            controller.updatePanes();
        }
        Scanner sc = new Scanner(System.in);
        debut = System.currentTimeMillis();
        // Boucle de jeu
        while (!cube.partieFinie() && cube.getValeurMax() < OBJECTIF) {
            // Affichage
            if (inConsole) {
                System.out.println("z -> HAUT\ns -> BAS\nq -> GAUCHE\nd -> DROITE\na -> AVANT\ne -> ARRIERE\nl -> Quitter la partie et retourner à l'interface");
                System.out.print("Choix : ");
                String s = sc.nextLine();
                while (!s.equals("z") && !s.equals("s") && !s.equals("q") && !s.equals("d") && !s.equals("a") && !s.equals("e") && !s.equals("l")) {
                    System.out.print("Saisie incorrecte, veuillez faire votre choix : ");
                    s = sc.nextLine();
                }
                switch (s) {
                    case "z" :
                        direction = HAUT;
                        break;
                    case "s" :
                        direction = BAS;
                        break;
                    case "q" :
                        direction = GAUCHE;
                        break;
                    case "d" :
                        direction = DROITE;
                        break;
                    case "a" :
                        direction = AVANT;
                        break;
                    case "e" : 
                        direction = ARRIERE;
                        break;
                    case "l" :
                        Thread.currentThread().interrupt();
                        break;
                }
                if (isInterrupted()) break;
            } else {
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
            // Déplacements
            boolean deplacement = cube.lanceurDeplacerCases(direction);
            // Score
            score += cube.getScoreTour();
            // Génération d'une nouvelle case si déplacement
            if (deplacement) {
                move++;
                cube.nouvelleCase();
                if (!inConsole) {
                    controller.updatePanes();
                }
            }
            if (inConsole) {
                System.out.println("");
                System.out.println("Déplacements : " + move + " Score : " + score + " Max : " + cube.getValeurMax());
                afficherCube();
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
            if (inConsole) {
                afficherCube();
            } else {
                controller.updatePanes();
            }
            if (cube.getValeurMax() >= OBJECTIF) {
                if (inConsole) {
                    cube.victory();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            controller.getStage().show();
                            controller.getStage().requestFocus();
                            controller.getStage().toFront();
                            Platform.setImplicitExit(true);
                        }
                    });
                } else {
                    controller.victory();
                } 
            } else {
                if (inConsole) {
                    cube.gameOver();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            controller.getStage().show();
                            controller.getStage().requestFocus();
                            controller.getStage().toFront();
                            Platform.setImplicitExit(true);
                        }
                    });
                } else {
                    controller.gameOver();
                } 
            }
        } else {
            if (inConsole) {
                System.out.println("Partie interrompue");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        controller.getStage().show();
                        controller.getStage().requestFocus();
                        controller.getStage().toFront();
                        Platform.setImplicitExit(true);
                    }
                });
            }
        }
    }
    
    /**
     * Ajoute le temps depuis le lancement de Thread au temps total de la Partie.
     * Cela permet d'avoir un temps total cohérent si on sauvegarde la Partie et qu'on la reprend plus tard.
     */
    public void sauvegardeTemps() {
        temps += System.currentTimeMillis() - debut;
    }

    /**
     * Retourne le score de la Partie.
     * 
     * @return Le score de la Partie.
     */
    public int getScore() {
        return score;
    }

    /**
     * Retourne le nombre de déplacement de la Partie.
     * 
     * @return Le nombre de déplacement de la Partie.
     */
    public int getMove() {
        return move;
    }

    /**
     * Retourne le Cube de la Partie.
     * 
     * @return Le Cube de la Partie.
     * 
     * @see Cube
     */
    public Cube getCube() {
        return cube;
    }

    /**
     * Défini la direction pour le prochain déplacement.
     * 
     * @param _direction Nouvelle direction.
     */
    public void setDirection(int _direction) {
        this.direction = _direction;
    }

    /**
     * Défini le Joueur de la Partie.
     * Permet d'ajouter le Joueur si jamais il se connecte après avoir commencé la Partie ou lorsqu'il recharge la Partie.
     * 
     * @param _joueur Joueur de la Partie.
     * 
     * @see Joueur
     */
    public void setJoueur(Joueur _joueur) {
        this.joueur = _joueur;
    }

    /**
     * Défini le Controller de la Partie.
     * Utilisée lorsque l'on recharge la Partie, car le Controller n'est pas Serializable.
     * 
     * @param _controller Controller de la Partie.
     * 
     * @see application.FXMLDocumentController
     */
    public void setController(FXMLDocumentController _controller) {
        this.controller = _controller;
    }

    /**
     * Défini la BaseDeDonnees de la Partie.
     * Utilisée lorsque l'on recharge la Partie, car la BaseDeDonnees n'est pas Serializable.
     * 
     * @param _bdd BaseDeDonnees de la Partie.
     * 
     * @see bdd.BaseDeDonnees
     */
    public void setBdd(BaseDeDonnees _bdd) {
        this.bdd = _bdd;
    }

    /**
     * Défini sur la Partie est rechargée ou pas.
     * Utilisée lorsque l'on sauvegarde la Partie.
     * 
     * @param _reload <code>true</code> si la Partie sera rechargée.<br>
     * En principe, toujours utilisée avec <code>true</code>.
     */
    public void setReload(boolean _reload) {
        this.reload = _reload;
    }
}
