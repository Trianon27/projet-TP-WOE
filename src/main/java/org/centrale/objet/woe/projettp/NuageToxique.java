/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.objet.woe.projettp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * La classe {@code NuageToxique} représente un élément dangereux du monde du
 * jeu générant des dégâts continus dans une zone déterminée pendant un certain
 * nombre de tours.
 * <p>
 * Un nuage toxique est un objet environnemental (héritant de {@link Objet}) qui
 * inflige un certain nombre de points de dégâts à toute créature se trouvant
 * dans sa zone d’effet, et disparaît après une durée limitée.
 * </p>
 *
 * <h3>Caractéristiques principales :</h3>
 * <ul>
 * <li>Dégâts infligés par tour ({@code degatParTour}).</li>
 * <li>Taille de la zone affectée - elle doit etre un nombre impaire pour qu'une
 * position au centre existe ({@code taille}).</li>
 * <li>Durée de vie du nuage, en nombre de tours ({@code duree}).</li>
 * </ul>
 *
 * <h3>Exemple d’utilisation :</h3>
 * <pre>{@code
 * NuageToxique n = new NuageToxique(
 *     "Gaz vert",
 *     "Nuage toxique dense et corrosif",
 *     new Point2D(4, 4),
 *     3,   // dégâts par tour
 *     5,   // taille 5x5
 *     8    // dure 8 tours
 * );
 * n.affiche();
 * }</pre>
 *
 * @author hayta
 * @version 2.0
 */
public class NuageToxique extends Objet implements Deplacable, Combattant, Analyze {

    // ===================== ATTRIBUTS =====================
    /**
     * Dégâts infligés par tour aux créatures présentes dans la zone du nuage.
     */
    private int degatParTour;

    /**
     * Taille du nuage (en unités de position).
     * <p>
     * La zone affectée correspond à un carré de {@code taille × taille} autour
     * de la position centrale.</p>
     */
    private int taille;

    /**
     * Durée de vie restante du nuage, en nombre de tours.
     * <p>
     * Lorsque cette valeur atteint zéro, le nuage disparaît du monde.</p>
     */
    private int duree;

    // ===================== CONSTRUCTEURS =====================
    /**
     * Constructeur par défaut.
     * <p>
     * Initialise un nuage toxique générique de 5×5 unités infligeant 1 point de
     * dégât par tour pendant 5 tours.
     * </p>
     */
    public NuageToxique() {
        super();
        this.degatParTour = 5;
        this.taille = 2;
        this.duree = 10;
    }

    /**
     * Constructeur complet.
     *
     * @param nom Nom du nuage
     * @param description Description du nuage
     * @param p Position centrale du nuage
     * @param degat Dégâts infligés par tour
     * @param taille Taille de la zone d’effet (en unités)
     * @param duree Durée de vie du nuage (en nombre de tours)
     */
    public NuageToxique(String nom, String description, Point2D p, int degat, int taille, int duree) {
        super(nom, description, p);
        this.degatParTour = degat;
        this.taille = taille;
        this.duree = duree;
    }

    /**
     * Constructeur par copie.
     *
     * @param autreNuage Nuage à copier
     */
    public NuageToxique(NuageToxique autreNuage) {
        super(autreNuage);
        this.degatParTour = autreNuage.degatParTour;
        this.taille = autreNuage.taille;
        this.duree = autreNuage.duree;
    }

    // ===================== GETTERS / SETTERS =====================
    /**
     * Retourne les dégâts infligés par tour.
     *
     * @return dégâts infligés par tour
     */
    public int getDegatParTour() {
        return degatParTour;
    }

    /**
     * Modifie les dégâts infligés par tour.
     *
     * @param degatParTour nouveaux dégâts par tour
     */
    public void setDegatParTour(int degatParTour) {
        this.degatParTour = degatParTour;
    }

    /**
     * Retourne la taille du nuage (longueur d’un côté du carré d’effet).
     *
     * @return taille du nuage
     */
    public int getTaille() {
        return taille;
    }

    /**
     * Définit la taille du nuage.
     *
     * @param taille nouvelle taille (en unités)
     */
    public void setTaille(int taille) {
        this.taille = taille;
    }

    /**
     * Retourne la durée de vie restante du nuage.
     *
     * @return durée de vie (en tours)
     */
    public int getDuree() {
        return duree;
    }

    /**
     * Définit la durée de vie du nuage.
     *
     * @param duree nouvelle durée (en tours)
     */
    public void setDuree(int duree) {
        this.duree = duree;
    }

    // ===================== MÉTHODES =====================
    /**
     * Réduit la durée du nuage d’un tour.
     * <p>
     * À appeler à chaque tour du jeu. Si la durée devient nulle, le nuage est
     * considéré comme dissipé.</p>
     */
    public void decrementerDuree() {
        if (this.duree > 0) {
            this.duree--;
        }
    }

    /**
     * Vérifie si le nuage est encore actif.
     *
     * @return {@code true} si le nuage est encore présent dans le monde,
     * {@code false} sinon
     */
    public boolean estActif() {
        return this.duree > 0;
    }

