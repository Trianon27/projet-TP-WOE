/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.objet.woe.projettp;

/**
 * La classe {@code Objet} représente un objet dans le jeu.
 * <p>
 * Un objet possède un nom et une description, et peut être affiché ou copié.
 * </p>
 * 
 * @author srodr
 */
public class Objet {
    
    /** Nom de l'objet */
    private String nom;
    
    /** Description de l'objet */
    private String description;
    
    /** Position */
    private Point2D position; 
    
    // ================= CONSTRUCTEURS =================

    /**
     * Constructeur par défaut.
     * Initialise l'objet avec le nom et la description "None".
     */
    public Objet() {
        this.nom = "None";
        this.description = "None";
        this.position = new Point2D(0,0);
    }

    /**
     * Constructeur complet.
     * 
     * @param nom Nom de l'objet
     * @param description Description de l'objet
     * @param position
     */
    public Objet(String nom, String description, Point2D position) {
        this.nom = nom;
        this.description = description;
        this.position = position; 
    }
    
    /**
     * Constructeur par copie.
     * 
     * @param o Objet à copier
     */
    public Objet(Objet o) {
        this.nom = o.nom;
        this.description = o.description;
        this.position = o.position; 
    }

    // ================= GETTERS ET SETTERS =================

    /**
     * Retourne le nom de l'objet.
     * 
     * @return Nom de l'objet
     */
    public String getNom() {
        return nom;
    }

    /**
     * Retourne la description de l'objet.
     * 
     * @return Description de l'objet
     */
    public String getDescription() {
        return description;
    }

    /**
     * Définit le nom de l'objet.
     * 
     * @param nom Nouveau nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Définit la description de l'objet.
     * 
     * @param description Nouvelle description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }
    
    // ================= MÉTHODES =================

    /**
     * Affiche les informations de l'objet sur la console.
     */
    public void affiche() {
        System.out.println();
        System.out.println("Nom : " + this.nom);
        System.out.println("Description : " + this.description);
        System.out.println("Position : (" + position.getX() + ", " + position.getY() + ")");
    }
}
