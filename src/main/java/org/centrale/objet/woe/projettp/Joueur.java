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
    /**
     * Personnage contrôle par le joueur.
     */
    public Personnage hero;

    /**
     * Nom du joueur (pseudo).
     */
    private String nomJoueur;

    /**
     * Indique si une action a ete effectuee ce tour.
     */
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
     * Implementation minimale requise par l'interface Analyze. Cette version
     * est utilisee par le moteur IA (non connectee à la base). Ici, on appelle
     * simplement la version complète avec les mêmes paramètres, mais sans
     * connexion.
     */
    @Override
    public void analyzer(Set<Point2D> positionWorld, List<Creature> creatures,
            List<Objet> objets, int tailleMonde) {
        // Par defaut : simple appel vers la version enrichie sans sauvegarde.
        // (utile quand le monde tourne sans base de donnees)
        analyzer(positionWorld, creatures, objets, null, null);
    }

    /**
     * Variante de analyzer pour le mode connecte à la base. Appelee par
     * World.tourDeJour(..., Connection).
     */
    public void analyzer(Set<Point2D> positionWorld, List<Creature> creatures,
            List<Objet> objets, World world, Connection conn) {

        Scanner sc = new Scanner(System.in);
        Point2D posHero = this.hero.getPos();

        do {
            actionEffectuee = false;
            System.out.println("\nActuellement, vous pouvez :");

            List<String> options = new ArrayList<>();
            List<Runnable> actions = new ArrayList<>();

            // 0️⃣ Sauvegarder la partie (toujours disponible)
            options.add("Sauvegarder la partie");
            actions.add(() -> {
                sc.nextLine(); // consommer le retour ligne
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
            });

            // 1️⃣ Déplacement
            options.add("Se déplacer");
            actions.add(() -> deplacerController(creatures, world.TAILLE_MONDE));

            // 2️⃣ Attaque (si cibles proches)
            List<Creature> ciblesAdjacentes = new ArrayList<>();
            for (Creature c : creatures) {
                double dx = Math.abs(c.getPos().getX() - posHero.getX());
                double dy = Math.abs(c.getPos().getY() - posHero.getY());
                if (dx <= this.hero.getDistAttMax() && dy <= this.hero.getDistAttMax() && !(dx == 0 && dy == 0)) {
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
                    options.add("Interagir avec un objet");
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

            // Afficher les options dynamiques
            for (int i = 0; i < options.size(); i++) {
                System.out.println(i + " - " + options.get(i));
            }

            System.out.print("Sélectionnez une option : ");
            int choix;
            try {
                choix = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("⚠️ Entrée invalide ! Veuillez entrer un nombre.");
                sc.nextLine();
                continue;
            }

            if (choix >= 0 && choix < actions.size()) {
                actions.get(choix).run();
            } else {
                System.out.println("Option invalide.");
            }

        } while (!actionEffectuee);
    }

    // ===================== CONTRÔLEURS =====================
    /**
     * Deplacement avec diagonales et verification des collisions
     */
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
                case 1 ->
                    dy = -1;
                case 2 ->
                    dy = 1;
                case 3 ->
                    dx = -1;
                case 4 ->
                    dx = 1;
                case 5 -> {
                    dx = -1;
                    dy = -1;
                }
                case 6 -> {
                    dx = 1;
                    dy = -1;
                }
                case 7 -> {
                    dx = -1;
                    dy = 1;
                }
                case 8 -> {
                    dx = 1;
                    dy = 1;
                }
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

    /**
     * Attaque les creatures à portee
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
     * Interagit avec un objet sur la case actuelle
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
                default ->
                    System.out.println("Option invalide !");
            }
        } while (!choixValide);
    }

    /**
     * Permet d’utiliser un objet de l’inventaire
     */
    public void utiliserObjetController() {
        Scanner sc = new Scanner(System.in);
        List<Objet> inventaire = hero.getInventaire();

        if (inventaire.isEmpty()) {
            System.out.println("Inventaire vide !");
            return;
        }

        System.out.println("Inventaire :");
        for (int i = 0; i < inventaire.size(); i++) {
            System.out.println((i + 1) + " - " + inventaire.get(i).getNom());
        }
        System.out.println("0 - Retour");

        int choix = sc.nextInt();
        if (choix == 0) {
            return;
        }

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
