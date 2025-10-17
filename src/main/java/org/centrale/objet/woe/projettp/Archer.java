package org.centrale.objet.woe.projettp;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Archer extends Personnage implements Combattant {

    private int nbFleches;

    public Archer(String n, boolean etat, int pVie, int dAtt, int pPar, int paAtt, int paPar, Point2D p, int dMax, int distanceVision, int nbFleches) {
        super(n, etat, pVie, dAtt, pPar, paAtt, paPar, dMax, p, distanceVision);
        this.nbFleches = nbFleches;
    }

    public Archer(Archer a) {
        super(a);
        this.nbFleches = a.getNbFleches();
    }

    public Archer() {
        super();
        this.nbFleches = 0;
    }

    public int getNbFleches() {
        return nbFleches;
    }

    public void setNbFleches(int nbFleches) {
        this.nbFleches = nbFleches;
    }

    @Override
    public void combattre(Creature c, Set<Point2D> positionWorld, List<Creature> creatures) {
        if (!this.isEtat() || !c.isEtat()) {
            return;
        }
        if (this.getPos().distance(c.getPos()) <= (Math.sqrt(2))) {
            System.out.println();
            System.out.println("=============COMBAT CORPS A CORPS=============");
            System.out.println("Attaquant : " + this.getNom());
            this.setDegAtt(10);
            System.out.println("Pt attaque corp a corp : " + this.getDegAtt());
            System.out.println("Defenseur : " + c.getNom());
            System.out.println("==========================");
            System.out.println();
            if (jeuDeAtt()) {
                System.out.println("Attaque reussie");
                if (jeuDeDe(c)) {
                    System.out.println("Defense reussie");
                    if (c.getPtPar() <= 0) {
                        int e_vie = c.getPtVie() - this.getDegAtt();
                        c.setPtVie(e_vie);
                    } else {
                        int e_def = c.getPtPar() - this.getDegAtt();
                        if (e_def <= 0) {
                            c.setPtPar(0);
                            int e_vie = c.getPtVie() + e_def;
                            c.setPtVie(e_vie);
                        } else {
                            c.setPtPar(e_def);
                        }
                    }
                } else {
                    int e_vie = c.getPtVie() - this.getDegAtt();
                    c.setPtVie(e_vie);
                }
            } else {
                System.out.println("Attaque echoue");
            }
        } else if (this.getPos().distance(c.getPos()) <= (this.getDistAttMax() * Math.sqrt(2)) && this.getPos().distance(c.getPos()) > (Math.sqrt(2)) && this.getNbFleches() > 0) {
            System.out.println();
            System.out.println("=============COMBAT A DISTANCE=============");
            System.out.println("Attaquant : " + this.getNom());
            System.out.println("Defenseur : " + c.getNom());
            System.out.println("==========================");
            System.out.println();
            if (jeuDeAtt()) {
                System.out.println("Attaque reussie");
                int e_vie = c.getPtVie() - this.getDegAtt();
                c.setPtVie(e_vie);
            } else {
                System.out.println("Attaque ratee");
            }
            int arrows = this.getNbFleches() - 1;
            this.setNbFleches(arrows);
        }
        System.out.println();
        System.out.println("=============RESULTATS=============");
        System.out.println("Points de vie de " + c.getNom() + " : " + c.getPtVie());
        System.out.println("Points de defense de " + c.getNom() + " : " + c.getPtPar());
        System.out.println("Fleches restantes : " + this.getNbFleches());
        System.out.println("==========================");
        System.out.println();
        if (c.getPtVie() <= 0) {
            System.out.println();
            System.out.println("******* " + c.getNom() + " a ete vaincu. *******");
            System.out.println();
            c.mourir(positionWorld, creatures);
        }
    }

    public boolean jeuDeAtt() {
        Random n = new Random();
        return n.nextInt(100) < this.getPageAtt();
    }

    public boolean jeuDeDe(Creature c) {
        Random n = new Random();
        return n.nextInt(100) < c.getPagePar();
    }

    @Override
    public void affiche() {
        super.affiche();
        System.out.println("Le nbr de fleches : " + this.nbFleches);
    }

    public void saveArcher(Connection conn, int idPersonnage) {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO Archer (id_personnage, nbFleches) VALUES (?, ?)")) {
            ps.setInt(1, idPersonnage);
            ps.setInt(2, this.getNbFleches());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur Archer.saveArcher : " + e.getMessage());
        }
    }

    public void combattre(Creature c, Set<Point2D> positionWorld) {
    }
}
