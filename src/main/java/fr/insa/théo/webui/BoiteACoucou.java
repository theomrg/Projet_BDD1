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

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import fr.insa.beuvron.utils.database.ConnectionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author theom
 */
@Route(value="BAC")
public class BoiteACoucou extends VerticalLayout {
    private TextField tfnom;
    private TextArea tamessage;
    private Button bcoucou;
    private BoutonImportant bsalut;
    private HorizontalLayout hlbutton;

    public BoiteACoucou() {
        this.tfnom = new TextField("nom");
        this.tamessage = new TextArea();
        this.tamessage.setWidth("75%");
        this.tamessage.setHeight("20em");
        this.bcoucou = new Button("coucou2222");
        this.bcoucou.getStyle().set("color", "red");
        this.bcoucou.addClickListener((t) -> {
            String nom = this.tfnom.getValue();
            append(this.tamessage,"coucou " + nom + "\n" );
            try (Connection con = ConnectionPool.getConnection()) {
                //select categorie from joueur where surnom = 'toto'
                PreparedStatement pst = con.prepareStatement(
                "select categorie from joueur where surnom = ?");
                pst.setString(1, nom);
                ResultSet res = pst.executeQuery();
                if (res.next()) {
                    String cat = res.getString("categorie");
                    append(this.tamessage,"vous êtes catégorie" + cat);
                
                    
                } else {
                    append(this.tamessage,"vous n'existez pas");
                }
                
            } catch (SQLException ex) {
                Notification.show("problème : "+ ex.getMessage());
            }
        });
        this.bsalut = new BoutonImportant("salut");
        this.hlbutton = new HorizontalLayout(this.bcoucou,this.bsalut);
        this.add(this.tfnom,this.tamessage,this.hlbutton);
        

    }
    public static void append(TextArea ou,String quoi) {
        ou.setValue(ou.getValue()+quoi);
    }
    
    
}
