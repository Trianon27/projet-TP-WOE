package org.centrale.objet.woe.projettp;

public abstract class ElementDeJeu {

    protected String nom;

    protected Point2D pos;

    public ElementDeJeu() {
        this.nom = "None";
        this.pos = new Point2D(0, 0);
    }

    public ElementDeJeu(String nom, Point2D pos) {
        this.nom = nom;
        this.pos = pos;
    }

    public ElementDeJeu(ElementDeJeu E) {
        this.nom = E.nom;
        this.pos = E.pos;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Point2D getPos() {
        return pos;
    }

    public void setPos(Point2D pos) {
        this.pos = pos;
    }

    public abstract void affiche();
}
