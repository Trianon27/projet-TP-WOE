/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.objet.woe.projettp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * La classe {@code Monstre} représente un monstre dans le jeu.
 * <p>
 * Un monstre hérite de toutes les caractéristiques de {@link Creature} et
 * possède un niveau de dangerosité défini par l'énum {@link Dangerosite}.
 * </p>
 * 
 * <p>
 * Les monstres peuvent se déplacer aléatoirement, attaquer des cibles proches
 * ou rester inactifs selon leur niveau de dangerosité.
 * </p>
 *
 * @author
 * @version 3.0 (fusion complète)
 */
public class Monstre extends Creature implements Analyze {

    // ================= ENUMÉRATION =================

    /**
     * Enumération représentant le niveau de dangerosité d'un monstre.
     */
    public enum Dangerosite {
        DOCILE,      // Monstre non agressif
        MOYENNE,     // Monstre moyennement agressif
        DANGEREUX    // Monstre très agressif
    }

    /** Niveau de dangerosité du monstre */
    private Dangerosite dangerosite;

    // ================= CONSTRUCTEURS =================

    /**
     * Constructeur par défaut.
     * Initialise un monstre avec les valeurs par défaut de {@link Creature}
     * et un niveau de dangerosité DOCILE.
     */
    public Monstre() {
        super();
        this.dangerosite = Dangerosite.DOCILE;
    }

    /**
     * Constructeur complet.
     *
     * @param nom Nom du monstre
     * @param etat État vivant ou mort
     * @param pVie Points de vie
     * @param dAtt Dégâts d’attaque
     * @param pPar Points de parade
     * @param paAtt Pourcentage d’attaque
     * @param paPar Pourcentage de parade
     * @param p Position initiale (Point2D)
     * @param distAttMax Distance maximale d’attaque
     * @param distanceVision Distance de vision
     * @param dangerosite Niveau de dangerosité
     */
    public Monstre(String nom, boolean etat, int pVie, int dAtt, int pPar,
                   int paAtt, int paPar, Point2D p, int distAttMax,
                   int distanceVision, Dangerosite dangerosite) {
        super(nom, etat, pVie, dAtt, pPar, paAtt, paPar, p, distAttMax, distanceVision);
        this.dangerosite = dangerosite;
    }

    /**
     * Constructeur par copie.
     *
     * @param m Monstre à copier
     */
    public Monstre(Monstre m) {
        super(m);
        this.dangerosite = m.dangerosite;
    }

    // ================= GETTERS / SETTERS =================

    public Dangerosite getDangerosite() {
        return dangerosite;
    }

    public void setDangerosite(Dangerosite dangerosite) {
        this.dangerosite = dangerosite;
    }

    // ================= MÉTHODES COMPORTEMENTALES =================

    /**
     * Analyse le comportement du monstre à chaque tour.
     * <ul>
     *   <li>Les monstres <b>dociles</b> se déplacent aléatoirement.</li>
     *   <li>Les monstres de dangerosité <b>moyenne</b> attaquent s’ils ont une
     *       cible proche, sinon se déplacent.</li>
     *   <li>Les monstres <b>dangereux</b> attaquent toutes les cibles à portée.</li>
     * </ul>
     *
     * @param positionWorld Ensemble des positions occupées
     * @param creatures Liste de toutes les créatures du monde
     * @param objets Liste des objets du monde
     * @param tailleMonde Taille de la grille du monde
     */
    @Override
    public void analyzer(Set<Point2D> positionWorld, List<Creature> creatures, List<Objet> objets, int tailleMonde) {
        Random rand = new Random();

        // Si le monstre est mort, aucune action
        if (!this.isEtat()) return;

        switch (this.dangerosite) {
            case DOCILE -> {
                this.deplacementAleatoire(positionWorld, tailleMonde);
            }
            case MOYENNE, DANGEREUX -> {
                Point2D posMonstre = this.getPos();
                List<Creature> ciblesAdjacentes = new ArrayList<>();

                for (Creature c : creatures) {
                    if (c != this && c.isEtat()) {
                        double dx = Math.abs(c.getPos().getX() - posMonstre.getX());
                        double dy = Math.abs(c.getPos().getY() - posMonstre.getY());
                        if (dx <= this.getDistAttMax() && dy <= this.getDistAttMax() && !(dx == 0 && dy == 0)) {
                            ciblesAdjacentes.add(c);
                        }
                    }
                }

                // Si aucune cible, déplacement aléatoire
                if (ciblesAdjacentes.isEmpty()) {
                    this.deplacementAleatoire(positionWorld, tailleMonde);
                    return;
                }

                // Sinon, attaquer selon le type de dangerosité
                if (this.dangerosite == Dangerosite.DANGEREUX) {
                    System.out.println("🔥 " + this.getNom() + " (DANGEREUX) attaque toutes les cibles a portee !");
                    for (Creature cible : ciblesAdjacentes) {
                        if (this instanceof Combattant combattant) {
                            combattant.combattre(cible, positionWorld, creatures);
                        }
                    }
                } else { // Dangerosité moyenne
                    Creature cible = ciblesAdjacentes.get(rand.nextInt(ciblesAdjacentes.size()));
                    System.out.println("⚔️ " + this.getNom() + " attaque " + cible.getNom() + " !");
                    if (this instanceof Combattant combattant) {
                        combattant.combattre(cible, positionWorld, creatures);
                    }
                }
            }
        }
    }

    /**
     * Affiche les caractéristiques principales du monstre.
     */
    @Override
    public void affiche() {
        super.affiche();
        System.out.println("Dangerosite : " + dangerosite);
    }
}
