/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.centrale.objet.woe.projettp;

import java.util.List;
import java.util.Set;

/**
 * Interface {@code Combattant} définissant le comportement de tout élément
 * du jeu capable d’effectuer une action de combat dans le monde.
 * <p>
 * Les classes qui implémentent cette interface (comme {@link Guerrier},
 * {@link Archer}, {@link Loup} ou {@link NuageToxique}) doivent fournir
 * leur propre implémentation de la méthode {@link #combattre(Creature, Set)},
 * décrivant la manière dont elles attaquent une cible et interagissent
 * avec la carte du monde (ex. suppression d'une créature morte, mise à jour
 * des positions, effets de zone, etc.).
 * </p>
 *
 * <h3>Objectifs :</h3>
 * <ul>
 *   <li>Uniformiser la logique de combat entre différentes entités du jeu.</li>
 *   <li>Gérer les effets des combats directement dans le contexte du monde 
 *       (via {@code positionWorld}).</li>
 *   <li>Encourager le polymorphisme : chaque type d’entité combat à sa manière
 *       (corps à corps, distance, effet de zone, etc.).</li>
 * </ul>
 *
 * <h3>Exemples d’implémentation :</h3>
 * <ul>
 *   <li>{@link Guerrier} — attaque au corps à corps avec une arme de mêlée.</li>
 *   <li>{@link Archer} — attaque à distance en vérifiant la portée et la ligne de tir.</li>
 *   <li>{@link Loup} — attaque par morsure lorsqu’il est proche de la cible.</li>
 *   <li>{@link NuageToxique} — inflige des dégâts de zone à toutes les créatures
 *       présentes dans sa zone d’effet à chaque tour.</li>
 * </ul>
 *
 * <h3>Exemple d’utilisation :</h3>
 * <pre>{@code
 * Set<Point2D> positions = new HashSet<>();
 * positions.add(new Point2D(2, 3));
 * positions.add(new Point2D(3, 3));
 *
 * Combattant guerrier = new Guerrier(
 *     "Bjorn", true, 120, 15, 10, 70, 40, 
 *     new Point2D(2, 3), 1, 3
 * );
 * 
 * Creature loup = new Loup(
 *     "Loup sauvage", true, 60, 8, 3, 50, 20, 
 *     new Point2D(3, 3), 1, 2
 * );
 * 
 * guerrier.combattre(loup, positions); // Le guerrier attaque et met à jour le monde
 * }</pre>
 *
 * @author hayta
 * @version 1.1
 */
public interface Combattant {

    /**
     * Effectue une attaque sur une créature cible, en tenant compte
     * du contexte du monde (positions occupées, suppression d’éléments, etc.).
     * <p>
     * Cette méthode représente l’action de combat principale dans le jeu.
     * Chaque classe implémentant cette interface définit sa propre logique de combat :
     * <ul>
     *   <li>Calcul des chances de réussite de l’attaque (probabilités, distance, etc.).</li>
     *   <li>Calcul des dégâts infligés et gestion de la parade.</li>
     *   <li>Réduction des points de vie de la cible ou suppression de celle-ci si elle meurt.</li>
     *   <li>Interaction avec {@code positionWorld} pour refléter les changements sur la carte.</li>
     * </ul>
     * </p>
     *
     * @param c la créature cible de l’attaque (instance de {@link Creature})
     * @param positionWorld l’ensemble des positions actuellement occupées dans le monde du jeu
     * @param creatures
     */
    void combattre(Creature c, Set<Point2D> positionWorld, List<Creature> creatures);
}
