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
public class Equipe extends ClasseMiroir {
    private int num;
    private int score;
    private int idmatch;
            
    public Equipe(int num, int score, Match match) {
        this.num = num;
        this.score = score;
        this.idmatch= match.getId();
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
        return "Equipe{" + "num=" + num + ", score=" + score + '}';
    }

    @Override
    protected Statement saveSansId(Connection con) throws SQLException {
        PreparedStatement pst=con.prepareStatement("insert into equipe (num, score, ) values (?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setInt(1, this.num);
            pst.setInt(2, this.score);
            pst.executeUpdate();
            return pst;
    }

  


    
    
    
}
