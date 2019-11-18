/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import bdd.BaseDeDonnees;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import model.Case;
import model.Parametres;
import static model.Parametres.TAILLE;
import model.Partie;

/**
 *
 * @author Lucille
 */
public class FXMLDocumentController implements Initializable, Parametres {

    @FXML
    private Pane container; // panneau recouvrant toute la fenêtre
    @FXML
    private GridPane grid1; //Grille à gauche dans le jeu
    @FXML
    private GridPane grid2; //Grille au milieu
    @FXML
    private GridPane grid3; //Grille à droite

    @FXML
    private Label score_label; // value will be injected by the FXMLLoader
    @FXML
    private Pane score_pane; //Conteneur du label score et du score actuel
    @FXML
    private Label nb_score; //Valeur du score à l'écran

    @FXML
    private Label move_label; //le mot "Move" dans le jeu
    @FXML
    private Pane move_pane; //Le conteneur de move_label + nb_move
    @FXML
    private Label nb_move; //Nombre de mouvements effectués

    @FXML
    private Button start_button; //Bouton "START"

    @FXML
    private Menu option_button;
    @FXML
    private Menu game_button;

    @FXML
    private MenuBar menu_bar;
    @FXML
    private MenuItem pt_restart; //bouton "recommencer"
    @FXML
    private MenuItem pt_giveup; //bouton "abandonner"
    @FXML
    private MenuItem pt_save; //bouton "sauvegarder"

    @FXML
    private MenuItem logout_button; //bouton déconnexion

    @FXML
    private MenuItem op_theme;
    @FXML
    private MenuItem op_son;

    // variables globales non définies dans la vue (fichier .fxml)
    private BaseDeDonnees bdd;
    private Partie partie;
    //Cases du jeu
    private final Map<Integer, Pane> panes = new HashMap<>();
    private final int wh_case = 80; //largeur et hauteur de la case (en px)
    private int x_1 = 37, x_2 = 280, x_3 = 523, y = 270;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bdd = BaseDeDonnees.getInstance();
        System.out.println("le contrôleur initialise la vue");
        // utilisation de styles pour la grille et la tuile (voir style_2048.css)
        grid1.getStyleClass().add("grid");
        grid2.getStyleClass().add("grid");
        grid3.getStyleClass().add("grid");

        score_pane.getStyleClass().add("score_pane");
        score_label.getStyleClass().add("score_label");
        nb_score.getStyleClass().add("nb_score");

        move_pane.getStyleClass().add("move_pane");
        move_label.getStyleClass().add("move_label");
        nb_move.getStyleClass().add("nb_move");
        //bouton start et les boutons du menu
        start_button.getStyleClass().add("bouton_start");
        //MENU
        menu_bar.getStyleClass().add("menu_bar");
        option_button.getStyleClass().add("bouton_menu");
        game_button.getStyleClass().add("bouton_menu");
        //boutons sous-menu MENU>>PARTIE>>
        pt_restart.getStyleClass().add("bouton_sous_menu");
        pt_giveup.getStyleClass().add("bouton_sous_menu");
        pt_save.getStyleClass().add("bouton_sous_menu");
        logout_button.getStyleClass().add("bouton_sous_menu");
        //bouton SON et THEME (switch)
        //op_son.getStyleClass().add("bouton");
        //op_theme.getStyleClass().add("bouton");

