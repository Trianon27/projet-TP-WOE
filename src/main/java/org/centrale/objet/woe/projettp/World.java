package org.centrale.objet.woe.projettp;

import java.util.*;
import java.sql.*;

/**
 * Represente le monde du jeu WoE avec ses personnages, creatures et objets.
 * <p>
 * Cette classe permet de :
 * <ul>
 * <li>Creer un monde aleatoire avec des positions uniques pour chaque entite.</li>
 * <li>Generer des personnages, monstres et objets aleatoirement.</li>
 * <li>Afficher l’etat du monde et ses protagonistes.</li>
 * <li>Analyser et simuler les tours du jeu.</li>
 * <li>Sauvegarder l’etat complet du monde dans une base PostgreSQL.</li>
 * </ul>
 *
 * @author
 * @version 3.0 (fusion IA + BDD)
 */
public class World {

    // ====================== ATTRIBUTS ======================
    /** Taille du monde (carre TAILLE_MONDE x TAILLE_MONDE) */
    public int TAILLE_MONDE;

    /** Liste de toutes les creatures */
    public ArrayList<Creature> ListCreature;

    /** Liste de tous les objets */
    public LinkedList<Objet> ListObjets;

    /** Liste des entites analytiques (IA, PNJ, etc.) */
    public ArrayList<Analyze> ListAnalyze;

    /** Liste de tous les elements du jeu (creatures + objets) */
    public ArrayList<ElementDeJeu> ListElementJeu;

    /** Ensemble des positions occupees */
    private final Set<Point2D> positionsOccupees;

    // ====================== CONSTRUCTEUR ======================
    public World() {
        this.TAILLE_MONDE = 20;
        this.ListCreature = new ArrayList<>();
        this.ListObjets = new LinkedList<>();
        this.ListAnalyze = new ArrayList<>();
        this.ListElementJeu = new ArrayList<>();
        this.positionsOccupees = new HashSet<>();
    }

    // ====================== CReATION DU JOUEUR ======================
    public Joueur creationJoueur() {
        Random rand = new Random();
        Joueur moi = new Joueur();
        Scanner sc = new Scanner(System.in);
        String nom;
        int choix;

        boolean valide;
        do {
            valide = true;
            System.out.println("Choisissez un personnage :");
            System.out.println("1 - Guerrier");
            System.out.println("2 - Archer");
            System.out.println("3 - Aleatoire");
            choix = sc.nextInt();

            if (choix == 3) choix = rand.nextInt(2) + 1;

            System.out.print("Nom du personnage : ");
            nom = sc.next();

            switch (choix) {
                case 1 -> {
                    Point2D p = positionAleatoire(rand);
                    moi.hero = new Guerrier(nom, true,
                            rand.nextInt(101) + 50, rand.nextInt(21) + 10,
                            rand.nextInt(21) + 5, rand.nextInt(51) + 50,
                            rand.nextInt(51) + 30, p, 1, 5);
                }
                case 2 -> {
                    Point2D p = positionAleatoire(rand);
                    moi.hero = new Archer(nom, true,
                            rand.nextInt(21) + 80, rand.nextInt(11) + 5,
                            rand.nextInt(11) + 5, rand.nextInt(51) + 50,
                            rand.nextInt(51) + 30, p, 2, 6, rand.nextInt(11) + 5);
                }
                default -> {
                    System.out.println("Choix invalide !");
                    valide = false;
                }
            }
        } while (!valide);

        moi.hero.affiche();
        return moi;
    }

    // ====================== GeNeRATION DU MONDE ======================
    public void creerMondeAlea() {
        Random rand = new Random();
        generationCreatures(40, rand, this.ListCreature);
        generationObjets(40, rand, this.ListObjets);

        for (ElementDeJeu e : this.ListElementJeu)
            if (e instanceof Analyze analyze) this.ListAnalyze.add(analyze);
    }

    private void generationCreatures(int n, Random rand, Collection<Creature> col) {
        for (int i = 0; i < n; i++) {
            Point2D pos = positionAleatoire(rand);
            Creature c = (rand.nextBoolean()) ? GenerationP(i + 1, pos) : GenerationM(i + 1, pos);
            col.add(c);
            this.ListElementJeu.add(c);
        }
    }

    private void generationObjets(int n, Random rand, Collection<Objet> col) {
        for (int i = 0; i < n; i++) {
            Point2D pos = positionAleatoire(rand);
            Objet o = GenerationO(i + 1, pos);
            col.add(o);
            this.ListElementJeu.add(o);
        }
    }

