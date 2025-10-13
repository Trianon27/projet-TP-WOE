package org.centrale.objet.woe.projettp;

import java.sql.Connection;

/**
 * Classe principale de test pour le jeu World of ECN (WoE).
 * 
 * Elle gère :
 * - la connexion à la base PostgreSQL ;
 * - la création du monde et du joueur ;
 * - la boucle principale de jeu ;
 * - la sauvegarde dynamique via le menu du joueur.
 * 
 * @author O.
 * @version 4.0
 */
public class TestWoE {

    public static void main(String[] args) {

        System.out.println("=== 🌍 LANCEMENT DU MONDE WoE ===");

        // 1️⃣ Connexion à la base PostgreSQL
        ConnexionBD db = new ConnexionBD();
        db.connect();
        Connection conn = db.getConnection();

        if (conn == null) {
            System.err.println("❌ Impossible de démarrer le jeu : la connexion à la base a échoué.");
            return;
        }

        // 2️⃣ Création du monde
        World w = new World();
        w.creerMondeAlea();

        // 3️⃣ Création du joueur
        Joueur moi = w.creationJoueur();




        w.afficheWorld(moi);

        // 4️⃣ Déroulement du jeu (avec sauvegarde à tout moment)
        try {
            w.tourDeJour(15, moi, conn);
        } catch (Exception e) {
            System.err.println("❌ Erreur pendant la simulation : " + e.getMessage());
            e.printStackTrace();
        }

        // 5️⃣ Fermeture propre
        db.close();


        System.out.println("\n=== 🏁 Fin du jeu WoE ===");
    }
}