        //GridPane.setHalignment(case_pane, HPos.CENTER);
        //GridPane.setHalignment(case_label_2, HPos.CENTER);
        //grid1.getChildren().add(case_pane); //ajout de la case à son conteneur: la grille 1
        //case_pane.getChildren().add(case_label_2);
        // on place la tuile en précisant les coordonnées (x,y) du coin supérieur gauche d'une des trois grilles
        //Case 0,0 grille 1: layout X=38, layout Y=271
        //Case 0,0 grille 2: layout X=282
        //Case 0,0 grille 3: layout X=525
        //case_pane.setLayoutX(x_1);  //a gauche
        //case_pane.setLayoutY(y);    //en haut
        //case_pane.setPrefSize(wh_case, wh_case);
        //case_pane.setVisible(true);
        //case_label_2.setVisible(true);
    }

    @FXML
    private void clickStart(ActionEvent event) {
        if (!start_button.isDisable()) {
            System.out.println("touche START appuyée");
            partie = new Partie(this);
            partie.start();
            start_button.setDisable(true);
            container.requestFocus();
        }
    }

    @FXML
    public void keyPressed(KeyEvent ke) {
        System.out.println("touche appuyée");
        String touche = ke.getText();

        if (touche.compareTo("q") == 0) {
            partie.setDirection(GAUCHE);
            synchronized(partie) {
                partie.notify();
            }
        } else if (touche.compareTo("d") == 0) {
            partie.setDirection(DROITE);
            synchronized(partie) {
                partie.notify();
            }
        } else if (touche.compareTo("z") == 0) {
            partie.setDirection(HAUT);
            synchronized(partie) {
                partie.notify();
            }
        } else if (touche.compareTo("s") == 0) {
            partie.setDirection(BAS);
            synchronized(partie) {
                partie.notify();
            }
        } else if (touche.compareTo("r") == 0) {
            partie.setDirection(AVANT);
            synchronized(partie) {
                partie.notify();
            }
        } else if (touche.compareTo("f") == 0) {
            partie.setDirection(ARRIERE);
            synchronized(partie) {
                partie.notify();
            }
        }
    }

    // Mets à jour les cases dans la vue
    public void updatePanes() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                cleanMap();
                nb_score.setText("" + partie.getScore());
                nb_move.setText("" + partie.getMove());
                Case cube[][][] = partie.getCube().getCube();
                for (int k = 0; k < TAILLE; k++) {
                    for (int j = 0; j < TAILLE; j++) {
                        for (int i = 0; i < TAILLE; i++) {
                            Case c = cube[k][j][i];
                            if (c != null) {
                                if (panes.containsKey(c.getId())) {
                                    Pane p = panes.get(c.getId());
                                    if (c.getZ() != c.getOldZ()) {
                                        switch (c.getZ()) {
                                            case 0:
                                                p.relocate(x_1 + c.getX() * wh_case, p.getLayoutY());
                                                break;
                                            case 1:
                                                p.relocate(x_2 + c.getX() * wh_case, p.getLayoutY());
                                                break;
                                            case 2:
                                                p.relocate(x_3 + c.getX() * wh_case, p.getLayoutY());
                                                break;
                                        }
                                    } else if (c.getX() != c.getOldX()) {
                                        switch (c.getZ()) {
                                            case 0:
                                                p.relocate(x_1 + c.getX() * wh_case, p.getLayoutY());
                                                break;
                                            case 1:
                                                p.relocate(x_2 + c.getX() * wh_case, p.getLayoutY());
                                                break;
                                            case 2:
                                                p.relocate(x_3 + c.getX() * wh_case, p.getLayoutY());
                                                break;
                                        }
                                    } else if (c.getY() != c.getOldY()) {
                                        p.relocate(p.getLayoutX(), y + c.getY() * wh_case);
                                    }
                                    if (c.getValeur() != c.getOldValeur()) {
                                        Label l = (Label) p.getChildren().get(0);
                                        l.setText("" + c.getValeur());
                                        l.getStyleClass().remove(0);
                                        l.getStyleClass().add("case_label_" + c.getValeur());
                                    }
                                } else {
                                    Pane p = new Pane();
                                    p.getStyleClass().add("case_pane");
                                    container.getChildren().add(p);
                                    switch (c.getZ()) {
                                        case 0:
                                            p.setLayoutX(x_1 + c.getX() * wh_case);
                                            break;
                                        case 1:
                                            p.setLayoutX(x_2 + c.getX() * wh_case);
                                            break;
                                        case 2:
                                            p.setLayoutX(x_3 + c.getX() * wh_case);
                                            break;
                                    }
                                    p.setLayoutY(y + c.getY() * wh_case);
                                    Label l = new Label("" + c.getValeur());
                                    l.getStyleClass().add("case_label_" + c.getValeur());
                                    p.getChildren().add(l);
                                    p.setPrefSize(wh_case, wh_case);
                                    l.setPrefSize(wh_case, wh_case);
                                    l.setAlignment(Pos.CENTER);
                                    p.setVisible(true);
                                    panes.put(c.getId(), p);
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    private void cleanMap() {
        Iterator it = panes.entrySet().iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            if (partie.getCube().getCase((int) entry.getKey()) == null) {
                Pane p = (Pane) entry.getValue();
                container.getChildren().remove(p);
                it.remove();
            }
        }
    }

}
