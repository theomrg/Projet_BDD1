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
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import fr.insa.beuvron.utils.database.ConnectionPool;
import fr.insa.beuvron.vaadin.utils.dataGrid.ResultSetGrid;
import com.vaadin.flow.component.html.Span;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author theom
 */
@Route(value = "BAC")


public class MainView extends VerticalLayout {

    private TextField tfnom;
    private TextArea tamessage;
    private Button bcoucou;
    private BoutonOnglet bsalut;
    private HorizontalLayout hlbutton;
    private Div contenu;
    
    public MainView() {
        
    this.tfnom = new TextField("nom");
        this.tamessage = new TextArea();
        this.tamessage.setWidth("75%");
        this.tamessage.setHeight("20em");
        this.bcoucou = new Button("coucou");
        this.bcoucou.getStyle().set("color", "red");
        this.bcoucou.addClickListener((t) -> {
            String nom = this.tfnom.getValue();
            append(this.tamessage, "coucou " + nom + "\n");
            try (Connection con = ConnectionPool.getConnection()) {
                //select categorie from joueur where surnom = 'toto'
                PreparedStatement pst = con.prepareStatement(
                        "select categorie from joueur where surnom = ?");
                pst.setString(1, nom);
                ResultSet res = pst.executeQuery();
                if (res.next()) {
                    String cat = res.getString("categorie");
                    append(this.tamessage, "vous êtes catégorie" + cat);

                } else {
                    append(this.tamessage, "vous n'existez pas");
                }

            } catch (SQLException ex) {
                Notification.show("problème : " + ex.getMessage());
            }
        });
        this.bsalut = new BoutonOnglet("salut");
        this.hlbutton = new HorizontalLayout(this.bcoucou, this.bsalut);
        this.add(this.tfnom, this.tamessage, this.hlbutton);

        int ideq =2;
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(
                    "select equipe.id,equipe.score,matchs.id,matchs.ronde\n"
                    + "from equipe\n"
                    + "  join matchs on equipe.idmatch = matchs.id\n"
                    + "  where equipe.id = ?");
            pst.setInt(1, ideq);
            ResultSetGrid gr = new ResultSetGrid(pst);
            this.add(gr);
        } catch (SQLException ex) {
            Notification.show("Problème : " + ex.getLocalizedMessage());
        }
    BoutonOnglet accueilBtn = new BoutonOnglet("Accueil");
    BoutonOnglet profilBtn = new BoutonOnglet("Profil");
    BoutonOnglet paramètresBtn = new BoutonOnglet("Paramètres");
    this.contenu = new Div(); // zone d'affichage
    contenu.setWidthFull();
    contenu.setHeight("200px");
    contenu.getStyle().set("border", "1px solid #ccc");
    contenu.getStyle().set("padding", "20px");
    contenu.getStyle().set("margin-top", "10px");

    accueilBtn.addClickListener(e -> {
            contenu.removeAll();
            contenu.add(new Span("Bienvenue sur l'accueil "));
        });

        profilBtn.addClickListener(e -> {
            contenu.removeAll();
            contenu.add(new Span("Voici ton profil utilisateur "));
        });

        paramètresBtn.addClickListener(e -> {
            contenu.removeAll();
            contenu.add(new Span("Paramètres de l'application ️"));
        });

        // Barre de navigation horizontale
        HorizontalLayout barreOnglets = new HorizontalLayout(accueilBtn, profilBtn, paramètresBtn);
        barreOnglets.setSpacing(true);

        // Ajout à la vue principale
        add(barreOnglets, contenu);
        
        
        
        
        
        
        
    }
    public static void append(TextArea ou, String quoi) {
        ou.setValue(ou.getValue() + quoi);
    }

}
