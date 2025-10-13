/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.objet.woe.projettp;

import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * La classe {@code Creature} représente un être vivant du jeu
 * avec ses caractéristiques de combat, son état (vivant ou mort) 
 * et sa position dans le monde.
 * <p>
 * Une créature peut se déplacer, attaquer, être vaincue et afficher 
 * ses informations principales.
 * </p>
 * 
 * @author srodr
 */
public class Creature extends ElementDeJeu implements Deplacable {

    // ================= ATTRIBUTS =================
    
    /** État de la créature (vivant ou mort) */
    private boolean etat; 

    /** Points de vie de la créature */
    protected int ptVie;

    /** Dégâts infligés lors d’une attaque */
    protected int degAtt;

    /** Points de parade permettant de réduire les dégâts reçus */
    protected int ptPar;

    /** Pourcentage de réussite d’attaque */
    protected int pageAtt;

    /** Pourcentage de réussite de parade */
    protected int pagePar;
    
    /** Distance maximale d’attaque */
    protected int distAttMax;

    
    /** Distance de vision de la créature */
    protected int distanceVision; 

    // ================= CONSTRUCTEURS =================

    /**
     * Construit une créature avec toutes ses caractéristiques.
     *
     * @param nom Nom de la créature
     * @param etat État (vivant ou mort)
     * @param pVie Points de vie
     * @param dAtt Dégâts d’attaque
     * @param pPar Points de parade
     * @param paAtt Pourcentage d’attaque
     * @param paPar Pourcentage de parade
     * @param p Position initiale (Point2D)
     * @param distAttMax Distance maximale d’attaque
     * @param distanceVision Distance de vision
     */
    public Creature(String nom, boolean etat, int pVie, int dAtt, int pPar, int paAtt, int paPar, Point2D p, int distAttMax, int distanceVision) {
        super(nom,p);
        this.etat = etat;
        this.ptVie = pVie;
        this.degAtt = dAtt;
        this.ptPar = pPar;
        this.pageAtt = paAtt;
        this.pagePar = paPar;
        this.distAttMax = distAttMax;
        this.distanceVision = distanceVision; 
    }

    /**
     * Construit une créature en copiant une autre.
     *
     * @param c Créature à copier
     */
    public Creature(Creature c) {
        super(c);
        this.etat = c.etat; 
        this.ptVie = c.ptVie;
        this.degAtt = c.degAtt;
        this.ptPar = c.ptPar;
        this.pageAtt = c.pageAtt;
        this.pagePar = c.pagePar;
        this.distAttMax = c.distAttMax;
        this.distanceVision = c.distanceVision; 
    }

    /**
     * Construit une créature par défaut avec des valeurs prédéfinies.
     */
    public Creature() {
        super();
        this.etat = true; 
        this.ptVie = 50;
        this.degAtt = 5;
        this.ptPar = 2;
        this.pageAtt = 50;
        this.pagePar = 30;
        this.distAttMax = 1;
        this.distanceVision = 1; 
    }

    // ================= GETTERS / SETTERS =================


    
    /** @return les points de vie actuels */
    public int getPtVie() {
        return ptVie;
    }

    /** @param ptVie nouveaux points de vie */
    public void setPtVie(int ptVie) {
        this.ptVie = ptVie;
    }

    /** @return les dégâts d’attaque */
    public int getDegAtt() {
        return degAtt;
    }

    /** @param degAtt nouveaux dégâts d’attaque */
    public void setDegAtt(int degAtt) {
        this.degAtt = degAtt;
    }

    /** @return les points de parade */
    public int getPtPar() {
        return ptPar;
    }

    /** @param ptPar nouveaux points de parade */
    public void setPtPar(int ptPar) {
        this.ptPar = ptPar;
    }

    /** @return le pourcentage de réussite d’attaque */
    public int getPageAtt() {
        return pageAtt;
    }

    /** @param pageAtt nouveau pourcentage d’attaque */
    public void setPageAtt(int pageAtt) {
        this.pageAtt = pageAtt;
    }

    /** @return le pourcentage de réussite de parade */
    public int getPagePar() {
        return pagePar;
    }

    /** @param pagePar nouveau pourcentage de parade */
    public void setPagePar(int pagePar) {
        this.pagePar = pagePar;
    }



    public int getDistanceVision() {
        return distanceVision;
    }

    public void setDistanceVision(int distanceVision) {
        this.distanceVision = distanceVision;
    }

    public int getDistAttMax() {
        return distAttMax;
    }

    public void setDistAttMax(int distAttMax) {
        this.distAttMax = distAttMax;
    }

    public boolean isEtat() {
        return etat;
    }

    public void setEtat(boolean etat) {
        this.etat = etat;
    }

    // ================= MÉTHODES =================

    /**
     * Déplace la créature en fonction des valeurs dx et dy.
     *
     * @param dx déplacement horizontal
     * @param dy déplacement vertical
     */
    @Override
    public void deplacer(int dx, int dy) {
        this.pos.translate(dx, dy);
    }

    /**
     * Déplace la créature de manière aléatoire sur une case adjacente.
     * <p>
     * La créature ne peut jamais rester immobile, elle doit bouger au moins 
     * d’une case autour d’elle.
     * </p>
     */
    public void deplaceAleatoire() {
        if(this.etat){
            Random rand = new Random();
            int dx, dy;
            do {
                dx = rand.nextInt(-1, 2);
                dy = rand.nextInt(-1, 2);
            } while(dx == 0 && dy == 0);
            this.pos.translate(dx, dy);
        }
    }
    
    /**
     * Tue la créature en mettant son état à faux
     * et en supprimant sa position de la carte du monde.
     *
     * @param positionWorld ensemble des positions occupées dans le monde
     * @param creatures
     */
    public void mourir(Set<Point2D> positionWorld, List<Creature> creatures){
        this.etat = false; 
        positionWorld.remove(this.pos);
        creatures.remove(this);
    }

    /**
     * Affiche toutes les informations principales de la créature sur la console.
     */
    @Override
    public void affiche() {
        System.out.println();
        System.out.println();
        System.out.println("Nom : " + nom);
        System.out.println("Points de vie : " + ptVie);
        System.out.println("Degats dattaque : " + degAtt);
        System.out.println("Points de parade : " + ptPar);
        System.out.println("Pourcentage dattaque : " + pageAtt);
        System.out.println("Pourcentage de parade : " + pagePar);
        System.out.println("Distance dattaque : " + distAttMax);
        System.out.println("Position : (" + pos.getX() + ", " + pos.getY() + ")");
    }
}
