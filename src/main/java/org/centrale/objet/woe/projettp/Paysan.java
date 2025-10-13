/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.objet.woe.projettp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * La classe {@code Paysan} représente un personnage paysan dans le jeu.
 * <p>
 * Hérite de toutes les caractéristiques et comportements de {@link Personnage}.
 * </p>
 * 
 * @author srodr
 */
public class Paysan extends Personnage {

    /**
     * Constructeur complet.
     * 
     * @param n Nom du paysan
     * @param etat État vivant ou mort
     * @param pVie Points de vie
     * @param dAtt Dégâts d'attaque
     * @param pPar Points de parade
     * @param paAtt Pourcentage d'attaque
     * @param paPar Pourcentage de parade
     * @param dMax Distance maximale d'attaque
     * @param p Position initiale (Point2D)
     * @param distanceVision Distance de vision
     */
    public Paysan(String n, boolean etat, int pVie, int dAtt, int pPar, int paAtt, int paPar, int dMax, Point2D p, int distanceVision) {
        super(n, etat, pVie, dAtt, pPar, paAtt, paPar, dMax, p, distanceVision);
    }
    
    /**
     * Constructeur par copie.
     * 
     * @param p Paysan à copier
     */
    public Paysan(Paysan p) {
        super(p);
    }

    /**
     * Constructeur par défaut.
     * Initialise un paysan avec les valeurs par défaut de {@link Personnage}.
     */
    public Paysan() {
        super();
    }
    
    

    public void savePaysan(Connection conn, int idPersonnage) {
        try (PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO Paysan (id_personnage) VALUES (?)")) {
            ps.setInt(1, idPersonnage);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur Paysan.savePaysan : " + e.getMessage());
        }
    }


    }
