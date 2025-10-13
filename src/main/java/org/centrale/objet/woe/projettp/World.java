package org.centrale.objet.woe.projettp;

import java.util.*;

/**
 * Représente le monde du jeu WoE avec ses personnages, créatures et objets.
 *
 * Cette classe permet de :
 * <ul>
 * <li>Créer un monde aléatoire avec des positions uniques pour chaque
 * entité.</li>
 * <li>Générer des personnages, monstres et objets aléatoirement.</li>
 * <li>Afficher l’état du monde et ses protagonistes.</li>
 * <li>Mesurer des temps d’exécution pour des calculs sur différentes
 * collections Java.</li>
 * </ul>
 *
 *
 * <p>
 * Les positions sont représentées dans un espace 2D borné.</p>
 *
 * @author srodr
 */
public class World {

    // ================= PERSONNAGES =================
    /**
     * Archer principal du monde
     */
    public Archer robin;

    /**
     * Archer secondaire
     */
    public Archer guillaumeT;

    /**
     * Paysan présent dans le monde
     */
    public Paysan peon;

    /**
     * Guerrier présent dans le monde
     */
    public Guerrier grosBill;

    /**
     * Lapin présent dans le monde
     */
    public Lapin bugs;

    /**
     * Deuxième lapin
     */
    public Lapin bugs2;

    /**
     * Loup présent dans le monde
     */
    public Loup wolfie;

    /**
     * Potion de soin présente dans le monde
     */
    public PotionSoin potionV;

    /**
     * Taille (dimension maximale) du monde 2D
     */
    public int TAILLE_MONDE;

    public ArrayList<Creature> ListCreature;

    public LinkedList<Objet> ListObjets;

    // ================= POSITIONS =================
    /**
     * Ensemble des positions occupées pour éviter les superpositions
     */
    private final Set<Point2D> positionsOccupees;

    // ================= CONSTRUCTEUR =================
    /**
     * Constructeur par défaut. Initialise les personnages principaux avec des
     * valeurs de base et l’ensemble des positions occupées.
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

        potionV = new PotionSoin("Potion de Vie", "Potion tres forte", p3, 20);

        positionsOccupees = new HashSet<>();
        TAILLE_MONDE = 8;

        this.ListCreature = new ArrayList<>();
        this.ListObjets = new LinkedList<>();

    }

    // ================= MÉTHODES =================
    public Joueur creationJoueur() {
        Random rand = new Random();
        Joueur moi = new Joueur();
        String nom;
        int election;
        Scanner sc = new Scanner(System.in);
        boolean choixValide;

        do {
            choixValide = true;

            System.out.println("Choisissez un personnage :");
            System.out.println("1 - Guerrier");
            System.out.println("2 - Archer");
            System.out.println("3 - Choix aléatoire");

            election = sc.nextInt();

            if (election == 3) {
                election = rand.nextInt(2) + 1; // 1 ou 2 aléatoirement
                System.out.println("Un personnage aléatoire a été choisi pour vous !");
            }

            System.out.println("Choisissez le nom de votre personnage : ");
            nom = sc.next();

            switch (election) {
                case 1 -> {
                    boolean etat = true;
                    int pVie = rand.nextInt(101) + 50;
                    int dAtt = rand.nextInt(21) + 10;
                    int pPar = rand.nextInt(21) + 5;
                    int paAtt = rand.nextInt(51) + 50;
                    int paPar = rand.nextInt(51) + 30;
                    Point2D p = positionAleatoire(rand);
                    int dMax = 1;
                    int distanceVision = 10;
                    moi.hero = new Guerrier(nom, etat, pVie, dAtt, pPar, paAtt, paPar, p, dMax, distanceVision);
                    moi.hero.affiche();
                }

                case 2 -> {
                    boolean etatArcher = true;
                    int pVieArcher = rand.nextInt(21) + 80;
                    int dAttArcher = rand.nextInt(11) + 5;
                    int pParArcher = rand.nextInt(11) + 5;
                    int paAttArcher = rand.nextInt(51) + 50;
                    int paParArcher = rand.nextInt(51) + 30;
                    Point2D pArcher = positionAleatoire(rand);
                    int dMaxArcher = 2;
                    int distanceVisionArcher = 12;
                    int nbFleches = rand.nextInt(11) + 5;

                    moi.hero = new Archer(nom, etatArcher, pVieArcher, dAttArcher, pParArcher,
                            paAttArcher, paParArcher, pArcher, dMaxArcher, distanceVisionArcher, nbFleches);
                    moi.hero.affiche();
                }

                default -> {
                    System.out.println("Choix invalide ! Veuillez réessayer.");
                    choixValide = false;
                }
            }

        } while (!choixValide);

        return moi;
    }

    /**
     * Place aléatoirement les protagonistes du monde dans un espace 2D.
     * <p>
     * Les coordonnées sont des entiers dans [0, TAILLE_MONDE]. Les positions
     * générées sont uniques et ne se superposent pas.
     * </p>
     */
    public void creerMondeAlea() {
        Random rand = new Random();
        positionsOccupees.clear();
        robin.setPos(positionAleatoire(rand));
        peon.setPos(positionAleatoire(rand));
        bugs.setPos(positionAleatoire(rand));
        guillaumeT.setPos(positionAleatoire(rand));

        generationCreatures(5, rand, this.ListCreature);
        generationObjets(5, rand, this.ListObjets);

        //afficheListes(ListCreature, ListObjets);
        //affichePointDeVieParTaille(ListCreature);
    }

