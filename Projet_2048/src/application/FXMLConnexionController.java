/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import bdd.BaseDeDonnees;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author TomWyso
 */
public class FXMLConnexionController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private Pane container;
    @FXML
    private Pane field_pane;
    @FXML
    private Label label_mdp;
    @FXML
    private Label label_identifiant;
    @FXML
    private TextField txt_connexion;
    @FXML
    private TextField txt_mdp;
    @FXML
    private Button connexion_bouton;
    
    private BaseDeDonnees bdd;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bdd = BaseDeDonnees.getInstance();
        /*****SET LES TAILLES*****/
        container.setPrefSize(800, 600);
        /*****SET LE CSS*****/
        container.getStyleClass().add("container");
        field_pane.getStyleClass().add("field_pane");
        label_mdp.getStyleClass().add("label_id");
        label_identifiant.getStyleClass().add("label_id");
        txt_connexion.getStyleClass().add("txt_connexion");
        txt_mdp.getStyleClass().add("txt_mdp");
        connexion_bouton.getStyleClass().add("connexion_bouton");
    }    
    
}
