/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.objet.woe.projettp;

import java.util.ArrayList;
import java.util.List;
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
 * @version 3.0 (fusion complète)
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

    // ===================== MÉTHODE PRINCIPALE =====================

    /**
     * Boucle principale d’action du joueur.
     * <p>
     * Présente les options disponibles selon la position du héros et les entités présentes :
     * déplacement, attaque, interaction, utilisation d’objet ou inactivité.
     * </p>
     *
     * @param positionWorld Ensemble des positions actuellement occupées
     * @param creatures Liste des créatures présentes dans le monde
     * @param objets Liste des objets disponibles dans le monde
     */
    public void analyzer(Set<Point2D> positionWorld, List<Creature> creatures, List<Objet> objets) {
        Scanner sc = new Scanner(System.in);
        Point2D posHero = this.hero.getPos();

        do {
            actionEffectuee = false;
            System.out.println("\nActuellement, vous pouvez :");

            List<String> options = new ArrayList<>();
            List<Runnable> actions = new ArrayList<>();

            // 1️⃣ Déplacement
            options.add("Se déplacer");
            actions.add(() -> deplacerController(creatures));

            // 2️⃣ Attaque (si cibles proches)
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
                actions.add(() -> attaqueController(ciblesAdjacentes, positionWorld, creatures));
            }

            // 3️⃣ Interaction avec un objet présent
            for (Objet o : objets) {
                if (o.getPosition().equals(posHero)) {
                    options.add("Interagir avec l'objet");
                    actions.add(() -> interactionController(o, positionWorld, objets));
                    break;
                }
            }

            // 4️⃣ Utiliser un objet de l’inventaire
            if (!hero.getInventaire().isEmpty()) {
                options.add("Utiliser un objet de l'inventaire");
                actions.add(this::utiliserObjetController);
            }

            // 5️⃣ Ne rien faire
            options.add("Ne rien faire");
            actions.add(() -> {
                System.out.println("Vous avez décidé de ne rien faire ce tour.");
                actionEffectuee = true;
            });

            // Afficher les options
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

    // ===================== CONTRÔLEURS =====================

    /**
     * Gère le déplacement du héros, en vérifiant qu’aucune créature n’occupe la case cible.
     * 
     * @param creatures Liste de toutes les créatures pour détecter les collisions
     */
    public void deplacerController(List<Creature> creatures) {
        Scanner sc = new Scanner(System.in);
        boolean choixValide;

        do {
            choixValide = true;
            System.out.println("Vous pouvez vous déplacer d'une case adjacente ou retourner au menu principal.");
            System.out.println("0-Retour | 1-Haut | 2-Bas | 3-Gauche | 4-Droite | 5-Haut-Gauche | 6-Haut-Droite | 7-Bas-Gauche | 8-Bas-Droite");

            int choix = sc.nextInt();
            int dx = 0, dy = 0;

            if (choix == 0) {
                System.out.println("Retour au menu principal...");
                actionEffectuee = false;
                return;
            }

            switch (choix) {
                case 1 -> dy = -1;
                case 2 -> dy = 1;
                case 3 -> dx = -1;
                case 4 -> dx = 1;
                case 5 -> { dx = -1; dy = 1; }
                case 6 -> { dx = 1; dy = 1; }
                case 7 -> { dx = -1; dy = -1; }
                case 8 -> { dx = 1; dy = -1; }
                default -> { System.out.println("Choix invalide !"); choixValide = false; continue; }
            }

            Point2D newPos = new Point2D(this.hero.getPos().getX() + dx, this.hero.getPos().getY() + dy);
            boolean bloque = creatures.stream().anyMatch(c -> c.getPos().equals(newPos));

            if (bloque) {
                System.out.println("❌ Une créature bloque le passage !");
                choixValide = false;
            } else {
                this.hero.deplacer(dx, dy);
                System.out.println("✅ Déplacement effectué vers : " + this.hero.getPos());
                actionEffectuee = true;
            }

        } while (!choixValide);
    }

    /**
     * Gère le système d’attaque du joueur contre les créatures proches.
     * 
     * @param ciblesAdjacentes Liste des créatures à portée d’attaque
     * @param positionWorld Ensemble des positions occupées
     * @param creatures Liste complète des créatures (pour mise à jour après combat)
     */
    public void attaqueController(List<Creature> ciblesAdjacentes, Set<Point2D> positionWorld, List<Creature> creatures) {
        Scanner sc = new Scanner(System.in);
        boolean choixValide;

        do {
            choixValide = true;
            System.out.println("Vous pouvez attaquer l'une des créatures adjacentes :");
            for (int i = 0; i < ciblesAdjacentes.size(); i++) {
                Creature c = ciblesAdjacentes.get(i);
                System.out.println((i + 1) + " - " + c.getNom() + " (" + c.getPtVie() + " PV)");
            }

            int optionNeRienFaire = ciblesAdjacentes.size() + 1;
            int optionRetour = ciblesAdjacentes.size() + 2;

            System.out.println(optionNeRienFaire + " - Ne rien faire");
            System.out.println(optionRetour + " - Retour");
            System.out.println("Sélectionnez une option :");
            int choix = sc.nextInt();

            if (choix > 0 && choix <= ciblesAdjacentes.size()) {
                Creature cible = ciblesAdjacentes.get(choix - 1);
                if (hero instanceof Combattant combattant) {
                    combattant.combattre(cible, positionWorld, creatures);
                    actionEffectuee = true;
                } else {
                    System.out.println(hero.getNom() + " ne peut pas attaquer !");
                }
            } else if (choix == optionNeRienFaire) {
                System.out.println("Vous décidez de ne rien faire.");
                actionEffectuee = true;
            } else if (choix == optionRetour) {
                System.out.println("Retour au menu principal...");
                actionEffectuee = false;
            } else {
                System.out.println("Option invalide !");
                choixValide = false;
            }

        } while (!choixValide);
    }

    /**
     * Gère les interactions avec les objets présents dans le monde :
     * <ul>
     *   <li>Utilisation immédiate de l’objet</li>
     *   <li>Ajout à l’inventaire (si {@link ObjetUtilisable})</li>
     * </ul>
     *
     * @param o Objet sur la même case que le joueur
     * @param positionWorld Ensemble des positions occupées
     * @param objets Liste des objets existants dans le monde (mise à jour en cas de retrait)
     */
    public void interactionController(Objet o, Set<Point2D> positionWorld, List<Objet> objets) {
        Scanner sc = new Scanner(System.in);
        boolean choixValide = false;

        do {
            System.out.println("Vous avez trouvé : " + o.getNom());
            System.out.println("1 - Utiliser immédiatement");
            System.out.println("2 - Ajouter à l'inventaire");
            System.out.println("0 - Retour");

            int choix = sc.nextInt();

            switch (choix) {
                case 1 -> {
                    this.hero.prendObjet(o, positionWorld);
                    objets.remove(o);
                    actionEffectuee = true;
                    choixValide = true;
                }
                case 2 -> {
                    if (o instanceof ObjetUtilisable) {
                        hero.getInventaire().add(o);
                        positionWorld.remove(o.getPosition());
                        objets.remove(o);
                        System.out.println(o.getNom() + " ajouté à l'inventaire.");
                        actionEffectuee = true;
                        choixValide = true;
                    } else {
                        System.out.println("❌ Cet objet ne peut pas être conservé !");
                    }
                }
                case 0 -> {
                    System.out.println("Retour au menu principal...");
                    actionEffectuee = false;
                    choixValide = true;
                }
                default -> System.out.println("Option invalide !");
            }
        } while (!choixValide);
    }

    /**
     * Permet au joueur d’utiliser un objet de son inventaire.
     * <p>
     * Si l’objet implémente {@link ObjetUtilisable}, son effet est appliqué au héros
     * puis l’objet est retiré de l’inventaire.
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

        System.out.println("Inventaire :");
        for (int i = 0; i < inventaire.size(); i++) {
            System.out.println((i + 1) + " - " + inventaire.get(i).getNom());
        }
        System.out.println("0 - Retour");

        int choix = sc.nextInt();
        if (choix == 0) {
            System.out.println("Retour...");
            actionEffectuee = false;
            return;
        }

        if (choix < 1 || choix > inventaire.size()) {
            System.out.println("Option invalide !");
            return;
        }

        Objet objet = inventaire.get(choix - 1);
        if (objet instanceof ObjetUtilisable objetUtilisable) {
            objetUtilisable.appliquerEffet(hero);
            hero.getInventaire().remove(objet);
            System.out.println("✅ Vous avez utilisé : " + objet.getNom());
            actionEffectuee = true;
        } else {
            System.out.println("Cet objet n'est pas utilisable !");
        }
    }
}