    /**
     * Déplace la nuage (en deplacant le centre) en fonction des valeurs dx et
     * dy.
     *
     * @param dx déplacement horizontal
     * @param dy déplacement vertical
     */
    @Override
    public void deplacer(int dx, int dy) {
        this.pos.translate(dx, dy);
    }

    @Override
    public void deplacementAleatoire(Set<Point2D> positionsOccupees, int tailleMonde) {
        Random rand = new Random();
        final int MAX_ESSAIS = 9;
        int essais = 0;

        Point2D anciennePos = this.pos;

        while (essais < MAX_ESSAIS) {
            int dx = rand.nextInt(3) - 1; // -1, 0, 1
            int dy = rand.nextInt(3) - 1;

            // éviter le non-déplacement
            if (dx == 0 && dy == 0) {
                essais++;
                continue;
            }

            int nx = anciennePos.getX() + dx;
            int ny = anciennePos.getY() + dy;

            // nouvelle position déclarée dans la boucle => effectively final pour la lambda
            Point2D nouvellePos = new Point2D(nx, ny);

            // vérifier les limites du monde
            boolean dansMonde = nx >= 0 && nx < tailleMonde && ny >= 0 && ny < tailleMonde;
            if (!dansMonde) {
                essais++;
                continue;
            }
            // vérifier si la case est occupée (en utilisant lambda mais avec nouvellePos locale)
            boolean occupee = positionsOccupees.stream().anyMatch(p -> p.equals(nouvellePos));

            if (!occupee) {
                // mettre à jour la liste des positions occupées (si nécessaire)
                // IMPORTANT : cela suppose que equals/hashCode de Point2D sont corrects
                positionsOccupees.remove(anciennePos);
                positionsOccupees.add(nouvellePos);

                // effectuer le déplacement
                this.pos = nouvellePos; // ou this.pos.translate(dx, dy) si tu veux garder l'instance
                System.out.println(this.getNom() + " se déplace en (" + nx + ", " + ny + ").");
                return;
            }
            essais++;
        }

    }

    /**
     * Inflige des dégâts à une créature si elle se trouve dans la zone d’effet
     * du nuage.
     * <p>
     * Cette méthode représente le comportement offensif du nuage toxique à
     * chaque tour : elle vérifie si la créature passée en paramètre est dans la
     * zone d’effet (carré de taille {@code taille} centré sur {@code pos}), et
     * lui inflige {@code degatParTour} points de dégâts. Si la créature meurt,
     * elle est retirée du monde via {@link Creature#mourir(Set)}.
     * </p>
     *
     * @param c la créature potentiellement affectée par le nuage
     * @param positionWorld ensemble des positions actuellement occupées dans le
     * monde
     * @param creatures
     */
    @Override
    public void combattre(Creature c, Set<Point2D> positionWorld, List<Creature> creatures) {
        // Si le nuage est dissipé, il n'agit plus
        if (!this.estActif()) {
            return;
        }

        // Coordonnées de la créature
        int xC = c.getPos().getX();
        int yC = c.getPos().getY();

        // Vérifie si la créature est vivante et dans la zone d'effet
            System.out.println();
            System.out.println("Le nuage toxique \"" + this.getNom() + "\" affecte " + c.getNom() + " !");
            System.out.println("Position du nuage : (" + pos.getX() + ", " + pos.getY() + ")");
            System.out.println("Position de la créature : (" + xC + ", " + yC + ")");
            System.out.println("Dégâts infligés : " + degatParTour);

            // Applique les dégâts
            c.setPtVie(c.getPtVie() - degatParTour);

            // Vérifie l'état de la créature après l'attaque
            if (c.getPtVie() <= 0) {
                System.out.println("" + c.getNom() + " a succombe au nuage toxique !");
                c.mourir(positionWorld, creatures);  // Supprime la créature du monde et met son état à faux
            } else {
                System.out.println("Il reste " + c.getPtVie() + " points de vie a " + c.getNom() + ".");
            }

            System.out.println("Duree restante du nuage : " + duree + " tours.");
            System.out.println("----------------------------------------------");
        
    }

    /**
     *
     * @param positionWorld
     * @param creatures
     * @param objets
     * @param tailleMonde
     */
    @Override
    public void analyzer(Set<Point2D> positionWorld, List<Creature> creatures, List<Objet> objets, int tailleMonde) {
        // 1. Déplacement aléatoire du nuage
        this.deplacementAleatoire(positionWorld, tailleMonde);

        // 2. Calcul des limites de la zone d'effet
        int demiTaille = taille / 2;
        int xMin = this.pos.getX() - demiTaille;
        int xMax = this.pos.getX() + demiTaille;
        int yMin = this.pos.getY() - demiTaille;
        int yMax = this.pos.getY() + demiTaille;

        // 3. Récupérer toutes les créatures vivantes dans la zone d'effet
        List<Creature> creaturesAffectees = new ArrayList<>();
        for (Creature c : creatures) {
            int xC = c.getPos().getX();
            int yC = c.getPos().getY();

            if (c.isEtat() && xC >= xMin && xC <= xMax && yC >= yMin && yC <= yMax) {
                creaturesAffectees.add(c);
            }
        }

        // 4. Appliquer l'attaque à toutes les créatures affectées
        for (Creature c : creaturesAffectees) {
            this.combattre(c, positionWorld, creatures);
        }
    }

}
