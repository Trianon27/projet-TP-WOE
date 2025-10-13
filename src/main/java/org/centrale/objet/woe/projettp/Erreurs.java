/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.objet.woe.projettp;
import java.util.*;


/**
 * Classe de démonstration des différents types d’exceptions et d’erreurs Java.
 * <p>
 * Cette classe illustre la gestion des exceptions via des exemples concrets,
 * afin de comprendre leur apparition et leur traitement à l’aide de blocs
 * {@code try-catch}. Elle ne dépend pas du moteur de jeu et peut être exécutée
 * indépendamment pour des tests pédagogiques.
 * </p>
 *
 * <h3>Exceptions couvertes :</h3>
 * <ul>
 *   <li>{@code NullPointerException}</li>
 *   <li>{@code IndexOutOfBoundsException}</li>
 *   <li>{@code ClassCastException}</li>
 *   <li>{@code ArithmeticException}</li>
 *   <li>{@code ConcurrentModificationException}</li>
 *   <li>{@code StackOverflowError}</li>
 *   <li>{@code NumberFormatException}</li>
 *   <li>{@code IllegalArgumentException}</li>
 *   <li>{@code IllegalStateException}</li>
 *   <li>{@code OutOfMemoryError}</li>
 * </ul>
 *
 * @author hayta
 * @version 1.0
 */
public class Erreurs {

    /**
     * Exécute une démonstration complète de gestion des exceptions Java.
     * Chaque type d’erreur est provoqué, intercepté et affiché de manière contrôlée.
     */
    public void demonstrationErreursJava() {
        System.out.println("=== DÉMONSTRATION DES DIFFÉRENTS TYPES D'ERREURS JAVA ===");

        // NullPointerException
        System.out.println("\n1. NullPointerException:");
        try {
            Point2D pointNull = null;
            System.out.println("Coordonnée X: " + pointNull.getX());
        } catch (NullPointerException e) {
            afficherErreur(e);
        }

        // IndexOutOfBoundsException
        System.out.println("\n2. IndexOutOfBoundsException:");
        try {
            List<Creature> petiteListe = new ArrayList<>();
            petiteListe.add(GenerationM(1, new Point2D(0, 0)));
            Creature creature = petiteListe.get(5); // index inexistant
        } catch (IndexOutOfBoundsException e) {
            afficherErreur(e);
        }

        //  ClassCastException
        System.out.println("\n3. ClassCastException:");
        try {
            List<Object> objetsDivers = new ArrayList<>();
            objetsDivers.add(new Archer("Archer", true, 100, 80, 20, 80, 50,
                    new Point2D(0, 0), 2, 5, 10));
            Loup loup = (Loup) objetsDivers.get(0); // cast invalide
        } catch (ClassCastException e) {
            afficherErreur(e);
        }

        // ArithmeticException
        System.out.println("\n4. ArithmeticException:");
        try {
            int pointsDeVie = 100;
            int diviseur = 0;
            int resultat = pointsDeVie / diviseur;
        } catch (ArithmeticException e) {
            afficherErreur(e);
        }

        //  ConcurrentModificationException
        System.out.println("\n5. ConcurrentModificationException:");
        try {
            List<Creature> creatures = new ArrayList<>();
            creatures.add(GenerationM(1, new Point2D(0, 0)));
            creatures.add(GenerationM(2, new Point2D(1, 1)));

            for (Creature creature : creatures) {
                if (creature.getNom().equals("Loup 1")) {
                    creatures.remove(creature); // modification interdite
                }
            }
        } catch (ConcurrentModificationException e) {
            afficherErreur(e);
        }

        //  StackOverflowError
        System.out.println("\n6. StackOverflowError:");
        try {
            methodeRecursive(0);
        } catch (StackOverflowError e) {
            System.out.println("❌ " + e.getClass().getSimpleName() + " détectée !");
        }

        //  NumberFormatException
        System.out.println("\n7. NumberFormatException:");
        try {
            String texteInvalide = "pas_un_nombre";
            int nombre = Integer.parseInt(texteInvalide);
        } catch (NumberFormatException e) {
            afficherErreur(e);
        }

        //  IllegalArgumentException
        System.out.println("\n8. IllegalArgumentException:");
        try {
            new Archer("", true, -100, -80, -20, -80, -50, new Point2D(0, 0), -2, -5, -10);
        } catch (IllegalArgumentException e) {
            afficherErreur(e);
        }

        // OutOfMemoryError (simulation légère)
        System.out.println("\n9. OutOfMemoryError (simulation sûre):");
        try {
            List<Point2D> grandeListe = new ArrayList<>();
            for (int i = 0; i < 1000000; i++) {
                grandeListe.add(new Point2D(i, i));
            }
            System.out.println("✅ Gestion mémoire réussie avec " + grandeListe.size() + " éléments.");
        } catch (OutOfMemoryError e) {
            System.out.println("❌ " + e.getClass().getSimpleName() + " détectée !");
        }

        // IllegalStateException
        System.out.println("\n10. IllegalStateException:");
        try {
            List<Creature> creatures = new ArrayList<>();
            Iterator<Creature> iterator = creatures.iterator();
            iterator.remove(); // utilisation invalide
        } catch (IllegalStateException e) {
            afficherErreur(e);
        }

        System.out.println("\n=== FIN DE LA DÉMONSTRATION ===");
    }

    /**
     * Affiche proprement une exception attrapée.
     * @param e l’exception interceptée
     */
    private void afficherErreur(Exception e) {
        System.out.println("❌ Erreur attrapée: " + e.getClass().getSimpleName());
        if (e.getMessage() != null)
            System.out.println("Message: " + e.getMessage());
    }

    /**
     * Méthode utilitaire récursive pour déclencher un StackOverflowError.
     * @param compteur compteur d'appels récursifs
     */
    private void methodeRecursive(int compteur) {
        methodeRecursive(compteur + 1); // aucune condition d'arrêt
    }

    /**
     * Génère un monstre aléatoire pour les tests d’exceptions.
     * @param id identifiant du monstre
     * @param p position
     * @return une instance de Loup ou Lapin
     */
    private Monstre GenerationM(int id, Point2D p) {
        Random rand = new Random();
        int randint = rand.nextInt(2);
        return switch (randint) {
            case 0 -> new Lapin("Lapin " + id, true, 50, 20, 5, 10, 10,
                    p, 1, 2, Monstre.Dangerosite.DOCILE);
            case 1 -> new Loup("Loup " + id, true, 80, 30, 15, 20, 30,
                    p, 2, 4, Monstre.Dangerosite.DANGEREUX);
            default -> null;
        };
    }

    /**
     * Point d’entrée pour lancer les tests indépendamment.
     * @param args arguments de la ligne de commande
     */
    public static void main(String[] args) {
        Erreurs test = new Erreurs();
        test.demonstrationErreursJava();
    }
}