package org.centrale.objet.woe.projettp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * La classe {@code Personnage} représente un personnage du jeu.
 * <p>
 * Un personnage hérite des caractéristiques de {@link Creature} et peut se
 * déplacer, afficher ses informations, combattre, interagir avec des objets et
 * être copié.
 * </p>
 *
 * <p>
 * Cette classe sert de base pour des personnages spécialisés comme
 * {@link Archer}.
 * </p>
 *
 * @author srodr
 */
public class Personnage extends Creature {

    private List<ObjetUtilisable> effetsActifs= new ArrayList<>();
    private List<Objet> inventaire = new ArrayList<>();

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

    public List<ObjetUtilisable> getEffetsActifs() {
        return effetsActifs;
    }

    public List<Objet> getInventaire() {
        return inventaire;
    }

    
    
    public void setEffetsActifs(List<ObjetUtilisable> effetsActifs) {
        this.effetsActifs = effetsActifs;
    }

    public void setInventaire(List<Objet> inventaire) {
        this.inventaire = inventaire;
    }
    
    
    
    

    // ================= MÉTHODES =================
    /**
     * Permet au personnage de prendre un objet situé sur sa position.
     * <p>
     * Cette méthode vérifie d'abord que la position du personnage correspond à
     * la position de l'objet. Ensuite, selon le type de l'objet, elle applique
     * ses effets :
     * </p>
     * <ul>
     * <li>{@link PotionSoin} : augmente les points de vie du personnage.</li>
     * <li>{@link Epee} : augmente les dégâts d'attaque du personnage.</li>
     * <li>Autres types : aucune action.</li>
     * </ul>
     * <p>
     * Après l'interaction, l'objet est retiré de l'ensemble des positions
     * occupées du monde {@code positionWorld}.
     * </p>
     *
     * @param o L'objet à ramasser
     * @param positionWorld L'ensemble des positions occupées dans le monde
     */
    public void prendObjet(Objet o, Set<Point2D> positionWorld) {
        if (this.getPos().equals(o.getPosition())) {

            switch (o) {
                case PotionSoin potion -> {
                    this.setPtVie(this.getPtVie() + potion.getpVie());
                    System.out.println("Potion consommée, vie actuelle : " + this.getPtVie());
                }
                case Epee epee -> {
                    this.setDegAtt(this.getDegAtt() + epee.getpAtt());
                    System.out.println("Épée prise, attaque actuelle : " + this.getDegAtt());
                }
                case ObjetUtilisable utilisable -> {
                    utilisable.appliquerEffet(this);
                    this.effetsActifs.add(utilisable);
                    System.out.println("Objet utilisable activé : " + o.getNom());
                }
                default -> {
                    // Aucun effet
                }
            }

            // Retire l’objet du monde
            positionWorld.remove(o.getPosition());
        }
    }

    public void mettreAJourEffets() {
        Iterator<ObjetUtilisable> it = effetsActifs.iterator();
        while (it.hasNext()) {
            ObjetUtilisable effet = it.next();

            // On décrémente la durée de vie de l'effet
            effet.decrementerDuree();

            // Si l'effet n'est plus actif, on le retire
            if (!effet.estActif()) {
                effet.retirerEffet(this);
                it.remove();
                System.out.println("Effet terminé et retiré : " + effet);
            }
        }
    }

}
