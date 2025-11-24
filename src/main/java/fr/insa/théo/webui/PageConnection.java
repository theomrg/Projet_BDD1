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
package fr.insa.théo.webui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.beuvron.utils.database.ConnectionPool;
import static fr.insa.théo.webui.MainView.append;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 *
 * @author francois
 */
@Route(value = "")
@PageTitle("Likes")
public class PageConnection extends VerticalLayout {
     private TextField idtf;
     private TextField mdptf;
     private TextArea messageta;
     private TextArea accueilta;
     private BoutonOnglet connectionbtn;

    public PageConnection() {
        this.accueilta = new TextArea("Bienvue sur l'interface de gestion de tournoi");
        this.accueilta.setWidth("50%");
        this.accueilta.setHeight("10em");
        this.messageta = new TextArea();
        this.idtf = new TextField("Identifiant");
        this.mdptf = new TextField("Mot de passe");
        this.connectionbtn = new BoutonOnglet("Connection");
        this.messageta = new TextArea();
        this.connectionbtn.addClickListener((t) -> {
            String idutilisateur = this.idtf.getValue();
            String mdp = this.mdptf.getValue();
            try (Connection con = ConnectionPool.getConnection()) {
                PreparedStatement pst = con.prepareStatement(
                        "SELECT * FROM utilisateurs WHERE idutilisateur=? AND mdp=?");
                pst.setString(1, idutilisateur);
                pst.setString(2, mdp);
                ResultSet res = pst.executeQuery();
                if (res.next()) {
                UI.getCurrent().getPage().setLocation("http://localhost:8080/Projet");

            } else {
                append(this.messageta, "Identifiant ou mot de passe incorect");
            }
            } catch (SQLException ex) {
                Notification.show("problème : " + ex.getMessage());
            }    
            });    
        this.add(accueilta,idtf,mdptf,connectionbtn,messageta);
        
        
       
    }

}
