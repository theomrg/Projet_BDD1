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
 * @author tmaringer01
 */
public class Match extends ClasseMiroir {
    private int rondes; 
    
public Match(int rondes) {
    super();
    this.rondes=rondes;
}

public Match(int id, int rondes) {
    super(id);
    this.rondes=rondes;
}

public int getRondes(){
    return rondes; 
}

public void setRondes(int rondes){
    this.rondes=rondes;
}    

@Override
public String toString() {
        return "match{" + "id=" + this.getId() + ", rondes=" + rondes + '}';
    }


   



    @Override
    protected Statement saveSansId(Connection con) throws SQLException {
        PreparedStatement pst=con.prepareStatement("insert into matchs (ronde) values (?)", PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setInt(1, this.rondes);
            pst.executeUpdate();
            return pst;
        
    }
    
public static void testcreer() {
    try { //essaye de faire ça//
        Match match1 = new Match(3);
        System.out.println(match1);
        match1.saveInDB(ConnectionSimpleSGBD.defaultCon());
    } catch (SQLException ex) {
        throw new Error(ex);    
        }
}
    
    public static void main(String[] args) {
        testcreer();
        
        
    }
}