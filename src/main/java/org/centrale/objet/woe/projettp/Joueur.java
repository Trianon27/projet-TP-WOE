package org.centrale.objet.woe.projettp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * Classe {@code Joueur} représentant un joueur humain dans le monde WoE.
 * <p>
 * Le joueur contrôle un {@link Personnage} jouable (héros) et peut interagir
 * avec le monde à travers plusieurs actions :
 * </p>
 *
 * <ul>
 * <li>Sauvegarder la partie (en base PostgreSQL)</li>
 * <li>Se déplacer (8 directions possibles)</li>
 * <li>Attaquer des créatures adjacentes</li>
 * <li>Interagir avec des objets du monde</li>
 * <li>Utiliser un objet de l'inventaire</li>
 * <li>Ou ne rien faire</li>
 * </ul>
 *
 * @author Fusion
 * @version 5.0 (fusion analyse + sauvegarde dynamique)
 */
public class Joueur implements Analyze {

    // ===================== ATTRIBUTS =====================
    /** Personnage contrôlé par le joueur. */
    public Personnage hero;

    /** Nom du joueur (pseudo). */
    private String nomJoueur;

    /** Indique si une action a été effectuée ce tour. */
    private boolean actionEffectuee;

    // ===================== CONSTRUCTEURS =====================
    public Joueur() {
        this.actionEffectuee = false;
    }

    public Joueur(String nomJoueur) {
        this.nomJoueur = nomJoueur;
        this.actionEffectuee = false;
    }

    // ===================== GETTERS / SETTERS =====================
    public String getNomJoueur() {
        return nomJoueur;
    }

    public void setNomJoueur(String nomJoueur) {
        this.nomJoueur = nomJoueur;
    }

    public Personnage getHero() {
        return hero;
    }

    public void setHero(Personnage hero) {
        this.hero = hero;
    }

    // ===================== MÉTHODE PRINCIPALE =====================
    /**
     * Boucle principale d’action du joueur.
     * Ajoute l’option "0 - Sauvegarder la partie" (BDD PostgreSQL).
     *
     * @param positionWorld Ensemble des positions actuellement occupées
     * @param creatures Liste des créatures présentes dans le monde
     * @param objets Liste des objets disponibles dans le monde
     * @param world Référence du monde
     * @param conn Connexion active à la base
     */
    public void analyzer(Set<Point2D> positionWorld, List<Creature> creatures, List<Objet> objets, int tailleMonde) {
        // surcharge non utilisée sans base
        throw new UnsupportedOperationException("Utiliser analyzer(..., world, conn)");
    }

    public void analyzer(Set<Point2D> positionWorld, List<Creature> creatures, List<Objet> objets,
                         World world, Connection conn) {
        Scanner sc = new Scanner(System.in);

        do {
            actionEffectuee = false;
            System.out.println("\n=== ACTIONS DISPONIBLES ===");
            System.out.println("0 - Sauvegarder la partie");
            System.out.println("1 - Se déplacer");
            System.out.println("2 - Attaquer");
            System.out.println("3 - Interagir avec un objet");
            System.out.println("4 - Utiliser un objet de l'inventaire");
            System.out.println("5 - Ne rien faire");
            System.out.print("Choix : ");

            int choix = sc.nextInt();
            switch (choix) {
                case 0 -> {
                    System.out.println("💾 Sauvegarde en cours...");
                    world.saveWorldToDB(conn, this);
                    System.out.println("✅ Partie sauvegardée !");
                    actionEffectuee = false; // retourne au menu
                }
                case 1 -> deplacerController(creatures, tailleMonde);
                case 2 -> attaquerController(creatures, positionWorld);
                case 3 -> interactionController(objets, positionWorld);
                case 4 -> utiliserObjetController();
                case 5 -> {
                    System.out.println("Vous choisissez de ne rien faire.");
                    actionEffectuee = true;
                }
                default -> System.out.println("❌ Option invalide !");
            }

        } while (!actionEffectuee);
    }

    // ===================== CONTRÔLEURS =====================
    /** Déplacement avec diagonales et vérification des collisions */
    public void deplacerController(List<Creature> creatures, int tailleMonde) {
        Scanner sc = new Scanner(System.in);
        boolean choixValide;

        do {
            choixValide = true;
            System.out.println("""
                Déplacement :
                0-Retour | 1-Haut | 2-Bas | 3-Gauche | 4-Droite |
                5-Haut-Gauche | 6-Haut-Droite | 7-Bas-Gauche | 8-Bas-Droite
            """);

            int choix = sc.nextInt();
            int dx = 0, dy = 0;

            switch (choix) {
                case 0 -> {
                    actionEffectuee = false;
                    return;
                }
                case 1 -> dy = -1;
                case 2 -> dy = 1;
                case 3 -> dx = -1;
                case 4 -> dx = 1;
                case 5 -> { dx = -1; dy = -1; }
                case 6 -> { dx = 1; dy = -1; }
                case 7 -> { dx = -1; dy = 1; }
                case 8 -> { dx = 1; dy = 1; }
                default -> {
                    System.out.println("Choix invalide !");
                    choixValide = false;
                    continue;
                }
            }

            Point2D newPos = new Point2D(hero.getPos().getX() + dx, hero.getPos().getY() + dy);

            // Limites du monde
            if (newPos.getX() < 0 || newPos.getY() < 0 || newPos.getX() >= tailleMonde || newPos.getY() >= tailleMonde) {
                System.out.println("❌ Déplacement hors du monde !");
                choixValide = false;
                continue;
            }

            boolean bloque = creatures.stream().anyMatch(c -> c.getPos().equals(newPos));
            if (bloque) {
                System.out.println("❌ Une créature bloque le passage !");
                choixValide = false;
            } else {
                hero.deplacer(dx, dy);
                System.out.println("✅ Vous êtes maintenant en " + hero.getPos());
                actionEffectuee = true;
            }

        } while (!choixValide);
    }

