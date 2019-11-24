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
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Pair;
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
    private double destX, destY, currX, currY;
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
    private void clickRecommencer(ActionEvent event) {
        System.out.println("Recommencer");
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Recommencer la partie");
        alert.setHeaderText("Recommencer la partie");
        alert.setContentText("Voulez-vous recommencer la partie ?\nToute progression non sauvegardée sera perdue");
        alert.setGraphic(new ImageView("/img/reset.png"));
        if (alert.showAndWait().get() == ButtonType.OK) {
            resetGame();
        } else {
            alert.close();
        }
    }

    /***POPUP CONNEXION***/
    @FXML
    private void clickConnexion(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Dialog<Pair<String, String>> dialog = new Dialog<>();
                dialog.setTitle("connexion.exe");
                dialog.setHeaderText("Connecte-toi !");
                Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
                //Icônes
                stage.getIcons().add(new Image(this.getClass().getResource("img/noun_Lock.png").toString()));
                dialog.setGraphic(new ImageView(this.getClass().getResource("img/noun_Lock.png").toString()));
                //Set le type de bouton
                ButtonType bouton_connexion = new ButtonType("Connexion", ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(bouton_connexion, ButtonType.CANCEL);//Création du label et txt field de pseudo et mdp
                //Set les cases des champs
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));

                TextField username_tf = new TextField();
                username_tf.setPromptText("Pseudo");
                PasswordField password_tf = new PasswordField();
                password_tf.setPromptText("Mot de passe");

                grid.add(new Label("Pseudo:"), 0, 0);
                grid.add(username_tf, 1, 0);
                grid.add(new Label("Mot de passe:"), 0, 1);
                grid.add(password_tf, 1, 1);
                
                // Enable/Disable login button depending on whether a username was entered.
                Node loginButton = dialog.getDialogPane().lookupButton(bouton_connexion);
                loginButton.setDisable(true);
                
                username_tf.textProperty().addListener((observable, oldValue, newValue) -> {
                loginButton.setDisable(newValue.trim().isEmpty());
                });
                dialog.getDialogPane().setContent(grid);
                
                Platform.runLater(() -> username_tf.requestFocus());

                // Convert the result to a username-password-pair when the login button is clicked.
                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == bouton_connexion) {
                        return new Pair<>(username_tf.getText(), password_tf.getText());
                    }
                    else return null;
                });
                Optional<Pair<String, String>> result = dialog.showAndWait();
                result.ifPresent(usernamePassword -> {
                    System.out.println("Pseudo=" + usernamePassword.getKey() + ", Mot de passe=" + usernamePassword.getValue());
                });
            }
        });
    }
    @FXML
    private void clickInscription(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Dialog<Pair<String, String>> dialog = new Dialog<>();
                dialog.setTitle("inscription.exe");
                dialog.setHeaderText("Inscris-toi !");
                Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
                
                stage.getIcons().add(new Image(this.getClass().getResource("img/noun_Lock.png").toString()));
                dialog.setGraphic(new ImageView(this.getClass().getResource("img/noun_Lock.png").toString()));
                
                ButtonType bouton_inscription = new ButtonType("Connexion", ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(bouton_inscription, ButtonType.CANCEL);

                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));

                TextField username_tf = new TextField();
                username_tf.setPromptText("Pseudo");
                TextField mail_tf = new TextField();
                mail_tf.setPromptText("E-mail");
                PasswordField password_tf = new PasswordField();
                password_tf.setPromptText("Mot de passe");
                PasswordField password_cf_tf = new PasswordField();
                password_cf_tf.setPromptText("Confirmation");
                //Positionnement des fields et labels sur une grille prédéfinie
                grid.add(new Label("E-mail:"), 0,0);
                grid.add(mail_tf, 0,1);
                grid.add(new Label("Pseudo:"), 1,0);
                grid.add(username_tf, 1,1);
                grid.add(new Label("Mot de passe:"), 2,0);
                grid.add(password_tf, 2,1);
                grid.add(new Label("Confirmation:"), 3,0);
                grid.add(password_cf_tf, 3,1);
                
                Node regButton = dialog.getDialogPane().lookupButton(bouton_inscription);
                
                username_tf.textProperty().addListener((observable, oldValue, newValue) -> {
                    
                    regButton.setDisable(newValue.trim().isEmpty());
                });
                dialog.getDialogPane().setContent(grid);
                
                Platform.runLater(() -> mail_tf.requestFocus());
                Platform.runLater(() -> username_tf.requestFocus());
                Platform.runLater(() -> password_tf.requestFocus());
                Platform.runLater(() -> password_cf_tf.requestFocus());
                
                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == bouton_inscription) {
                        return new Pair<>(username_tf.getText(), password_tf.getText());
                    }
                    else return null;
                });
                Optional<Pair<String, String>> result = dialog.showAndWait();
                result.ifPresent(usernamePassword -> {
                    System.out.println("Pseudo=" + usernamePassword.getKey() + ", Mot de passe=" + usernamePassword.getValue());
                });
            }
        });
    }
    @FXML
    public void keyPressed(KeyEvent ke) {
        System.out.println("touche appuyée");
        String touche = ke.getText();

        if (partie != null) {
            if (touche.compareTo("q") == 0) {
                partie.setDirection(GAUCHE);
                synchronized (partie) {
                    partie.notify();
                }
            } else if (touche.compareTo("d") == 0) {
                partie.setDirection(DROITE);
                synchronized (partie) {
                    partie.notify();
                }
            } else if (touche.compareTo("z") == 0) {
                partie.setDirection(HAUT);
                synchronized (partie) {
                    partie.notify();
                }
            } else if (touche.compareTo("s") == 0) {
                partie.setDirection(BAS);
                synchronized (partie) {
                    partie.notify();
                }
            } else if (touche.compareTo("a") == 0) {
                partie.setDirection(AVANT);
                synchronized (partie) {
                    partie.notify();
                }
            } else if (touche.compareTo("e") == 0) {
                partie.setDirection(ARRIERE);
                synchronized (partie) {
                    partie.notify();
                }
            }
        } else if (touche.compareTo(" ") == 0) {
            clickStart(new ActionEvent());
        }
    }

    // Mets à jour les cases dans la vue
    public void updatePanes() {
        cleanMap();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                nb_score.setText("" + partie.getScore());
                nb_move.setText("" + partie.getMove());
            }
        });
        Case cube[][][] = partie.getCube().getCube();
        for (int k = 0; k < TAILLE; k++) {
            for (int j = 0; j < TAILLE; j++) {
                for (int i = 0; i < TAILLE; i++) {
                    Case c = cube[k][j][i];
                    if (c != null) {
                        if (panes.containsKey(c.getId())) {
                            Pane p = panes.get(c.getId());
                            destX = c.getX() * wh_case;
                            destY = y + c.getY() * wh_case;
                            switch (c.getZ()) {
                                case 0:
                                    destX += x_1;
                                    break;
                                case 1:
                                    destX += x_2;
                                    break;
                                case 2:
                                    destX += x_3;
                                    break;
                            }
                            currX = p.getLayoutX();
                            currY = p.getLayoutY();
                            if (false) {
                                /*if (!c.isChangeDeGrille()) {
                                if (currX != destX) {
                                    Task task = new Task<Void>() {
                                        @Override
                                        protected Void call() throws Exception {
                                            while (currX != destX) {
                                                if (currX < destX) {
                                                    currX += 1;
                                                } else {
                                                    currX -= 1;
                                                }
                                                Platform.runLater(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        p.relocate(currX, destY);
                                                        p.setVisible(true);
                                                    }
                                                });
                                                Thread.sleep(5);
                                            }
                                            return null;
                                        }
                                    };
                                    Thread thread = new Thread(task);
                                    thread.setDaemon(true);
                                    thread.start();
                                } else if (currY != destY) {
                                    Task task = new Task<Void>() {
                                        @Override
                                        protected Void call() throws Exception {
                                            while (currY != destY) {
                                                if (currY < destY) {
                                                    currY += 1;
                                                } else {
                                                    currY -= 1;
                                                }
                                                Platform.runLater(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        p.relocate(destX, currY);
                                                        p.setVisible(true);
                                                    }
                                                });
                                                Thread.sleep(5);
                                            }
                                            return null;
                                        }
                                    };
                                    Thread thread = new Thread(task);
                                    thread.setDaemon(true);
                                    thread.start();
                                }*/
                            } else {
                                p.relocate(destX, destY);
                                p.setVisible(true);
                                c.setChangeDeGrille(false);
                            }
                            if (c.getValeur() != c.getOldValeur()) {
                                Label l = (Label) p.getChildren().get(0);
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        l.setText("" + c.getValeur());
                                    }
                                });
                                l.getStyleClass().remove(0);
                                l.getStyleClass().add("case_label_" + c.getValeur());
                            }
                        } else {
                            Pane p = new Pane();
                            Label l = new Label("" + c.getValeur());
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    container.getChildren().add(p);
                                    p.getChildren().add(l);
                                }
                            });
                            p.getStyleClass().add("case_pane");
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
                            l.getStyleClass().add("case_label_" + c.getValeur());
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

    // Supprime les cases détruites dans le modèle sur la vue et dans panes
    private void cleanMap() {
        Iterator it = panes.entrySet().iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            if (partie.getCube().getCase((int) entry.getKey()) == null) {
                Pane p = (Pane) entry.getValue();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        container.getChildren().remove(p);
                    }
                });
                it.remove();
            }
        }
    }

    // Affiche un pop up en cas de victoire
    public void victory() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Victoire");
                alert.setHeaderText("Bien joué, vous avez atteint l'objectif !!!");
                alert.setContentText("Vous avez terminé la partie\nScore : " + partie.getScore() + "\nDéplacements : " + partie.getMove());
                alert.setGraphic(new ImageView("/img/victory.png"));
                if (alert.showAndWait().get() == ButtonType.OK) {
                    resetGame();
                    alert.close();
                }
            }
        });
    }

    // Affiche un pop up en cas de défaite
    public void gameOver() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Défaite");
                alert.setHeaderText("Vous n'avez pas atteint l'objectif...");
                alert.setContentText("Vous avez atteint " + partie.getCube().getValeurMax() + "\nScore : " + partie.getScore() + "\nDéplacements : " + partie.getMove());
                alert.setGraphic(new ImageView("/img/gameOver.png"));
                if (alert.showAndWait().get() == ButtonType.OK) {
                    resetGame();
                    alert.close();
                }
            }
        });
    }

    // Réinitialise l'interface pour commencer une nouvelle partie
    private void resetGame() {
        Iterator it = panes.entrySet().iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            Pane p = (Pane) entry.getValue();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    container.getChildren().remove(p);
                }
            });
            it.remove();
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                nb_move.setText("0");
                nb_score.setText("0");
            }
        });
        panes.clear();
        if (partie != null) {
            partie.interrupt();
            partie = null;
        }
        start_button.setDisable(false);
    }
}
