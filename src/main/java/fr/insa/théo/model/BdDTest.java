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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author francois
 */
public class BdDTest {

    public static void createBdDTestV1(Connection con) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement("insert into matchs(ronde) values (?)")) {
            pst.setInt(1, 1);
            pst.executeUpdate();
            pst.setInt(1, 1);
            pst.executeUpdate();
        } 
        try (PreparedStatement pst = con.prepareStatement("insert into joueur(surnom,categorie,taillecm) values (?,?,?)")) {
            pst.setString(1, "Valou");
            pst.setString(2, "S");
            pst.setInt(3, 180);
            pst.executeUpdate();
            pst.setString(1, "Aude");
            pst.setString(2, "J");
            pst.setInt(3, 160);
            pst.executeUpdate();
            pst.setString(1, "Théo");
            pst.setString(2, " ");
            pst.setInt(3,0);
            pst.executeUpdate();
            pst.setString(1, "Gaétan");
            pst.setString(2, " ");
            pst.setInt(3,170);
            pst.executeUpdate();
            pst.setString(1, "Karmann");
            pst.setString(2, "J");
            pst.setInt(3,190);
            pst.executeUpdate();
        } 
        try (PreparedStatement pst = con.prepareStatement("insert into equipe(num,score,idmatch) values (?,?,?)")) {
            pst.setInt(1, 6);
            pst.setInt(2, 10);
            pst.setInt(3, 1);
            pst.executeUpdate();
            pst.setInt(1, 6);
            pst.setInt(2, 15);
            pst.setInt(3, 1);
            pst.executeUpdate();
            pst.setInt(1, 6);
            pst.setInt(2, 12);
            pst.setInt(3, 2);
            pst.executeUpdate();
            pst.setInt(1, 6);
            pst.setInt(2, 5);
            pst.setInt(3, 2);
            pst.executeUpdate();
        } 
         try (PreparedStatement pst = con.prepareStatement("insert into composition(idequipe,idjoueur) values (?,?)")) {
            pst.setInt(1, 1);
            pst.setInt(2, 1);
            pst.executeUpdate();
            pst.setInt(1, 1);
            pst.setInt(2, 2);
            pst.executeUpdate(); 
            pst.setInt(1, 2);
            pst.setInt(2, 3);
            pst.executeUpdate();
            pst.setInt(1, 2);
            pst.setInt(2, 4);
            pst.executeUpdate();
            pst.setInt(1, 3);
            pst.setInt(2, 5);
            pst.executeUpdate();
            pst.setInt(1, 3);
            pst.setInt(2, 3);
            pst.executeUpdate();
            pst.setInt(1, 4);
            pst.setInt(2, 4);
            pst.executeUpdate();
            pst.setInt(1, 4);
            pst.setInt(2, 2);
            pst.executeUpdate();
        }
         try (PreparedStatement pst = con.prepareStatement("insert into utilisateurs(idutilisateur,mdp) values (?,?)")) {
            pst.setString(1, "m3_tmaringer01");
            pst.setString(2, "8120c063");
            pst.executeUpdate();
            pst.setString(1, "m3_alefevre01");
            pst.setString(2, "123456"); 
            pst.executeUpdate();
         }
    }

    public BdDTest() {
    }

    /*public static void createBdDTestV2(Connection con) throws SQLException {
        List<Utilisateur> users = List.of(
                new Utilisateur("toto", "p1", 1),
                new Utilisateur("titi", "p2", 2),
                new Utilisateur("tutu", "p2", 2)
        );
        for (var u : users) {
            u.saveInDB(con);
        }
        List<Loisir> loisirs = List.of(
                new Loisir("tennis", "c'est fatiguant"),
                new Loisir("sieste", "c'est reposantm"),
                new Loisir("lecture", "trop intello")
        );
        for (var lo : loisirs) {
            lo.saveInDB(con);
        }
        int[][] apprecient = new int[][]{
            {0, 1},
            {1, 1},
            {1, 2},
            {2, 1},};
        try (PreparedStatement app = con.prepareStatement(
                "insert into apprecie (u1,u2) values (?,?)")) {
            for (int[] a : apprecient) {
                app.setInt(1, users.get(a[0]).getId());
                app.setInt(2, users.get(a[1]).getId());
                app.executeUpdate();
            }
        }
        int[][] pratiques = new int[][]{
            {0, 1, 1},
            {1, 0, 2},
            {1, 2, -2},
            {2, 1, -1},};
        try (PreparedStatement pra = con.prepareStatement(
                "insert into pratique (idutilisateur,idloisir,niveau) values (?,?,?)")) {
            for (int[] p : pratiques) {
                pra.setInt(1, users.get(p[0]).getId());
                pra.setInt(2, loisirs.get(p[1]).getId());
                pra.setInt(3, p[2]);
                pra.executeUpdate();
            }
        }
    }*/

    public static void main(String[] args) {
        try (Connection con = ConnectionSimpleSGBD.defaultCon()) {
            GestionSchema.razBdd(con);
            createBdDTestV1(con);
            /*createBdDTestV2(con);*/
        } catch (SQLException ex) {
            throw new Error(ex);
        }
    }

}
