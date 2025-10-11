/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.objet.woe.projettp;

/**
 * Classe abstraite {@code ElementDeJeu} représentant tout élément du monde du jeu.
 * <p>
 * Cette classe sert de superclasse commune à tous les éléments pouvant exister dans le monde :
 * créatures, objets, effets, etc. 
 * Elle définit les attributs de base partagés par ces éléments, à savoir un nom et une position 
 * dans un espace 2D, ainsi qu'une méthode abstraite {@link #affiche()} que chaque sous-classe
 * doit implémenter pour afficher ses propres informations.
 * </p>
 * 
 * <h3>Responsabilités principales :</h3>
 * <ul>
 *   <li>Fournir un modèle générique pour tous les éléments du jeu.</li>
 *   <li>Garantir que chaque élément possède un nom et une position.</li>
 *   <li>Imposer une méthode d’affichage spécifique via la méthode abstraite {@code affiche()}.</li>
 * </ul>
 *
 * <h3>Exemples de sous-classes :</h3>
 * <ul>
 *   <li>{@link Creature} — représente une entité vivante capable de se déplacer et de combattre.</li>
 *   <li>{@link Objet} — représente un objet interactif ou ramassable présent dans le monde.</li>
 *   <li>{@link NuageToxique} — exemple d’élément environnemental affectant les créatures.</li>
 * </ul>
 *
 * @author hayta
 * @version 1.0
 */
public abstract class ElementDeJeu {

    // ===================== ATTRIBUTS =====================

    /** 
     * Nom de l’élément de jeu.
     * <p>Permet d’identifier l’élément dans le monde (ex : “Loup”, “Épée”, “Potion”).</p>
     */
    protected String nom;

    /**
     * Position actuelle de l’élément dans le monde (coordonnées 2D).
     */
    protected Point2D pos;

    // ===================== CONSTRUCTEURS =====================

    /**
     * Constructeur par défaut.
     * <p>
     * Initialise l’élément avec un nom générique "None" et une position à l’origine (0,0).
     * </p>
     */
    public ElementDeJeu() {
        this.nom = "None";
        this.pos = new Point2D(0, 0);
    }

    
    /**
    * Constructeur avec paramètres.
    * 
    * @param nom Nom de l’élément
    * @param pos Position initiale
    */
    public ElementDeJeu(String nom, Point2D pos) {
        this.nom = nom;
        this.pos = pos;
    }
     /**
     * Construit un élément de jeu en copiant un autre.
     *
     * @param E ElementDeJeu à copier
     */
    
    public ElementDeJeu(ElementDeJeu E) {
        this.nom = E.nom;
        this.pos = E.pos;
    }
    
    // ===================== GETTERS / SETTERS =====================

    /**
     * Retourne le nom de l’élément.
     *
     * @return le nom de l’élément de jeu
     */
    public String getNom() {
        return nom;
    }

    /**
     * Définit le nom de l’élément.
     *
     * @param nom le nouveau nom à attribuer à cet élément
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Retourne la position actuelle de l’élément.
     *
     * @return la position (objet {@link Point2D}) de l’élément
     */
    public Point2D getPos() {
        return pos;
    }

    /**
     * Modifie la position de l’élément.
     *
     * @param pos nouvelle position (objet {@link Point2D})
     */
    public void setPos(Point2D pos) {
        this.pos = pos;
    }

    // ===================== MÉTHODES ABSTRAITES =====================

    /**
     * Méthode abstraite d’affichage.
     * <p>
     * Chaque sous-classe d’{@code ElementDeJeu} doit implémenter cette méthode 
     * afin d’afficher les informations propres à son type d’élément.
     * </p>
     * 
     * <h4>Exemples :</h4>
     * <pre>{@code
     * // Exemple pour une créature
     * public void affiche() {
     *     System.out.println("Créature : " + getNom() + " (" + getPos().toString() + ")");
     * }
     *
     * // Exemple pour un objet
     * public void affiche() {
     *     System.out.println("Objet : " + getNom() + " à la position " + getPos());
     * }
     * }</pre>
     */
    public abstract void affiche();
}
