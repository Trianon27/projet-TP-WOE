package org.centrale.objet.woe.projettp;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Représente le monde du jeu WoE avec ses personnages et leurs positions.
 * Permet de créer un monde aléatoire, gérer les positions et afficher l'état du
 * monde.
 * <p>
 * Les personnages sont placés dans un monde 2D avec des positions uniques pour éviter
 * les superpositions.
 * </p>
 * 
 * @author srodr
 */
public class World {

    // ================= PERSONNAGES =================
    
    /** Archer principal du monde */
    public Archer robin;

    /** Archer secondaire */
    public Archer guillaumeT;

    /** Paysan présent dans le monde */
    public Paysan peon;

    /** Guerrier présent dans le monde */
    public Guerrier grosBill;

    /** Lapin présent dans le monde */
    public Lapin bugs;

    /** Deuxième lapin */
    public Lapin bugs2; 

    /** Loup présent dans le monde */
    public Loup wolfie; 
    
    /** */
    public PotionSoin potionV; 
         

    // ================= POSITIONS =================
    
    /** Ensemble des positions occupées pour éviter les superpositions */
    private final Set<Point2D> positionsOccupees;

    // ================= CONSTRUCTEUR =================

    /**
     * Constructeur par défaut.
     * Initialise les personnages avec des valeurs de base et initialise
     * l'ensemble des positions occupées.
     */
    public World() {
        Point2D p = new Point2D(0, 0);
        robin = new Archer("Robin", true, 100, 80, 20, 80, 50, p, 2, 5, 10);
        guillaumeT = new Archer(robin);
        guillaumeT.setNom("GuillaumeT");
        Point2D p2 = new Point2D(1, 1);
        grosBill = new Guerrier("grosBill", true, 100, 80, 20, 80, 50, p2, 1, 3); 
        peon = new Paysan("Paysan", true, 100, 100, 100, 100, 100, 100, p, 5);
        bugs = new Lapin("Lapin", true, 100, 100, 100, 100, 100, p, 1, 3, Monstre.Dangerosite.DOCILE);
        bugs2 = new Lapin(bugs);
        Point2D p3 = new Point2D(0, 1);
        wolfie = new Loup("Loup", true, 100, 10, 10, 50, 50, p3, 1, 3, Monstre.Dangerosite.DANGEREUX);
        potionV = new PotionSoin("Potion de Vie","Potion tres fort",p3,20);
        
        positionsOccupees = new HashSet<>();
    }

    // ================= MÉTHODES =================

    /**
     * Place les protagonistes aléatoirement dans le monde 2D.
     * Hypothèse : coordonnées entières dans [0, 100].
     * Les positions sont uniques et ne se superposent pas.
     */
    public void creerMondeAlea() {
        Random rand = new Random();
        positionsOccupees.clear(); // on vide au cas où

        robin.setPos(positionAleatoire(rand));
        peon.setPos(positionAleatoire(rand));
        bugs.setPos(positionAleatoire(rand));
        guillaumeT.setPos(positionAleatoire(rand));
    }

    /**
     * Génère une position unique non encore utilisée.
     *
     * @param rand Générateur de nombres aléatoires
     * @return une position Point2D libre dans le monde
     */
    private Point2D positionAleatoire(Random rand) {
        Point2D p;
        do {
            int x = rand.nextInt(101); // entre 0 et 100
            int y = rand.nextInt(101);
            p = new Point2D(x, y);
        } while (positionsOccupees.contains(p));
        positionsOccupees.add(p);
        return p;
    }

    /**
     * Effectue un tour de jour pour tous les protagonistes.
     * Chaque personnage se déplace de manière aléatoire.
     *
     * @param nbTours Nombre de tours à effectuer
     */
    public void tourDeJour(int nbTours) {
        for (int t = 0; t < nbTours; t++) {
            robin.deplaceAleatoire();
            guillaumeT.deplaceAleatoire();
            peon.deplaceAleatoire();
            bugs.deplaceAleatoire();

            afficheWorld();
        }
    }

    /** 
     * Retourne l'ensemble des positions actuellement occupées.
     *
     * @return Set des positions occupées
     */
    public Set<Point2D> getPositionsOccupees() {
        return positionsOccupees;
    }

    /**
     * Affiche l'état complet du monde et de ses protagonistes.
     */
    public void afficheWorld() {
        System.out.println();
        System.out.println("=== Monde WoE ===");
        robin.affiche();
        guillaumeT.affiche();
        grosBill.affiche();
        peon.affiche();
        bugs.affiche();
        bugs2.affiche();
        wolfie.affiche();
        potionV.affiche();
        System.out.println("===============");
        System.out.println();
    }
}
