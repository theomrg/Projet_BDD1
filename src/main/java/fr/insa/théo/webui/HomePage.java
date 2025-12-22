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
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import fr.insa.théo.model.ConnectionSimpleSGBD;
import fr.insa.théo.model.Joueur;
import java.sql.Connection;
import java.time.LocalDate;
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
    private TextField tfsurnom;
    private TextField tfcatégorie;
    private TextField tfprénom;
    private TextField tfnom;
    private TextField tfsexe;
    private DatePicker dateN;
    private TextField tfnomEquipe;
    private Grid<Ronde> gridRondes;
    private Grid<Match> gridMatchs;
    private Grid<Equipe> gridEquipes;
    
    
    public HomePage() {
        
        //définition de tous les objets
        
        this.tfnuméro= new TextField("Numéro de la ronde");
        this.tfnuméro.addClassName("glass-field");
        this.tfstatut = new TextField("Statut");
        this.tfstatut.addClassName("glass-field");
        this.tfnom = new TextField("Nom");
        this.tfnom.addClassName("glass-field");
        this.tfsurnom= new TextField("Surnom");
        this.tfsurnom.addClassName("glass-field");
        this.tfcatégorie= new TextField("Catégorie");
        this.tfcatégorie.addClassName("glass-field");
        this.tfprénom= new TextField("Prénom");
        this.tfprénom.addClassName("glass-field");
        this.tfsexe= new TextField("Sexe");
        this.tfsexe.addClassName("glass-field");
        this.dateN= new DatePicker("Date de Naissance");
        this.dateN.addClassName("glass-field");
        this.tfnomEquipe= new TextField("Nom de l'équipe");
        this.tfnomEquipe.addClassName("glass-field");
       
        
        Tab tabGestion = new Tab("Gérer le Tournoi");
        Tab tabStats = new Tab("Statistiques");
        Tab tabCon = new Tab("Connection");
        Tab tabaide = new Tab(new Icon(VaadinIcon.QUESTION_CIRCLE));
        Tabs tabs = new Tabs(tabGestion,tabStats,tabCon,tabaide);
        tabs.setWidthFull();
        tabs.addThemeVariants(TabsVariant.LUMO_CENTERED);
        tabs.addClassName("full-width-tabs");
        VerticalLayout contenuGauche = new VerticalLayout();
        VerticalLayout contenuMid = new VerticalLayout();
        VerticalLayout contenuDroit = new VerticalLayout();
        HorizontalLayout mepGT = new HorizontalLayout(contenuGauche,contenuMid,contenuDroit);
        HorizontalLayout mepS = new HorizontalLayout();
        DatePicker.DatePickerI18n i18n = new DatePicker.DatePickerI18n();
        i18n.setDateFormat("dd/MM/yyyy");
        dateN.setI18n(i18n);
        
        Span accueil = new Span("Bienvenue sur l'éditeur de Tournoi !");
        accueil.addClassName("texte-custom-gras");
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
        selecteurEquipe.setWidth("200px");
        ComboBox<Joueur> selecteurJoueur = new ComboBox<>("Sélectionner le joueur");
        selecteurJoueur.addClassName("glass-combobox");
        HorizontalLayout hlbutton1 = new HorizontalLayout(tfnuméro,tfstatut);
        HorizontalLayout hlbutton2 = new HorizontalLayout(selecteurRonde);
        HorizontalLayout hlbutton3 = new HorizontalLayout(tfnomEquipe,selecteurMatch);
        HorizontalLayout hlbutton4 = new HorizontalLayout(tfprénom,tfnom,tfsurnom,tfcatégorie,tfsexe,dateN);
        Details detailsRonde = new Details("🛠️ Créer les Rondes", hlbutton1,créerrondebtn);
        detailsRonde.addClassName("glass-details");
        Details detailsMatch = new Details("🛠️ Créer les Matchs", hlbutton2,ajoutmatchbtn);
        detailsMatch.addClassName("glass-details");
        Details detailsEquipe = new Details("🛠️ Créer less Equipes", hlbutton3,boutonGenererEquipes);
        detailsEquipe.addClassName("glass-details");
        Details detailsJoueur = new Details("🛠 Ajouter les joueurs️", hlbutton4,ajoutjoueurbtn);
        detailsJoueur.addClassName("glass-details");
        Details detailsCompo = new Details("🛠️ Composer les équipes", selecteurMatch,boutonAleatoire);
        Grid<Joueur> grilleJoueurs = new Grid<>(Joueur.class, false);
        grilleJoueurs.addClassName("glass-grid-v2");    
       
        
        
        //Barre d'onglets
        mepS.setVisible(false);
        mepS.add(new H2("Statistiques"), new Paragraph("Contenu à venir..."));
        tabs.addSelectedChangeListener(event -> {
        Tab selectedTab = event.getSelectedTab();
        if (selectedTab.equals(tabGestion)) {
            rafraichirToutesLesDonnees();
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
        grilleJoueurs.setHeight("300px");
        grilleJoueurs.setWidth("600px");
        grilleJoueurs.setAllRowsVisible(true);
        grilleJoueurs.addClassName("glass-grid-v2");
        grilleJoueurs.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_NO_BORDER);
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
        
        // --- 1. Grid des RONDES ---
        gridRondes = new Grid<>(Ronde.class, false);
        gridRondes.addClassName("glass-grid-v2");
        gridRondes.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_NO_BORDER);
        gridRondes.addColumn(Ronde::getNumero).setHeader("Numéro");
        gridRondes.addColumn(Ronde::getStatut).setHeader("Statut");
        gridRondes.setAllRowsVisible(true);
        gridRondes.setHeight("300px");
        gridRondes.setWidth("600px");

        // --- 2. Grid des MATCHS ---
        gridMatchs = new Grid<>(Match.class, false);
        gridMatchs.addClassName("glass-grid-v2");
        gridMatchs.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_NO_BORDER);
        gridMatchs.addColumn(Match::getId).setHeader("Match");
        gridMatchs.addColumn(Match::getStatut).setHeader("Statut");
        gridMatchs.addColumn(Match::getIdronde).setHeader("Ronde");
        gridMatchs.setAllRowsVisible(true);
        gridMatchs.setHeight("300px");
        gridMatchs.setWidth("600px");

        // --- 3. Grid des ÉQUIPES ---
        gridEquipes = new Grid<>(Equipe.class, false);
        gridEquipes.addClassName("glass-grid-v2");
        gridEquipes.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_NO_BORDER);
        gridEquipes.addColumn(Equipe::getNomEquipe).setHeader("Nom");
        gridEquipes.addColumn(Equipe::getScore).setHeader("Score");
        gridEquipes.addColumn(Equipe::getIdmatch).setHeader("Match");
        gridEquipes.setAllRowsVisible(true);
        gridEquipes.setHeight("300px");
        gridEquipes.setWidth("600px");
        
        
        
        contenuGauche.add(detailsRonde,detailsMatch,detailsEquipe,detailsJoueur,detailsCompo);
        contenuMid.add(grilleJoueurs,selecteurEquipe,gridEquipes);
        contenuDroit.add(gridRondes,selecteurJoueur,gridMatchs);
       
        // Ajout de tous les composants dans le VerticalLayout (Vue principale)
        this.add(tabs,accueil,mepGT);
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
                rafraichirToutesLesDonnees();
        });
        // Création des matchs
        ajoutmatchbtn.addClickListener(a -> {
            Ronde rondeSelectionnee = selecteurRonde.getValue();
                if (rondeSelectionnee != null) {
                    // On appelle la méthode modifiée en passant tout l'objet
                    Match.créerMatch(rondeSelectionnee); 
                    rafraichirToutesLesDonnees();
                } else {
                    Notification.show("Veuillez sélectionner une ronde d'abord !");
                    }
        });
        
        // Création des équipes
        try {
        selecteurMatch.setItems(Match.getAllMatchs());
        selecteurMatch.setItemLabelGenerator(match -> 
            "Match n° : " + match.getId() 
        );
        } catch (SQLException e) {
         e.printStackTrace(); }
        
        try {
        selecteurEquipe.setItems(Equipe.getAllTeams());
        selecteurEquipe.setItemLabelGenerator(equipe -> 
        equipe.getNomEquipe()    
        ); 
        } catch (SQLException e) {
         e.printStackTrace(); }
        
        
                boutonGenererEquipes.addClickListener(click -> {
                    Match match = selecteurMatch.getValue();

                    // Vérification de base
                    if (match == null) {
                        Notification.show("Veuillez sélectionner un match d'abord !");
                        return;
                    }

        try {
            // 1. On regarde combien d'équipes existent déjà pour ce match
            List<Equipe> equipesActuelles = Equipe.getEquipesDuMatch(match.getId());
            int nbEquipes = equipesActuelles.size();

            if (nbEquipes >= 2) {
                Notification.show("Ce match a déjà ses 2 équipes !");
                return;
            }

            // 2. On calcule combien il en manque
            int aCreer = 2 - nbEquipes;

            // 3. Boucle de création
            for (int i = 0; i < aCreer; i++) {
                // Génération automatique du nom : 
                // Si 0 équipe existe, on crée "Équipe 1" puis "Équipe 2"
                // Si 1 équipe existe déjà, on crée "Équipe 2"
                int numeroEquipe = nbEquipes + i + 1; 
                String nomAutomatique = "Équipe " + numeroEquipe;

                // Appel de la méthode mise à jour (Nom + Match)
                // On passe 0 comme score initial par défaut
                Equipe.créerEquipe(nomAutomatique, match);
            }

            Notification.show(aCreer + " équipe(s) générée(s) pour le match " + match.getId());

            // 4. Mise à jour de l'affichage
            rafraichirToutesLesDonnees();
            // Optionnel : mettre à jour le sélecteur si besoin
            selecteurEquipe.setItems(Equipe.getEquipesDuMatch(match.getId()));

        } catch (SQLException ex) {
            Notification.show("Erreur lors de la génération : " + ex.getMessage());
            ex.printStackTrace();
        }
    });
       
     // Création des joueurs
       ajoutjoueurbtn.addClickListener(g -> {
           String prenom = tfprénom.getValue();
           String nom = tfprénom.getValue();
           String surnom =tfsurnom.getValue();
           String catégorie =tfcatégorie.getValue();
           String sexe= tfsexe.getValue();
           LocalDate dateNaissance = dateN.getValue();
           Joueur.créerJoueur(surnom, catégorie, prenom, nom, sexe, dateNaissance);  
           rafraichirToutesLesDonnees();
       });
       
       try {
        selecteurJoueur.setItems(Joueur.getAllPlayers());
        selecteurJoueur.setItemLabelGenerator(joueur ->  
        "Prénom : " + joueur.getPrénom()+ 
        " | Nom : " + joueur.getNom()+ 
        " | Surnom : " + joueur.getSurnom()+
        " | Sexe : "  + joueur.getSexe() +
        " | Date de Naissance: " + joueur.getDateNaissance() +
        " | Catégorie : " + joueur.getCategorie()
        ); } catch (SQLException ex) {
         ex.printStackTrace(); }
      
       selecteurEquipe.addValueChangeListener(event -> {
       Equipe eq = event.getValue();
       if (eq != null) {
            try {
                    
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
        rafraichirToutesLesDonnees();
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
    private void rafraichirToutesLesDonnees() {
        try {
            if(gridRondes != null) gridRondes.setItems(Ronde.getAllRondes());
            if(gridMatchs != null) gridMatchs.setItems(Match.getAllMatchs());
            if(gridEquipes != null) gridEquipes.setItems(Equipe.getAllTeams());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
    
