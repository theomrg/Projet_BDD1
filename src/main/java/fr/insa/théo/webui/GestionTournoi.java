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
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.théo.model.Equipe;
import java.sql.SQLException;
import fr.insa.théo.model.Match;
import fr.insa.théo.model.Ronde;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.server.VaadinSession;
import fr.insa.théo.model.Joueur;
import java.util.List;


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
    private TextField tfsurnom;
    private TextField tfcatégorie;
    private TextField tftaille;
    
    
    public GestionTournoi() {
        
        //définition de tous les objets
        
        this.tfnuméro= new TextField("Numéro de la ronde");
        this.tfstatut = new TextField("Statut");
        this.tfnum = new TextField("Nombre de joueurs");
        this.tfsurnom= new TextField("Surnom");
        this.tfcatégorie= new TextField("Catégorie");
        this.tftaille= new TextField("Taille (cm)");
        
        BoutonAjout ajoutéquipebtn = new BoutonAjout("Ajouter une équipe");
        BoutonAjout ajoutmatchbtn = new BoutonAjout("Ajouter un match");
        BoutonAjout créerrondebtn = new BoutonAjout("Créer une ronde");
        BoutonAjout ajoutjoueurbtn = new BoutonAjout("Ajouter un joueur au tournoi");
        BoutonAjout boutonCompoEquipe = new BoutonAjout("Ajouter le joueur sélectionné à l'équipe");
        ComboBox<Match> selecteurMatch = new ComboBox<>("Sélectionner un match");
        ComboBox<Ronde> selecteurRonde = new ComboBox<>("Sélectionner la ronde");
        ComboBox<Equipe> selecteurEquipe = new ComboBox<>("Sélectionner l'équipe");
        ComboBox<Joueur> selecteurJoueur = new ComboBox<>("Sélectionner le joueur");
        HorizontalLayout hlbutton1 = new HorizontalLayout(tfnuméro,tfstatut);
        HorizontalLayout hlbutton2 = new HorizontalLayout(selecteurRonde);
        HorizontalLayout hlbutton3 = new HorizontalLayout(tfnum,selecteurMatch);
        HorizontalLayout hlbutton4 = new HorizontalLayout(tfsurnom,tfcatégorie,tftaille,selecteurEquipe,selecteurJoueur);
        Grid<Joueur> grilleJoueurs = new Grid<>(Joueur.class, false);
        
        grilleJoueurs.addColumn(Joueur::getSurnom).setHeader("Surnom");
        grilleJoueurs.addColumn(Joueur::getCategorie).setHeader("Catégorie");
        grilleJoueurs.addColumn(Joueur::getTaille).setHeader("Taille");
        grilleJoueurs.setHeight("200px");
        grilleJoueurs.setWidth("500px");
        hlbutton1.setSpacing(true);
        hlbutton1.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        hlbutton2.setSpacing(true);
        hlbutton2.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        hlbutton3.setSpacing(true);
        hlbutton3.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        hlbutton4.setSpacing(true);
        hlbutton4.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        hlbutton1.setWidthFull();
        hlbutton2.setWidthFull();
        hlbutton3.setWidthFull();
        hlbutton4.setWidthFull();
        selecteurEquipe.setWidth("200px");
        // Ajout de tous les composants dans le VerticalLayout (Vue principale)
        this.add(hlbutton1,créerrondebtn,hlbutton2,ajoutmatchbtn,hlbutton3,ajoutéquipebtn,hlbutton4,ajoutjoueurbtn,boutonCompoEquipe,grilleJoueurs);
        this.setPadding(true);
        this.setAlignItems(Alignment.CENTER);
        
        // Fenêtre pop-up
        Dialog guide = new Dialog();
        guide.setHeaderTitle("Bienvenue dans le Gestionnaire de Tournoi !");
        VerticalLayout contenu = new VerticalLayout();
        contenu.setSpacing(false);
        contenu.setPadding(false);
        contenu.add(new H3("Comment ça marche ?"));
        contenu.add(new Paragraph("1️. Sélectionnez une ronde ou créez-en une nouvelle."));
        contenu.add(new Paragraph("2. Ajoutez un match à la ronde en sélectionnant une des rondes dans le menu déroulant."));
        contenu.add(new Paragraph("3. Sélectionnez un match dans le menu déroulant pour créer les équipes."));
        contenu.add(new Paragraph("4. Ajoutez dans un premier temps les joueurs au tournoi, puis sélectionnez dans les menus déroulants un joueur et un équipe pour faire la composition."));
        guide.add(contenu);
        Button boutonCompris = new Button("C'est parti !", e -> guide.close());
        boutonCompris.addClassName("bouton-onglet");
        boutonCompris.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        guide.getFooter().add(boutonCompris);
        if (VaadinSession.getCurrent().getAttribute("guideDejaVu") == null) {
        guide.open();
        VaadinSession.getCurrent().setAttribute("guideDejaVu", true);
        }
        
        //Création des rondes
        try {
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
        // Création des matchs
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
        
        // Création des équipes
        try {
        selecteurMatch.setItems(Match.getAllMatchs());
        selecteurMatch.setItemLabelGenerator(match -> 
            "ID du match : " + match.getId() + 
            " (Ronde " + match.getIdronde()+")"
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
       // Création des joueurs
       try {
        selecteurEquipe.setItems(Equipe.getAllTeams());
        selecteurEquipe.setItemLabelGenerator(equipe -> 
        "ID : " + equipe.getId() + 
        " | Nombre de joueurs : " + equipe.getNum() + 
        " | Score : " + equipe.getScore() + 
        " | Match : " + equipe.getIdmatch()
        ); } catch (SQLException ex) {
         ex.printStackTrace(); }
    
       ajoutjoueurbtn.addClickListener(g -> {
           int taille = Integer.parseInt(tftaille.getValue());
           String surnom =tfsurnom.getValue();
           String catégorie =tfcatégorie.getValue();
           Joueur.créerJoueur(surnom, catégorie, taille);  
       });
       
       try {
        selecteurJoueur.setItems(Joueur.getAllPlayers());
        selecteurJoueur.setItemLabelGenerator(joueur ->  
        "Surnom : " + joueur.getSurnom()+ 
        " | Catégorie : " + joueur.getCategorie()+ 
        " | Taille : " + joueur.getTaille()
        ); } catch (SQLException ex) {
         ex.printStackTrace(); }
      
       selecteurEquipe.addValueChangeListener(event -> {
       Equipe eq = event.getValue();
       if (eq != null) {
            try {
                // On récupère les joueurs DE CETTE ÉQUIPE (méthode vue précédemment)
                List<Joueur> listeJoueursE = Equipe.getJoueursDeLEquipe(eq.getId());
                grilleJoueurs.setItems(listeJoueursE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            grilleJoueurs.setItems(); // On vide si rien n'est sélectionné
        }
    });
    
        boutonCompoEquipe.addClickListener(click -> {
        Equipe eq = selecteurEquipe.getValue();
        Joueur j = selecteurJoueur.getValue();

        // Vérification que les deux sont bien sélectionnés
        if (eq != null && j != null) {
            try {
                // A. On appelle le modèle pour faire l'INSERT SQL
                Equipe.ajouterJoueurDansEquipe(eq.getId(), j.getId());

                Notification.show(j.getSurnom() + " ajouté à l'équipe " + eq.getId() + " !");

                // B. Mise à jour immédiate de l'affichage (Grille)
                // On recharge la liste des membres pour voir le nouveau venu
                List<Joueur> membresAJour = Equipe.getJoueursDeLEquipe(eq.getId());
                grilleJoueurs.setItems(membresAJour);

                // C. Optionnel : On vide le sélecteur de joueur pour enchainer
                selecteurJoueur.clear();

            } catch (SQLException ex) {
                // Gestion des erreurs (ex: Joueur déjà dans l'équipe - Doublon clé primaire)
                if (ex.getMessage().contains("Duplicate")) { // Message d'erreur SQL typique
                     Notification.show("Ce joueur est déjà dans cette équipe !");
                } else {
                     Notification.show("Erreur SQL : " + ex.getMessage());
                }
            }
        } else {
            Notification.show("Veuillez sélectionner une équipe ET un joueur.");
        }
    });
    }
    
}
    
