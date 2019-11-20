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
import javafx.scene.layout.Pane;
import model.Partie;

/**
 * FXML Controller class
 *
 * @author Lucille
 */
public class FXMLAccueilController implements Initializable {

    @FXML
    private Pane container;
    @FXML
    private Button start_button;
    @FXML
    private Label label_username;
    @FXML
    private Button connexion_button;
    @FXML
    private Button inscription_button;
    @FXML
    private Label label_title;
    
    //Variables globales non définies dans la vue
    private BaseDeDonnees bdd;
    private Partie partie;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        bdd = BaseDeDonnees.getInstance();
        container.getStyleClass().add("container");
        start_button.getStyleClass().add("start_button");
        label_username.getStyleClass().add("label_username");
        connexion_button.getStyleClass().add("connexion_button");
        inscription_button.getStyleClass().add("inscription_button");
    }    
    
}
