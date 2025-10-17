package org.centrale.objet.woe.projettp;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.PropertyResourceBundle;

public class ConnexionBD {

    private Connection conn;

    /**
     * Établit la connexion à la base de données en lisant le fichier bd.properties.
     */
    public void connect() {
        try {
            FileInputStream fis = new FileInputStream("bd.properties");
            PropertyResourceBundle props = new PropertyResourceBundle(fis);

            String url = props.getString("db.url");
            String user = props.getString("db.user");
            String password = props.getString("db.password");
            String driver = props.getString("db.driver");

            // Chargement du driver JDBC
            Class.forName(driver);

            // Connexion à la base
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Connexion reussie à la base de donnees !");
        } catch (IOException e) {
            System.err.println("❌ Erreur lecture fichier de propriétés : " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("❌ Erreur SQL : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Driver JDBC introuvable !");
        }
    }

    /**
     * Retourne la connexion active.
     */
    public Connection getConnection() {
        return conn;
    }

    /**
     * Ferme proprement la connexion.
     */
    public void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("🔒 Connexion fermee.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture : " + e.getMessage());
        }
    }
}
