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
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.dependency.CssImport;


/** v4
 *
 * @author theom
 */

public class BoutonOnglet extends Button {
    public BoutonOnglet(String label) {
        super(label);

        // Style de base (mauve)
        this.getStyle().set("background-color", "#8e44ad");
        this.getStyle().set("color", "white");
        this.getStyle().set("font-weight", "bold");
        this.getStyle().set("border-radius", "8px");
        this.getStyle().set("padding", "10px 20px");
        this.getStyle().set("transition", "all 0.3s ease");

        // Hover : couleur plus claire
        this.getElement().addEventListener("mouseover", e -> {
            this.getStyle().set("background-color", "#9b59b6");
            this.getStyle().set("transform", "scale(1.05)");
            this.getStyle().set("box-shadow", "0 6px 10px rgba(0,0,0,0.3)");
        });

        // Mouse out : revenir au style initial
        this.getElement().addEventListener("mouseout", e -> {
            this.getStyle().set("background-color", "#8e44ad");
            this.getStyle().remove("transform");
            this.getStyle().remove("box-shadow");
        });

 /*this.tfnom = new TextField("nom");
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
     */  
    
    /*Tabs ongletsmanager = new Tabs();
    Tab joueur = new Tab("Joueur");
    Tab equipe = new Tab("Equipe");
    Tab matchs= new Tab("Matchs");
    ongletsmanager.add(joueur, equipe, matchs);
    
    this.contenu = new Div(); // zone d'affichage
    contenu.setWidthFull();
    contenu.setHeight("200px");
    contenu.getStyle().set("border", "1px solid #ccc");
    contenu.getStyle().set("padding", "20px");
    contenu.getStyle().set("margin-top", "10px");
    ongletsmanager.addSelectedChangeListener(event -> {
    contenu.removeAll();
    Tab selected = event.getSelectedTab();
    if (selected.equals(joueur)) {
        contenu.add(new Span("Bienvenue sur l'accueil"));
    } else if (selected.equals(equipe)) {
        contenu.add(new Span("Voici ton profil"));
    } else if (selected.equals(matchs)) {
        contenu.add(new Span("Paramètres à venir"));
    }
});*/
 

    }
}
