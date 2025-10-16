package org.centrale.objet.woe.projettp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * Classe {@code Joueur} representant un joueur humain dans le monde WoE.
 * <p>
 * Le joueur contrôle un {@link Personnage} jouable (heros) et peut interagir
 * avec le monde à travers plusieurs actions :
 * </p>
 *
 * <ul>
 * <li>Sauvegarder la partie (en base PostgreSQL)</li>
 * <li>Se deplacer (8 directions possibles)</li>
 * <li>Attaquer des creatures adjacentes</li>
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
    /** Personnage contrôle par le joueur. */
    public Personnage hero;

    /** Nom du joueur (pseudo). */
    private String nomJoueur;

    /** Indique si une action a ete effectuee ce tour. */
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
    /**
     * Implementation minimale requise par l'interface Analyze.
     * Cette version est utilisee par le moteur IA (non connectee à la base).
     * Ici, on appelle simplement la version complète avec les mêmes paramètres,
     * mais sans connexion.
     */
    @Override
    public void analyzer(Set<Point2D> positionWorld, List<Creature> creatures,
                         List<Objet> objets, int tailleMonde) {
        // Par defaut : simple appel vers la version enrichie sans sauvegarde.
        // (utile quand le monde tourne sans base de donnees)
        analyzer(positionWorld, creatures, objets, null, null);
    }

    /**
    * Variante de analyzer pour le mode connecte à la base.
    * Appelee par World.tourDeJour(..., Connection).
    */
    public void analyzer(Set<Point2D> positionWorld, List<Creature> creatures,
                         List<Objet> objets, World world, Connection conn) {

        // Appelle la version complète avec sauvegarde
        Scanner sc = new Scanner(System.in);
        Point2D posHero = this.hero.getPos();

        do {
            actionEffectuee = false;
            System.out.println("\n=== ACTIONS DISPONIBLES ===");
            System.out.println("0 - Sauvegarder la partie");
            System.out.println("1 - Se deplacer");
            System.out.println("2 - Attaquer");
            System.out.println("3 - Interagir avec un objet");
            System.out.println("4 - Utiliser un objet de l'inventaire");
            System.out.println("5 - Ne rien faire");
            System.out.print("Choix : ");

            int choix = sc.nextInt();
            switch (choix) {
                case 0 -> {
                    sc.nextLine(); // ⚠️ pour consommer le retour ligne de nextInt()
                    System.out.print("💾 Entrez un nom pour votre partie : ");
                    String nomPartie = sc.nextLine();

                    if (nomPartie.trim().isEmpty()) {
                        nomPartie = "Partie_sans_nom_" + System.currentTimeMillis();
                    }

                    int tourActuel = world.getCurrentTurn();
                    int toursRestants = world.getRemainingTurns();

                    System.out.println("\n💾 Sauvegarde de la partie '" + nomPartie + "' en cours...");
                    int idPartie = world.saveWorldToDB(conn, this, nomPartie, tourActuel, toursRestants);

                    if (idPartie != -1) {
                        // ✅ on utilise le setter au lieu d'accéder directement à l'attribut privé
                        world.setCurrentPartieId(idPartie);

                        System.out.println("""
                            ✅ Partie sauvegardée avec succès !
                            🆔 ID de la partie : """ + idPartie + """

                            💡 Conservez cet identifiant précieusement.
                            Il vous sera nécessaire pour restaurer votre partie plus tard.
                        """);
                    } else {
                        System.out.println("❌ Erreur : la sauvegarde n’a pas pu être réalisée.");
                    }

                    actionEffectuee = false; // reste dans le menu après sauvegarde
                }
                case 1 -> deplacerController(creatures, world.TAILLE_MONDE);
                case 2 -> attaquerController(creatures, positionWorld);
                case 3 -> interactionController(objets, positionWorld);
                case 4 -> utiliserObjetController();
                case 5 -> {
                    System.out.println("Vous choisissez de ne rien faire.");
                    actionEffectuee = true;
                }
                default -> System.out.println("Option invalide !");
            }

        } while (!actionEffectuee);
    }

    // ===================== CONTRÔLEURS =====================
    /** Deplacement avec diagonales et verification des collisions */
    public void deplacerController(List<Creature> creatures, int tailleMonde) {
        Scanner sc = new Scanner(System.in);
        boolean choixValide;

        do {
            choixValide = true;
            System.out.println("""
                Deplacement :
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
                System.out.println("❌ Deplacement hors du monde !");
                choixValide = false;
                continue;
            }

            boolean bloque = creatures.stream().anyMatch(c -> c.getPos().equals(newPos));
            if (bloque) {
                System.out.println("❌ Une creature bloque le passage !");
                choixValide = false;
            } else {
                hero.deplacer(dx, dy);
                System.out.println("Vous êtes maintenant en " + hero.getPos());
                actionEffectuee = true;
            }

        } while (!choixValide);
    }

    /** Attaque les creatures à portee */
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
            System.out.println("Aucune cible à proximite !");
            return;
        }

        System.out.println("Cibles à portee :");
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

        System.out.println("Objet trouve : " + cible.getNom());
        System.out.println("1 - Utiliser immediatement");
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
                    System.out.println("" + cible.getNom() + " ajoute à l’inventaire !");
                    actionEffectuee = true;
                } else {
                    System.out.println("Cet objet ne peut pas être stocke !");
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
            System.out.println("Vous avez utilise : " + o.getNom());
            actionEffectuee = true;
        } else {
            System.out.println("Cet objet n’est pas utilisable !");
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
