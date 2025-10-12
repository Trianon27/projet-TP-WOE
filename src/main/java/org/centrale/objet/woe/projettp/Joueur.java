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
 *
 * @author srodr
 */
public class Joueur {

    public Personnage hero;
    private boolean actionEffectuee; // Drapeau général pour savoir si le joueur a agi ou non

    public Joueur() {
        this.actionEffectuee = false; // Par défaut, aucune action n'a encore été effectuée
    }

    public void analyzer(Set<Point2D> positionWorld, List<Creature> creatures, List<Objet> objets) {
        Scanner sc = new Scanner(System.in);
        Point2D posHero = this.hero.getPos();

        do {
            actionEffectuee = false; // Réinitialise à chaque tour

            System.out.println("Actuellement, vous pouvez :");

            List<String> options = new ArrayList<>();
            List<Runnable> actions = new ArrayList<>();

            // 1. Déplacement
            options.add("Se déplacer");
            actions.add(() -> deplacerController(positionWorld));

            // 2. Attaque
            List<Creature> ciblesAdjacentes = new ArrayList<>();
            for (Creature c : creatures) {
                double distanceX = Math.abs(c.getPos().getX() - posHero.getX());
                double distanceY = Math.abs(c.getPos().getY() - posHero.getY());
                if (distanceX <= 1 && distanceY <= 1 && !(distanceX == 0 && distanceY == 0)) {
                    ciblesAdjacentes.add(c);
                }
            }

            if (!ciblesAdjacentes.isEmpty()) {
                options.add("Attaquer");
                actions.add(() -> attaqueController(ciblesAdjacentes, positionWorld));
            }

            // 3. Interaction avec les objets
            for (Objet o : objets) {
                if (o.getPosition().getX() == posHero.getX() && o.getPosition().getY() == posHero.getY()) {
                    options.add("Interagir");
                    actions.add(() -> interactionController(o, positionWorld));
                    break;
                }
            }

            if (!hero.getInventaire().isEmpty()) {
                options.add("Utiliser un objet de l'inventaire");
                actions.add(() -> utiliserObjetController());
            }

            // 5. Ne rien faire
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

        } while (!actionEffectuee); // On continue tant que le joueur revient en arrière
    }

    public void deplacerController(Set<Point2D> positionWorld) {
        Scanner sc = new Scanner(System.in);
        boolean choixValide;

        do {
            choixValide = true;

            System.out.println("Vous pouvez vous déplacer d'une case adjacente ou retourner au menu principal.");
            System.out.println("Choisissez une direction :");
            System.out.println("0 - Retour");
            System.out.println("1 - Haut");
            System.out.println("2 - Bas");
            System.out.println("3 - Gauche");
            System.out.println("4 - Droite");
            System.out.println("5 - Haut-Gauche");
            System.out.println("6 - Haut-Droite");
            System.out.println("7 - Bas-Gauche");
            System.out.println("8 - Bas-Droite");

            int choix = sc.nextInt();
            int dx = 0, dy = 0;

            if (choix == 0) {
                System.out.println("Retour au menu principal...");
                actionEffectuee = false; // Le joueur retourne à analyzer
                return;
            }

            switch (choix) {
                case 1 ->
                    dy = 1;
                case 2 ->
                    dy = -1;
                case 3 ->
                    dx = -1;
                case 4 ->
                    dx = 1;
                case 5 -> {
                    dx = -1;
                    dy = 1;
                }
                case 6 -> {
                    dx = 1;
                    dy = 1;
                }
                case 7 -> {
                    dx = -1;
                    dy = -1;
                }
                case 8 -> {
                    dx = 1;
                    dy = -1;
                }
                default -> {
                    System.out.println("Choix invalide !");
                    choixValide = false;
                    continue;
                }
            }

            Point2D newPos = new Point2D(this.hero.getPos().getX() + dx, this.hero.getPos().getY() + dy);

            if (positionWorld.contains(newPos)) {
                System.out.println("Impossible de se déplacer ici, position occupée !");
                choixValide = false;
            } else {
                this.hero.deplacer(dx, dy);
                System.out.println("Déplacement effectué en : " + this.hero.getPos().getX() + " , " + this.hero.getPos().getY());
                actionEffectuee = true; // Une action a été faite
            }

        } while (!choixValide);
    }

