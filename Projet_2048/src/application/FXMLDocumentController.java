/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 *
 * @author Lucille
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Label score; // value will be injected by the FXMLLoader
    @FXML
    private Label move_label; //le mot "Move" dans le jeu
    @FXML
    private Label nb_move; //Nombre de mouvements effectués
    @FXML
    private Pane container; // panneau recouvrant toute la fenêtre
    @FXML
    private Pane move_pane; //Le conteneur de move_label + nb_move
    @FXML
    private GridPane grid1; //Grille à gauche dans le jeu
    @FXML
    private GridPane grid2; //Grille au milieu
    @FXML
    private GridPane grid3; //Grille à droite
    @FXML
    private Button start_button; //Bouton "START"
    
    //private Cube grilleModele = new Cube(Partie p);
    // variables globales non définies dans la vue (fichier .fxml)
    private final Pane p = new Pane(); // panneau utilisé pour dessiner une tuile "2"
    private final Label c = new Label("2");
    private int x_1 = 22, x_2 = 267, x_3 = 514, y = 270;
    private int objectifx_1 = 22, objectifx_2 = 267, objectifx_3 = 514, objectify = 270;


    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        System.out.println("le contrôleur initialise la vue");
        // utilisation de styles pour la grille et la tuile (voir styles.css)
        p.getStyleClass().add("pane"); 
        c.getStyleClass().add("tuile");
        grid1.getStyleClass().add("grid1");
        grid2.getStyleClass().add("grid2");
        grid3.getStyleClass().add("grid3");
        GridPane.setHalignment(c, HPos.CENTER);
        container.getChildren().add(p);
        p.getChildren().add(c);

        // on place la tuile en précisant les coordonnées (x,y) du coin supérieur gauche d'une des trois grille
        p.setLayoutX(x_1);
        p.setLayoutX(x_2);
        p.setLayoutX(x_3);
        p.setLayoutY(y);
        p.setVisible(true);
        c.setVisible(true);
    }    
    
}
