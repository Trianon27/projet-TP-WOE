/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.objet.woe.projettp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

/**
 * Classe {@code Joueur} représentant un joueur humain dans le monde WoE.
 * <p>
 * Le joueur contrôle un {@link Personnage} jouable (héros) et peut interagir
 * avec le monde à travers plusieurs actions :
 * </p>
 * 
 * <ul>
 *   <li>Se déplacer dans le monde.</li>
 *   <li>Attaquer des créatures adjacentes.</li>
 *   <li>Interagir avec des objets (ramasser, utiliser, stocker).</li>
 *   <li>Utiliser les objets de son inventaire.</li>
 *   <li>Ou ne rien faire durant un tour.</li>
 * </ul>
 *
 * <p>
 * Cette classe gère la boucle d’interaction principale du joueur (menu console),
 * permettant de choisir les actions à effectuer à chaque tour.  
 * Le joueur agit uniquement via des entrées utilisateur fournies par la console.
 * </p>
 *
 * @author srodr
 * @version 2.0
 */
public class Joueur {

    /** 
     * Personnage contrôlé par le joueur. 
     * Peut être une instance de {@link Guerrier}, {@link Archer}, etc.
     */
    public Personnage hero;

    /** 
     * Indique si le joueur a effectué une action durant le tour courant. 
     * Sert à gérer la boucle du menu d’actions.
     */
    private boolean actionEffectuee;

    // ===================== CONSTRUCTEUR =====================

    /**
     * Constructeur par défaut.
     * <p>
     * Initialise un joueur sans héros et avec aucune action effectuée.
     * </p>
     */
    public Joueur() {
        this.actionEffectuee = false;
    }

    // ===================== MÉTHODES PRINCIPALES =====================

    /**
     * Méthode principale du joueur.
     * <p>
     * Présente au joueur les différentes actions possibles en fonction de sa
     * position, de l’environnement et de l’état du monde :
     * </p>
     * <ul>
     *   <li>Se déplacer vers une case libre.</li>
     *   <li>Attaquer une créature adjacente.</li>
     *   <li>Interagir avec un objet présent sur la case actuelle.</li>
     *   <li>Utiliser un objet de l’inventaire.</li>
     *   <li>Ne rien faire.</li>
     * </ul>
     * 
     * <p>
     * La méthode fonctionne sous la forme d’un menu interactif qui se répète
     * tant qu’aucune action n’a été confirmée.
     * </p>
     *
     * @param positionWorld Ensemble des positions actuellement occupées
     * @param creatures Liste des créatures présentes dans le monde
     * @param objets Liste des objets disponibles sur la carte
     */
    public void analyzer(Set<Point2D> positionWorld, List<Creature> creatures, List<Objet> objets) {
        Scanner sc = new Scanner(System.in);
        Point2D posHero = this.hero.getPos();

        do {
            actionEffectuee = false;
            System.out.println("Actuellement, vous pouvez :");

            List<String> options = new ArrayList<>();
            List<Runnable> actions = new ArrayList<>();

            // ---- 1️⃣ Déplacement ----
            options.add("Se déplacer");
            actions.add(() -> deplacerController(positionWorld));

            // ---- 2️⃣ Attaque ----
            List<Creature> ciblesAdjacentes = new ArrayList<>();
            for (Creature c : creatures) {
                double dx = Math.abs(c.getPos().getX() - posHero.getX());
                double dy = Math.abs(c.getPos().getY() - posHero.getY());
                if (dx <= 1 && dy <= 1 && !(dx == 0 && dy == 0)) {
                    ciblesAdjacentes.add(c);
                }
            }

            if (!ciblesAdjacentes.isEmpty()) {
                options.add("Attaquer");
                actions.add(() -> attaqueController(ciblesAdjacentes, positionWorld));
            }

            // ---- 3️⃣ Interaction avec un objet ----
            for (Objet o : objets) {
                if (o.getPosition().equals(posHero)) {
                    options.add("Interagir");
                    actions.add(() -> interactionController(o, positionWorld));
                    break;
                }
            }

            // ---- 4️⃣ Utiliser un objet ----
            if (!hero.getInventaire().isEmpty()) {
                options.add("Utiliser un objet de l'inventaire");
                actions.add(this::utiliserObjetController);
            }

            // ---- 5️⃣ Ne rien faire ----
            options.add("Ne rien faire");
            actions.add(() -> {
                System.out.println("Vous avez décidé de ne rien faire ce tour.");
                actionEffectuee = true;
            });

            // Affichage du menu
            for (int i = 0; i < options.size(); i++) {
                System.out.println((i + 1) + " - " + options.get(i));
            }

            System.out.println("Sélectionnez une option : ");
            int choix = sc.nextInt();

            if (choix > 0 && choix <= actions.size()) {
                actions.get(choix - 1).run();
            } else {
                System.out.println("Option invalide.");
            }

        } while (!actionEffectuee);
    }