    private Point2D positionAleatoire(Random rand) {
        Point2D p;
        do {
            p = new Point2D(rand.nextInt(TAILLE_MONDE), rand.nextInt(TAILLE_MONDE));
        } while (positionsOccupees.contains(p));
        positionsOccupees.add(p);
        return p;
    }

    
    /**
 * Fait tourner le monde pendant plusieurs tours.
 * À chaque tour :
 *  - le joueur choisit une action (deplacement, attaque, sauvegarde, etc.)
 *  - les autres entites executent leur IA (Analyze)
 *  - le monde est reaffiche
 *
 * @param nbTours nombre de tours à simuler
 * @param moi le joueur
 * @param conn connexion SQL active (permet la sauvegarde via option 0)
 */
public void tourDeJour(int nbTours, Joueur moi, Connection conn) {
    for (int t = 0; t < nbTours; t++) {
        System.out.println("\n===== TOUR " + (t + 1) + " =====");

        // 🔹 le joueur agit (avec possibilite de sauvegarde via option 0)
        moi.analyzer(this.positionsOccupees, this.ListCreature, this.ListObjets, this, conn);

        // 🔹 IA des autres creatures (Analyse du monde)
        for (Analyze e : this.ListAnalyze) {
            if (e != null && e != moi) {
                try {
                    e.analyzer(this.positionsOccupees, this.ListCreature, this.ListObjets, this.TAILLE_MONDE);
                } catch (Exception ex) {
                    System.err.println("⚠️ Erreur IA d'une entite : " + ex.getMessage());
                }
            }
        }

        // 🔹 reafficher le monde après les actions
        this.afficheWorld(moi);
    }

    System.out.println("🏁 Simulation terminee après " + nbTours + " tours !");
}

    
    
    
    
    
    
    
    
    
    // ====================== AFFICHAGE ======================
    public void afficheWorld(Joueur moi) {
        int taille = TAILLE_MONDE;
        char[][] monde = new char[taille][taille];

        for (int y = 0; y < taille; y++) Arrays.fill(monde[y], '.');

        for (Creature c : ListCreature)
            if (c != null && c.getPos() != null)
                placerDansMonde(monde, c, getSymbolePourCreature(c));

        for (Objet o : ListObjets)
            if (o != null && o.getPos() != null)
                placerDansMonde(monde, o, getSymbolePourObjet(o));

        placerDansMonde(monde, moi.hero, 'S');
        afficherZoneVisible(monde, moi.hero);
        System.out.println("\n=== STATS DU HeROS ===");
        moi.hero.affiche();
        System.out.println("=======================");
    }

    private void placerDansMonde(char[][] monde, ElementDeJeu e, char s) {
        int x = e.getPos().getX(), y = e.getPos().getY();
        if (x >= 0 && x < monde[0].length && y >= 0 && y < monde.length)
            monde[y][x] = s;
    }

    private char getSymbolePourCreature(Creature c) {
        if (c instanceof Archer) return 'A';
        if (c instanceof Guerrier) return 'G';
        if (c instanceof Paysan) return 'P';
        if (c instanceof Loup) return 'W';
        if (c instanceof Lapin) return 'L';
        return 'C';
    }

    private char getSymbolePourObjet(Objet o) {
        if (o instanceof PotionSoin) return 'O';
        if (o instanceof Epee) return 'E';
        if (o instanceof Nourriture) return 'N';
        if (o instanceof NuageToxique) return 'X';
        return '?';
    }

    private void afficherZoneVisible(char[][] monde, Creature hero) {
        int vision = hero.getDistanceVision();
        int xHero = hero.getPos().getX(), yHero = hero.getPos().getY();

        System.out.println("\n=== MONDE VISIBLE ===");
        for (int y = yHero - vision; y <= yHero + vision; y++) {
            for (int x = xHero - vision; x <= xHero + vision; x++) {
                if (y >= 0 && y < monde.length && x >= 0 && x < monde[0].length)
                    System.out.print(monde[y][x] + " ");
                else System.out.print("*");
            }
            System.out.println();
        }
    }

    // ====================== GeNeRATION DES TYPES ======================
    private Personnage GenerationP(int id, Point2D p) {
        Random r = new Random();
        return switch (r.nextInt(3)) {
            case 0 -> new Archer("Archer " + id, true, 100, 80, 20, 80, 50, p, 2, 5, 10);
            case 1 -> new Paysan("Paysan " + id, true, 100, 100, 100, 100, 100, 100, p, 5);
            default -> new Guerrier("Guerrier " + id, true, 100, 80, 20, 80, 50, p, 1, 3);
        };
    }

    private Monstre GenerationM(int id, Point2D p) {
        Random r = new Random();
        return (r.nextBoolean())
                ? new Lapin("Lapin " + id, true, 50, 20, 5, 10, 10, p, 1, 2, Monstre.Dangerosite.DOCILE)
                : new Loup("Loup " + id, true, 80, 30, 15, 20, 30, p, 2, 4, Monstre.Dangerosite.DANGEREUX);
    }

