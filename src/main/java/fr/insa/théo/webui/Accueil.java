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
import com.vaadin.flow.component.combobox.ComboBox;
import fr.insa.théo.model.Joueur;


import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.router.PageTitle;
import fr.insa.théo.model.Match;
import fr.insa.théo.model.Ronde;
import java.sql.SQLException;

/**
 *
 * @author theom
 */
@Route(value = "Accueil")
@PageTitle("Accueil")


public class Accueil extends VerticalLayout {

    private TextField tfsurnom;
    private TextField tfcategorie;
    private TextField tftaillecm;
    private TextField tfnum;
    private TextField tfronde;
    private TextField tfscore;
    private TextField tfnuméro;
    private TextField tfstatut;
    private TextField tfidmatch;
    private Div contenu;
    private BoutonOnglet confirmer;
    
    public Accueil() {
        
        this.tfsurnom = new TextField("Surnom");
        this.tfcategorie = new TextField("Catégorie");
        this.tftaillecm = new TextField("Taille");
        this.tfnum = new TextField("Nombre de joueurs");
        this.tfronde = new TextField("ID de la Ronde");
        this.tfscore= new TextField("Score");
        this.tfidmatch = new TextField("Id du match");
        this.tfnuméro= new TextField("Numéro de la ronde");
        this.tfstatut = new TextField("Statut");
        this.confirmer = new BoutonOnglet("Confirmer");
        
        ComboBox<Ronde> selecteurRonde = new ComboBox<>("Choisir la ronde");
        HorizontalLayout setattributs = new HorizontalLayout();
        BoutonAjout ajoutjoueurbtn = new BoutonAjout("Ajouter un joueur");
        BoutonAjout ajoutéquipebtn = new BoutonAjout("Ajouter une équipe");
        BoutonAjout ajoutmatchbtn = new BoutonAjout("Ajouter un match");
        BoutonAjout créerrondebtn = new BoutonAjout("Créer une ronde");
        BoutonOnglet joueurBtn = new BoutonOnglet("Joueurs");
        BoutonOnglet gestionTournoiBtn = new BoutonOnglet("Gérer le tournoi");
        this.contenu = new Div(); 
        contenu.addClassName("contenu");
        contenu.setWidthFull();
        contenu.setHeight("250px");
        
        
        gestionTournoiBtn.addClickListener(v -> {
            UI.getCurrent().getPage().setLocation("http://localhost:8080/GestionTournoi");
        
        });
       
        joueurBtn.addClickListener(e -> {
        contenu.removeAll();
        contenu.add(ajoutjoueurbtn);
        ajoutjoueurbtn.addClickListener(t -> {
            setattributs.removeAll();
            setattributs.add(this.tfcategorie,this.tfsurnom,this.tftaillecm);
            contenu.add(setattributs);
            contenu.add(confirmer);
            confirmer.addClickListener(a -> {
                String surnom = tfsurnom.getValue();
                String catégorie =tfcategorie.getValue();
                int taillecm = Integer.parseInt(tftaillecm.getValue());
                Joueur.créerJoueur(surnom, catégorie, taillecm);
                  });
              });
          });

        /*equipeBtn.addClickListener(a -> {
        contenu.removeAll();
        contenu.add(ajoutéquipebtn);
        ajoutéquipebtn.addClickListener(t -> {
            setattributs.removeAll();
            setattributs.add(this.tfnum,this.tfidmatch);
            contenu.add(setattributs);
            contenu.add(confirmer);
            confirmer.addClickListener(x -> {
                int num = Integer.parseInt(tfnum.getValue());
                int idmatch = Integer.parseInt(tfidmatch.getValue());
              
            });
         });
        }); */

        
        // Barre de navigation horizontale
        HorizontalLayout barreOnglets = new HorizontalLayout(joueurBtn,gestionTournoiBtn);
        barreOnglets.setWidthFull();
        barreOnglets.setSpacing(true);
        barreOnglets.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        this.add(barreOnglets,contenu); 
        // Ajout à la vue principale
       
    }
    public static void append(TextArea ou, String quoi) {
        ou.setValue(ou.getValue() + quoi);
    }

}