    /**
     * Crée un laboratoire de test pour comparer les temps d’exécution entre
     * différentes structures de données (List, Set, etc.).
     * <p>
     * On mesure le temps nécessaire pour calculer le total des points de vie
     * d’un ensemble de créatures en utilisant deux méthodes :
     * <ol>
     * <li>Basée sur la taille de la collection (accès indexé si List).</li>
     * <li>Basée sur les itérateurs Java.</li>
     * </ol>
     * </p>
     *
     * @param collection collection utilisée pour stocker les créatures
     * @param iteration
     */
    public void creationLabDeMondePourComparerDesTemps(Collection<Creature> collection, int iteration) {
        Random rand = new Random();
        System.out.println("=== Type de collection utilisee : " + collection.getClass().getSimpleName() + " ===");

        this.TAILLE_MONDE = 1000;
        int populationInitiale = 100;

        System.out.println("""
        Dans cet exercice, nous allons mesurer le temps necessaire pour calculer
        le total des points de vie dun ensemble de personnages.
        Le calcul sera effectue de deux manieres :
          1. Base sur la taille de la collection.
          2. En utilisant les iterateurs Java.
        Pour cette experience, nous genererons un monde contenant jusqua un million
        de personnages, en tenant compte egalement du nombre dobjets generes.
        Les mesures seront realisees en millisecondes et en nanosecondes.
        """);

        for (int i = 0; i < iteration; i++) {
            collection.clear();
            long startNs, startMs, endMs, endNs;

            System.out.println();
            System.out.println("=== Population actuelle : " + populationInitiale + " personnages ===\n");

            generationCreatures(populationInitiale, rand, collection);

            if (collection instanceof List) {
                // Calcul par taille de collection
                System.out.println("--- Calcul du total des points de vie (methode par taille) ---");
                startNs = System.nanoTime();
                startMs = System.currentTimeMillis();
                affichePointDeVieParTaille((List<Creature>) collection);
                endMs = System.currentTimeMillis();
                endNs = System.nanoTime();
                System.out.println("Temps dexecution : " + (endMs - startMs) + " millisecondes\n");
                System.out.println("Temps dexecution : " + (endNs - startNs) + " nanosecondes\n");
            }

            // Calcul par itérateurs
            System.out.println("--- Calcul du total des points de vie (methode par iterateurs) ---");
            startNs = System.nanoTime();
            startMs = System.currentTimeMillis();
            affichePointDeVieParIterateurs(collection);
            endMs = System.currentTimeMillis();
            endNs = System.nanoTime();
            System.out.println("Temps dexecution : " + (endMs - startMs) + " millisecondes\n");
            System.out.println("Temps dexecution : " + (endNs - startNs) + " nanosecondes\n");

            populationInitiale *= 10; // Augmentation exponentielle
        }
    }

    /**
     * Génère un ensemble de créatures aléatoires et les ajoute à une
     * collection.
     *
     * @param maxCreatures nombre maximum de créatures à générer
     * @param rand générateur de nombres aléatoires
     * @param collectionCreature collection où ajouter les créatures
     */
    private void generationCreatures(int maxCreatures, Random rand, Collection<Creature> collectionCreature) {
        for (int i = 0; i < maxCreatures; i++) {
            Point2D cPoint = positionAleatoire(rand);
            int randint = rand.nextInt(2);
            int id = i + 1;
            switch (randint) {
                case 0 ->
                    collectionCreature.add(GenerationP(id, cPoint));
                case 1 ->
                    collectionCreature.add(GenerationM(id, cPoint));
            }
        }
    }

