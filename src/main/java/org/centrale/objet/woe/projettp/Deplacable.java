/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.centrale.objet.woe.projettp;

import java.util.Set;

/**
 * Interface {@code Deplacable} définissant le comportement commun
 * à tous les éléments du jeu pouvant se déplacer dans l’espace 2D.
 * <p>
 * Les classes qui implémentent cette interface doivent fournir leur
 * propre implémentation de la méthode {@link #deplacer(int, int)} afin de
 * gérer la logique du mouvement (ex. déplacement d’un personnage, d’une
 * créature ou d’un objet mobile).
 * </p>
 *
 * <h3>Objectif :</h3>
 * <ul>
 *   <li>Uniformiser le comportement de déplacement dans le monde du jeu.</li>
 *   <li>Permettre le polymorphisme : traiter différents types d’éléments
 *       (créatures, personnages, monstres, etc.) de la même manière lorsqu’ils se déplacent.</li>
 *   <li>Séparer la logique de déplacement de la structure d’héritage
 *       principale (ex. {@link ElementDeJeu}).</li>
 * </ul>
 *
 * <h3>Exemples d’implémentation :</h3>
 * <ul>
 *   <li>{@link Creature} — déplace sa position selon ses coordonnées internes.</li>
 *   <li>{@link Personnage} — se déplace en réponse à une action du joueur.</li>
 *   <li>{@link Monstre} — effectue un déplacement aléatoire ou ciblé.</li>
 *   <li>{@link NuageToxique} — effectue un déplacement automatique du nuage.</li>
 * </ul>
 *
 * <h3>Exemple d’utilisation :</h3>
 * <pre>{@code
 * Deplacable loup = new Creature("Loup", true, 50, 10, 5, 80, 50, new Point2D(2, 3), 1, 3);
 * loup.deplacer(1, -1); // Déplace le loup d'une case vers la droite et vers le bas
 * }</pre>
 *
 * @author fusion
 * @version 2.0
 */
public interface Deplacable {

    /**
     * Déplace l’objet dans le plan selon les coordonnées indiquées.
     * <p>
     * Les paramètres représentent la variation de position sur les axes X et Y :
     * </p>
     * <ul>
     *   <li>{@code dx} — déplacement horizontal (positif = droite, négatif = gauche)</li>
     *   <li>{@code dy} — déplacement vertical (positif = haut, négatif = bas)</li>
     * </ul>
     *
     * @param dx déplacement horizontal à appliquer
     * @param dy déplacement vertical à appliquer
     */
    void deplacer(int dx, int dy);

    /**
     * Effectue un déplacement aléatoire dans les limites du monde.
     * <p>
     * Utilisé principalement par les créatures non contrôlées (monstres,
     * animaux, nuages, etc.).
     * </p>
     *
     * @param positionsOccupees ensemble des positions déjà prises (pour éviter les collisions)
     * @param tailleMonde taille maximale du monde (borne X/Y)
     */
    void deplacementAleatoire(Set<Point2D> positionsOccupees, int tailleMonde);
}
