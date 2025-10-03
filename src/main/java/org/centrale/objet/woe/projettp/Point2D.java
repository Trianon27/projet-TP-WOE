/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.objet.woe.projettp;

/**
 * La classe {@code Point2D} représente un point dans un plan à deux dimensions.
 * <p>
 * Chaque point possède des coordonnées x et y, peut être déplacé, affiché et calculer
 * la distance par rapport à un autre point.
 * </p>
 * 
 * @author srodr
 */
public class Point2D {
    
    /** Coordonnée x du point */
    private int x;
    
    /** Coordonnée y du point */
    private int y;
    
    // ================= CONSTRUCTEURS =================

    /**
     * Constructeur par défaut.
     * Initialise le point à l'origine (0,0).
     */
    public Point2D() {
        this.x = 0;
        this.y = 0;
    }
    
    /**
     * Constructeur avec paramètres définis.
     * 
     * @param x Coordonnée x
     * @param y Coordonnée y
     */
    public Point2D(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Constructeur de copie.
     * 
     * @param p Point2D à copier
     */
    public Point2D(Point2D p) {
        this.x = p.getX();
        this.y = p.getY();
    }

    // ================= GETTERS ET SETTERS =================

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    /**
     * Définit la position du point.
     * 
     * @param x Nouvelle coordonnée x
     * @param y Nouvelle coordonnée y
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Déplace le point selon dx et dy.
     * 
     * @param dx Déplacement horizontal
     * @param dy Déplacement vertical
     */
    public void translate(int dx, int dy) {
        this.x = this.x + dx;
        this.y = this.y + dy;
    }

    /**
     * Affiche les coordonnées du point.
     */
    public void affiche() {
        System.out.println("La position est x: " + this.x + " et y: " + this.y);
    }

    /**
     * Calcule la distance entre ce point et un autre point donné.
     * 
     * @param p Point de référence
     * @return Distance entre les deux points
     */
    public float distance(Point2D p) {
        float subDistX = (float) Math.pow((p.getX() - this.x), 2);
        float subDistY = (float) Math.pow((p.getY() - this.y), 2);
        return (float) Math.sqrt(subDistX + subDistY);
    }
}
