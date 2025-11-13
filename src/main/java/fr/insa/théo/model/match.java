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

import com.fasterxml.jackson.databind.deser.DataFormatReaders.Match;
import fr.insa.beuvron.utils.database.ClasseMiroir;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Visiteur
 */

public class match extends ClasseMiroir {
    private int id;
    private int rondes;
    
public match(int id, int rondes) {
    this.id=identifiant;
    this.rondes=rondes;
}

public int getId(){
    return id;
}

    
public void setId(int id){
    this.id=identifiant;
}    

public int getRondes(){
    return rondes;

public void setRondes(int rondes){
    this.rondes=rondes;
}    

@Override
public String toString() {
        return "match{" + "id=" + id + ", rondes=" + rondes + '}';
    }


   



    @Override
    protected Statement saveSansId(Connection con) throws SQLException {
        PreparedStatement pst=con.prepareStatement("insert into match (id,rondes) values (?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setString(1, this.id);
            pst.setString(2, this.rondes);
            pst.executeUpdate();
            return pst;
        
    }
    
public static void testcreer() {
    try { //essaye de faire ça//
        Match m = new match(1,3);
        System.out.println(m);
        m.saveInDB(ConnectionSimpleSGBD.defaultCon());
        m.saveSansId(ConnectionSimpleSGBD.defaultCon());
    } catch (SQLException ex) {
        throw new Error(ex);    
        }
}
    
    public static void main(String[] args) {
        testcreer();
        
        
    }
}

            
        
    
    

