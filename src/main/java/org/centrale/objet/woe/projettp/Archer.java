/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.objet.woe.projettp;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * La classe {@code Archer} représente un personnage spécialisé dans l’attaque à
 * distance avec des flèches.
 * <p>
 * Un archer possède un nombre limité de flèches et peut combattre d’autres
 * créatures en utilisant une probabilité de réussite pour l’attaque et la
 * défense.
 * </p>
 * <p>
 * Cette classe hérite de {@link Personnage} et ajoute la mécanique spécifique
 * du combat à distance avec gestion des flèches.
 * </p>
 * 
 * @author srodr
 */
public class Archer extends Personnage implements Combattant {

    /**
     * Nombre de flèches disponibles pour l’archer.
     */
    private int nbFleches;

    /**
     * Construit un archer avec des caractéristiques spécifiques.
     *
     * @param n Nom de l’archer
     * @param etat État de l’archer (vivant ou mort)
     * @param pVie Points de vie
     * @param dAtt Dégâts d’attaque
     * @param pPar Points de parade (défense)
     * @param paAtt Pourcentage de réussite d’attaque
     * @param paPar Pourcentage de réussite de parade
     * @param p Position initiale dans le monde
     * @param dMax Distance maximale d’attaque
     * @param distanceVision Distance de vision
     * @param nbFleches Nombre initial de flèches
     */
    public Archer(String n, boolean etat, int pVie, int dAtt, int pPar, int paAtt, int paPar, Point2D p, int dMax, int distanceVision, int nbFleches) {
        super(n, etat, pVie, dAtt, pPar, paAtt, paPar, dMax, p, distanceVision);
        this.nbFleches = nbFleches;
    }

    /**
     * Construit un nouvel archer à partir d’un autre archer (copie).
     *
     * @param a L’archer à copier
     */
    public Archer(Archer a) {
        super(a);
        this.nbFleches = a.getNbFleches();
    }

    /**
     * Construit un archer par défaut avec 0 flèche.
     */
    public Archer() {
        super();
        this.nbFleches = 0;
    }

    /**
     * Retourne le nombre de flèches disponibles.
     *
     * @return le nombre de flèches
     */
    public int getNbFleches() {
        return nbFleches;
    }

    /**
     * Définit le nombre de flèches disponibles.
     *
     * @param nbFleches le nouveau nombre de flèches
     */
    public void setNbFleches(int nbFleches) {
        this.nbFleches = nbFleches;
    }

    /**
     * Permet à l’archer d’attaquer une créature cible.
     * <p>
     * Vérifie d’abord si la cible est à portée et vivante, puis lance un jet
     * d’attaque. La défense de la cible est également prise en compte pour
     * déterminer les dégâts subis. Si la créature meurt, elle est retirée du
     * monde.
     * </p>
     *
     * @param c La créature ciblée
     * @param positionWorld L’ensemble des positions occupées dans le monde
     */
    
