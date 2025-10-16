package org.centrale.objet.woe.projettp;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Classe principale de test pour le jeu World of ECN (WoE).
 * 
 * Combine les tests academiques (TPs) et la version connectee à la base PostgreSQL.
 * 
 * Fonctionnalites :
 *  - Connexion à la base PostgreSQL
 *  - Creation d’un monde aleatoire
 *  - Creation d’un joueur interactif
 *  - Simulation de plusieurs tours de jeu
 *  - Sauvegarde complète du monde en base
 *  - Experimentations sur les collections (List, LinkedList, etc.)
 * 
 * @version 5.0 
 */
public class TestWoE {

    public static void main(String[] args) {

        System.out.println("=== LANCEMENT DU MONDE WoE ===");

        // ======================================================
        // 1️⃣ Connexion à la base PostgreSQL
        // ======================================================
        ConnexionBD db = new ConnexionBD();
        db.connect();
        Connection conn = db.getConnection();

        if (conn == null) {
            System.err.println("Impossible de demarrer le jeu : connexion base echouee.");
            return;
        }

        // ======================================================
        // 2️⃣ Creation du monde et du joueur
        // ======================================================
        World w = new World();
        w.creerMondeAlea();

        Joueur moi = w.creationJoueur();
        w.afficheWorld(moi);

        // ======================================================
        // 3️⃣ Boucle principale du jeu
        // ======================================================
        try {
            // Simulation de 10 à 15 tours (modifiable)
            w.tourDeJour(10, moi, conn);
        } catch (Exception e) {
            System.err.println("Erreur pendant la simulation : " + e.getMessage());
            e.printStackTrace();
        }

        // ======================================================
        // 4️⃣ Sauvegarde complète du monde et du joueur
        // ======================================================

        // ======================================================
        // 5️⃣ Fermeture propre de la connexion
        // ======================================================
        db.close();
        System.out.println("\n=== Fin du jeu WoE ===");

        // ======================================================
        // EXPeRIMENTATIONS SUPPLeMENTAIRES (TPs precedents)
        // ======================================================
        /*
        // --- Test distance entre deux points ---
        Point2D p1 = new Point2D(0,0);
        Point2D p2 = new Point2D(2,2);
        System.out.println("Distance : " + p1.distance(p2));

        // --- Test combat ---
        World w2 = new World();
        w2.creerMondeAlea();
        w2.ListCreature.get(0).combattre(w2.ListCreature.get(1), w2.getPositionsOccupees());

        // --- Test comparatif de performance ---
        World w3 = new World();
        ArrayList<Creature> arrayList = new ArrayList<>();
        LinkedList<Creature> linkedList = new LinkedList<>();
        w3.creationLabDeMondePourComparerDesTemps(arrayList, 3);
        w3.creationLabDeMondePourComparerDesTemps(linkedList, 3);
        */
    }
}
