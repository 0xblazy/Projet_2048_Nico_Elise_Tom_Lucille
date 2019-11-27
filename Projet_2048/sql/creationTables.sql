USE projet2048elnt;

CREATE TABLE IF NOT EXISTS Joueur (
    nom VARCHAR(50) NOT NULL,
    mdp VARCHAR(64) NOT NULL,
    meilleur_deplacements INT,
    meilleur_temps INT,
    score_max INT,
    UNIQUE (nom),
    PRIMARY KEY (nom)
);

CREATE TABLE IF NOT EXISTS Partie (
    id INT AUTO_INCREMENT,
    joueur VARCHAR(50) NOT NULL,
    deplacements INT,
    temps INT,
    score INT,
    valeur_max INT,
    PRIMARY KEY (id),
    FOREIGN KEY (joueur) REFERENCES Joueur(nom)
);