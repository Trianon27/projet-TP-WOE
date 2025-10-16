package org.centrale.objet.woe.projettp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * La classe {@code NuageToxique} représente un élément dangereux du monde du jeu
 * générant des dégâts continus dans une zone déterminée pendant un certain nombre de tours.
 * <p>
 * Un nuage toxique est un objet environnemental (héritant de {@link Objet})
 * qui inflige un certain nombre de points de dégâts à toute créature
 * se trouvant dans sa zone d’effet, et disparaît après une durée limitée.
 * </p>
 *
 * <h3>Caractéristiques principales :</h3>
 * <ul>
 *   <li>Dégâts infligés par tour ({@code degatParTour}).</li>
 *   <li>Taille de la zone affectée (doit être impaire pour qu’un centre existe).</li>
 *   <li>Durée de vie du nuage en nombre de tours ({@code duree}).</li>
 * </ul>
 *
 * @author
 * @version 3.0 (fusion complète)
 */
public class NuageToxique extends Objet implements Deplacable, Combattant, Analyze {

    // ===================== ATTRIBUTS =====================

    /** Dégâts infligés par tour aux créatures présentes dans la zone du nuage. */
    private int degatParTour;

    /** Taille du nuage (zone carrée taille × taille). */
    private int taille;

    /** Durée de vie restante (en tours). */
    private int duree;

    // ===================== CONSTRUCTEURS =====================

    /** Constructeur par défaut. */
    public NuageToxique() {
        super();
        this.degatParTour = 5;
        this.taille = 3;
        this.duree = 8;
    }

    /** Constructeur complet. */
    public NuageToxique(String nom, String description, Point2D p, int degat, int taille, int duree) {
        super(nom, description, p);
        this.degatParTour = degat;
        this.taille = taille;
        this.duree = duree;
    }

    /** Constructeur par copie. */
    public NuageToxique(NuageToxique autreNuage) {
        super(autreNuage);
        this.degatParTour = autreNuage.degatParTour;
        this.taille = autreNuage.taille;
        this.duree = autreNuage.duree;
    }

    // ===================== GETTERS / SETTERS =====================

    public int getDegatParTour() {
        return degatParTour;
    }

    public void setDegatParTour(int degatParTour) {
        this.degatParTour = degatParTour;
    }

    public int getTaille() {
        return taille;
    }

    public void setTaille(int taille) {
        this.taille = taille;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    // ===================== MÉCANIQUES DE JEU =====================

    /** Réduit la durée du nuage d’un tour. */
    public void decrementerDuree() {
        if (this.duree > 0) this.duree--;
    }

    /** Indique si le nuage est encore actif. */
    public boolean estActif() {
        return this.duree > 0;
    }

    /** Déplace le centre du nuage. */
    @Override
    public void deplacer(int dx, int dy) {
        this.pos.translate(dx, dy);
    }

    /**
     * Déplacement aléatoire limité à la taille du monde.
     */
    @Override
    public void deplacementAleatoire(Set<Point2D> positionsOccupees, int tailleMonde) {
        Random rand = new Random();
        final int MAX_ESSAIS = 8;
        Point2D anciennePos = this.pos;

        for (int essais = 0; essais < MAX_ESSAIS; essais++) {
            int dx = rand.nextInt(3) - 1;
            int dy = rand.nextInt(3) - 1;
            if (dx == 0 && dy == 0) continue;

            int nx = anciennePos.getX() + dx;
            int ny = anciennePos.getY() + dy;
            Point2D nouvellePos = new Point2D(nx, ny);

            boolean dansMonde = nx >= 0 && nx < tailleMonde && ny >= 0 && ny < tailleMonde;
            if (!dansMonde) continue;

            boolean occupee = positionsOccupees.stream().anyMatch(p -> p.equals(nouvellePos));
            if (!occupee) {
                positionsOccupees.remove(anciennePos);
                positionsOccupees.add(nouvellePos);
                this.pos = nouvellePos;
                System.out.println(this.getNom() + " se déplace en (" + nx + ", " + ny + ").");
                return;
            }
        }
    }

    /**
     * Inflige des dégâts à une créature si elle se trouve dans la zone d’effet du nuage.
     */
    @Override
    public void combattre(Creature c, Set<Point2D> positionWorld, List<Creature> creatures) {
        if (!this.estActif()) return;

        int demiTaille = taille / 2;
        int xMin = this.pos.getX() - demiTaille;
        int xMax = this.pos.getX() + demiTaille;
        int yMin = this.pos.getY() - demiTaille;
        int yMax = this.pos.getY() + demiTaille;

        int xC = c.getPos().getX();
        int yC = c.getPos().getY();

        if (c.isEtat() && xC >= xMin && xC <= xMax && yC >= yMin && yC <= yMax) {
            System.out.println("☣️ Le nuage toxique \"" + this.getNom() + "\" affecte " + c.getNom() + " !");
            System.out.println("Dégâts infligés : " + degatParTour);

            c.setPtVie(c.getPtVie() - degatParTour);
            if (c.getPtVie() <= 0) {
                System.out.println("💀 " + c.getNom() + " a succombé au nuage toxique !");
                c.mourir(positionWorld, creatures);
            } else {
                System.out.println("❤️ Il reste " + c.getPtVie() + " PV à " + c.getNom() + ".");
            }
        }
    }

    /**
     * Comportement automatique du nuage à chaque tour (appelé par World.analyzer()).
     */
    public void analyzer(Set<Point2D> positionWorld, List<Creature> creatures, List<Objet> objets, int tailleMonde) {
        if (!this.estActif()) return;

        // Déplacement aléatoire léger
        this.deplacementAleatoire(positionWorld, tailleMonde);

        // Affecter toutes les créatures dans la zone
        for (Creature c : creatures) {
            this.combattre(c, positionWorld, creatures);
        }

        // Diminuer la durée de vie du nuage
        this.decrementerDuree();
    }

    // ===================== SAUVEGARDE SQL =====================

    /**
     * Enregistre le nuage toxique dans la base de données.
     */
    public void saveToDB(Connection conn, int idPartie) {
        String sql = """
            INSERT INTO NuageToxique (nom, description, posX, posY, degAtt, duree, tailleZone, id_partie)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, this.getNom());
            ps.setString(2, this.getDescription());
            ps.setInt(3, this.getPos().getX());
            ps.setInt(4, this.getPos().getY());
            ps.setInt(5, this.getDegatParTour());
            ps.setInt(6, this.getDuree());
            ps.setInt(7, this.getTaille());
            ps.setInt(8, idPartie);
            ps.executeUpdate();
            System.out.println("✅ NuageToxique inséré en base : " + this.getNom());
        } catch (SQLException e) {
            System.err.println("Erreur NuageToxique.saveToDB : " + e.getMessage());
        }
    }
}
