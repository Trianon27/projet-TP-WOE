package org.centrale.objet.woe.projettp;

import java.sql.*;
import java.util.*;

/**
 * La classe {@code Personnage} représente un personnage du jeu.
 * <p>
 * Un personnage hérite des caractéristiques de {@link Creature} et peut se
 * déplacer, afficher ses informations, combattre, interagir avec des objets et
 * être copié.
 * </p>
 *
 * <p>
 * Cette classe sert de base pour des personnages spécialisés comme
 * {@link Archer}, {@link Guerrier} ou {@link Paysan}.
 * </p>
 *
 * @author
 * @version 3.0 (fusion complète)
 */
public class Personnage extends Creature implements Analyze {

    /**
     * Liste des effets actifs sur le personnage (potions, nourritures, etc.)
     */
    private List<ObjetUtilisable> effetsActifs = new ArrayList<>();

    /**
     * Liste des objets possédés (inventaire).
     */
    private List<Objet> inventaire = new ArrayList<>();

    // ================= CONSTRUCTEURS =================
    /**
     * Constructeur par défaut.
     */
    public Personnage() {
        super();
    }

    /**
     * Constructeur complet.
     */
    public Personnage(String nom, boolean etat, int pVie, int dAtt, int pPar, int paAtt,
            int paPar, int dMax, Point2D p, int distanceVision) {
        super(nom, etat, pVie, dAtt, pPar, paAtt, paPar, p, dMax, distanceVision);
    }

    /**
     * Constructeur par copie.
     */
    public Personnage(Personnage perso) {
        super(perso);
        this.effetsActifs = new ArrayList<>(perso.effetsActifs);
        this.inventaire = new ArrayList<>(perso.inventaire);
    }

    // ================= GETTERS / SETTERS =================
    public List<ObjetUtilisable> getEffetsActifs() {
        return effetsActifs;
    }

    public void setEffetsActifs(List<ObjetUtilisable> effetsActifs) {
        this.effetsActifs = effetsActifs;
    }

    public List<Objet> getInventaire() {
        return inventaire;
    }

    public void setInventaire(List<Objet> inventaire) {
        this.inventaire = inventaire;
    }

    // ================= MÉTHODES =================
    /**
     * Permet au personnage de ramasser un objet situé sur sa position et d’en
     * appliquer les effets.
     *
     * @param o L’objet à ramasser
     * @param positionWorld L’ensemble des positions occupées du monde
     */
    public void prendObjet(Objet o, Set<Point2D> positionWorld) {
        if (this.getPos().equals(o.getPosition())) {

            switch (o) {
                case PotionSoin potion -> {
                    this.setPtVie(this.getPtVie() + potion.getpVie());
                    System.out.println("💊 Potion consommée, vie actuelle : " + this.getPtVie());
                }
                case Epee epee -> {
                    this.setDegAtt(this.getDegAtt() + epee.getpAtt());
                    System.out.println("⚔️ Épée prise, attaque actuelle : " + this.getDegAtt());
                }
                case ObjetUtilisable utilisable -> {
                    utilisable.appliquerEffet(this);
                    this.effetsActifs.add(utilisable);
                    System.out.println("✨ Objet utilisable activé : " + o.getNom());
                }
                default -> {
                    // Aucun effet particulier
                }
            }

            // Retire l’objet du monde
            positionWorld.remove(o.getPosition());
        }
    }

    /**
     * Met à jour les effets actifs (décrémentation et suppression si expirés).
     */
    public void mettreAJourEffets() {
        Iterator<ObjetUtilisable> it = effetsActifs.iterator();
        while (it.hasNext()) {
            ObjetUtilisable effet = it.next();
           Nourriture n = (Nourriture) effet;

            // Decrementamos la duración
            effet.decrementerDuree();

            // Si sigue activo, mostramos cuánto tiempo le queda
            if (effet.estActif()) {
                  
                    System.out.println("⏳ Effet de '" + n.getNom() + "' restant : " + n.getCoolDown()+ " tours.");
               
            } else {
                effet.retirerEffet(this);
                it.remove();
                System.out.println("✅ Effet terminé et retiré : " + n.getNom());
            }
        }
    }

