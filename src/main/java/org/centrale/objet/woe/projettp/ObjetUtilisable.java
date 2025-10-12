/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.centrale.objet.woe.projettp;

/**
 *
 * @author srodr
 */
public interface ObjetUtilisable {
    void appliquerEffet(Personnage p);  
    void retirerEffet(Personnage p);                        
    void decrementerDuree();  
    boolean estActif();
}
