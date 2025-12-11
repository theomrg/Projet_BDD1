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
        
        
        BoutonOnglet statBtn = new BoutonOnglet("Statistiques");
        BoutonOnglet gestionTournoiBtn = new BoutonOnglet("Gérer le tournoi");
        gestionTournoiBtn.addClickListener(v -> {
            UI.getCurrent().getPage().setLocation("http://localhost:8080/GestionTournoi");
        
        });
       
        statBtn.addClickListener(e -> {
  
        });

        HorizontalLayout barreOnglets = new HorizontalLayout(statBtn,gestionTournoiBtn);
        barreOnglets.setWidthFull();
        barreOnglets.setSpacing(true);
        barreOnglets.addClassName("barre-onglets");
        barreOnglets.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        this.add(barreOnglets); 
       
       
    }
    public static void append(TextArea ou, String quoi) {
        ou.setValue(ou.getValue() + quoi);
    }

}
