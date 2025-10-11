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
public class Objet extends ElementDeJeu {
    
    
    /** Description de l'objet */
    private String description;

    // ================= CONSTRUCTEURS =================

    /**
     * Constructeur par défaut.
     * Initialise l'objet avec le nom et la description "None".
     */
    public Objet() {
        super();
        this.description = "None";
    }

    /**
     * Constructeur complet.
     * 
     * @param nom Nom de l'objet
     * @param description Description de l'objet
     * @param position
     */
    public Objet(String nom, String description, Point2D position) {
        super(nom,position);
        this.description = description;
    }
    
    /**
     * Constructeur par copie.
     * 
     * @param o Objet à copier
     */
    public Objet(Objet o) {
        this.nom = o.nom;
        this.description = o.description;
        this.pos = o.pos; 
    }

    // ================= GETTERS ET SETTERS =================



    /**
     * Retourne la description de l'objet.
     * 
     * @return Description de l'objet
     */
    public String getDescription() {
        return description;
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
        return pos;
    }

    public void setPosition(Point2D position) {
        this.pos = position;
    }
    
    // ================= MÉTHODES =================

    /**
     * Affiche les informations de l'objet sur la console.
     */
    @Override
    public void affiche() {
        System.out.println();
        System.out.println("Nom : " + this.nom);
        System.out.println("Description : " + this.description);
        System.out.println("Position : (" + pos.getX() + ", " + pos.getY() + ")");
    }
}
