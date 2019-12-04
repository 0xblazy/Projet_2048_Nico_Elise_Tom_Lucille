/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import bdd.BaseDeDonnees;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;
import model.Case;
import model.Joueur;
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
    private Menu classement_button;
    @FXML
    private Menu historique_button;

    @FXML
    private MenuBar menu_bar;
    @FXML
    private MenuItem pt_restart; //bouton "recommencer"
    @FXML
    private MenuItem pt_save; //bouton "sauvegarder"
    @FXML
    private MenuItem pt_load; //bouton "charger"

    @FXML
    private MenuItem logout_button; //bouton déconnexion

    @FXML
    private Button switch_theme;

    @FXML
    private Button connexion_button;
    @FXML
    private Button inscription_button;
    @FXML
    private Label connect_has;

    // variables globales non définies dans la vue (fichier .fxml)
    private BaseDeDonnees bdd;
    private Partie partie;
    private Joueur joueur = null;
    private boolean reloaded = false;
    //Cases du jeu
    private final Map<Integer, Pane> panes = new HashMap<>();
    private final int wh_case = 80; //largeur et hauteur de la case (en px)

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bdd = BaseDeDonnees.getInstance();
        System.out.println("le contrôleur initialise la vue");

        container.getStylesheets().clear();
        container.getStylesheets().add("css/style_2048.css");

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
        connexion_button.getStyleClass().add("boutons");
        inscription_button.getStyleClass().add("boutons");
        //MENU
        menu_bar.getStyleClass().add("menu_bar");
        option_button.getStyleClass().add("bouton_menu");
        game_button.getStyleClass().add("bouton_menu");
        classement_button.getStyleClass().add("bouton_menu");
        historique_button.getStyleClass().add("bouton_menu");
        //boutons sous-menu MENU>>PARTIE>>
        pt_load.getStyleClass().add("bouton_sous_menu");
        pt_restart.getStyleClass().add("bouton_sous_menu");
        pt_save.getStyleClass().add("bouton_sous_menu");
        logout_button.getStyleClass().add("bouton_sous_menu");
        //BOUTON CHANGER THEME
        switch_theme.setGraphic(new ImageView("/img/theme_icon.png"));
        switch_theme.getStyleClass().add("switch_theme");
       
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
            if (start_button.getText().equals("JOUER")) { //Si c'est "jouer" sur le bouton, nous sommes dans la page d'acceuil
                start_button.setText("START");
                afficherComposant();
            } else if (start_button.getText().equals("START")) {
                start_button.setDisable(true);
                pt_load.setDisable(true);
                pt_restart.setDisable(false);
                if (joueur != null) {
                    pt_save.setDisable(false);
                }

                partie = new Partie(this, joueur);
                partie.start();

                container.requestFocus();
            }
        }
    }

    // Affiche les éléments du jeu
    private void afficherComposant() {
        grid1.setVisible(true);
        grid2.setVisible(true);
        grid3.setVisible(true);
        score_label.setVisible(true);
        score_pane.setVisible(true);
        nb_score.setVisible(true);
        move_pane.setVisible(true);
        move_label.setVisible(true);
        nb_move.setVisible(true);
    }

    // Cache les éléments du jeu
    private void cacherComposant() {
        grid1.setVisible(false);
        grid2.setVisible(false);
        grid3.setVisible(false);
        score_label.setVisible(false);
        score_pane.setVisible(false);
        nb_score.setVisible(false);
        move_pane.setVisible(false);
        move_label.setVisible(false);
        nb_move.setVisible(false);
    }

    // Pop up pour recommencer la partie
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
            reloaded = false;
        } else {
            alert.close();
        }
    }

    // Pop up de connexion
    @FXML
    private void clickConnexion() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Connexion");
        dialog.setHeaderText("Connecte-toi !");
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        Label wrong_pseudo = new Label("");
        wrong_pseudo.setTextFill(Color.RED);
        wrong_pseudo.setPrefWidth(150);
        Label wrong_pass = new Label("");
        wrong_pass.setTextFill(Color.RED);
        //Icônes
        stage.getIcons().add(new Image(this.getClass().getResource("/img/noun_Lock_small.png").toString()));
        dialog.setGraphic(new ImageView(this.getClass().getResource("/img/noun_Lock_small.png").toString()));
        // Défini le type de bouton
        ButtonType bouton_connexion = new ButtonType("Connexion", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(bouton_connexion, ButtonType.CANCEL);//Création du label et txt field de pseudo et mdp
        // Défini les cases des champs
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 10));

        TextField username_tf = new TextField();
        username_tf.setPromptText("Pseudo");
        PasswordField password_tf = new PasswordField();
        password_tf.setPromptText("Mot de passe");

        grid.add(new Label("Pseudo:"), 0, 0);
        grid.add(username_tf, 1, 0);
        grid.add(wrong_pseudo, 2, 0);
        grid.add(new Label("Mot de passe:"), 0, 1);
        grid.add(password_tf, 1, 1);
        grid.add(wrong_pass, 2, 1);

        // Active ou désactive le bouton de connexion en fonction de la situation
        Node loginButton = dialog.getDialogPane().lookupButton(bouton_connexion);
        loginButton.setDisable(true);

        Node cancelButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);

        username_tf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!username_tf.getText().equals("") && !password_tf.getText().equals("") && checkPseudo(username_tf.getText()) && checkPassword(password_tf.getText())) {
                loginButton.setDisable(false);
            } else {
                loginButton.setDisable(true);
            }
        });
        password_tf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!username_tf.getText().equals("") && !password_tf.getText().equals("") && checkPseudo(username_tf.getText()) && checkPassword(password_tf.getText())) {
                loginButton.setDisable(false);
            } else {
                loginButton.setDisable(true);
            }
        });

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(() -> username_tf.requestFocus());

        dialog.setOnCloseRequest(e -> {
            if (cancelButton.isFocused()) {
                dialog.close();
            } else {
                if (bdd.connection()) {
                    int code = bdd.connectJoueur(username_tf.getText(), password_tf.getText());
                    if (code == CORRECT_DATA) {
                        joueur = new Joueur(username_tf.getText());
                        logout_button.setDisable(false);
                        if (partie == null) {
                            pt_load.setDisable(false);
                        }
                        inscription_button.setVisible(false);
                        connexion_button.setVisible(false);
                        connect_has.setText("Connecté sur le compte de " + username_tf.getText());
                        connect_has.setVisible(true);
                        if (partie != null) {
                            partie.setJoueur(joueur);
                            pt_save.setDisable(false);
                            container.requestFocus();
                        }
                        dialog.close();
                    } else if (code == ERROR_NOPLAYER) {
                        wrong_pseudo.setText("Pseudo incorrect");
                        wrong_pass.setText("");
                        e.consume();
                    } else if (code == ERROR_WRONG_PASS) {
                        wrong_pseudo.setText("");
                        wrong_pass.setText("Mot de passe incorrect");
                        e.consume();
                    }
                    bdd.deconnection();
                }
            }

        });

        dialog.showAndWait();
    }

    // Pop up d'inscription
    @FXML
    private void clickInscription() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Inscription");
        dialog.setHeaderText("Inscris-toi !");
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        Label pseudoExist = new Label("");
        pseudoExist.setTextFill(Color.RED);

        stage.getIcons().add(new Image(this.getClass().getResource("/img/noun_Lock_small.png").toString()));
        dialog.setGraphic(new ImageView(this.getClass().getResource("/img/noun_Lock_small.png").toString()));

        ButtonType bouton_inscription = new ButtonType("Inscription", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(bouton_inscription, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 10));

        TextField username_tf = new TextField();
        username_tf.setPromptText("Pseudo");
        PasswordField password_tf = new PasswordField();
        password_tf.setPromptText("Mot de passe");
        PasswordField password_cf_tf = new PasswordField();
        password_cf_tf.setPromptText("Confirmation");
        //Positionnement des fields et labels sur une grille prédéfinie
        grid.add(new Label("Le pseudo peut contenir uniquement les caractères suivants : a-z, A-Z, 0-9, -, _ et doit faire au moins 5 caractères\n"
                + "Le mot de passe doit contenir une majuscule, une minuscule, un chiffre et doit faire au moins 6 caractères"), 0, 0, 3, 1);
        grid.add(new Label("Pseudo:"), 0, 1);
        grid.add(username_tf, 1, 1);
        grid.add(pseudoExist, 2, 1);
        grid.add(new Label("Mot de passe:"), 0, 2);
        grid.add(password_tf, 1, 2);
        grid.add(new Label("Confirmation:"), 0, 3);
        grid.add(password_cf_tf, 1, 3);

        Node regButton = dialog.getDialogPane().lookupButton(bouton_inscription);
        regButton.setDisable(true);

        username_tf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!username_tf.getText().equals("") && !password_tf.getText().equals("") && password_cf_tf.getText().equals(password_tf.getText()) && checkPseudo(username_tf.getText()) && checkPassword(password_tf.getText()) && !pseudoExist(username_tf.getText())) {
                regButton.setDisable(false);
            } else {
                regButton.setDisable(true);
            }
        });
        username_tf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pseudoExist(username_tf.getText())) {
                pseudoExist.setText("Ce pseudo est déjà pris");
            } else {
                pseudoExist.setText("");
            }
        });
        password_tf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!username_tf.getText().equals("") && !password_tf.getText().equals("") && password_cf_tf.getText().equals(password_tf.getText()) && checkPseudo(username_tf.getText()) && checkPassword(password_tf.getText()) && !pseudoExist(username_tf.getText())) {
                regButton.setDisable(false);
            } else {
                regButton.setDisable(true);
            }
        });
        password_cf_tf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!username_tf.getText().equals("") && !password_tf.getText().equals("") && password_cf_tf.getText().equals(password_tf.getText()) && checkPseudo(username_tf.getText()) && checkPassword(password_tf.getText()) && !pseudoExist(username_tf.getText())) {
                regButton.setDisable(false);
            } else {
                regButton.setDisable(true);
            }
        });

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(() -> username_tf.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == bouton_inscription) {
                return new Pair<>(username_tf.getText(), password_tf.getText());
            } else {
                return null;
            }
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(usernamePassword -> {
            if (bdd.connection()) {
                bdd.creationJoueur(usernamePassword.getKey(), usernamePassword.getValue());
                joueur = new Joueur(usernamePassword.getKey());
                logout_button.setDisable(false);
                if (partie == null) {
                    pt_load.setDisable(false);
                }
                inscription_button.setVisible(false);
                connexion_button.setVisible(false);
                connect_has.setText("Connecté sur le compte de " + usernamePassword.getKey());
                connect_has.setVisible(true);
                if (partie != null) {
                    partie.setJoueur(joueur);
                    pt_save.setDisable(false);
                    container.requestFocus();
                }
                bdd.deconnection();
            }
        });
    }

    // Popup pour SAUVEGARDER la partie en cours
    @FXML
    private void clickSauvegarder() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Sauvegarde");
        alert.setHeaderText("Sauvegarde");
        alert.setContentText("Voulez-vous sauvegarder la partie en cours ?");
        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                File file = new File("saves/" + joueur.getNom() + ".save");
                file.getParentFile().mkdirs();
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file, false);
                ObjectOutputStream os = new ObjectOutputStream(fos);
                partie.sauvegardeTemps();
                partie.setReload(true);
                os.writeObject(partie);
                //System.out.println("Objetc sérialisé !");
                os.close();
                System.out.println("Partie sauvegardée !");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
            alert.close();
        } else {
            alert.close();
        }

    }

    // Popup pour CHARGER une partie enregistrée
    @FXML
    private void clickCharger() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Charger");
        alert.setHeaderText("Charger partie");
        alert.setContentText("Voulez-vous charger la partie précédente ?");
        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                FileInputStream fis = new FileInputStream("saves/" + joueur.getNom() + ".save");
                ObjectInputStream ois = new ObjectInputStream(fis);
                this.partie = (Partie) ois.readObject();
                System.out.println("Partie chargée !");
                ois.close();
                afficherComposant();
                start_button.setDisable(true);
                pt_load.setDisable(true);
                pt_restart.setDisable(false);
                pt_save.setDisable(false);
                partie.setController(this);
                partie.setBdd(bdd);
                partie.start();
                reloaded = true;
                container.requestFocus();
            } catch (FileNotFoundException ex) {
                Alert a = new Alert(AlertType.ERROR);
                alert.setTitle("Aucune sauvegarde");
                alert.setHeaderText("Aucune sauvegarde");
                alert.setContentText("Aucune sauvegarde n'a été trouvée pour " + joueur.getNom());
                alert.show();
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            alert.close();
        }
    }

    // Pop up pour se déconnecter
    @FXML
    private void clickDeconnexion() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Déconnexion");
        alert.setHeaderText("Déconnexion");
        alert.setContentText("Voulez-vous vous déconnecter ?\nToute progression non sauvegardée sera perdue");
        alert.setGraphic(new ImageView("/img/noun_Lock_small.png"));
        if (alert.showAndWait().get() == ButtonType.OK) {
            resetGame();
            reloaded = false;
            joueur = null;
            cacherComposant();
            logout_button.setDisable(true);
            pt_load.setDisable(true);
            connexion_button.setVisible(true);
            inscription_button.setVisible(true);
            connect_has.setVisible(false);
            start_button.setText("JOUER");
        } else {
            alert.close();
        }
    }

    // Vérifie la forme du pseudo
    private boolean checkPseudo(String _pseudo) {
        Pattern p = Pattern.compile("^[a-zA-Z0-9-\\_]{5,}$");
        Matcher m = p.matcher(_pseudo);
        return m.matches();
    }

    // Vérifie si le pseudo existe dans la bdd
    private boolean pseudoExist(String _pseudo) {
        if (bdd.connection()) {
            boolean exist = bdd.joueurExiste(_pseudo);
            bdd.deconnection();
            return exist;
        }
        return false;
    }

    // Vérifie la forme du mot de passe
    private boolean checkPassword(String _password) {
        Pattern p = Pattern.compile("^(?=.{6,}$)(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[0-9])[a-zA-Z0-9]*$");
        Matcher m = p.matcher(_password);
        return m.matches();
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
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if (panes.containsKey(c.getId())) {
                                    Pane p = panes.get(c.getId());
                                    grid1.getChildren().remove(p);
                                    grid2.getChildren().remove(p);
                                    grid3.getChildren().remove(p);
                                    switch (c.getZ()) {
                                        case 0:
                                            grid1.add(p, c.getX(), c.getY());
                                            break;
                                        case 1:
                                            grid2.add(p, c.getX(), c.getY());
                                            break;
                                        case 2:
                                            grid3.add(p, c.getX(), c.getY());
                                            break;
                                    }
                                    if (c.getValeur() != c.getOldValeur()) {
                                        Label l = (Label) p.getChildren().get(0);

                                        l.setText("" + c.getValeur());
                                        l.getStyleClass().remove(0);
                                        l.getStyleClass().add("case_label_" + c.getValeur());
                                    }
                                } else {
                                    Pane p = new Pane();
                                    Label l = new Label("" + c.getValeur());
                                    p.getStyleClass().add("case_pane");
                                    switch (c.getZ()) {
                                        case 0:
                                            grid1.add(p, c.getX(), c.getY());
                                            p.getChildren().add(l);
                                            break;
                                        case 1:
                                            grid2.add(p, c.getX(), c.getY());
                                            p.getChildren().add(l);
                                            break;
                                        case 2:
                                            grid3.add(p, c.getX(), c.getY());
                                            p.getChildren().add(l);
                                            break;
                                    }
                                    l.getStyleClass().add("case_label_" + c.getValeur());
                                    p.setPrefSize(wh_case, wh_case);
                                    l.setPrefSize(wh_case, wh_case);
                                    l.setAlignment(Pos.CENTER);
                                    p.setVisible(true);
                                    panes.put(c.getId(), p);
                                }
                            }
                        });
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
                        grid1.getChildren().remove(p);
                        grid2.getChildren().remove(p);
                        grid3.getChildren().remove(p);
                    }
                });
                it.remove();
            }
        }
    }

    // Affiche un pop up en cas de victoire{
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
                    if (reloaded) {
                        File file = new File("saves/" + joueur.getNom() + ".save");
                        file.delete();
                        reloaded = false;
                    }
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
                    reloaded = false;
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
                    grid1.getChildren().remove(p);
                    grid2.getChildren().remove(p);
                    grid3.getChildren().remove(p);
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
        pt_load.setDisable(false);
        pt_save.setDisable(true);
        pt_restart.setDisable(true);
        reloaded = false;
    }
}
