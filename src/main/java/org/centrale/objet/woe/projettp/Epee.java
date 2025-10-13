/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.objet.woe.projettp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/**
 * La classe {@code Epee} représente une épée dans le jeu.
 * <p>
 * Une épée hérite de {@link Objet} et possède des points d’attaque
 * ainsi qu’un effet secondaire optionnel défini par l’énumération {@link Etat}.
 * </p>
 * 
 * Les effets possibles sont :
 * <ul>
 *   <li>{@code ATT} : bonus d’attaque</li>
 *   <li>{@code VEL} : bonus de vitesse</li>
 *   <li>{@code NONE} : aucun effet</li>
 * </ul>
 * 
 * @author srodr
 */
public class Epee extends Objet {

    /**
     * Enumération pour représenter les effets secondaires possibles de l’épée.
     */
    public enum Etat {
        /** Bonus d’attaque */
        ATT,    
        /** Aucun effet */
        NONE,   
        /** Bonus de vitesse */
        VEL     
    }

    /** Points d’attaque supplémentaires conférés par l’épée */
    private int pAtt;
    
    /** Effet secondaire actuel de l’épée */
    private Etat effet;
    
    // ================= CONSTRUCTEURS =================
    
    /**
     * Constructeur par défaut.
     * Initialise les attributs hérités et définit l’effet par défaut à {@code NONE}.
     */
    public Epee() {
        super();
        this.pAtt = 10;
        this.effet = Etat.NONE;
    }

    /**
     * Constructeur complet.
     * 
     * @param nom Nom de l’épée
     * @param description Description de l’épée
     * @param p
     * @param pAtt Points d’attaque supplémentaires
     * @param effet Effet secondaire appliqué
     */
    public Epee(String nom, String description, Point2D p, int pAtt, Etat effet) {
        super(nom, description,p);
        this.effet = effet;
        this.pAtt = pAtt; 
    }

    /**
     * Constructeur par copie.
     * 
     * @param e Épée à copier
     */
    public Epee(Epee e) {
        super(e);
        this.effet = e.effet;
        this.pAtt = e.pAtt;
    }

    // ================= GETTERS / SETTERS =================

    /**
     * Retourne l’effet secondaire actuel de l’épée.
     * 
     * @return l’effet secondaire de type {@link Etat}
     */
    public Etat getEffet() {
        return effet;
    }

    /**
     * Définit un nouvel effet secondaire pour l’épée.
     * 
     * @param effet nouvel effet de type {@link Etat}
     */
    public void setEffet(Etat effet) {
        this.effet = effet;
    }

    /**
     * Retourne les points d’attaque de l’épée.
     * 
     * @return points d’attaque
     */
    public int getpAtt() {
        return pAtt;
    }

    /**
     * Définit les points d’attaque de l’épée.
     * 
     * @param pAtt nouveaux points d’attaque
     */
    public void setpAtt(int pAtt) {
        this.pAtt = pAtt;
    }
    


public void saveToDB(Connection conn, int idPartie) {
    try (PreparedStatement ps = conn.prepareStatement("""
        INSERT INTO Epee (nom, description, posX, posY, id_partie)
        VALUES (?, ?, ?, ?, ?)
    """)) {
        ps.setString(1, this.getNom());
        ps.setString(2, this.getDescription());
        ps.setInt(3, this.getPos().getX());
        ps.setInt(4, this.getPos().getY());
        ps.setInt(5, idPartie);
        ps.executeUpdate();
    } catch (SQLException e) {
        System.err.println("Erreur Epee.saveToDB : " + e.getMessage());
    }
}

}