    /** Attaque les créatures à portée */
    public void attaquerController(List<Creature> creatures, Set<Point2D> positionWorld) {
        Scanner sc = new Scanner(System.in);
        List<Creature> cibles = new ArrayList<>();

        for (Creature c : creatures) {
            double dx = Math.abs(c.getPos().getX() - hero.getPos().getX());
            double dy = Math.abs(c.getPos().getY() - hero.getPos().getY());
            if (dx <= hero.getDistAttMax() && dy <= hero.getDistAttMax() && !(dx == 0 && dy == 0)) {
                cibles.add(c);
            }
        }

        if (cibles.isEmpty()) {
            System.out.println("Aucune cible à proximité !");
            return;
        }

        System.out.println("Cibles à portée :");
        for (int i = 0; i < cibles.size(); i++)
            System.out.println((i + 1) + " - " + cibles.get(i).getNom() + " (" + cibles.get(i).getPtVie() + " PV)");
        System.out.println("0 - Retour");

        int choix = sc.nextInt();
        if (choix > 0 && choix <= cibles.size() && hero instanceof Combattant combattant) {
            combattant.combattre(cibles.get(choix - 1), positionWorld, creatures);
            actionEffectuee = true;
        }
    }

    /** Interagit avec un objet sur la case actuelle */
    public void interactionController(List<Objet> objets, Set<Point2D> positionWorld) {
        Scanner sc = new Scanner(System.in);
        Objet cible = objets.stream()
                .filter(o -> o.getPosition().equals(hero.getPos()))
                .findFirst()
                .orElse(null);

        if (cible == null) {
            System.out.println("Aucun objet ici.");
            return;
        }

        System.out.println("Objet trouvé : " + cible.getNom());
        System.out.println("1 - Utiliser immédiatement");
        System.out.println("2 - Ajouter à l'inventaire");
        System.out.println("0 - Retour");

        int choix = sc.nextInt();
        switch (choix) {
            case 1 -> {
                hero.prendObjet(cible, positionWorld);
                objets.remove(cible);
                actionEffectuee = true;
            }
            case 2 -> {
                if (cible instanceof ObjetUtilisable) {
                    hero.getInventaire().add(cible);
                    positionWorld.remove(cible.getPosition());
                    objets.remove(cible);
                    System.out.println("✅ " + cible.getNom() + " ajouté à l’inventaire !");
                    actionEffectuee = true;
                } else {
                    System.out.println("❌ Cet objet ne peut pas être stocké !");
                }
            }
            case 0 -> System.out.println("Retour au menu principal...");
            default -> System.out.println("Option invalide !");
        }
    }

    /** Permet d’utiliser un objet de l’inventaire */
    public void utiliserObjetController() {
        Scanner sc = new Scanner(System.in);
        List<Objet> inventaire = hero.getInventaire();

        if (inventaire.isEmpty()) {
            System.out.println("Inventaire vide !");
            return;
        }

        System.out.println("Inventaire :");
        for (int i = 0; i < inventaire.size(); i++)
            System.out.println((i + 1) + " - " + inventaire.get(i).getNom());
        System.out.println("0 - Retour");

        int choix = sc.nextInt();
        if (choix == 0) return;

        if (choix < 1 || choix > inventaire.size()) {
            System.out.println("Choix invalide !");
            return;
        }

        Objet o = inventaire.get(choix - 1);
        if (o instanceof ObjetUtilisable ou) {
            ou.appliquerEffet(hero);
            inventaire.remove(o);
            System.out.println("✅ Vous avez utilisé : " + o.getNom());
            actionEffectuee = true;
        } else {
            System.out.println("❌ Cet objet n’est pas utilisable !");
        }
    }

    // ===================== SAUVEGARDE JOUEUR =====================
    public void saveToDB(Connection conn, int idPersonnage) {
        String sql = "INSERT INTO Joueur (pseudo, id_personnage) VALUES (?, ?) RETURNING id_joueur";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, this.nomJoueur);
            ps.setInt(2, idPersonnage);
            ps.executeQuery();
        } catch (SQLException e) {
            System.err.println("Erreur sauvegarde Joueur : " + e.getMessage());
        }
    }
}
