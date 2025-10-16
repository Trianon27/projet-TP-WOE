package org.centrale.objet.woe.projettp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

/**
 * Classe représentant une Nourriture pouvant être utilisée par un Personnage.
 * <p>
 * Elle applique un effet temporaire (bonus/malus) sur certaines
 * caractéristiques du personnage pendant un certain nombre de tours.
 * </p>
 *
 * <p>
 * Chaque type de nourriture a un effet et une durée différente :
 * <ul>
 *   <li>🍺 ALCOHOOL — réduit l’attaque, augmente la parade</li>
 *   <li>🥦 LEGUMBRE — augmente attaque + parade</li>
 *   <li>🥤 BOISSONRICHE — augmente la distance d’attaque mais diminue la parade</li>
 *   <li>🍎 POMMEDOR — augmente fortement attaque + parade</li>
 * </ul>
 * </p>
 *
 * <p>La classe gère aussi la persistance en base de données via {@link #saveToDB(Connection, int)}.</p>
 *
 * @author
 * @version 3.0 (fusion complète)
 */
public class Nourriture extends Objet implements ObjetUtilisable {

    // ================= ATTRIBUTS =================

    /** Durée restante de l'effet en tours. */
    private int coolDown;

    /** Type de nourriture. */
    public enum Nourritures {
        ALCOHOOL,
        LEGUMBRE,
        BOISSONRICHE,
        POMMEDOR
    }

    private Nourritures typeNourriture;

    // ================= CONSTRUCTEURS =================

    /** Constructeur simple avec génération automatique de durée aléatoire. */
    public Nourriture(Nourritures typeNourriture) {
        super();
        this.typeNourriture = typeNourriture;
        this.coolDown = genererCoolDownAleatoire(typeNourriture);
    }

    /** Constructeur complet (avec position et description). */
    public Nourriture(Nourritures typeNourriture, String nom, String description, Point2D position) {
        super(nom, description, position);
        this.typeNourriture = typeNourriture;
        this.coolDown = genererCoolDownAleatoire(typeNourriture);
    }

    /** Constructeur par copie. */
    public Nourriture(Nourritures typeNourriture, Objet o) {
        super(o);
        this.typeNourriture = typeNourriture;
        this.coolDown = genererCoolDownAleatoire(typeNourriture);
    }

    /** Constructeur direct avec cooldown défini (utile pour chargement BDD). */
    public Nourriture(int coolDown, Nourritures typeNourriture, String nom, String description, Point2D position) {
        super(nom, description, position);
        this.typeNourriture = typeNourriture;
        this.coolDown = coolDown;
    }

    // ================= GÉNÉRATION ALÉATOIRE =================

    private int genererCoolDownAleatoire(Nourritures type) {
        Random r = new Random();
        return switch (type) {
            case ALCOHOOL -> 2 + r.nextInt(2);     // entre 2 et 3 tours
            case LEGUMBRE -> 3 + r.nextInt(3);     // entre 3 et 5 tours
            case BOISSONRICHE -> 1 + r.nextInt(2); // entre 1 et 2 tours
            case POMMEDOR -> 4 + r.nextInt(3);     // entre 4 et 6 tours
        };
    }

    // ================= GETTERS / SETTERS =================

    public int getCoolDown() {
        return coolDown;
    }

    public void setCoolDown(int coolDown) {
        this.coolDown = coolDown;
    }

    public Nourritures getTypeNourriture() {
        return typeNourriture;
    }

    public void setTypeNourriture(Nourritures typeNourriture) {
        this.typeNourriture = typeNourriture;
    }

    @Override
    public String getNom() {
        return nom;
    }

    @Override
    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public Point2D getPos() {
        return pos;
    }

    @Override
    public void setPos(Point2D pos) {
        this.pos = pos;
    }

    // ================= MÉCANIQUE D’EFFET =================

    @Override
    public void appliquerEffet(Personnage p) {
        switch (this.typeNourriture) {
            case ALCOHOOL -> {
                p.setPageAtt(Math.max(0, p.getPageAtt() - 20));
                p.setPagePar(Math.min(100, p.getPagePar() + 20));
            }
            case LEGUMBRE -> {
                p.setPageAtt(Math.min(100, p.getPageAtt() + 30));
                p.setPagePar(Math.min(100, p.getPagePar() + 30));
            }
            case BOISSONRICHE -> {
                p.setPagePar(Math.max(0, p.getPagePar() - 20));
            }
            case POMMEDOR -> {
                p.setPageAtt(Math.min(100, p.getPageAtt() + 40));
                p.setPagePar(Math.min(100, p.getPagePar() + 40));
            }
        }
    }

    @Override
    public void retirerEffet(Personnage p) {
        switch (this.typeNourriture) {
            case ALCOHOOL -> {
                p.setPageAtt(p.getPageAtt() + 20);
                p.setPagePar(p.getPagePar() - 20);
            }
            case LEGUMBRE -> {
                p.setPageAtt(p.getPageAtt() - 30);
                p.setPagePar(p.getPagePar() - 30);
            }
            case BOISSONRICHE -> {
                p.setPagePar(p.getPagePar() + 20);
            }
            case POMMEDOR -> {
                p.setPageAtt(p.getPageAtt() - 40);
                p.setPagePar(p.getPagePar() - 40);
            }
        }

        // 🔒 Clamp entre 0 et 100
        p.setPageAtt(Math.max(0, Math.min(100, p.getPageAtt())));
        p.setPagePar(Math.max(0, Math.min(100, p.getPagePar())));
    }

    @Override
    public void decrementerDuree() {
        if (coolDown > 0) coolDown--;
    }

    @Override
    public boolean estActif() {
        return coolDown > 0;
    }

    // ================= SAUVEGARDE EN BASE =================

    /**
     * Sauvegarde la nourriture dans la base de données, liée à une partie.
     * 
     * @param conn connexion JDBC ouverte
     * @param idPartie identifiant de la partie
     */
    public void saveToDB(Connection conn, int idPartie) {
        String sql = """
            INSERT INTO Nourriture (nom, description, posX, posY, id_partie)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, this.getNom());
            ps.setString(2, this.getDescription());
            ps.setInt(3, this.getPos().getX());
            ps.setInt(4, this.getPos().getY());
            ps.setInt(5, idPartie);
            ps.executeUpdate();
            System.out.println("✅ Nourriture insérée en base : " + this.getNom());
        } catch (SQLException e) {
            System.err.println("Erreur Nourriture.saveToDB : " + e.getMessage());
        }
    }
}
