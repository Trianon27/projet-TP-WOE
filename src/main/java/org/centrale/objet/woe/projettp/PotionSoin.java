/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.objet.woe.projettp;

/**
 * La classe {@code PotionSoin} représente une potion qui restaure des points de vie.
 * <p>
 * Une potion de soin hérite des caractéristiques de {@link Objet} et ajoute
 * un nombre de points de vie à restaurer.
 * </p>
 * 
 * @author srodr
 */
public class PotionSoin extends Objet {
    
    /** Points de vie que la potion peut restaurer */
    private int pVie;
    
    // ================= CONSTRUCTEURS =================

    /**
     * Constructeur par défaut.
     * Initialise la potion avec 100 points de vie et les valeurs par défaut de {@link Objet}.
     */
    public PotionSoin() {
        super();
        this.pVie = 20;
    }

    /**
     * Constructeur complet.
     * 
     * @param pVie Points de vie que la potion restaure
     * @param nom Nom de la potion
     * @param p
     * @param description Description de la potion
     */
    public PotionSoin(String nom, String description,Point2D p, int pVie) {
        super(nom, description, p);
        this.pVie = pVie;
    }

    /**
     * Constructeur à partir d'un objet existant.
     * 
     * @param pVie Points de vie que la potion restaure
     * @param o Objet à copier
     */
    public PotionSoin(int pVie, Objet o) {
        super(o);
        this.pVie = pVie;
    }

    // ================= GETTERS ET SETTERS =================

    /**
     * Retourne le nombre de points de vie restaurés par la potion.
     * 
     * @return Points de vie
     */
    public int getpVie() {
        return pVie;
    }

    /**
     * Définit le nombre de points de vie restaurés par la potion.
     * 
     * @param pVie Points de vie à restaurer
     */
    public void setpVie(int pVie) {
        this.pVie = pVie;
    }
}
