package org.centrale.objet.woe.projettp;

import java.sql.Connection;
import java.util.Scanner;

/**
 * Classe principale de test pour le jeu World of ECN (WoE).
 *
 * Combine les tests académiques (TPs) et la version connectée à la base PostgreSQL.
 *
 * Fonctionnalités :
 * - Connexion à la base PostgreSQL
 * - Menu principal (charger ou créer une nouvelle partie)
 * - Création d’un monde aléatoire
 * - Création ou restauration d’un joueur interactif
 * - Simulation de plusieurs tours de jeu
 * - Sauvegarde complète du monde en base
 *
 * @version 6.0 (ajout du chargement de partie au démarrage)
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
            System.err.println("❌ Impossible de demarrer le jeu : connexion base echouee.");
            return;
        }
        System.out.println("Connexion reussie a la base de donnees !");

        // ======================================================
        // 2️⃣ Menu principal : Charger ou Nouvelle partie
        // ======================================================
        Scanner sc = new Scanner(System.in);
        World w = new World();
        Joueur moi;

        System.out.println("""
            === MENU PRINCIPAL ===
            0 - Charger une partie existante
            1 - Nouvelle partie
        """);

        System.out.print("Votre choix : ");
        int choix = -1;
        try {
            choix = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("⚠️ Entree invalide, nouvelle partie par defaut.");
        }

        // ======================================================
        // 3️⃣ Selon le choix de l'utilisateur
        // ======================================================
        if (choix == 0) {
            // 🔁 Chargement d’une partie existante
            moi = new Joueur();
            moi.chargerPartieDepuisDebut(conn, w);

        } else {
            // 🆕 Création d’une nouvelle partie
            System.out.println("\n=== NOUVELLE PARTIE ===");
            w.creerMondeAlea();
            moi = w.creationJoueur();
            w.afficheWorld(moi);

            try {
                w.tourDeJour(10, moi, conn);
            } catch (Exception e) {
                System.err.println("⚠️ Erreur pendant la simulation : " + e.getMessage());
                e.printStackTrace();
            }
        }

        // ======================================================
        // 4️⃣ Fermeture propre de la connexion
        // ======================================================
        db.close();
        System.out.println("\n=== Fin du jeu WoE ===");
    }
}