    /**
     * Analyse le comportement automatique d’un PNJ (déplacement ou combat).
     *
     * @param positionWorld
     * @param creatures
     */
    @Override
    public void analyzer(Set<Point2D> positionWorld, List<Creature> creatures,
            List<Objet> objets, int tailleMonde) {

        if (this instanceof Paysan) {
            this.deplacementAleatoire(positionWorld, tailleMonde);
            return;
        }

        Random rand = new Random();
        Point2D posPersonnage = this.getPos();

        // Liste des cibles adjacentes
        List<Creature> ciblesAdjacentes = new ArrayList<>();
        for (Creature c : creatures) {
            if (c != this) {
                double dx = Math.abs(c.getPos().getX() - posPersonnage.getX());
                double dy = Math.abs(c.getPos().getY() - posPersonnage.getY());
                if (dx <= this.getDistAttMax() && dy <= this.getDistAttMax() && !(dx == 0 && dy == 0)) {
                    ciblesAdjacentes.add(c);
                }
            }
        }

        int action = rand.nextInt(3);
        switch (action) {
            case 0 ->
                this.deplacementAleatoire(positionWorld, tailleMonde);
            case 1 -> {
                if (!ciblesAdjacentes.isEmpty()) {
                    Creature cible = ciblesAdjacentes.get(rand.nextInt(ciblesAdjacentes.size()));
                    System.out.println(this.getNom() + " attaque " + cible.getNom() + " !");
                    if (this instanceof Combattant combattant) {
                        combattant.combattre(cible, positionWorld, creatures);
                    }
                } else {
                    System.out.println(this.getNom() + " veut attaquer mais il n'y a personne à proximité.");
                }
            }
            default -> {
                // Ne rien faire
            }
        }
    }

    // ===================== SAUVEGARDE EN BASE =====================
    /**
     * Sauvegarde le personnage dans la base de données (table Personnage), puis
     * appelle la méthode spécifique selon le type concret (Archer, Guerrier,
     * etc.).
     *
     * @param conn connexion JDBC ouverte
     * @param idPartie identifiant de la partie à laquelle appartient ce
     * personnage
     */
    public void saveToDB(Connection conn, int idPartie) {
        String sql = """
            INSERT INTO Personnage (
                type_personnage, nom, ptVie, degAtt, ptPar,
                pourcentageAtt, pourcentagePar, distAttMax,
                distVue, posX, posY, id_partie
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            RETURNING id_personnage
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, this.getClass().getSimpleName());
            ps.setString(2, this.getNom());
            ps.setInt(3, this.getPtVie());
            ps.setInt(4, this.getDegAtt());
            ps.setInt(5, this.getPtPar());
            ps.setInt(6, this.getPageAtt());
            ps.setInt(7, this.getPagePar());
            ps.setInt(8, this.getDistAttMax());
            ps.setInt(9, this.getDistanceVision());
            ps.setInt(10, this.getPos().getX());
            ps.setInt(11, this.getPos().getY());
            ps.setInt(12, idPartie);

            ResultSet rs = ps.executeQuery();
            rs.next();
            int idPersonnage = rs.getInt("id_personnage");

            // Appel de la méthode spécifique selon le type concret
            if (this instanceof Archer archer) {
                archer.saveArcher(conn, idPersonnage);
            } else if (this instanceof Guerrier guerrier) {
                guerrier.saveGuerrier(conn, idPersonnage);
            } else if (this instanceof Paysan paysan) {
                paysan.savePaysan(conn, idPersonnage);
            }

            System.out.println("✅ " + this.getClass().getSimpleName()
                    + " inséré en base (ID " + idPersonnage + ")");
        } catch (SQLException e) {
            System.err.println("❌ Erreur Personnage.saveToDB : " + e.getMessage());
        }
    }
}
