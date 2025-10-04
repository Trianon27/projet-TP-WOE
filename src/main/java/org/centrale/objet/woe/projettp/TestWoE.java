/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package org.centrale.objet.woe.projettp;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;

/**
 *
 * @author srodr
 */
public class TestWoE {

    public static void main(String[] args) {
        
        //Premier test (Création du monde aléatoire)
        /**
        Point2D point = new Point2D(0,0);
        Personnage p = new Personnage("a",100, 100, 100, 100,100,100, point);
        
        p.affiche();
        
        Archer a = new Archer("b",100, 100, 100, 100,100,100, point,2);
        a.affiche();
        
        Paysan paysan1 = new Paysan("c",100, 100, 100, 100,100,100, point);
        paysan1.affiche();

        World w = new World();
        w.creerMondeAlea();
        w.afficheWorld();**/
        
        
        //Deuxieme test (Deplacement)
        /**
        World w = new World();
        w.creerMondeAlea();
        w.afficheWorld();
        
        w.robin.deplaceAleatoire();
        w.peon.deplaceAleatoire();
        w.bugs.deplaceAleatoire();
        
        w.affiche();**/
        
        //Troisieme exercice 
        /*
        World w = new World();
        w.afficheWorld();
        w.robin.deplaceAleatoire();
        w.afficheWorld();*/
        
        //Cinquieme exercie
        /*
        float dist;
        Point2D point = new Point2D(0,0);
        point.affiche();
        Point2D pointa = new Point2D(2,2);
        pointa.affiche();
        dist = point.distance(pointa);
        System.out.println("La distance est: " + dist);
        System.out.println();
        Point2D pointb = new Point2D(1,0);
        point.affiche();
        pointb.affiche();
        dist = point.distance(pointb);
        System.out.println("La distance est: " + dist);
        */
        
        // sixième Combattre
        /*
        World w = new World();
        w.afficheWorld();
        System.out.println("Combat Guerrier vs Loup");
        System.out.println("====================");
        for(int i = 0; i<10; i++){
            w.grosBill.combattre(w.wolfie, w.getPositionsOccupees());
            w.wolfie.combattre(w.grosBill, w.getPositionsOccupees());
        }
        System.out.println();
        System.out.println("Combat Archer vs Archer");
        System.out.println("====================");
        w.robin.deplace(2, 0);
        for(int i = 0; i<10; i++){
            w.robin.combattre(w.guillaumeT, w.getPositionsOccupees());
            w.guillaumeT.combattre(w.robin, w.getPositionsOccupees());
        }
        
       
        w.robin.setEtat(true);
        w.robin.setPtVie(100);
        System.out.println();
        System.out.println("Combat Archer vs Guerrier");
        System.out.println("====================");
        w.robin.deplace(-1, 0);
        for(int i = 0; i<10; i++){
            w.robin.combattre(w.grosBill, w.getPositionsOccupees());
            w.grosBill.combattre(w.robin, w.getPositionsOccupees());
        }
        */
        
        
        //septième exercice
        /*World w = new World();
        w.grosBill.affiche();
        w.potionV.affiche();
        w.grosBill.deplace(-1, 0);
        System.out.println();
        System.out.println("======DEPLACEMENT=====");
        w.grosBill.affiche();
        w.potionV.affiche();
        w.grosBill.prendObjet(w.potionV, w.getPositionsOccupees());
        w.grosBill.affiche();*/
        
        
        
        
        //TP4
        //Exercice 1
        /*World W= new World();
        W.creerMondeAlea();*/
        
        //Experimentation avec les mondes:
        World W= new World();
        ArrayList<Creature> creaturesArrayList = new ArrayList<>();
        LinkedList<Creature> creaturesLinkedList = new LinkedList<>();
        W.creationLabDeMondePourComparerDesTemps(creaturesArrayList,5);
        W.creationLabDeMondePourComparerDesTemps(creaturesLinkedList,4);

        
        
        
        
        
    }
}

