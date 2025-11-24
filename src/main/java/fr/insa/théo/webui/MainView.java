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
import fr.insa.théo.model.Joueur;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import fr.insa.beuvron.utils.database.ConnectionPool;
import fr.insa.beuvron.vaadin.utils.dataGrid.ResultSetGrid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import fr.insa.théo.model.ConnectionSimpleSGBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author theom
 */
@Route(value = "Projet")


public class MainView extends VerticalLayout {

    private TextField tfsurnom;
    private TextField tfnom;
    private TextField tfcategorie;
    private TextField tftaillecm;
    private TextArea tamessage;
    private Button bcoucou;
    private BoutonOnglet bsalut;
    private HorizontalLayout hlbutton;
    private Div contenu;
    
    public MainView() {
        
        this.tfsurnom = new TextField("Surnom");
        this.tfcategorie = new TextField("Catégorie");
        this.tftaillecm = new TextField("Taille");
        BoutonOnglet confirmerbtn = new BoutonOnglet("Confirmer");
        HorizontalLayout setattributs = new HorizontalLayout(this.tfcategorie,this.tfsurnom,this.tftaillecm,confirmerbtn);
        BoutonOnglet ajoutjoueurbtn = new BoutonOnglet("Ajouter un joueur");
        BoutonOnglet joueurBtn = new BoutonOnglet("Joueurs");
        BoutonOnglet equipeBtn = new BoutonOnglet("Equipes");
        BoutonOnglet matchsBtn = new BoutonOnglet("Matchs");
        this.contenu = new Div(); 
        contenu.setWidthFull();
        contenu.setHeight("250px"); // un peu plus d’espace

        // Style général
        contenu.getStyle()
    .set("background", "#f8f4fc") // fond mauve très clair
    .set("border", "1px solid #d3bce6") // bordure mauve pastel
    .set("border-radius", "16px")
    .set("padding", "30px")
    .set("box-shadow", "0 8px 20px rgba(0,0,0,0.1)")
    .set("font-family", "'Segoe UI', sans-serif")
    .set("color", "#5e3c76") // texte mauve foncé
    .set("transition", "all 0.3s ease");

        
        

        joueurBtn.addClickListener(e -> {
        contenu.removeAll();
        contenu.add(ajoutjoueurbtn);
        ajoutjoueurbtn.addClickListener(t -> {
          contenu.add(setattributs); 
          confirmerbtn.addClickListener(a -> {
            String surnom = tfsurnom.getValue();
            String catégorie =tfcategorie.getValue();
            int taillecm = Integer.parseInt(tftaillecm.getValue());

            try (Connection con = ConnectionPool.getConnection()) {
                Joueur j = new Joueur(surnom,catégorie,taillecm);
                j.saveInDB(ConnectionSimpleSGBD.defaultCon());
                PreparedStatement pst = con.prepareStatement(
                        "insert into joueur (surnom, categorie, taillecm) values (?,?,?)");
                pst.setString(1, surnom);
                pst.setString(2, catégorie);
                pst.setInt(3, taillecm);
                int res = pst.executeUpdate(); }
            catch (SQLException ex) {
                  Notification.show("problème : " + ex.getMessage()); }
                  });
              });
          });

        equipeBtn.addClickListener(e -> {
        contenu.removeAll();
        contenu.add(new Span("Liste des équipes"));
        });

        matchsBtn.addClickListener(e -> {
        contenu.removeAll();
        contenu.add(new Span("Résultats des matchs"));
        });

        // Barre de navigation horizontale
        HorizontalLayout barreOnglets = new HorizontalLayout(joueurBtn, equipeBtn, matchsBtn);
        barreOnglets.setWidthFull();
        barreOnglets.setSpacing(true);
        barreOnglets.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        // Ajout à la vue principale
       
           
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
        this.add(barreOnglets, contenu, tamessage, hlbutton);
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
        
    }
    public static void append(TextArea ou, String quoi) {
        ou.setValue(ou.getValue() + quoi);
    }

}