    private Objet GenerationO(int id, Point2D p) {
        Random r = new Random();
        return switch (r.nextInt(7)) {
            case 0 -> new PotionSoin("Potion " + id, "Potion magique", p, 20);
            case 1 -> new Epee("Epee " + id, "Epee en acier", p, 15, Epee.Etat.NONE);
            case 2 -> new Nourriture(Nourriture.Nourritures.ALCOHOOL, "Alcool", "Très fort", p);
            case 3 -> new Nourriture(Nourriture.Nourritures.LEGUMBRE, "Legume", "Bon pour la sante", p);
            case 4 -> new Nourriture(Nourriture.Nourritures.BOISSONRICHE, "Boisson riche", "Energetique", p);
            case 5 -> new Nourriture(Nourriture.Nourritures.POMMEDOR, "Pomme d'or", "Magique", p);
            default -> new NuageToxique("NuageT " + id, "Gaz toxique", p, r.nextInt(10) + 1, r.nextInt(3) + 1, r.nextInt(11) + 1);
        };
    }

    // ====================== SAUVEGARDE MONDE ======================
    public void saveWorldToDB(Connection conn, Joueur joueur) {
        try {
            // 1️⃣ Creer la partie
            String sqlPartie = """
                INSERT INTO Partie (nom_partie, id_joueur)
                VALUES (?, NULL)
                RETURNING id_partie
            """;
            int idPartie;
            try (PreparedStatement ps = conn.prepareStatement(sqlPartie)) {
                ps.setString(1, "Partie_" + System.currentTimeMillis());
                ResultSet rs = ps.executeQuery();
                rs.next();
                idPartie = rs.getInt("id_partie");
            }
            System.out.println("Partie creee (id_partie=" + idPartie + ")");

            // 2️⃣ Sauvegarde personnage et joueur
            joueur.hero.saveToDB(conn, idPartie);
            int idJoueur;
            try (PreparedStatement ps = conn.prepareStatement("""
                INSERT INTO Joueur (id_personnage)
                VALUES ((SELECT id_personnage FROM Personnage WHERE id_partie=? ORDER BY id_personnage DESC LIMIT 1))
                RETURNING id_joueur
            """)) {
                ps.setInt(1, idPartie);
                ResultSet rs = ps.executeQuery();
                rs.next();
                idJoueur = rs.getInt("id_joueur");
            }

            try (PreparedStatement ps = conn.prepareStatement("UPDATE Partie SET id_joueur=? WHERE id_partie=?")) {
                ps.setInt(1, idJoueur);
                ps.setInt(2, idPartie);
                ps.executeUpdate();
            }

            // 3️⃣ Inventaire
            int idInventaire;
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO Inventaire (id_joueur) VALUES (?) RETURNING id_inventaire")) {
                ps.setInt(1, idJoueur);
                ResultSet rs = ps.executeQuery();
                rs.next();
                idInventaire = rs.getInt("id_inventaire");
            }

            // 4️⃣ Objets du monde
            for (Objet o : this.ListObjets) {
                if (o instanceof Epee e) e.saveToDB(conn, idPartie);
                else if (o instanceof PotionSoin p) p.saveToDB(conn, idPartie);
                else if (o instanceof Nourriture n) n.saveToDB(conn, idPartie);
                else if (o instanceof NuageToxique x) x.saveToDB(conn, idPartie);
            }

            // 5️⃣ Inventaire du heros
            try (PreparedStatement ps = conn.prepareStatement("""
                INSERT INTO Contenu_Inventaire (id_inventaire, id_nourriture, quantite)
                VALUES (?, ?, 1)
            """)) {
                for (Objet o : joueur.hero.getInventaire()) {
                    if (o instanceof Nourriture n) {
                        n.saveToDB(conn, idPartie);
                        PreparedStatement get = conn.prepareStatement("""
                            SELECT id_nourriture FROM Nourriture
                            WHERE nom=? AND id_partie=? ORDER BY id_nourriture DESC LIMIT 1
                        """);
                        get.setString(1, n.getNom());
                        get.setInt(2, idPartie);
                        ResultSet rs = get.executeQuery();
                        if (rs.next()) {
                            ps.setInt(1, idInventaire);
                            ps.setInt(2, rs.getInt("id_nourriture"));
                            ps.addBatch();
                        }
                    }
                }
                ps.executeBatch();
            }

            // 6️⃣ Creatures
            for (Creature c : this.ListCreature) {
                if (c instanceof Loup l) l.saveToDB(conn, idPartie);
                else if (c instanceof Lapin la) la.saveToDB(conn, idPartie);
            }

            System.out.println("Monde sauvegarde avec succès (id_partie=" + idPartie + ")");
        } catch (SQLException e) {
            System.err.println("Erreur World.saveWorldToDB : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
