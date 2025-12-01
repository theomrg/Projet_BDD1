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
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.beuvron.utils.database.ConnectionPool;
import fr.insa.théo.model.Equipe;
import static fr.insa.théo.webui.Accueil.append;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import fr.insa.théo.model.Match;
import fr.insa.théo.model.Ronde;
import fr.insa.théo.model.Joueur;


/**
 *
 * @author theom
 */

@Route(value = "GestionTournoi")
@PageTitle("GestionTournoi")
public class GestionTournoi extends VerticalLayout{
    
    private TextField tfnuméro;
    private TextField tfstatut;
    private TextField tfnum;
    
    
    public GestionTournoi() {
        
        this.tfnuméro= new TextField("Numéro de la ronde");
        this.tfstatut = new TextField("Statut");
        this.tfnum = new TextField("Nombre de joueurs");
        BoutonAjout ajoutéquipebtn = new BoutonAjout("Ajouter une équipe");
        BoutonAjout ajoutmatchbtn = new BoutonAjout("Ajouter un match");
        BoutonAjout créerrondebtn = new BoutonAjout("Créer une ronde");
        ComboBox<Match> selecteurMatch = new ComboBox<>("Sélectionner un match");
        ComboBox<Ronde> selecteurRonde = new ComboBox<>("Choisir la ronde");
        HorizontalLayout hlbutton1 = new HorizontalLayout(tfnuméro,tfstatut);
        BoutonOnglet selectionRonde = new BoutonOnglet("Sélection des rondes");
        
        this.add(créerrondebtn,hlbutton1,ajoutmatchbtn,selecteurRonde,ajoutéquipebtn,this.tfnum,selecteurMatch);
        
        try {
        // 2. On remplit le composant avec la liste venant de la BDD
        selecteurRonde.setItems(Ronde.getAllRondes());
        } catch (SQLException ex) {
         ex.printStackTrace(); }
        
        selecteurRonde.setItemLabelGenerator(ronde -> 
        "Ronde n°" + ronde.getNumero() + " (" + ronde.getStatut() + ") " + "id : " + ronde.getId()
        );
        créerrondebtn.addClickListener(t -> {
                int numéro = Integer.parseInt(tfnuméro.getValue());
                String statut =tfstatut.getValue();
                Ronde.créerRonde(numéro, statut);
        });
        ajoutmatchbtn.addClickListener(a -> {
            Ronde rondeSelectionnee = selecteurRonde.getValue();
                if (rondeSelectionnee != null) {
                    // On appelle la méthode modifiée en passant tout l'objet
                    Match.créerMatch(rondeSelectionnee); 
                    System.out.println("Ronde sélectionnée : " + rondeSelectionnee.getNumero() + " | ID BDD : " + rondeSelectionnee.getId());
                } else {
                    Notification.show("Veuillez sélectionner une ronde d'abord !");
                    }
        });
        
        try {
        // 2. Remplissage avec les données
        selecteurMatch.setItems(Match.getAllMatchs());
        selecteurMatch.setItemLabelGenerator(match -> 
            "ID du match" + match.getId() + 
            " (Ronde " + match.getIdronde()
        );
        } catch (SQLException e) {
         e.printStackTrace(); }
       
        ajoutéquipebtn.addClickListener(b -> {
            Match matchselectionne = selecteurMatch.getValue();
            int num = Integer.parseInt(tfnum.getValue());
            if (matchselectionne == null) {
                Notification.show("Veuillez sélectionner un match d'abord !");
                return;
                 }
                 try {       
                // A. On demande combien il y a déjà d'équipes
                int nbEquipesActuelles = Equipe.getNbEquipesParMatch(matchselectionne.getId());
                // B. On vérifie la limite (2 selon le cahier des charges)
                if (nbEquipesActuelles >= 2) {
                    // C. Si c'est plein, on affiche une erreur et ON S'ARRÊTE
                    Notification.show("Impossible : Ce match a déjà 2 équipes !");
                    return; 
                }
                    // On appelle la méthode modifiée en passant tout l'objet
                    Equipe.créerEquipe(num,matchselectionne); 
                    System.out.println("Match sélectionnée : " + matchselectionne.getId());
                } catch (SQLException e) {
                e.printStackTrace(); }
        });
        
    }
    
}
    
