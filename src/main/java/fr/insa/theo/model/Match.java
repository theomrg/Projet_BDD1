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
package fr.insa.theo.model;

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

/**
 *
 * @author tmaringer01
 */
public class Match extends ClasseMiroir {
    private int idronde; 

    public Match(int idronde) {
    this.idronde = idronde;
        }
    
    public Match(int id, int idronde) {
        super(id);
        this.idronde = idronde;
    

}

    public int getIdronde() {
        return idronde;
    }

    public void setIdronde(int idronde) {
        this.idronde = idronde;
    }

    @Override
    public String toString() {
        return "Match{" + "idronde=" + idronde + this.getId()+ '}';
    }

    @Override
    protected Statement saveSansId(Connection con) throws SQLException {
        PreparedStatement pst=con.prepareStatement("insert into matchs (idronde) values (?)", PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setInt(1, this.idronde);
            pst.executeUpdate();
            return pst;
        
    }
    
public static void créerMatch(Ronde r) {
    try (Connection con = ConnectionPool.getConnection()) {
                Match m= new Match(r.getId());
                m.saveInDB(ConnectionPool.getConnection());
                Notification.show("Match créé ! Appartient à la ronde " + r.getNumero());
                }
            catch (SQLException ex) {
                  Notification.show("problème : " + ex.getMessage()); }
}

public static List<Match> getAllMatchs() throws SQLException {
    List<Match> listeMatchs = new ArrayList<>();
    
    // 1. Connexion et Requête
    try (Connection con = ConnectionPool.getConnection(); // ou votre Pool
         Statement st = con.createStatement();
         // On sélectionne toutes les colonnes nécessaires
         ResultSet rs = st.executeQuery("SELECT * FROM matchs")) {
         
        while (rs.next()) {
            // 2. Création de l'objet Match à partir de la ligne BDD
            // Match(int id, int idRonde, String statut)
            Match m = new Match(rs.getInt("id"), rs.getInt("idronde")    
            );
            
            listeMatchs.add(m);
        }
    }
    return listeMatchs;
}
public static List<Match> getMatchsDeLaRonde(int idRonde) throws SQLException {
    List<Match> listeMatchs = new ArrayList<>();
    
    // Assurez-vous que le nom de la table est bien 'Matchs' ou 'Match' selon votre BDD
    String sql = "SELECT * FROM matchs WHERE idronde = ?";
    
    try (Connection con = ConnectionPool.getConnection();
         PreparedStatement pst = con.prepareStatement(sql)) {
         
        pst.setInt(1, idRonde);
        
        try (ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                // Utilisation du constructeur de lecture (id, ronde_id, statut)
                Match m = new Match(
                    rs.getInt("id"),
                    rs.getInt("idronde")
                );
                listeMatchs.add(m);
            }
        }
    }
    return listeMatchs;
}

public void delete(Connection con) throws SQLException {
    // 1. Récupérer toutes les équipes de ce match pour les supprimer une par une
    // (Utilisez votre méthode existante getEquipesDuMatch ou faites une requête)
    List<Equipe> equipes = Equipe.getEquipesDuMatch(this.getId());
    
    for (Equipe e : equipes) {
        // On appelle la méthode delete() de l'équipe qu'on vient de créer au point 1
        e.delete(con); 
    }

    // 2. Maintenant que le match est "vide", on le supprime
    String sql = "DELETE FROM Matchs WHERE id = ?";
    try (PreparedStatement pst = con.prepareStatement(sql)) {
        pst.setInt(1, this.getId());
        pst.executeUpdate();
    }
    
    this.entiteSupprimee();
}
    
    public static void main(String[] args) {
      
    }
}