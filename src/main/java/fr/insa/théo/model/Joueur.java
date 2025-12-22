/*
Copyright 2000- Francois de Bertrand de Beuvron

This file is part of CoursBeuvron.

CoursBeuvron is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

CoursBeuvron is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with CoursBeuvron.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.insa.théo.model;
import com.vaadin.flow.component.notification.Notification;
import fr.insa.beuvron.utils.database.ClasseMiroir;
import fr.insa.beuvron.utils.database.ConnectionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.sql.Date;
/**
 *
 * @author theom
 */

public class Joueur extends ClasseMiroir{
private String surnom;
private String categorie;
private String prenom;
private String nom;
private String sexe;
private LocalDate dateNaissance;


    public Joueur(String surnom, String categorie, String prenom, String nom, String sexe, LocalDate dateNaissance) {
        this.surnom = surnom;
        this.categorie = categorie;
        this.prenom = prenom;
        this.nom = nom;
        this.sexe = sexe;
        this.dateNaissance = dateNaissance;
    }
    
    public Joueur (int id, String surnom, String categorie, String prenom, String nom, String sexe, LocalDate dateNaissance) {
        super(id);
        this.surnom = surnom;
        this.categorie = categorie;
        this.prenom = prenom;
        this.nom = nom;
        this.sexe = sexe;
        this.dateNaissance = dateNaissance;
    }   
        
    public String getSurnom() {
        return surnom;
    }

    public void setSurnom(String surnom) {
        this.surnom = surnom;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getPrénom() {
        return prenom;
    }

    public void setPrénom(String prénom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    @Override
    public String toString() {
        return "Joueur{" + "surnom=" + surnom + ", categorie=" + categorie + ", prenom=" + prenom + ", nom=" + nom + ", sexe=" + sexe + ", dateNaissance=" + dateNaissance + this.getId() +'}';
    }
   
        

    @Override
    protected Statement saveSansId(Connection con) throws SQLException {
        String sql = "INSERT INTO joueur (prenom, nom, surnom, sexe, dateNaissance, categorie) "
               + "VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement pst = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        pst.setString(1, this.prenom);
        pst.setString(2, this.nom);
        pst.setString(3, this.surnom);
        pst.setString(4, this.sexe);
        if (this.dateNaissance != null) {
            pst.setDate(5, java.sql.Date.valueOf(this.dateNaissance));
        } else {
            pst.setNull(5, java.sql.Types.DATE);
        }
        pst.setString(6, this.categorie);
        pst.executeUpdate();
        return pst;
    }

    
public static void créerJoueur(String a,String b,String c,String d,String e,LocalDate f) {
     try (Connection con = ConnectionPool.getConnection()) {
                Joueur j = new Joueur(a,b,c,d,e,f);
                j.saveInDB(ConnectionPool.getConnection());
                Notification.show("Le joueur " + a + " a été ajouté au tournoi");
     }
            catch (SQLException ex) {
                  Notification.show("problème : " + ex.getMessage()); }
}

public static List<Joueur> getAllPlayers() throws SQLException {
    List<Joueur> listeJoueurs = new ArrayList<>();
    
    // 1. Connexion et Requête
    try (Connection con = ConnectionPool.getConnection(); 
         Statement st = con.createStatement();
         ResultSet rs = st.executeQuery("SELECT * FROM joueur")) { 
         while (rs.next()) {
            int id = rs.getInt("id");
            String surnom = rs.getString("surnom");
            String categorie = rs.getString("categorie");
            String prenom = rs.getString("prenom");
            String nom = rs.getString("nom");
            String sexe = rs.getString("sexe");
            java.sql.Date sqlDate = rs.getDate("dateNaissance");
            LocalDate dateN = null;
            if (sqlDate != null) {
                dateN = sqlDate.toLocalDate();
            }
            Joueur j = new Joueur(id, surnom, categorie, prenom, nom, sexe, dateN);
            listeJoueurs.add(j);
        }
    }
    return listeJoueurs;
}
public static boolean estDejaInscritDansRonde(int idjoueur, int idronde) throws SQLException {
    // La requête fait une triple jointure pour remonter du Joueur jusqu'à la Ronde
    String sql = "SELECT COUNT(*) FROM composition c " +
                 "JOIN equipe e ON c.idequipe = e.id " +
                 "JOIN matchs m ON e.idmatch = m.id " +
                 "WHERE c.idjoueur = ? AND m.idronde = ?";
                 
    try (Connection con = ConnectionPool.getConnection();
         PreparedStatement pst = con.prepareStatement(sql)) {
         
        pst.setInt(1, idjoueur);
        pst.setInt(2, idronde);
        
        try (ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                // Si le compte est > 0, c'est qu'il est déjà inscrit !
                return rs.getInt(1) > 0;
            }
        }
    }
    return false;
}
public static int getEquipeActuelleDuJoueur(int idJoueur, int idRonde) throws SQLException {
    // ON CHANGE LE SELECT : on veut 'e.id' (l'équipe) au lieu de 'm.id' (le match)
    // On garde les jointures pour faire le lien jusqu'à la ronde
    String sql = "SELECT e.id FROM composition c " +
                 "JOIN equipe e ON c.idequipe = e.id " +
                 "JOIN matchs m ON e.idmatch = m.id " +
                 "WHERE c.idjoueur = ? AND m.idronde = ?";
                 
    try (Connection con = ConnectionPool.getConnection();
         PreparedStatement pst = con.prepareStatement(sql)) {
         
        pst.setInt(1, idJoueur);
        pst.setInt(2, idRonde);
        
        try (ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                // On a trouvé ! On retourne l'ID de l'équipe conflictuelle
                return rs.getInt("id");
            }
        }
    }
    return -1; // Rien trouvé, le joueur est libre
}

public void delete(Connection con) throws SQLException {
    // 1. On retire le joueur de toutes les compositions
    String sqlClean = "DELETE FROM composition WHERE idjoueur = ?";
    try (PreparedStatement pst = con.prepareStatement(sqlClean)) {
        pst.setInt(1, this.getId());
        pst.executeUpdate();
    }

    // 2. On supprime le joueur
    String sqlDelete = "DELETE FROM joueur WHERE id = ?";
    try (PreparedStatement pst = con.prepareStatement(sqlDelete)) {
        pst.setInt(1, this.getId());
        pst.executeUpdate();
    }
    
    this.entiteSupprimee();
}

public void update(Connection con) throws SQLException {
        String sql = "UPDATE joueur SET prenom = ?, nom = ?, surnom = ?, sexe = ?, dateNaissance = ?, categorie = ? WHERE id = ?";
    
    try (PreparedStatement pst = con.prepareStatement(sql)) {
        // 2. Remplissage des paramètres (ordre 1 à 6)
        pst.setString(1, this.prenom);
        pst.setString(2, this.nom);
        pst.setString(3, this.surnom);
        pst.setString(4, this.sexe);
        
        if (this.dateNaissance != null) {
            pst.setDate(5, java.sql.Date.valueOf(this.dateNaissance));
        } else {
            pst.setNull(5, java.sql.Types.DATE);
        }
        pst.setString(6, this.categorie);
        pst.executeUpdate();
        }
    }   
    
    public static void main(String[] args) {
    }
}
