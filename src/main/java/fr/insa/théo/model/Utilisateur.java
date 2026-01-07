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

/**
 *
 * @author theom
 */
public class Utilisateur extends ClasseMiroir{
    private String identifiant;
    private String mdp;
    private String role;

    public Utilisateur(String identifiant, String mdp, String role, int id) {
        super(id);
        this.identifiant = identifiant;
        this.mdp = mdp;
        this.role = role;
    }

    public Utilisateur(String identifiant, String mdp, String role) {
        this.identifiant = identifiant;
        this.mdp = mdp;
        this.role = role;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Utilisateur{" + "identifiant=" + identifiant + ", mdp=" + mdp + ", role=" + role + '}';
    }
    
    @Override
    protected Statement saveSansId(Connection con) throws SQLException {
        PreparedStatement pst=con.prepareStatement("insert into utilisateurs (identifiant,mdp,role) values (?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setString(1, this.identifiant);
            pst.setString(2, this.mdp);
            pst.setString(3, "USER");
            pst.executeUpdate();
            return pst;
       
    }
    
    public static void créerUtilisateur(String a,String b) {
     try (Connection con = ConnectionPool.getConnection()) {
                Utilisateur u = new Utilisateur(a,b,"USER");
                u.saveInDB(ConnectionPool.getConnection());
                Notification.show("Nouvel utlisateur ajouté avec succès");
     }
            catch (SQLException ex) {
                  Notification.show("problème : " + ex.getMessage()); }
}
    
public static List<Utilisateur> getAllUtilisateurs() throws SQLException {
        List<Utilisateur> list = new ArrayList<>();
        String sql = "SELECT * FROM utilisateurs ORDER BY identifiant ASC";
        
        try (Connection con = ConnectionPool.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
             
            while (rs.next()) {
                Utilisateur u = new Utilisateur(
                    rs.getString("identifiant"),
                    rs.getString("mdp"),
                    rs.getString("role"),
                    rs.getInt("id")
                );
                list.add(u);
            }
        }
        return list;
    }

    /**
     * Promeut cet utilisateur au rang d'ADMIN en base de données.
     */
    public void devenirAdmin() throws SQLException {
        String sql = "UPDATE utilisateurs SET role = 'ADMIN' WHERE id = ?";
        
        try (Connection con = ConnectionPool.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setInt(1, this.getId());
            pst.executeUpdate();
            
            // Mise à jour de l'objet local
            this.setRole("ADMIN");
        }
    }
 

    
}
