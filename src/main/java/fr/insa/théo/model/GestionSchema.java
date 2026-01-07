/*
Copyright 2000- Francois de Bertrand de Beuvron

This file is ecole of CoursBeuvron.

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
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author francois
 */
public class GestionSchema {

    /**
     *
     * @param con
     * @throws SQLException
     */
    public static void creeSchema(Connection con)
            throws SQLException {
        try {
            con.setAutoCommit(false);
            try (Statement st = con.createStatement()) {
                // creation des tables
                
                st.executeUpdate("create table joueur ("
                        + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ","
                        + " prenom varchar(20) not null,"
                        + " nom varchar(20) not null,"
                        + " surnom varchar(20) not null,"  
                        + " sexe varchar(10) not null,"
                        + " dateNaissance DATE,"        
                        + " categorie varchar(1) "    
                        + ") "   );
                st.executeUpdate("create table ronde ("
                        + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ","
                        + " numéro int,"
                        + " statut varchar(20) "
                        + ") "   );
                
                st.executeUpdate("create table matchs ("
                        + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ","
                        + " idronde int not null, "
                        + " foreign key (idronde) references ronde(id),"
                        + " statut varchar (10) not null"        
                        + ")" );
                
                st.executeUpdate("create table equipe ("
                        + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ","
                        + " nomEquipe varchar(20) not null,"
                        + " score int, " 
                        + " idmatch int,  " 
                        + " foreign key (idmatch) references matchs(id)"
                        + ") "   );
                
                st.executeUpdate("create table composition ("
                        + " idequipe int,"
                        + " idjoueur int, "
                        + " foreign key (idequipe) references equipe(id),"
                        + " foreign key (idjoueur) references joueur(id)"
                        + ") "   );
                
                st.executeUpdate("create table utilisateurs ("
                        + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ","
                        + " identifiant varchar(30) not null unique,"
                        + " mdp varchar(30) not null, "
                        + " role varchar(20) not null "
                        + ") "   );
           
                
                con.commit();
                
                /* st.executeUpdate("alter table apprecie\n"
                        + "  add constraint fk_apprecie_u1\n"
                        + "  foreign key (u1) references utilisateur(id)" ); */
            }
        } catch (SQLException ex) {
            con.rollback();
            throw ex;
        } finally {
            con.setAutoCommit(true);
        }
    }

    /** 
     *
     * @param con
     * @throws SQLException
     */
    public static void deleteSchema(Connection con) throws SQLException {
        try (Statement st = con.createStatement()) {
            try {
                st.executeUpdate(
                        "alter table equipe "
                        + "drop foreign key idmatch");
            } catch (SQLException ex) {
            }
             try {
                st.executeUpdate(
                        "alter table composition "
                        + "drop foreign key idequipe,"
                        + "drop foreign key idjoueur");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("drop table joueur");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("drop table equipe");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("drop table composition");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("drop table matchs");
            } catch (SQLException ex) {
            }
             try {
                st.executeUpdate("drop table utilisateurs");
            } catch (SQLException ex) { }
             try {
                st.executeUpdate("drop table ronde");
            } catch (SQLException ex) { }
        }
    }

    /**
     *
     * @param con
     * @throws SQLException
     */
    public static void razBdd(Connection con) throws SQLException {
        deleteSchema(con);
        creeSchema(con);
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        try (Connection con = ConnectionSimpleSGBD.defaultCon()) {
            razBdd(con);
        } catch (SQLException ex) {
            throw new Error(ex);
        }
    }

}
