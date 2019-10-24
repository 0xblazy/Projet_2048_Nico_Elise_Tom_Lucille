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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
