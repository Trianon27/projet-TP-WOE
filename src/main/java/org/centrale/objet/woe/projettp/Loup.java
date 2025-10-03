/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.objet.woe.projettp;

import java.util.Random;
import java.util.Set;

/**
 * La classe {@code Loup} représente un monstre de type loup dans le jeu.
 * <p>
 * Le loup hérite de {@link Monstre} et peut attaquer des créatures.
 * Il possède toutes les caractéristiques des monstres, comme les points de vie,
 * les dégâts, la parade et la distance de vision.
 * </p>
 * 
 * @author srodr
 */
public class Loup extends Monstre {

    // ================= CONSTRUCTEURS =================

    /**
     * Constructeur par défaut.
     * Initialise un loup avec les valeurs par défaut de {@link Monstre}.
     */
    public Loup() {
        super();
    }

    /**
     * Constructeur complet.
     * 
     * @param nom Nom du loup
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
    public Loup(String nom, boolean etat, int pVie, int dAtt, int pPar, int paAtt, int paPar, Point2D p, int distAttMax, int distanceVision, Dangerosite dangerosite) {
        super(nom, etat, pVie, dAtt, pPar, paAtt, paPar, p, distAttMax, distanceVision, dangerosite);
    }

    /**
     * Constructeur par copie à partir d'un {@link Monstre}.
     * 
     * @param m Monstre à copier
     */
    public Loup(Monstre m) {
        super(m);
    }

    // ================= MÉTHODES =================

    /**
     * Permet au loup d’attaquer une créature.
     * <p>
     * Vérifie la portée, tente l’attaque et applique les dégâts si la cible échoue
     * sa parade. Affiche les résultats et tue la créature si ses points de vie
     * tombent à zéro ou moins.
     * </p>
     * 
     * @param c La créature cible
     * @param positionWorld Ensemble des positions occupées dans le monde
     */
    public void combattre(Creature c, Set<Point2D> positionWorld) {

        if (this.getPos().distance(c.getPos()) <= (this.getDistAttMax() * Math.sqrt(2)) && c.isEtat() && this.isEtat()) {

            System.out.println();
            System.out.println("=============COMBAT CORPS A CORPS=============");
            System.out.println("Attaquant : " + this.getNom());
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
            }else {
                System.out.println("Attaque ratee");
            }

            System.out.println();
            System.out.println("=============RESULTATS=============");
            System.out.println("Points de vie de " + c.getNom() + " : " + c.getPtVie());
            System.out.println("Points de defense de " + c.getNom() + " : " + c.getPtPar());
            System.out.println("==========================");
            System.out.println();

            if (c.getPtVie() <= 0) {
                System.out.println();
                System.out.println("**** " + c.getNom() + " a ete vaincu. ****");
                System.out.println();
                c.mourir(positionWorld);
            }
        }
    }

    /**
     * Détermine si le loup touche avec succès sa cible.
     * 
     * @return {@code true} si l’attaque réussit, {@code false} sinon
     */
    public boolean jeuDeAtt() {
        Random n = new Random();
        return n.nextInt(100) < this.getPageAtt();
    }

    /**
     * Détermine si la cible réussit sa parade contre l’attaque du loup.
     * 
     * @param c La créature qui se défend
     * @return {@code true} si la parade réussit, {@code false} sinon
     */
    public boolean jeuDeDe(Creature c) {
        Random n = new Random();
        return n.nextInt(100) < c.getPagePar();
    }
}
