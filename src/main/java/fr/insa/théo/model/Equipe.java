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
public class Equipe extends ClasseMiroir {
    private int num;
    private int score;
    private int idmatch;
            
    public Equipe(int num, int score, int idmatch) {
        this.num = num;
        this.score = score;
        this.idmatch = idmatch;
        
    }
    
    public Equipe(int id, int num, int score, int idmatch) {
        super(id);
        this.num = num;
        this.score = score;
        this.idmatch= idmatch; 
    }

    public int getIdmatch() {
        return idmatch;
    }
    

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Equipe{" + "num=" + num + ", score=" + score + ", idmatch=" + idmatch + '}';
    }

  

    @Override
    protected Statement saveSansId(Connection con) throws SQLException {
        PreparedStatement pst=con.prepareStatement("insert into equipe (num, score, idmatch) values (?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setInt(1, this.num);
            pst.setInt(2, this.score);
            pst.setInt(3, this.idmatch);
            pst.executeUpdate();
            return pst;
    }

public static void créerEquipe(int a, Match m) {
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
            Equipe e = new Equipe(rs.getInt("id"), rs.getInt("num"),rs.getInt("score"),rs.getInt("idmatch")  
            );
            
            listeEquipes.add(e);
        }
    }
    return listeEquipes;
}
    
    public static void main(String[] args) {
         
    }  
    
}