    /**
     * Génère un ensemble d’objets aléatoires et les ajoute à une collection.
     *
     * @param maxObjets nombre maximum d’objets à générer
     * @param rand générateur de nombres aléatoires
     * @param collectionObjet collection où ajouter les objets
     */
    private void generationObjets(int maxObjets, Random rand, Collection<Objet> collectionObjet) {
        for (int i = 0; i < maxObjets; i++) {
            int id = i + 1;
            Point2D cPoint = positionAleatoire(rand);
            collectionObjet.add(GenerationO(id, cPoint));
        }
    }

    /**
     * Génère une position libre non encore occupée.
     *
     * @param rand générateur aléatoire
     * @return une position Point2D unique dans le monde
     */
    private Point2D positionAleatoire(Random rand) {
        Point2D p;
        do {
            int x = rand.nextInt(TAILLE_MONDE);
            int y = rand.nextInt(TAILLE_MONDE);
            p = new Point2D(x, y);
        } while (positionsOccupees.contains(p));
        positionsOccupees.add(p);
        return p;
    }

    /**
     * Effectue une simulation de plusieurs tours : à chaque tour, tous les
     * personnages se déplacent aléatoirement.
     *
     * @param nbTours nombre de tours à exécuter
     * @param moi
     */
    public void tourDeJour(int nbTours, Joueur moi) {
        for (int t = 0; t < nbTours; t++) {
            moi.analyzer(this.positionsOccupees, this.ListCreature, this.ListObjets);
            robin.deplaceAleatoire();
            guillaumeT.deplaceAleatoire();
            peon.deplaceAleatoire();
            bugs.deplaceAleatoire();

            afficheWorld(moi);
        }
    }

    /**
     * Retourne l’ensemble des positions actuellement occupées dans le monde.
     *
     * @return ensemble des positions occupées
     */
    public Set<Point2D> getPositionsOccupees() {
        return positionsOccupees;
    }

