package org.centrale.objet.woe.projettp;

/**
 * Classe représentant une Nourriture pouvant être utilisée par un Personnage.
 * Elle applique un effet temporaire (bonus/malus) sur certaines
 * caractéristiques du personnage pendant un certain nombre de tours de jeu.
 *
 * @author srodr
 */
public class Nourriture extends Objet implements ObjetUtilisable {

    private int coolDown; // Durée restante de l'effet en tours

    public enum Nourritures {
        ALCOHOOL,
        LEGUMBRE,
        BOISSONRICHE,
        POMMEDOR
    }

    private Nourritures typeNourriture;

    // --- Constructeurs ---
    public Nourriture(int coolDown, Nourritures typeNourriture) {
        this.coolDown = coolDown;
        this.typeNourriture = typeNourriture;
    }

    public Nourriture(int coolDown, Nourritures typeNourriture, String nom, String description, Point2D position) {
        super(nom, description, position);
        this.coolDown = coolDown;
        this.typeNourriture = typeNourriture;
    }

    public Nourriture(int coolDown, Nourritures typeNourriture, Objet o) {
        super(o);
        this.coolDown = coolDown;
        this.typeNourriture = typeNourriture;
    }

    // --- Getters / Setters ---
    public int getCoolDown() {
        return coolDown;
    }

    public Nourritures getTypeNourriture() {
        return typeNourriture;
    }

    public void setCoolDown(int coolDown) {
        this.coolDown = coolDown;
    }

    public void setTypeNourriture(Nourritures typeNourriture) {
        this.typeNourriture = typeNourriture;
    }

    @Override
    public String getNom() {
        return nom;
    }

    @Override
    public Point2D getPos() {
        return pos;
    }

    @Override
    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public void setPos(Point2D pos) {
        this.pos = pos;
    }

    // --- Méthode principale : applique l'effet au personnage ---
    /**
     *
     * @param p
     */
    @Override
    public void appliquerEffet(Personnage p) {
        switch (this.typeNourriture) {
            case ALCOHOOL -> {
                // L'alcool diminue la probabilité d'attaque de 20 points
                // et augmente la probabilité de parade de 20 points (sans dépasser 100)
                if (p.getPageAtt() > 20) {
                    p.setPageAtt(p.getPageAtt() - 20);
                } else {
                    p.setPageAtt(0);
                }

                if (p.getPagePar() > 80) {
                    p.setPagePar(100);
                } else {
                    p.setPagePar(p.getPagePar() + 20);
                }
            }

            case LEGUMBRE -> {
                // Le légume augmente la probabilité d'attaque et de parade de 30 points
                if (p.getPageAtt() <= 70) {
                    p.setPageAtt(p.getPageAtt() + 30);
                } else {
                    p.setPageAtt(100);
                }

                if (p.getPagePar() <= 70) {
                    p.setPagePar(p.getPagePar() + 30);
                } else {
                    p.setPagePar(100);
                }
            }

            case BOISSONRICHE -> {
                // La boisson riche augmente le mouvement (distance maximale d’attaque) de 2
                // mais diminue la probabilité de parade de 20 points
                //p.setdMax(p.getdMax() + 2);

                if (p.getPagePar() >= 20) {
                    p.setPagePar(p.getPagePar() - 20);
                } else {
                    p.setPagePar(0);
                }
            }

            case POMMEDOR -> {
                // La pomme d’or augmente le mouvement de 2,
                // et la probabilité d’attaque et de parade de 40 points
                //p.setdMax(p.getdMax() + 2);

                if (p.getPageAtt() <= 60) {
                    p.setPageAtt(p.getPageAtt() + 40);
                } else {
                    p.setPageAtt(100);
                }

                if (p.getPagePar() <= 60) {
                    p.setPagePar(p.getPagePar() + 40);
                } else {
                    p.setPagePar(100);
                }
            }
        }
    }

    // --- Méthode pour retirer l'effet lorsque la durée est écoulée ---
    @Override
    public void retirerEffet(Personnage p) {
        switch (this.typeNourriture) {
            case ALCOHOOL -> {
                // On annule les modifications précédentes
                p.setPageAtt(p.getPageAtt() + 20);
                p.setPagePar(p.getPagePar() - 20);
            }

            case LEGUMBRE -> {
                p.setPageAtt(p.getPageAtt() - 30);
                p.setPagePar(p.getPagePar() - 30);
            }

            case BOISSONRICHE -> {
                //p.setdMax(p.getdMax() - 2);
                p.setPagePar(p.getPagePar() + 20);
            }

            case POMMEDOR -> {
                //p.setdMax(p.getdMax() - 2);
                p.setPageAtt(p.getPageAtt() - 40);
                p.setPagePar(p.getPagePar() - 40);
            }
        }

        // On s'assure que les valeurs restent dans les limites [0,100]
        if (p.getPageAtt() < 0) {
            p.setPageAtt(0);
        }
        if (p.getPagePar() < 0) {
            p.setPagePar(0);
        }
        if (p.getPageAtt() > 100) {
            p.setPageAtt(100);
        }
        if (p.getPagePar() > 100) {
            p.setPagePar(100);
        }
    }

    // --- Méthode pour décrémenter la durée de l'effet à chaque tour ---
    @Override
    public void decrementerDuree() {
        if (coolDown > 0) {
            coolDown--;
        }
    }

    // --- Indique si l'effet est encore actif ---
    @Override
    public boolean estActif() {
        return coolDown > 0;
    }
}
