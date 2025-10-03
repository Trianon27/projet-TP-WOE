package org.centrale.objet.woe.projettp;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.LinkedList;
import java.util.ArrayList;

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
        
        
        LinkedList<Creature> ListCreature = new LinkedList<>();
        LinkedList<Objet> ListObjet = new LinkedList<>();
        
        
        for(int i = 0; i < 10; i++){
            Random RandomGen = new Random();
            Point2D cPoint = positionAleatoire(RandomGen);
            int randint = RandomGen.nextInt(2);
            switch (randint){
            case 0 -> {
                ListCreature.add(GenerationP(i, cPoint));
            }
            case 1 -> {
                ListCreature.add(GenerationM(i, cPoint));
            }
            default -> {
            }
        }}
        for (int i = 0; i < 10; i++) { // 100 créatures aléatoires
            Random randomGen = new Random();
            Point2D cPoint = positionAleatoire(randomGen);
            ListObjet.add(GenerationO(i, cPoint));
            }
        afficheListes(ListCreature,ListObjet);

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
    
    
    
    /*----------------------------------*/
    
    
    private Personnage GenerationP(int id, Point2D p){
        Random RandomGen = new Random();
        int randint = RandomGen.nextInt(3);
        switch (randint){
            case 0 -> {
                return new Archer("Archer"+id, true, 100, 80, 20, 80, 50, p, 2, 5, 10);
            }
            case 1 -> {
                return new Paysan("paysan"+id, true, 100, 100, 100, 100, 100, 100, p, 5);
            }
            case 2 -> {
                return new Guerrier("Guerrier"+id, true, 100, 80, 20, 80, 50, p, 1, 3); 
            }
            default -> {
                return null;
            }
        }
    }
        /**
     * Génère un monstre aléatoire (Lapin ou Loup).
     *
     * @param id identifiant du monstre
     * @param p position dans le monde
     * @return un monstre de type Lapin ou Loup
     */
    private Monstre GenerationM(int id, Point2D p) {
        Random rand = new Random();
        int randint = rand.nextInt(2); // 0 ou 1
        switch (randint) {
            case 0 -> {
                return new Lapin("Lapin" + id, true, 50, 20, 5, 10, 10, p, 1, 2, Monstre.Dangerosite.DOCILE);
            }
            case 1 -> {
                return new Loup("Loup" + id, true, 80, 30, 15, 20, 30, p, 2, 4, Monstre.Dangerosite.DANGEREUX);
            }
            default -> {
                return null;
            }
        }
    }

    /**
     * Génère un objet aléatoire (PotionSoin ou Épée).
     *
     * @param id identifiant de l'objet
     * @param p position dans le monde
     * @return un objet de type PotionSoin ou Epée
     */
    private Objet GenerationO(int id, Point2D p) {
        Random rand = new Random();
        int randint = rand.nextInt(2); // 0 ou 1
        switch (randint) {
            case 0 -> {
                return new PotionSoin("Potion" + id, "Potion magique", p, 20);
            }
            case 1 -> {
                return new Epee("Epee" + id, "Épée en acier", p, 15, Epee.Etat.NONE);
            }
            default -> {
                return null;
            }
        }
    }
    
    
    /**
    * Affiche toutes les créatures et objets présents dans le monde.
    *
    * @param creatures la liste de créatures
    * @param objets la liste d'objets
    */
    public void afficheListes(LinkedList<Creature> creatures, LinkedList<Objet> objets) {
        System.out.println("\n===== LISTE DES CREATURES =====");
        for (Creature c : creatures) {
            if (c != null) {
                c.affiche();
            }
        }

        System.out.println("\n===== LISTE DES OBJETS =====");
        for (Objet o : objets) {
            if (o != null) {
                o.affiche();
            }
        }
        System.out.println("=============================\n");
    }


}
