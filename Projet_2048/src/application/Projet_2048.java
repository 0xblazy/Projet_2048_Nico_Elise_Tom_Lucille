/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import bdd.BaseDeDonnees;
import java.util.List;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Partie;

/**
 *
 * @author nKBlaZy
 */
public class Projet_2048 extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        
        BaseDeDonnees bdd = BaseDeDonnees.getInstance();
        if (bdd.connection()) {
            //System.out.println(bdd.creationJoueur("Smithy", "smith"));
            //System.out.println(bdd.connectJoueur("Smithy", "smith"));
            //System.out.println(bdd.connectJoueur("Yolo", "smith"));
            //System.out.println(bdd.connectJoueur("Smithy", "yolo"));
            //System.out.println(bdd.getScoreMax("Smithy"));
            //System.out.println(bdd.getScoreMax("Yolo"));
            //System.out.println(bdd.setScoreMax("Smithy", 42));
            //System.out.println(bdd.setScoreMax("Yolo", 42));
            //System.out.println(bdd.getScoreMax("Smithy"));
            //bdd.creationJoueur("Nico", "pass");
            /*bdd.setScoreMax("Nico", 64);
            List<Object[]> list = bdd.getClassement();
            for (Object[] j : list) {
                System.out.println(j[0].toString() + " " + (int) j[1]);
            } */   
            //System.out.println(bdd.insertionPartie("Smithy", 0, 2048) == -122);
            /*List<int[]> list = bdd.getHistorique("Smithy");
            for (int[] j : list) {
                System.out.println(j[0] + " " + j[1]);
            }*/
            //System.out.println(bdd.getNbParties("Smithy"));
            
            bdd.deconnection();
        }
        
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        Scene scene = new Scene(root,800,600);
        boolean add = scene.getStylesheets().add("css/style_2048_kawaii.css");
        
        stage.setTitle("2048 3D");
        stage.setScene(scene);
        stage.show();        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