    @Override
    public void combattre(Creature c, Set<Point2D> positionWorld, List<Creature> creatures) {
        // Quitter si aucune flèche restante ou si la cible est déjà morte
        if (!this.isEtat() || !c.isEtat()) {
            return;
        }

        // Vérifie si la cible est à portée d’attaque et est encore en vie
        if (this.getPos().distance(c.getPos()) <= (Math.sqrt(2))) {

            System.out.println();
            System.out.println("=============COMBAT CORPS A CORPS=============");
            System.out.println("Attaquant : " + this.getNom());
            this.setDegAtt(10);
            System.out.println("Pt attaque corp a corp : " + this.getDegAtt());
            System.out.println("Defenseur : " + c.getNom());
            System.out.println("==========================");
            System.out.println();

            if (jeuDeAtt()) {
                System.out.println("Attaque reussie");

                if (jeuDeDe(c)) {
                    System.out.println("Defense reussie");

                    if (c.getPtPar() <= 0) {
                        int e_vie = c.getPtVie() - this.getDegAtt();
                        c.setPtVie(e_vie);
                    } else {
                        int e_def = c.getPtPar() - this.getDegAtt();
                        if (e_def <= 0) {
                            c.setPtPar(0);
                            int e_vie = c.getPtVie() + e_def;
                            c.setPtVie(e_vie);
                        } else {
                            c.setPtPar(e_def);
                        }
                    }

                } else {
                    int e_vie = c.getPtVie() - this.getDegAtt();
                    c.setPtVie(e_vie);
                }

            }else{
                System.out.println("Attaque echoue");
            }
            

        } else if (this.getPos().distance(c.getPos()) <= (this.getDistAttMax() * Math.sqrt(2))
                && this.getPos().distance(c.getPos()) > (Math.sqrt(2)) && this.getNbFleches() > 0 ) {

            System.out.println();
            System.out.println("=============COMBAT A DISTANCE=============");
            System.out.println("Attaquant : " + this.getNom());
            System.out.println("Defenseur : " + c.getNom());
            System.out.println("==========================");
            System.out.println();

            if (jeuDeAtt()) {
                System.out.println("Attaque reussie");
                int e_vie = c.getPtVie() - this.getDegAtt();
                c.setPtVie(e_vie);
            } else {
                System.out.println("Attaque ratee");
            }

            int arrows = this.getNbFleches() - 1;
            this.setNbFleches(arrows);

        }

        System.out.println();
        System.out.println("=============RESULTATS=============");
        System.out.println("Points de vie de " + c.getNom() + " : " + c.getPtVie());
        System.out.println("Points de defense de " + c.getNom() + " : " + c.getPtPar());
        System.out.println("Fleches restantes : " + this.getNbFleches());
        System.out.println("==========================");
        System.out.println();

        if (c.getPtVie() <= 0) {
            System.out.println();
            System.out.println("******* " + c.getNom() + " a ete vaincu. *******");
            System.out.println();
            c.mourir(positionWorld,creatures);
        }

    }

    /**
     * Détermine si l’archer réussit son attaque en fonction de son pourcentage
     * de réussite.
     *
     * @return {@code true} si l’attaque réussit, {@code false} sinon
     */
    public boolean jeuDeAtt() {
        Random n = new Random();
        return n.nextInt(100) < this.getPageAtt();
    }

    /**
     * Détermine si la créature ciblée réussit à se défendre en fonction de son
     * pourcentage de parade.
     *
     * @param c La créature qui tente de se défendre
     * @return {@code true} si la défense réussit, {@code false} sinon
     */
    public boolean jeuDeDe(Creature c) {
        Random n = new Random();
        return n.nextInt(100) < c.getPagePar();
    }

    /**
     * Affiche les informations principales de l’archer, y compris son nombre de
     * flèches restantes.
     */
    @Override
    public void affiche() {
        super.affiche();
        System.out.println("Le nbr de fleches : " + this.nbFleches);
    }
    /**
    * Sauvegarde les informations spécifiques à un {@link Archer}
    * dans la table SQL correspondante.
    * <p>
    * Comme pour {@link Guerrier}, cette méthode lie simplement
    * l'identifiant du personnage déjà sauvegardé dans {@code Personnage}
    * à une ligne de la table <b>Archer</b>.
    * </p>
    *
    * <h4>Schéma visé :</h4>
    * <pre>
    * Table : Archer
    * Colonnes : id_personnage (PRIMARY KEY, FOREIGN KEY vers Personnage)
    * </pre>
    *
    * @param conn          Connexion SQL active (vers la base PostgreSQL)
    * @param idPersonnage  Identifiant du personnage à associer à l’Archer
    *
    * @throws SQLException en cas d’erreur d’exécution SQL
    *
    * @see Personnage#saveToDB(Connection, int)
    * @see World#saveWorldToDB(Connection, Joueur, String, int, int)
    */
    public void saveArcher(Connection conn, int idPersonnage) {
        try (PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO Archer (id_personnage, nbFleches) VALUES (?, ?)")) {
            ps.setInt(1, idPersonnage);
            ps.setInt(2, this.getNbFleches());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur Archer.saveArcher : " + e.getMessage());
        }
    }
}

