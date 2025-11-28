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
import java.util.ArrayList;
import java.util.List;
import com.vaadin.flow.component.notification.Notification;
import fr.insa.beuvron.utils.database.ClasseMiroir;
import fr.insa.beuvron.utils.database.ConnectionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author theom
 */
public class Ronde extends ClasseMiroir {
    private int numero;     
    private String statut;   

    public Ronde(int id, int numero, String statut) {
        super(id);
        this.numero = numero;
        this.statut = statut;
    }

    public Ronde(int numero, String statut) {
        this.numero = numero;
        this.statut = statut;
    }
  

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    @Override
    public String toString() {
        return "Ronde{" + "numero=" + numero + ", statut=" + statut + this.getId()+ '}';
    }
    
    @Override
    protected Statement saveSansId(Connection con) throws SQLException {
        PreparedStatement pst=con.prepareStatement("insert into ronde (numéro, statut) values (?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setInt(1, this.numero);
            pst.setString(2, this.statut);
            pst.executeUpdate();
            return pst;
       
    }
    
    
    public static void créerRonde(int a, String b) {
         try (Connection con = ConnectionPool.getConnection()) {
                Ronde r = new Ronde(a,b);
                r.saveInDB(ConnectionSimpleSGBD.defaultCon());
                Notification.show("La ronde numéro" + r.numero + " a été ajouté avec succès");
     }
            catch (SQLException ex) {
                  Notification.show("problème : " + ex.getMessage()); }
            }  
    
    public static List<Ronde> getAllRondes() throws SQLException {
    List<Ronde> listeRondes = new ArrayList<>();
    
    // On récupère la connexion (adaptez selon votre méthode de connexion)
    try (Connection con = ConnectionPool.getConnection();
         Statement st = con.createStatement();
         ResultSet rs = st.executeQuery("SELECT * FROM ronde")) {
        while (rs.next()) {
          Ronde r = new Ronde(rs.getInt("id"),rs.getInt("numéro"), rs.getString("statut")
          );  
          listeRondes.add(r);
            }
        }
        return listeRondes;
    }
}
