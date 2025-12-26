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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author theom
 */
public class Equipe extends ClasseMiroir {
    private String nomEquipe;
    private int score;
    private int idmatch;
            
    public Equipe(String nomEquipe, int score, int idmatch) {
        this.nomEquipe = nomEquipe;
        this.score = score;
        this.idmatch = idmatch;
        
    }
    
    public Equipe(int id, String nomEquipe, int score, int idmatch) {
        super(id);
        this.nomEquipe = nomEquipe;
        this.score = score;
        this.idmatch= idmatch; 
    }

    public int getIdmatch() {
        return idmatch;
    }

    public String getNomEquipe() {
        return nomEquipe;
    }

    public void setNomEquipe(String nomEquipe) {
        this.nomEquipe = nomEquipe;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Equipe{" + "num=" + nomEquipe + ", score=" + score + ", idmatch=" + idmatch + '}';
    }

  

    @Override
    protected Statement saveSansId(Connection con) throws SQLException {
        PreparedStatement pst=con.prepareStatement("insert into equipe (nomEquipe, score, idmatch) values (?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setString(1, this.nomEquipe);
            pst.setInt(2, this.score);
            pst.setInt(3, this.idmatch);
            pst.executeUpdate();
            return pst;
    }

public static void créerEquipe(String a, Match m) {
    try (Connection con = ConnectionPool.getConnection()) {
                Equipe e = new Equipe(a,0,m.getId());
                e.saveInDB(ConnectionPool.getConnection());
                Notification.show("Equipe créée ! Appartient au match " + m.getId());
               }
            catch (SQLException ex) {
                  Notification.show("problème : " + ex.getMessage()); }
}
public static int getNbEquipesParMatch(int idMatch) throws SQLException {
    // On prépare la requête qui compte les lignes
    String sql = "SELECT COUNT(*) FROM equipe WHERE idmatch = ?";
    
    // On utilise votre gestionnaire de connexion par défaut
    try (Connection con = ConnectionPool.getConnection();
        PreparedStatement pst = con.prepareStatement(sql)) {        
        pst.setInt(1, idMatch);
        
        try (ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                // Retourne le nombre trouvé (ex: 0, 1 ou 2)
                return rs.getInt(1);
            }
        }
    }
    return 0; // Par défaut, s'il n'y a rien
}
public static List<Equipe> getAllTeams() throws SQLException {
    List<Equipe> listeEquipes = new ArrayList<>();
    
    // 1. Connexion et Requête
    try (Connection con = ConnectionPool.getConnection(); 
         Statement st = con.createStatement();
         ResultSet rs = st.executeQuery("SELECT * FROM equipe")) { 
         while (rs.next()) {
            Equipe e = new Equipe(rs.getInt("id"), rs.getString("nomEquipe"),rs.getInt("score"),rs.getInt("idmatch")  
            );
            
            listeEquipes.add(e);
        }
    }
    return listeEquipes;
}
public static List<Joueur> getJoueursDeLEquipe(int idequipe) throws SQLException {
    List<Joueur> listeJoueursE = new ArrayList<>();

    // LA REQUÊTE SQL (C'est ici que la magie opère !)
    // On sélectionne les infos du JOUEUR (j.*)
    // En faisant une JOINTURE avec la table COMPOSITION (c)
    // Condition : on ne veut que les lignes où l'équipe correspond à 'idEquipe'
    String sql = "SELECT j.* " +
                 "FROM joueur j " +
                 "JOIN composition c ON j.id = c.idjoueur " +
                 "WHERE c.idequipe = ?";

    try (Connection con = ConnectionPool.getConnection();
         PreparedStatement pst = con.prepareStatement(sql)) {

        // On remplit le paramètre (?) avec l'ID de l'équipe reçue en argument
        pst.setInt(1, idequipe);

        try (ResultSet rs = pst.executeQuery()) {
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
                
                listeJoueursE.add(j);
            }
        }
    }
    return listeJoueursE;
}
public static void ajouterJoueurDansEquipe(int idequipe, int idjoueur) throws SQLException {
    // La requête SQL insère les deux IDs dans la table de jointure
    String sql = "INSERT INTO composition (idequipe, idjoueur) VALUES (?, ?)";
    
    try (Connection con = ConnectionPool.getConnection();
         PreparedStatement pst = con.prepareStatement(sql)) {
         
        pst.setInt(1, idequipe);
        pst.setInt(2, idjoueur);
        pst.executeUpdate();
    }
}
public static int getRondeIdDeLEquipe(int idEquipe) throws SQLException {
    String sql = "SELECT m.idronde FROM equipe e " +
                 "JOIN matchs m ON e.idmatch = m.id " +
                 "WHERE e.id = ?";
                 
    try (Connection con = ConnectionPool.getConnection();
         PreparedStatement pst = con.prepareStatement(sql)) {
        
        pst.setInt(1, idEquipe);
        ResultSet rs = pst.executeQuery();
        
        if (rs.next()) {
            return rs.getInt("idronde");
        }
    }
    return -1; // Erreur si non trouvé
}

public void delete(Connection con) throws SQLException {
    // 1. D'abord, on supprime les liens dans la table 'composition'
    String sqlCleanComposition = "DELETE FROM composition WHERE idequipe = ?";
    try (PreparedStatement pst = con.prepareStatement(sqlCleanComposition)) {
        pst.setInt(1, this.getId());
        pst.executeUpdate();
    }

    // 2. Ensuite, on peut supprimer l'équipe elle-même
    // Note : On utilise 'id' car votre classe hérite probablement de ClasseMiroir
    String sqlDeleteEquipe = "DELETE FROM equipe WHERE id = ?";
    try (PreparedStatement pst = con.prepareStatement(sqlDeleteEquipe)) {
        pst.setInt(1, this.getId());
        pst.executeUpdate();
    }
    
    // On signale à l'objet qu'il est supprimé (méthode de ClasseMiroir)
    this.entiteSupprimee(); 
}

public static List<Equipe> getEquipesDuMatch(int idMatch) throws SQLException {
    List<Equipe> listeEquipes = new ArrayList<>();
    
    String sql = "SELECT * FROM equipe WHERE idmatch = ?";
    
    try (Connection con = ConnectionSimpleSGBD.defaultCon();
         PreparedStatement pst = con.prepareStatement(sql)) {
         
        pst.setInt(1, idMatch);
        
        try (ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Equipe e = new Equipe(
                    rs.getInt("id"),
                    rs.getString("nomEquipe"),
                    rs.getInt("score"),
                    rs.getInt("idmatch")  
                );
               
                listeEquipes.add(e);
            }
        }
    }
    return listeEquipes;
}
public static List<Equipe> getEquipesDeLaRonde(int idRonde) throws SQLException {
    List<Equipe> list = new ArrayList<>();
    
    // Jointure : On prend les équipes (e) dont le match (m) appartient à la ronde demandée
    // Assurez-vous que le nom des colonnes (id_match, idronde) correspond bien à votre base !
    String sql = "SELECT e.* FROM equipe e " +
                 "JOIN matchs m ON e.idmatch = m.id " +
                 "WHERE m.idronde = ?";
                 
    try (Connection con = ConnectionPool.getConnection();
         PreparedStatement pst = con.prepareStatement(sql)) {
         
        pst.setInt(1, idRonde);
        
        try (ResultSet rs = pst.executeQuery()) {
            while(rs.next()) {
                Equipe eq = new Equipe(
                    rs.getInt("id"),
                    rs.getString("nomEquipe"),
                    rs.getInt("score"),
                    rs.getInt("idmatch")
                );
                list.add(eq);
            }
        }
    }
    return list;
}

public void update(Connection con) throws SQLException {
    String sql = "UPDATE equipe SET nomEquipe = ? WHERE id = ?";
    
    try (PreparedStatement pst = con.prepareStatement(sql)) {
        pst.setString(1, this.nomEquipe); // Le nouveau nom
        pst.setInt(2, this.getId());      // L'ID de l'équipe à modifier
        
        pst.executeUpdate();
    }
}
public static void setScoreEquipe(int idEquipe, int nouveauScore) throws SQLException {
    String sql = "UPDATE equipe SET score = ? WHERE id = ?";
    try (Connection con = ConnectionPool.getConnection();
         PreparedStatement pst = con.prepareStatement(sql)) {
        pst.setInt(1, nouveauScore);
        pst.setInt(2, idEquipe);
        pst.executeUpdate();
    }
}
    
    public static void main(String[] args) {
         
    }  
    
}