    // ===================== CONTRÔLEURS D’ACTIONS =====================

    /**
     * Gère le déplacement du joueur dans le monde.
     * <p>
     * Propose un menu directionnel (haut, bas, diagonales) et empêche tout
     * déplacement sur une case déjà occupée.
     * </p>
     *
     * @param positionWorld Ensemble des positions déjà occupées dans le monde
     */
    public void deplacerController(Set<Point2D> positionWorld) {
        Scanner sc = new Scanner(System.in);
        boolean choixValide;

        do {
            choixValide = true;
            System.out.println("Choisissez une direction (ou 0 pour revenir) :");
            System.out.println("1-Haut | 2-Bas | 3-Gauche | 4-Droite | 5-Haut-Gauche | 6-Haut-Droite | 7-Bas-Gauche | 8-Bas-Droite");

            int choix = sc.nextInt();
            int dx = 0, dy = 0;

            if (choix == 0) {
                System.out.println("Retour au menu principal...");
                actionEffectuee = false;
                return;
            }

            switch (choix) {
                case 1 -> dy = 1;
                case 2 -> dy = -1;
                case 3 -> dx = -1;
                case 4 -> dx = 1;
                case 5 -> { dx = -1; dy = 1; }
                case 6 -> { dx = 1; dy = 1; }
                case 7 -> { dx = -1; dy = -1; }
                case 8 -> { dx = 1; dy = -1; }
                default -> { System.out.println("Choix invalide !"); choixValide = false; continue; }
            }

            Point2D newPos = new Point2D(this.hero.getPos().getX() + dx, this.hero.getPos().getY() + dy);

            if (positionWorld.contains(newPos)) {
                System.out.println("❌ Position occupée !");
                choixValide = false;
            } else {
                this.hero.deplacer(dx, dy);
                System.out.println("✅ Déplacement effectué vers : " + this.hero.getPos());
                actionEffectuee = true;
            }

        } while (!choixValide);
    }

    /**
     * Gère l’attaque du joueur sur une ou plusieurs créatures adjacentes.
     * <p>
     * Vérifie que le héros implémente l’interface {@link Combattant}
     * avant d’autoriser l’action.
     * </p>
     *
     * @param ciblesAdjacentes liste des créatures adjacentes à attaquer
     * @param positionWorld ensemble des positions occupées du monde
     */
    public void attaqueController(List<Creature> ciblesAdjacentes, Set<Point2D> positionWorld) {
        Scanner sc = new Scanner(System.in);
        boolean choixValide;

        do {
            choixValide = true;
            System.out.println("Choisissez une cible à attaquer :");

            for (int i = 0; i < ciblesAdjacentes.size(); i++) {
                Creature c = ciblesAdjacentes.get(i);
                System.out.println((i + 1) + " - " + c.getNom() + " à la position " + c.getPos());
            }

            System.out.println((ciblesAdjacentes.size() + 1) + " - Ne rien faire");
            System.out.println((ciblesAdjacentes.size() + 2) + " - Retour");

            int choix = sc.nextInt();

            if (choix > 0 && choix <= ciblesAdjacentes.size()) {
                Creature cible = ciblesAdjacentes.get(choix - 1);
                if (hero instanceof Combattant combattant) {
                    combattant.combattre(cible, positionWorld);
                    actionEffectuee = true;
                } else {
                    System.out.println(hero.getNom() + " ne peut pas attaquer !");
                }
            } else if (choix == ciblesAdjacentes.size() + 1) {
                System.out.println("Vous décidez de ne rien faire.");
                actionEffectuee = true;
            } else if (choix == ciblesAdjacentes.size() + 2) {
                System.out.println("Retour au menu principal...");
                actionEffectuee = false;
            } else {
                System.out.println("Option invalide.");
                choixValide = false;
            }

        } while (!choixValide);
    }

