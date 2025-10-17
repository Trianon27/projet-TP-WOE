package org.centrale.objet.woe.projettp;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.PropertyResourceBundle;

public class ConnexionBD {

    private Connection conn;

    /**
     * Établit la connexion à la base de données en lisant le fichier bd.properties.
     * Et sélectionne automatiquement le schéma "OBJET".
     */
    public void connect() {
        try (FileInputStream fis = new FileInputStream("bd.properties")) {

            // Lecture du fichier de configuration
            PropertyResourceBundle props = new PropertyResourceBundle(fis);
            String url = props.getString("db.url");
            String user = props.getString("db.user");
            String password = props.getString("db.password");
            String driver = props.getString("db.driver");

            // Chargement du driver JDBC
            Class.forName(driver);

            // Connexion à la base PostgreSQL
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Connexion établie avec succès à la base de données !");

            // Forcer le schéma par défaut sur OBJET
            try (Statement st = conn.createStatement()) {
                st.execute("SET search_path TO OBJET;");
                System.out.println("📂 Schéma défini : OBJET");
            }

        } catch (IOException e) {
            System.err.println("❌ Erreur lors de la lecture du fichier de propriétés : " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("❌ Erreur SQL lors de la connexion : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Driver JDBC introuvable : " + e.getMessage());
        }
    }

    /**
     * Retourne la connexion active.
     */
    public Connection getConnection() {
        return conn;
    }

    /**
     * Ferme proprement la connexion à la base de données.
     */
    public void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("🔒 Connexion fermée proprement.");
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Erreur lors de la fermeture de la connexion : " + e.getMessage());
        }
    }
}
