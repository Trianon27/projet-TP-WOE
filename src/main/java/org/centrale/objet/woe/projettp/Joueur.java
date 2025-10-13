package org.centrale.objet.woe.projettp;

import java.util.*;
import java.sql.*;

/**
 * Classe {@code Joueur} représentant un joueur humain dans le monde WoE.
 * Le joueur contrôle un {@link Personnage} jouable (héros) et peut interagir
 * avec le monde à travers plusieurs actions (déplacement, combat, inventaire, etc.).
 *
 * @author srodr
 * @version 4.0 (ajout sauvegarde dynamique en base)
 */
public class Joueur {

    // ===================== ATTRIBUTS =====================

    /** Personnage contrôlé par le joueur. */
    public Personnage hero;

    /** Nom du joueur (pseudo). */
    private String nomJoueur;

    /** Indique si le joueur a effectué une action durant le tour courant. */
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
     * Ajout d’une option "0 - Sauvegarder la partie" qui écrit dans la base.
     *
     * @param positionWorld Ensemble des positions actuellement occupées
     * @param creatures Liste des créatures présentes dans le monde
     * @param objets Liste des objets disponibles dans le monde
     * @param world Référence du monde (pour la sauvegarde)
     * @param conn Connexion active à la base
     */
    public void analyzer(Set<Point2D> positionWorld, List<Creature> creatures, List<Objet> objets, World world, Connection conn) {
        Scanner sc = new Scanner(System.in);
        Point2D posHero = this.hero.getPos();

        do {
            actionEffectuee = false;
            System.out.println("\nActuellement, vous pouvez :");

            // ✅ Options principales
            System.out.println("0 - Sauvegarder la partie");
            System.out.println("1 - Se déplacer");
            System.out.println("2 - Attaquer");
            System.out.println("3 - Interagir avec un objet");
            System.out.println("4 - Utiliser un objet de l'inventaire");
            System.out.println("5 - Ne rien faire");

            System.out.print("Choix : ");
            int choix = sc.nextInt();

            switch (choix) {
                case 0 -> { // 🔴 Sauvegarder la partie
                    System.out.println("💾 Sauvegarde de la partie en cours...");
                    world.saveWorldToDB(conn, this);
                    System.out.println("✅ Partie sauvegardée avec succès !");
                    actionEffectuee = false;
                }
                case 1 -> deplacerController(creatures);
                case 2 -> {
                    List<Creature> ciblesAdjacentes = getCreaturesAdjacentes(creatures, posHero);
                    if (!ciblesAdjacentes.isEmpty())
                        attaqueController(ciblesAdjacentes, positionWorld, creatures);
                    else
                        System.out.println("Aucune cible à proximité.");
                }
                case 3 -> interactionAvecObjet(objets, positionWorld, posHero);
                case 4 -> utiliserObjetController();
                case 5 -> {
                    System.out.println("Vous ne faites rien ce tour.");
                    actionEffectuee = true;
                }
                default -> System.out.println("Option invalide !");
            }

        } while (!actionEffectuee);
    }

    // ===================== CONTRÔLEURS =====================

    private List<Creature> getCreaturesAdjacentes(List<Creature> creatures, Point2D posHero) {
        List<Creature> cibles = new ArrayList<>();
        for (Creature c : creatures) {
            double dx = Math.abs(c.getPos().getX() - posHero.getX());
            double dy = Math.abs(c.getPos().getY() - posHero.getY());
            if (dx <= 1 && dy <= 1 && !(dx == 0 && dy == 0)) {
                cibles.add(c);
            }
        }
        return cibles;
    }

    public void deplacerController(List<Creature> creatures) {
        Scanner sc = new Scanner(System.in);
        boolean choixValide;
        do {
            choixValide = true;
            System.out.println("Déplacement : 0-Retour | 1-Haut | 2-Bas | 3-Gauche | 4-Droite");
            int choix = sc.nextInt();
            int dx = 0, dy = 0;

            if (choix == 0) {
                actionEffectuee = false;
                return;
            }
            switch (choix) {
                case 1 -> dy = -1;
                case 2 -> dy = 1;
                case 3 -> dx = -1;
                case 4 -> dx = 1;
                default -> {
                    System.out.println("Choix invalide !");
                    choixValide = false;
                    continue;
                }
            }
            Point2D newPos = new Point2D(hero.getPos().getX() + dx, hero.getPos().getY() + dy);
            boolean bloque = creatures.stream().anyMatch(c -> c.getPos().equals(newPos));
            if (bloque) {
                System.out.println("❌ Une créature bloque le passage !");
                choixValide = false;
            } else {
                hero.deplacer(dx, dy);
                System.out.println("✅ Déplacement vers " + hero.getPos());
                actionEffectuee = true;
            }
        } while (!choixValide);
    }

    private void interactionAvecObjet(List<Objet> objets, Set<Point2D> positionWorld, Point2D posHero) {
        for (Objet o : objets) {
            if (o.getPosition().equals(posHero)) {
                interactionController(o, positionWorld, objets);
                return;
            }
        }
        System.out.println("Aucun objet ici.");
    }

    public void attaqueController(List<Creature> ciblesAdjacentes, Set<Point2D> positionWorld, List<Creature> creatures) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Choisissez une cible à attaquer :");
        for (int i = 0; i < ciblesAdjacentes.size(); i++) {
            Creature c = ciblesAdjacentes.get(i);
            System.out.println((i + 1) + " - " + c.getNom() + " (" + c.getPtVie() + " PV)");
        }
        int choix = sc.nextInt();
        if (choix > 0 && choix <= ciblesAdjacentes.size()) {
            Creature cible = ciblesAdjacentes.get(choix - 1);
            if (hero instanceof Combattant combattant) {
                combattant.combattre(cible, positionWorld, creatures);
                actionEffectuee = true;
            }
        }
    }

    public void interactionController(Objet o, Set<Point2D> positionWorld, List<Objet> objets) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Vous trouvez " + o.getNom());
        System.out.println("1 - Utiliser immédiatement");
        System.out.println("2 - Ajouter à l'inventaire");
        System.out.println("0 - Retour");

        int choix = sc.nextInt();
        switch (choix) {
            case 1 -> {
                hero.prendObjet(o, positionWorld);
                objets.remove(o);
                actionEffectuee = true;
            }
            case 2 -> {
                if (o instanceof ObjetUtilisable) {
                    hero.getInventaire().add(o);
                    objets.remove(o);
                    System.out.println(o.getNom() + " ajouté à l'inventaire !");
                    actionEffectuee = true;
                } else {
                    System.out.println("❌ Cet objet n'est pas utilisable !");
                }
            }
            default -> actionEffectuee = false;
        }
    }

    public void utiliserObjetController() {
        Scanner sc = new Scanner(System.in);
        List<Objet> inventaire = hero.getInventaire();

        if (inventaire.isEmpty()) {
            System.out.println("Inventaire vide !");
            actionEffectuee = false;
            return;
        }

        System.out.println("Inventaire :");
        for (int i = 0; i < inventaire.size(); i++) {
            System.out.println((i + 1) + " - " + inventaire.get(i).getNom());
        }
        int choix = sc.nextInt();

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
        }
    }

    // ===================== SAUVEGARDE DU JOUEUR =====================

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
