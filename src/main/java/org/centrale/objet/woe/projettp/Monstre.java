/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.objet.woe.projettp;

/**
 * La classe {@code Monstre} représente un monstre dans le jeu.
 * <p>
 * Un monstre hérite de toutes les caractéristiques de {@link Creature} et possède
 * un niveau de dangerosité défini par l'énum {@link Dangerosite}.
 * </p>
 * 
 * @author srodr
 */
public class Monstre extends Creature {
    
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
    public Monstre(String nom, boolean etat, int pVie, int dAtt, int pPar, int paAtt, int paPar, Point2D p, int distAttMax, int distanceVision, Dangerosite dangerosite) {
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

    // ================= GETTERS ET SETTERS =================

    /**
     * Retourne le niveau de dangerosité du monstre.
     * 
     * @return Niveau de dangerosité
     */
    public Dangerosite getDangerosite() {
        return dangerosite;
    }

    /**
     * Définit le niveau de dangerosité du monstre.
     * 
     * @param dangerosite Nouveau niveau de dangerosité
     */
    public void setDangerosite(Dangerosite dangerosite) {
        this.dangerosite = dangerosite;
    }
    
    // ================= MÉTHODES =================

   
}
