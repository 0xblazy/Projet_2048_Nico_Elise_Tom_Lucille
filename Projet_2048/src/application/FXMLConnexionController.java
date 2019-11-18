/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private Label label_inscription;
    @FXML
    private Label label_connexion;
    @FXML
    private TextField txt_connexion;
    @FXML
    private TextField txt_inscription;
    
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
