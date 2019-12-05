/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Classe main du projet.
 * 
 * @author nKBlaZy
 * @author Lixye
 * @author TomWyso
 */
public class Projet_2048 extends Application {

    /**
     * Arrête l'exécution du programme.
     * 
     * @throws Exception 
     */
    @Override
    public void stop() throws Exception {
        Platform.exit();
        System.exit(0);
    }
    
    /**
     * Appelé lors du lancement du programme.
     * 
     * @param stage Stage de l'application.
     * @throws Exception 
     */
    @Override
    public void start(Stage stage) throws Exception {        
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        Scene scene = new Scene(root,800,600);

        scene.getRoot().requestFocus();
        stage.setTitle("2048 3D");
        stage.getIcons().add(new Image("/img/logo.png"));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        stage.requestFocus();
    }

    /**
     * @param args Argument de la ligne de commande.
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
