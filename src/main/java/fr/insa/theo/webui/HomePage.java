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
package fr.insa.theo.webui;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.theo.model.Equipe;
import java.sql.SQLException;
import fr.insa.theo.model.Match;
import fr.insa.theo.model.Ronde;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import fr.insa.theo.model.ConnectionSimpleSGBD;
import fr.insa.theo.model.Joueur;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 *
 * @author theom
 */

@Route(value = "GestionTournoi")
@PageTitle("GestionTournoi")
public class HomePage extends VerticalLayout{
    
    private TextField tfnuméro;
    private TextField tfstatut;
    private TextField tfnum;
    private TextField tfsurnom;
    private TextField tfcatégorie;
    private TextField tftaille;
    
    
    public HomePage() {
        
        //définition de tous les objets
        
        this.tfnuméro= new TextField("Numéro de la ronde");
        this.tfnuméro.addClassName("glass-field");
        this.tfstatut = new TextField("Statut");
        this.tfstatut.addClassName("glass-field");
        this.tfnum = new TextField("Nombre de joueurs");
        this.tfnum.addClassName("glass-field");
        this.tfsurnom= new TextField("Surnom");
        this.tfsurnom.addClassName("glass-field");
        this.tfcatégorie= new TextField("Catégorie");
        this.tfcatégorie.addClassName("glass-field");
        this.tftaille= new TextField("Taille (cm)");
        this.tftaille.addClassName("glass-field");
        
        
        Tab tabGestion = new Tab("Gérer le Tournoi");
        Tab tabStats = new Tab("Statistiques");
        Tab tabCon = new Tab("Connection");
        Tab tabaide = new Tab(new Icon(VaadinIcon.QUESTION_CIRCLE));
        Tabs tabs = new Tabs(tabGestion,tabStats,tabCon,tabaide);
        tabs.setWidthFull();
        tabs.addThemeVariants(TabsVariant.LUMO_CENTERED);
        tabs.addClassName("full-width-tabs");
        VerticalLayout contenuGauche = new VerticalLayout();
        VerticalLayout contenuDroit = new VerticalLayout();
        HorizontalLayout mepGT = new HorizontalLayout(contenuGauche,contenuDroit);
        HorizontalLayout mepS = new HorizontalLayout();
        
       
        BoutonAjout boutonGenererEquipes = new BoutonAjout("🤝 Générer les équipes du Match");
        BoutonAjout ajoutmatchbtn = new BoutonAjout("🏐 Ajouter un match");
        BoutonAjout créerrondebtn = new BoutonAjout("📣 Créer une ronde");
        BoutonAjout ajoutjoueurbtn = new BoutonAjout("🤾‍♂ ️Ajouter un joueur au tournoi");
        BoutonAjout boutonAleatoire = new BoutonAjout("🎲 Composer aléatoirement les équipes");
        ComboBox<Match> selecteurMatch = new ComboBox<>("Sélectionner un match");
        selecteurMatch.addClassName("glass-combobox");
        ComboBox<Ronde> selecteurRonde = new ComboBox<>("Sélectionner la ronde");
        selecteurRonde.addClassName("glass-combobox");
        ComboBox<Equipe> selecteurEquipe = new ComboBox<>("Sélectionner l'équipe");
        selecteurEquipe.addClassName("glass-combobox");
        ComboBox<Joueur> selecteurJoueur = new ComboBox<>("Sélectionner le joueur");
        selecteurJoueur.addClassName("glass-combobox");
        HorizontalLayout hlbutton1 = new HorizontalLayout(tfnuméro,tfstatut);
        HorizontalLayout hlbutton2 = new HorizontalLayout(selecteurRonde);
        HorizontalLayout hlbutton3 = new HorizontalLayout(selecteurMatch);
        HorizontalLayout hlbutton4 = new HorizontalLayout(tfsurnom,tfcatégorie,tftaille);
        HorizontalLayout hlbutton5 = new HorizontalLayout(selecteurJoueur);
        Grid<Joueur> grilleJoueurs = new Grid<>(Joueur.class, false);
        grilleJoueurs.addClassName("glass-grid-v2");    
        
        
        //Barre d'onglets
        mepS.setVisible(false);
        mepS.add(new H2("Statistiques"), new Paragraph("Contenu à venir..."));
        tabs.addSelectedChangeListener(event -> {
        Tab selectedTab = event.getSelectedTab();
        if (selectedTab.equals(tabGestion)) {
            mepGT.setVisible(true);
            mepS.setVisible(false);
        } else if (selectedTab.equals(tabStats)) {
            mepGT.setVisible(false);
            mepS.setVisible(true);
        } else if(selectedTab.equals(tabaide)) {
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
            guide.open();   
        } else if(selectedTab.equals(tabCon)) {
            UI.getCurrent().getPage().setLocation("http://localhost:8080");
        }
    });
       
        grilleJoueurs.addColumn(Joueur::getSurnom).setHeader("Surnom");
        grilleJoueurs.addColumn(Joueur::getCategorie).setHeader("Catégorie");
        grilleJoueurs.addColumn(Joueur::getTaille).setHeader("Taille");
        grilleJoueurs.setHeight("300px");
        grilleJoueurs.setWidth("600px");
        grilleJoueurs.setAllRowsVisible(true);
        grilleJoueurs.addClassName("glass-grid");
        hlbutton1.setSpacing(true);
        hlbutton1.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        hlbutton2.setSpacing(true);
        hlbutton2.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        hlbutton3.setSpacing(true);
        hlbutton3.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        hlbutton4.setSpacing(true);
        hlbutton4.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        hlbutton1.setWidthFull();
        hlbutton2.setWidthFull();
        hlbutton3.setWidthFull();
        hlbutton4.setWidthFull();
        selecteurEquipe.setWidth("200px");
        contenuGauche.add(hlbutton1,créerrondebtn,hlbutton2,ajoutmatchbtn,hlbutton3,boutonGenererEquipes,hlbutton4,ajoutjoueurbtn,hlbutton5,boutonAleatoire);
        contenuDroit.add(grilleJoueurs,selecteurEquipe);
       
        // Ajout de tous les composants dans le VerticalLayout (Vue principale)
        this.add(tabs,mepGT);
        this.setPadding(false);
        this.setSpacing(false);
        this.setSizeFull();
        mepGT.setPadding(true);
        mepS.setPadding(true);
        
     
        
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
        
        try {
        selecteurEquipe.setItems(Equipe.getAllTeams());
        selecteurEquipe.setItemLabelGenerator(equipe -> 
            "ID: " + equipe.getId() + 
            " Score " + equipe.getScore() +" Match: " + equipe.getIdmatch()
        ); 
        } catch (SQLException e) {
         e.printStackTrace(); }
        
        
        boutonGenererEquipes.addClickListener(click -> {
        Match match = selecteurMatch.getValue();

        // On utilise une valeur par défaut pour le "nombre de joueurs" (ex: 0 ou la valeur du champ texte)
        // Ici je prends 0 par défaut pour éviter les erreurs de parsing si le champ est vide
        int nombreJoueursParDefaut = 0; 
        try {
            if (!tfnum.getValue().isEmpty()) {
                nombreJoueursParDefaut = Integer.parseInt(tfnum.getValue());
            }
        } catch (NumberFormatException e) { /* Ignorer */ }

        if (match == null) {
            Notification.show("Veuillez sélectionner un match d'abord !");
            return;
        }

        try {
            // 1. On regarde combien d'équipes existent déjà pour ce match
            // (J'utilise getEquipesDuMatch que nous avons créé précédemment)
            List<Equipe> equipesActuelles = Equipe.getEquipesDuMatch(match.getId());
            int nbEquipes = equipesActuelles.size();

            if (nbEquipes >= 2) {
                Notification.show("Ce match a déjà ses 2 équipes !");
                return;
            }

            // 2. On crée les équipes manquantes (pour arriver à 2)
            int aCreer = 2 - nbEquipes;
            for (int i = 0; i < aCreer; i++) {
                Equipe.créerEquipe(nombreJoueursParDefaut, match);
            }

            Notification.show(aCreer + " équipe(s) générée(s) automatiquement pour le match " + match.getId());

        

        } catch (SQLException ex) {
            Notification.show("Erreur lors de la génération : " + ex.getMessage());
            ex.printStackTrace();
        }
    });
       
     // Création des joueurs
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
       
    //Composition des équipes
        boutonAleatoire.addClickListener(click -> {
        Match match = selecteurMatch.getValue();
        final int MAX_PLAYERS = 6; // La nouvelle limite

        if (match == null) {
            Notification.show("Veuillez sélectionner un match d'abord !");
            return;
        }

        try {
            // 1. Récupération des données
            List<Equipe> equipesDuMatch = Equipe.getEquipesDuMatch(match.getId());
            List<Joueur> tousLesJoueurs = Joueur.getAllPlayers();
            int idRonde = match.getIdronde();

            // 2. Filtrage : On ne garde que les joueurs LIBRES dans cette ronde
            List<Joueur> joueursDisponibles = new ArrayList<>();
            for (Joueur j : tousLesJoueurs) {
                // "Le Gendarme" : On vérifie s'il joue déjà ailleurs dans la même ronde
                if (!Joueur.estDejaInscritDansRonde(j.getId(), idRonde)) {
                    joueursDisponibles.add(j);
                }
            }

            // 3. MÉLANGE ALÉATOIRE
            Collections.shuffle(joueursDisponibles);

            // 4. Distribution intelligente
            int compteurAjouts = 0;

            for (Equipe eq : equipesDuMatch) {
                // On regarde combien de places sont DÉJÀ prises
                List<Joueur> membresActuels = Equipe.getJoueursDeLEquipe(eq.getId());
                int nbJoueursActuels = membresActuels.size();

                // On calcule combien de places il reste avant d'atteindre 6
                int placesLibres = MAX_PLAYERS - nbJoueursActuels;

                // On remplit les places vides (si des joueurs sont dispos)
                for (int i = 0; i < placesLibres; i++) {
                    if (joueursDisponibles.isEmpty()) {
                        break; // Plus de joueurs sous la main !
                    }

                    Joueur chanceux = joueursDisponibles.remove(0); // On prend le premier
                    Equipe.ajouterJoueurDansEquipe(eq.getId(), chanceux.getId());
                    compteurAjouts++;
                }
            }

            // 5. Feedback utilisateur
            if (compteurAjouts > 0) {
                Notification.show(compteurAjouts + " joueurs répartis aléatoirement (Max " + MAX_PLAYERS + "/équipe) !");

                // Si une équipe était sélectionnée, on rafraîchit la grille pour voir le résultat
                if (selecteurEquipe.getValue() != null) {
                    grilleJoueurs.setItems(Equipe.getJoueursDeLEquipe(selecteurEquipe.getValue().getId()));
                }
            } else {
                Notification.show("Aucun joueur ajouté (Équipes complètes ou plus de joueurs disponibles).");
            }

        } catch (SQLException ex) {
            Notification.show("Erreur BDD : " + ex.getMessage());
            ex.printStackTrace();
        }
    });
        
    //Bouton supprimer un joueur 
    grilleJoueurs.addComponentColumn(joueur -> {
    
    Button boutonSupprimer = new Button(new Icon(VaadinIcon.TRASH));
    boutonSupprimer.addClassName("glass-button");
    
    boutonSupprimer.addClickListener(e -> {
        // --- Création d'une boite de dialogue de confirmation ---
        Dialog confirmDialog = new Dialog();
        confirmDialog.setHeaderTitle("Supprimer " + joueur.getSurnom() + " ?");
        confirmDialog.add("Êtes-vous sûr ? Cette action est irréversible.");
        
        Button btnOui = new Button("Oui, supprimer", click -> {
            try (Connection con = ConnectionSimpleSGBD.defaultCon()) {
                // APPEL DE LA MÉTHODE DU MODÈLE
                joueur.delete(con);
                
                Notification.show("Joueur supprimé.");

                confirmDialog.close();
            } catch (SQLException ex) {
                Notification.show("Erreur : " + ex.getMessage());
            }
        });
        Button btnNon = new Button("Annuler", click -> confirmDialog.close());
        confirmDialog.getFooter().add(btnNon, btnOui);
        confirmDialog.open();
    });
    
    return boutonSupprimer;
    });
        
        
   
    }
    
}
    
