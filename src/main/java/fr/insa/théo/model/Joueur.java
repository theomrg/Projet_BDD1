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
import fr.insa.beuvron.utils.database.ClasseMiroir;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
/**
 *
 * @author theom
 */

public class Joueur extends ClasseMiroir{
private String surnom;
private String categorie;
private int taillecm;

    public Joueur(String surnom, String categorie, int taille) {
        this.surnom = surnom;
        this.categorie = categorie;
        this.taillecm = taille;
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

    public int getTaille() {
        return taillecm;
    }

    public void setTaille(int taille) {
        this.taillecm = taille;
    }

    @Override
    public String toString() {
        return "Joueur{" + "surnom=" + surnom + ", categorie=" + categorie + ", taille=" + taillecm + " :" + this.getId()+ '}';
    }



    @Override
    protected Statement saveSansId(Connection con) throws SQLException {
        PreparedStatement pst=con.prepareStatement("insert into joueur (surnom, categorie, taillecm) values (?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setString(1, this.surnom);
            pst.setString(2, this.categorie);
            pst.setInt(3, this.taillecm);
            pst.executeUpdate();
            return pst;
        
    }
    
public static void testcreer() {
    try { //essaye de faire ça//
        Joueur j = new Joueur("Test","A",180);
        System.out.println(j);
        j.saveInDB(ConnectionSimpleSGBD.defaultCon());
    } catch (SQLException ex) {
        throw new Error(ex);    
        }
}
    
    public static void main(String[] args) {
        testcreer();
        
        
    }
}