    /**
     * Permet d’interagir avec un objet présent sur la même case que le joueur.
     * <p>
     * Deux actions possibles :
     * <ul>
     *   <li>Utiliser immédiatement l’objet.</li>
     *   <li>L’ajouter à l’inventaire (s’il implémente {@link ObjetUtilisable}).</li>
     * </ul>
     * </p>
     *
     * @param o objet sur lequel interagir
     * @param positionWorld ensemble des positions occupées dans le monde
     */
    public void interactionController(Objet o, Set<Point2D> positionWorld) {
        Scanner sc = new Scanner(System.in);
        boolean choixValide = false;

        do {
            System.out.println("Vous avez trouvé : " + o.getNom());
            System.out.println("1 - Utiliser immédiatement");
            System.out.println("2 - Ajouter à l'inventaire");
            System.out.println("0 - Retour");

            int choix = sc.nextInt();

            switch (choix) {
                case 1 -> { this.hero.prendObjet(o, positionWorld); actionEffectuee = true; choixValide = true; }
                case 2 -> {
                    if (o instanceof ObjetUtilisable) {
                        hero.getInventaire().add(o);
                        System.out.println(o.getNom() + " ajouté à l'inventaire.");
                        actionEffectuee = true;
                        choixValide = true;
                    } else {
                        System.out.println("Cet objet ne peut pas être conservé !");
                    }
                }
                case 0 -> { System.out.println("Retour au menu principal..."); actionEffectuee = false; choixValide = true; }
                default -> System.out.println("Option invalide !");
            }
        } while (!choixValide);
    }

    /**
     * Permet au joueur d’utiliser un objet de son inventaire.
     * <p>
     * Si l’objet est une instance de {@link ObjetUtilisable}, 
     * son effet est appliqué au héros, puis retiré de l’inventaire.
     * </p>
     */
    public void utiliserObjetController() {
        Scanner sc = new Scanner(System.in);
        List<Objet> inventaire = hero.getInventaire();

        if (inventaire.isEmpty()) {
            System.out.println("Votre inventaire est vide !");
            actionEffectuee = false;
            return;
        }

        System.out.println("Contenu de votre inventaire :");
        for (int i = 0; i < inventaire.size(); i++) {
            System.out.println((i + 1) + " - " + inventaire.get(i).getNom());
        }
        System.out.println("Sélectionnez un objet à utiliser ou 0 pour revenir :");
        int choix = sc.nextInt();

        if (choix == 0) { System.out.println("Retour..."); actionEffectuee = false; return; }
        if (choix < 1 || choix > inventaire.size()) { System.out.println("Option invalide !"); return; }

        Objet objet = inventaire.get(choix - 1);
        if (objet instanceof ObjetUtilisable objetUtilisable) {
            objetUtilisable.appliquerEffet(hero);
            hero.getInventaire().remove(objet);
            System.out.println("Vous avez utilisé : " + objet.getNom());
            actionEffectuee = true;
        } else {
            System.out.println("Cet objet n'est pas utilisable !");
        }
    }
}
