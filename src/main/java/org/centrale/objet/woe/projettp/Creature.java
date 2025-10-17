package org.centrale.objet.woe.projettp;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class Creature extends ElementDeJeu implements Deplacable {

    protected boolean etat;

    protected int ptVie;

    protected int degAtt;

    protected int ptPar;

    protected int pageAtt;

    protected int pagePar;

    protected int distAttMax;

    protected int distanceVision;

    public Creature(String nom, boolean etat, int pVie, int dAtt, int pPar, int paAtt, int paPar, Point2D p, int distAttMax, int distanceVision) {
        super(nom, p);
        this.etat = etat;
        this.ptVie = pVie;
        this.degAtt = dAtt;
        this.ptPar = pPar;
        this.pageAtt = paAtt;
        this.pagePar = paPar;
        this.distAttMax = distAttMax;
        this.distanceVision = distanceVision;
    }

    public Creature(Creature c) {
        super(c);
        this.etat = c.etat;
        this.ptVie = c.ptVie;
        this.degAtt = c.degAtt;
        this.ptPar = c.ptPar;
        this.pageAtt = c.pageAtt;
        this.pagePar = c.pagePar;
        this.distAttMax = c.distAttMax;
        this.distanceVision = c.distanceVision;
    }

    public Creature() {
        super();
        this.etat = true;
        this.ptVie = 50;
        this.degAtt = 5;
        this.ptPar = 2;
        this.pageAtt = 50;
        this.pagePar = 30;
        this.distAttMax = 1;
        this.distanceVision = 1;
    }

    public int getPtVie() {
        return ptVie;
    }

    public void setPtVie(int ptVie) {
        this.ptVie = ptVie;
    }

    public int getDegAtt() {
        return degAtt;
    }

    public void setDegAtt(int degAtt) {
        this.degAtt = degAtt;
    }

    public int getPtPar() {
        return ptPar;
    }

    public void setPtPar(int ptPar) {
        this.ptPar = ptPar;
    }

    public int getPageAtt() {
        return pageAtt;
    }

    public void setPageAtt(int pageAtt) {
        this.pageAtt = pageAtt;
    }

    public int getPagePar() {
        return pagePar;
    }

    public void setPagePar(int pagePar) {
        this.pagePar = pagePar;
    }

    public int getDistanceVision() {
        return distanceVision;
    }

    public void setDistanceVision(int distanceVision) {
        this.distanceVision = distanceVision;
    }

    public int getDistAttMax() {
        return distAttMax;
    }

    public void setDistAttMax(int distAttMax) {
        this.distAttMax = distAttMax;
    }

    public boolean isEtat() {
        return etat;
    }

    public void setEtat(boolean etat) {
        this.etat = etat;
    }

    @Override
    public void deplacer(int dx, int dy) {
        this.pos.translate(dx, dy);
    }

    @Override
    public void deplacementAleatoire(Set<Point2D> positionsOccupees, int tailleMonde) {
        if (!this.etat)
            return;
        Random rand = new Random();
        final int MAX_ESSAIS = 9;
        int essais = 0;
        Point2D anciennePos = this.pos;
        while (essais < MAX_ESSAIS) {
            int dx = rand.nextInt(3) - 1;
            int dy = rand.nextInt(3) - 1;
            if (dx == 0 && dy == 0) {
                essais++;
                continue;
            }
            int nx = anciennePos.getX() + dx;
            int ny = anciennePos.getY() + dy;
            Point2D nouvellePos = new Point2D(nx, ny);
            boolean dansMonde = nx >= 0 && nx < tailleMonde && ny >= 0 && ny < tailleMonde;
            if (!dansMonde) {
                essais++;
                continue;
            }
            boolean occupee = positionsOccupees.stream().anyMatch( p->p.equals(nouvellePos));
            if (!occupee) {
                positionsOccupees.remove(anciennePos);
                positionsOccupees.add(nouvellePos);
                this.pos = nouvellePos;
                return;
            }
            essais++;
        }
    }

    public void mourir(Set<Point2D> positionWorld, List<Creature> creatures) {
        this.etat = false;
        positionWorld.remove(this.pos);
        creatures.remove(this);
    }

    @Override
    public void affiche() {
        System.out.println();
        System.out.println("Nom : " + nom);
        System.out.println("Points de vie : " + ptVie);
        System.out.println("Degats d’attaque : " + degAtt);
        System.out.println("Points de parade : " + ptPar);
        System.out.println("Pourcentage d’attaque : " + pageAtt);
        System.out.println("Pourcentage de parade : " + pagePar);
        System.out.println("Distance d’attaque : " + distAttMax);
        System.out.println("Position : (" + pos.getX() + ", " + pos.getY() + ")");
    }

    public void deplaceAleatoire() {
    }

    public void mourir(Set<Point2D> positionWorld) {
    }
}
