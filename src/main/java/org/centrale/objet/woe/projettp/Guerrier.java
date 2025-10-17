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
 * La classe {@code Guerrier} représente un personnage combattant au corps à corps.
 * <p>
 * Le guerrier peut attaquer des créatures, infliger des dégâts et recevoir des
 * points de parade. Il hérite de la classe {@link Personnage}.
 * </p>
 * 
 * @author srodr
 */
public class Guerrier extends Personnage implements Combattant {

    // ================= CONSTRUCTEURS =================

    /**
     * Constructeur par défaut.
     * Initialise un guerrier avec les valeurs par défaut de {@link Personnage}.
     */
    public Guerrier() {
        super();
    }

    /**
     * Constructeur complet.
     * 
     * @param n Nom du guerrier
     * @param etat État vivant ou mort
     * @param pVie Points de vie
     * @param dAtt Dégâts d’attaque
     * @param pPar Points de parade
     * @param paAtt Pourcentage d’attaque
     * @param paPar Pourcentage de parade
     * @param p Position initiale (Point2D)
     * @param dMax Distance maximale d’attaque
     * @param distanceVision Distance de vision
     */
    public Guerrier(String n, boolean etat, int pVie, int dAtt, int pPar, int paAtt, int paPar, Point2D p, int dMax, int distanceVision) {
        super(n, etat, pVie, dAtt, pPar, paAtt, paPar, dMax, p, distanceVision);
    }

    /**
     * Constructeur par copie.
     * 
     * @param perso Personnage à copier
     */
    public Guerrier(Personnage perso) {
        super(perso);
    }

    // ================= MÉTHODES =================

    /**
     * Permet au guerrier d’attaquer une créature.
     * <p>
     * Vérifie la portée, tente l’attaque et applique les dégâts si la cible échoue
     * sa parade. Affiche les résultats et tue la créature si ses points de vie
     * tombent à zéro ou moins.
     * </p>
     * 
     * @param c La créature cible
     * @param positionWorld Ensemble des positions occupées dans le monde
     */
    @Override
    public void combattre(Creature c, Set<Point2D> positionWorld,List<Creature> creatures) {

        // Vérifie si la cible est à portée d'attaque et si les deux sont vivants
        if (this.getPos().distance(c.getPos()) <= (this.getDistAttMax() * Math.sqrt(2)) && c.isEtat() && this.isEtat()) {

            // Informations du combat
            System.out.println();
            System.out.println("=============COMBAT CORPS A CORPS=============");
            System.out.println("Attaquant : " + this.getNom());
            System.out.println("Defenseur : " + c.getNom());
            System.out.println("==========================");
            System.out.println();

            // Vérifie si l'attaque réussit
            if (jeuDeAtt()) {
                System.out.println("Attaque reussie");

                // Vérifie si la cible se défend avec succès
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
                    // Défense échouée : inflige les dégâts directement
                    int e_vie = c.getPtVie() - this.getDegAtt();
                    c.setPtVie(e_vie);
                }
            } else {
                System.out.println("Attaque ratee");
            }

            // Affichage des résultats
            System.out.println();
            System.out.println("=============RESULTATS=============");
            System.out.println("Points de vie de " + c.getNom() + " : " + c.getPtVie());
            System.out.println("Points de defense de " + c.getNom() + " : " + c.getPtPar());
            System.out.println("==========================");
            System.out.println();

            // Vérifie si la créature est vaincue
            if (c.getPtVie() <= 0) {
                System.out.println();
                System.out.println("****** " + c.getNom() + " a ete vaincu. ******");
                System.out.println();
                c.mourir(positionWorld,creatures);
            }
        }
    }

    /**
     * Détermine si le guerrier touche avec succès sa cible.
     * 
     * @return {@code true} si l’attaque réussit, {@code false} sinon
     */
    public boolean jeuDeAtt() {
        Random n = new Random();
        return n.nextInt(100) < this.getPageAtt();
    }

    /**
     * Détermine si la cible réussit sa parade contre l’attaque du guerrier.
     * 
     * @param c La créature qui se défend
     * @return {@code true} si la parade réussit, {@code false} sinon
     */
    public boolean jeuDeDe(Creature c) {
        Random n = new Random();
        return n.nextInt(100) < c.getPagePar();
    }
    
    /**
    * Sauvegarde les informations spécifiques à un {@link Guerrier}
    * dans la table SQL correspondante.
    * <p>
    * Cette méthode complète la sauvegarde générique du personnage effectuée
    * dans {@code Personnage}, en insérant l’identifiant du personnage
    * dans la table spécialisée <b>Guerrier</b>.  
    * Elle ne stocke pas d’attribut supplémentaire (les caractéristiques
    * communes sont déjà enregistrées dans {@code Personnage}).
    * </p>
    *
    * <h4>Schéma visé :</h4>
    * <pre>
    * Table : Guerrier
    * Colonnes : id_personnage (PRIMARY KEY, FOREIGN KEY vers Personnage)
    * </pre>
    *
    * @param conn          Connexion SQL active (vers la base PostgreSQL)
    * @param idPersonnage  Identifiant du personnage à associer au Guerrier
    *
    * @throws SQLException en cas d’erreur d’exécution SQL
    *
    * @see Personnage#saveToDB(Connection, int)
    * @see World#saveWorldToDB(Connection, Joueur, String, int, int)
    */
    
    public void saveGuerrier(Connection conn, int idPersonnage) {
        try (PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO Guerrier (id_personnage) VALUES (?)")) {
            ps.setInt(1, idPersonnage);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur Guerrier.saveGuerrier : " + e.getMessage());
        }
    }

}