    public void attaqueController(List<Creature> ciblesAdjacentes, Set<Point2D> positionWorld) {
        Scanner sc = new Scanner(System.in);
        boolean choixValide;

        do {
            choixValide = true;

            System.out.println("Vous pouvez attaquer l'une des créatures adjacentes :");
            for (int i = 0; i < ciblesAdjacentes.size(); i++) {
                Creature c = ciblesAdjacentes.get(i);
                System.out.println((i + 1) + " - " + c.getNom() + " à la position " + c.getPos());
            }

            int optionNeRienFaire = ciblesAdjacentes.size() + 1;
            int optionRetour = ciblesAdjacentes.size() + 2;

            System.out.println(optionNeRienFaire + " - Ne rien faire");
            System.out.println(optionRetour + " - Retour");
            System.out.println("Sélectionnez une créature à attaquer, ne rien faire ou retourner :");
            int choix = sc.nextInt();

            if (choix > 0 && choix <= ciblesAdjacentes.size()) {
                Creature cible = ciblesAdjacentes.get(choix - 1);
                System.out.println("Vous attaquez " + cible.getNom() + " !");
                if (hero instanceof Combattant combattant) {
                    combattant.combattre(cible, positionWorld);
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
                System.out.println("Option invalide.");
                choixValide = false;
            }

        } while (!choixValide);
    }

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
                case 1 -> {
                    this.hero.prendObjet(o, positionWorld);
                    actionEffectuee = true;
                    choixValide = true;
                }
                case 2 -> {
                    if (o instanceof ObjetUtilisable) {
                        hero.getInventaire().add(o);
                        System.out.println(o.getNom() + " a été ajouté à l'inventaire.");
                        actionEffectuee = true;
                        choixValide = true;
                    } else {
                        System.out.println("Cet objet ne peut pas etre garde dans l'inventoire !");
                        actionEffectuee = false;
                        choixValide = false;

                    }
                }
                case 0 -> {
                    System.out.println("Retour au menu principal...");
                    actionEffectuee = false;
                    choixValide = true;
                }
                default -> {
                    System.out.println("Option invalide !");
                }
            }
        } while (!choixValide);
    }

    public void utiliserObjetController() {
        Scanner sc = new Scanner(System.in);
        List<Objet> inventaire = hero.getInventaire(); 

        if (inventaire.isEmpty()) {
            System.out.println("Votre inventaire est vide !");
            actionEffectuee = false; // No se realiza ninguna acción
            return;
        }

        System.out.println("Voici le contenu de votre inventaire :");
        for (int i = 0; i < inventaire.size(); i++) {
            System.out.println((i + 1) + " - " + inventaire.get(i).getNom());
        }
        System.out.println("Sélectionnez le numéro de l'objet à utiliser ou 0 pour revenir :");

        int choix = sc.nextInt();
        if (choix == 0) {
            System.out.println("Retour au menu principal...");
            actionEffectuee = false;
            return;
        }

        if (choix < 1 || choix > inventaire.size()) {
            System.out.println("Option invalide !");
            actionEffectuee = false;
            return;
        }

        Objet objet = inventaire.get(choix - 1);

        if (objet instanceof ObjetUtilisable objetUtilisable) {
            objetUtilisable.appliquerEffet(hero); // Aplicar el efecto del objeto al personaje
            // Eliminar directamente del inventario
            hero.getInventaire().remove(objet);
            System.out.println("Vous avez utilisé et retiré : " + objet.getNom());

            actionEffectuee = true;
        } else {
            System.out.println("Cet objet n'est pas utilisable !");
            actionEffectuee = false;
        }

    }

}