    /**
     * Affiche l’état complet du monde (personnages, monstres et objets
     * principaux).
     *
     * @param moi
     */
    public void afficheWorld(Joueur moi) {
        int taille = TAILLE_MONDE;
        char[][] monde = new char[taille][taille];

        // 1️⃣ Initialiser le monde avec des cases vides
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                monde[i][j] = '.'; 
            }
        }

        // 2️⃣ Placer toutes les créatures du monde
        if (ListCreature != null) {
            for (Creature c : ListCreature) {
                if (c != null && c.getPos() != null) {
                    placerDansMonde(monde, c, getSymbolePourCreature(c));
                }
            }
        }

        // 3️⃣ Placer tous les objets du monde
        if (ListObjets != null) {
            for (Objet o : ListObjets) {
                if (o != null && o.getPos() != null) {
                    placerDansMonde(monde, o, getSymbolePourObjet(o));
                }
            }
        }

        // 4️⃣ Placer le héros (joueur)
        placerDansMonde(monde, moi.hero, 'S');

        // 5️⃣ Afficher la zone visible selon la vision du héros
        afficherZoneVisible(monde, moi.hero);

        // 6️⃣ Afficher les statistiques du héros
        System.out.println();
        System.out.println("=== STATS DU HÉROS ===");
        moi.hero.affiche();
        System.out.println("=======================");
    }

    /**
     * Méthode pour placer un élément sur la carte
     */
    private void placerDansMonde(char[][] monde, ElementDeJeu element, char symbole) {
        int x = element.getPos().getX();
        int y = element.getPos().getY();

        if (x >= 0 && x < monde[0].length && y >= 0 && y < monde.length) {
            monde[y][x] = symbole;
        }
    }

    /**
     * Détermine le symbole d'une créature selon son type
     */
    private char getSymbolePourCreature(Creature c) {
        if (c instanceof Archer) {
            return 'A';
        }
        if (c instanceof Guerrier) {
            return 'G';
        }
        if (c instanceof Loup) {
            return 'W';
        }
        if (c instanceof Paysan) {
            return 'P';
        }
        if (c instanceof Monstre) {
            return 'M';
        }
        return 'C'; // par défaut : "C" pour "Créature"
    }

    /**
     * Détermine le symbole d’un objet selon son type
     */
    private char getSymbolePourObjet(Objet o) {
        if (o instanceof PotionSoin) {
            return 'O';
        }
        if (o instanceof Nourriture) {
            return 'N';
        }
        if (o instanceof Epee) {
            return 'E';
        }
        return '?'; // objet inconnu
    }

    /**
     * Affiche uniquement la zone visible autour du héros
     */
    private void afficherZoneVisible(char[][] monde, Creature hero) {
        int vision = hero.getDistanceVision();
        int xHero = hero.getPos().getX();
        int yHero = hero.getPos().getY();

        System.out.println("\n=== MONDE VISIBLE ===");

        for (int y = yHero - vision; y <= yHero + vision; y++) {
            for (int x = xHero - vision; x <= xHero + vision; x++) {
                if (y >= 0 && y < monde.length && x >= 0 && x < monde[0].length) {
                    System.out.print(monde[y][x] + " ");
                } else {
                    System.out.print("/"); // bordure hors du monde
                }
            }
            System.out.println();
        }
    }

    /**
     * Génère un personnage aléatoire (Archer, Paysan ou Guerrier).
     *
     * @param id identifiant du personnage
     * @param p position dans le monde
     * @return une instance de Personnage
     */
    private Personnage GenerationP(int id, Point2D p) {
        Random randomGen = new Random();
        int randint = randomGen.nextInt(3);
        return switch (randint) {
            case 0 ->
                new Archer("Archer " + id, true, 100, 80, 20, 80, 50, p, 2, 5, 10);
            case 1 ->
                new Paysan("Paysan " + id, true, 100, 100, 100, 100, 100, 100, p, 5);
            case 2 ->
                new Guerrier("Guerrier " + id, true, 100, 80, 20, 80, 50, p, 1, 3);
            default ->
                null;
        };
    }

    /**
     * Génère un monstre aléatoire (Lapin ou Loup).
     *
     * @param id identifiant du monstre
     * @param p position dans le monde
     * @return une instance de Monstre
     */
    private Monstre GenerationM(int id, Point2D p) {
        Random rand = new Random();
        int randint = rand.nextInt(2);
        return switch (randint) {
            case 0 ->
                new Lapin("Lapin " + id, true, 50, 20, 5, 10, 10, p, 1, 2, Monstre.Dangerosite.DOCILE);
            case 1 ->
                new Loup("Loup " + id, true, 80, 30, 15, 20, 30, p, 2, 4, Monstre.Dangerosite.DANGEREUX);
            default ->
                null;
        };
    }

    /**
     * Génère un objet aléatoire (PotionSoin ou Épée).
     *
     * @param id identifiant de l’objet
     * @param p position dans le monde
     * @return une instance d’Objet
     */
    private Objet GenerationO(int id, Point2D p) {
        Random rand = new Random();
        int randint = rand.nextInt(2);
        return switch (randint) {
            case 0 ->
                new PotionSoin("Potion " + id, "Potion magique", p, 20);
            case 1 ->
                new Epee("Epe " + id, "Epee en acier", p, 15, Epee.Etat.NONE);
            default ->
                null;
        };
    }

    /**
     * Affiche toutes les créatures et objets générés dans le monde, ainsi que
     * leur comptage total par type.
     *
     * @param creatures liste des créatures
     * @param objets liste des objets
     */
    private void afficheListes(List<Creature> creatures, List<Objet> objets) {
        Map<Class<?>, Integer> counter = new HashMap<>();
        System.out.println("\n===== LISTE DES CREATURES =====");
        for (Creature c : creatures) {
            if (c != null) {
                c.affiche();
                counter.put(c.getClass(), counter.getOrDefault(c.getClass(), 0) + 1);
            }
        }

        System.out.println("\n===== LISTE DES OBJETS =====");
        for (Objet o : objets) {
            if (o != null) {
                o.affiche();
                counter.put(o.getClass(), counter.getOrDefault(o.getClass(), 0) + 1);
            }
        }

        System.out.println("=============================\n");
        System.out.println("====== TOTAL =====");
        for (Map.Entry<Class<?>, Integer> entry : counter.entrySet()) {
            System.out.println(entry.getKey().getSimpleName() + ": " + entry.getValue());
        }
        System.out.println("==================");
    }

    /**
     * Calcule et affiche le total des points de vie d’une liste de créatures en
     * parcourant via des indices (méthode adaptée aux List).
     *
     * @param creatures liste de créatures
     */
    private void affichePointDeVieParTaille(List<Creature> creatures) {
        int ptVieTotal = 0;
        for (int i = 0; i < creatures.size(); i++) {
            ptVieTotal += creatures.get(i).getPtVie();
        }
        System.out.println("====== TOTAL POINTS DE VIE =====");
        System.out.println("Points de vie total : " + ptVieTotal);
        System.out.println("==================");
    }

    /**
     * Calcule et affiche le total des points de vie d’une collection de
     * créatures en utilisant des itérateurs/for-each.
     *
     * @param creatures collection de créatures
     */
    private void affichePointDeVieParIterateurs(Collection<Creature> creatures) {
        int ptVieTotal = 0;
        for (Creature c : creatures) {
            ptVieTotal += c.getPtVie();
        }
        System.out.println("====== TOTAL POINTS DE VIE =====");
        System.out.println("Points de vie total : " + ptVieTotal);
        System.out.println("==================");
    }

}
