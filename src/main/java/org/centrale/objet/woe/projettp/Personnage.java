package org.centrale.objet.woe.projettp;

import java.util.Set;

/**
 * La classe {@code Personnage} représente un personnage du jeu.
 * <p>
 * Un personnage hérite des caractéristiques de {@link Creature} et peut se déplacer,
 * afficher ses informations, combattre, interagir avec des objets et être copié.
 * </p>
 * 
 * <p>
 * Cette classe sert de base pour des personnages spécialisés comme {@link Archer}.
 * </p>
 * 
 * @author srodr
 */
public class Personnage extends Creature {

    // ================= CONSTRUCTEURS =================

    /**
     * Constructeur par défaut.
     * <p>
     * Initialise un personnage avec les valeurs par défaut de {@link Creature}.
     * </p>
     */
    public Personnage() {
        super();
    }

    /**
     * Constructeur complet.
     *
     * @param nom Nom du personnage
     * @param etat État vivant ou mort
     * @param pVie Points de vie
     * @param dAtt Dégâts d'attaque
     * @param pPar Points de parade
     * @param paAtt Pourcentage d'attaque par tour
     * @param paPar Pourcentage de parade par tour
     * @param dMax Distance maximale d'attaque
     * @param p Position initiale (Point2D)
     * @param distanceVision Distance de vision
     */
    public Personnage(String nom, boolean etat, int pVie, int dAtt, int pPar, int paAtt, int paPar, int dMax, Point2D p, int distanceVision) {
        super(nom, etat, pVie, dAtt, pPar, paAtt, paPar, p, dMax, distanceVision);
    }

    /**
     * Constructeur par copie.
     *
     * @param perso Personnage à copier
     */
    public Personnage(Personnage perso) {
        super(perso);
    }

    // ================= MÉTHODES =================

    /**
     * Permet au personnage de prendre un objet situé sur sa position.
     * <p>
     * Cette méthode vérifie d'abord que la position du personnage correspond à la
     * position de l'objet. Ensuite, selon le type de l'objet, elle applique ses effets :
     * </p>
     * <ul>
     *   <li>{@link PotionSoin} : augmente les points de vie du personnage.</li>
     *   <li>{@link Epee} : augmente les dégâts d'attaque du personnage.</li>
     *   <li>Autres types : aucune action.</li>
     * </ul>
     * <p>
     * Après l'interaction, l'objet est retiré de l'ensemble des positions occupées
     * du monde {@code positionWorld}.
     * </p>
     *
     * @param o L'objet à ramasser
     * @param positionWorld L'ensemble des positions occupées dans le monde
     */
    public void prendObjet(Objet o, Set<Point2D> positionWorld) {
        // Vérifie que la position du personnage est la même que celle de l'objet
        if (this.getPos().getX() == o.getPosition().getX() &&
            this.getPos().getY() == o.getPosition().getY()) {

            switch (o) {
                case PotionSoin potion -> { 
                    // Cast vers PotionSoin et application de l'effet
                    this.setPtVie(this.getPtVie() + potion.getpVie());
                    System.out.println("Potion consommee, vie actuelle: " + this.getPtVie());
                }
                case Epee epee -> { 
                    // Cast vers Epee et application de l'effet
                    this.setDegAtt(this.getDegAtt() + epee.getpAtt());
                    System.out.println("Épee prise, attaque actuelle: " + this.getDegAtt());
                }
                default -> {
                    // Aucun effet pour les autres types d'objet
                }
            }

            // Retire la position de l'objet du monde
            positionWorld.remove(o.getPosition());
        }
    }

}