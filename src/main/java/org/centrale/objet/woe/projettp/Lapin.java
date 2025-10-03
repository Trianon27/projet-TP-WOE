/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.objet.woe.projettp;

/**
 * La classe {@code Lapin} représente un monstre de type lapin dans le jeu.
 * <p>
 * Le lapin hérite de la classe {@link Monstre} et possède toutes ses caractéristiques
 * telles que les points de vie, les dégâts d’attaque, la parade et la distance de vision.
 * </p>
 * <p>
 * Le lapin peut également avoir un niveau de dangerosité défini par {@link Dangerosite}.
 * </p>
 * 
 * @author srodr
 */
public class Lapin extends Monstre {

    // ================= CONSTRUCTEURS =================

    /**
     * Constructeur complet pour initialiser un lapin avec toutes ses caractéristiques.
     * 
     * @param nom Nom du lapin
     * @param etat État vivant ou mort
     * @param pVie Points de vie
     * @param dAtt Dégâts d’attaque
     * @param pPar Points de parade
     * @param paAtt Pourcentage d’attaque
     * @param paPar Pourcentage de parade
     * @param p Position initiale (Point2D)
     * @param dMax Distance maximale d’attaque
     * @param distanceVision Distance de vision
     * @param dangerosite Niveau de dangerosité
     */
    public Lapin(String nom, boolean etat, int pVie, int dAtt, int pPar, int paAtt, int paPar, Point2D p, int dMax, int distanceVision, Dangerosite dangerosite) {
        super(nom, etat, pVie, dAtt, pPar, paAtt, paPar, p, dMax, distanceVision, dangerosite);
    }

    /**
     * Constructeur par copie.
     * 
     * @param l Lapin à copier
     */
    public Lapin(Lapin l){
        super(l);
    }
    
    /**
     * Constructeur par défaut.
     * Initialise un lapin avec les valeurs par défaut de {@link Monstre}.
     */
    public Lapin() {
        super();
    }
}
