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
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.component.textfield.IntegerField;
import fr.insa.beuvron.utils.database.ConnectionPool;
import fr.insa.théo.model.Joueur;
import fr.insa.théo.model.Utilisateur;
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
    private Grid<Joueur> grilleJoueurs;
    private Grid<Joueur> gridAllJoueurs;
    private Grid<Utilisateur> gridUsers;
    
    
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
       
        
        
        H1 titre = new H1("🏆 Bienvenue au tournoi de Volley 🏆");
        H3 hjoueur = new H3("Tous les joueurs du tournoi");
        H3 hronde = new H3("Toutes les rondes du tournoi");
        H3 hmatch = new H3("Tous les matchs du tournoi");
        H3 héquipe = new H3("Toutes les équipes de la ronde sélectionnée");
        H3 hjoueurdeléquipe = new H3("Tous les joueurs de l'équipe sélectionnée");
        Tab tabGestion = new Tab("Gérer le Tournoi");
        Tab tabStats = new Tab("Statistiques");
        Tab tabCon = new Tab("Connection");
        Tab tabaide = new Tab(new Icon(VaadinIcon.QUESTION_CIRCLE));
        Tabs tabs = new Tabs(tabGestion,tabStats,tabCon,tabaide);
        tabs.setWidthFull();
        tabs.addThemeVariants(TabsVariant.LUMO_CENTERED);
        tabs.addClassName("full-width-tabs");
        VerticalLayout contenuGauche = new VerticalLayout();
        contenuGauche.setWidthFull();
        VerticalLayout contenuMid = new VerticalLayout();
        contenuMid.setWidthFull();
        VerticalLayout contenuMid2 = new VerticalLayout();
        contenuMid2.setWidthFull();
        VerticalLayout contenuDroit = new VerticalLayout();
        contenuDroit.setWidthFull();
        HorizontalLayout mepGT = new HorizontalLayout(contenuGauche,contenuMid,contenuMid2,contenuDroit);
        HorizontalLayout mepS = new HorizontalLayout();
        DatePicker.DatePickerI18n i18n = new DatePicker.DatePickerI18n();
        i18n.setDateFormat("dd/MM/yyyy");
        dateN.setI18n(i18n);
        
        
        BoutonAjout boutonGenererEquipes = new BoutonAjout("🤝 Générer les équipes du Match");
        BoutonAjout ajoutmatchbtn = new BoutonAjout("🏐 Ajouter un match");
        BoutonAjout créerrondebtn = new BoutonAjout("📣 Créer une ronde");
        BoutonAjout ajoutjoueurbtn = new BoutonAjout("🤾‍♂ ️Ajouter un joueur");
        BoutonAjout boutonAleatoire = new BoutonAjout("🎲 Composer aléatoirement les équipes");
        BoutonAjout btnModifierJoueur = new BoutonAjout("🖊️ Modifier un joueur");
        BoutonAjout btnCloturerRonde = new BoutonAjout("🏁 Clôturer la Ronde");
        IntegerField fieldTailleEquipe = new IntegerField("Nombre de joueurs par équipe");
        fieldTailleEquipe.setValue(4); // Valeur par défaut
        fieldTailleEquipe.setMin(3);
        fieldTailleEquipe.setMax(6);
        fieldTailleEquipe.addClassName("glass-field");
        btnModifierJoueur.setEnabled(false);
        BoutonAjout btnModifierEquipe = new BoutonAjout("🖊️ Modifier le nom");
        btnModifierEquipe.setEnabled(false);
        ComboBox<Match> selecteurMatch = new ComboBox<>("Sélectionner un match");
        selecteurMatch.addClassName("glass-combobox");
        ComboBox<Match> selecteurMatchAlea = new ComboBox<>("Sélectionner un match");
        selecteurMatchAlea.addClassName("glass-combobox");
        ComboBox<Ronde> selecteurRonde = new ComboBox<>("Sélectionner la ronde");
        selecteurRonde.addClassName("glass-combobox");
        ComboBox<Equipe> selecteurEquipe = new ComboBox<>();
        selecteurEquipe.addClassName("glass-combobox");
        selecteurEquipe.setWidth("200px");
        ComboBox<Joueur> selecteurJoueur = new ComboBox<>("Sélectionner le joueur");
        selecteurJoueur.addClassName("glass-combobox");
        HorizontalLayout hlbutton1 = new HorizontalLayout(tfnuméro,tfstatut);
        HorizontalLayout hlbutton2 = new HorizontalLayout(selecteurRonde);
        HorizontalLayout hlbutton3 = new HorizontalLayout(tfnomEquipe,selecteurMatch);
        HorizontalLayout hlbutton4 = new HorizontalLayout(tfprénom,tfnom);
        HorizontalLayout hlbutton5 = new HorizontalLayout(tfcatégorie,tfsexe);
        HorizontalLayout hlbutton6 = new HorizontalLayout (tfsurnom,dateN);
        Details detailsRonde = new Details("🛠️ Créer les Rondes", hlbutton1,créerrondebtn,btnCloturerRonde);
        detailsRonde.addClassName("glass-details");
        detailsRonde.setOpened(true);
        Details detailsMatch = new Details("🛠️ Créer les Matchs", hlbutton2,ajoutmatchbtn);
        detailsMatch.addClassName("glass-details");
        Details detailsEquipe = new Details("🛠️ Créer les Equipes", hlbutton3,fieldTailleEquipe,boutonGenererEquipes,btnModifierEquipe);
        detailsEquipe.addClassName("glass-details");
        Details detailsJoueur = new Details("🛠 Ajouter les joueurs️", hlbutton4,hlbutton5,hlbutton6,ajoutjoueurbtn,btnModifierJoueur);
        detailsJoueur.addClassName("glass-details");
        Details detailsCompo = new Details("🛠️ Composer les équipes",selecteurMatchAlea,boutonAleatoire);
        detailsCompo.addClassName("glass-details");
        Details detailsid = new Details("🛠️ Getion des identifiants");
        detailsid.addClassName("glass-details");

        //Barre d'onglets
        mepS.setVisible(false);
        mepS.add(new H2("Statistiques"), new Paragraph("Contenu à venir..."));
        tabs.addSelectedChangeListener(event -> {
        Tab selectedTab = event.getSelectedTab();
        if (selectedTab.equals(tabGestion)) {
            rafraichirToutesLesDonnees();
            mepGT.setVisible(true);
        } else if (selectedTab.equals(tabStats)) {
            mepGT.setVisible(false);
            UI.getCurrent().navigate("http://localhost:8080/Statistiques");
            
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
            UI.getCurrent().navigate("http://localhost:8080/Connexion");
        }
    });
        grilleJoueurs = new Grid<>(Joueur.class, false);
        grilleJoueurs.addClassName("glass-grid-v2");    
        grilleJoueurs.addColumn(Joueur::getSurnom).setHeader("Surnom");
        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setAlignItems(Alignment.CENTER); 
        headerLayout.setSpacing(true);
        headerLayout.setWidthFull();
        Span titreColonne = new Span("Catégorie");
        selecteurEquipe.setWidth("150px");
        selecteurEquipe.setPlaceholder("Equipes");
        headerLayout.add(titreColonne, selecteurEquipe);
        grilleJoueurs.addColumn(Joueur::getCategorie).setHeader(headerLayout).setWidth("280px");
        grilleJoueurs.setHeight("100%");
        grilleJoueurs.setWidthFull();
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
        gridRondes.setHeight("100%");
        gridRondes.setWidthFull();
        
        // --- 2. Grid des MATCHS ---
        gridMatchs = new Grid<>(Match.class, false);
        gridMatchs.addClassName("glass-grid-v2");
        gridMatchs.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_NO_BORDER);
        gridMatchs.addColumn(Match::getId).setHeader("Match");
        gridMatchs.addColumn(Match::getStatut).setHeader("Statut");
        gridMatchs.addColumn(Match::getIdronde).setHeader("Ronde");
        gridMatchs.setHeight("100%");
        gridMatchs.setWidthFull();
    

        // --- 3. Grid des ÉQUIPES ---
        gridEquipes = new Grid<>(Equipe.class, false);
        gridEquipes.addClassName("glass-grid-v2");
        gridEquipes.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_NO_BORDER);
        gridEquipes.addColumn(Equipe::getNomEquipe).setHeader("Nom");
        gridEquipes.addColumn(Equipe::getScore).setHeader("Score");
        gridEquipes.addColumn(Equipe::getIdmatch).setHeader("Match");
        gridEquipes.setHeight("100%");
        gridEquipes.setWidthFull();
        
        // grille de tous les joueurs 
        gridAllJoueurs = new Grid<>(Joueur.class, false);
        gridAllJoueurs.addClassName("glass-grid-v2");
        gridAllJoueurs.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_NO_BORDER);
        gridAllJoueurs.addColumn(Joueur::getSurnom).setHeader("Surnom");
        gridAllJoueurs.addColumn(Joueur::getPrénom).setHeader("Prénom");
        gridAllJoueurs.addColumn(Joueur::getNom).setHeader("Nom");
        gridAllJoueurs.addColumn(Joueur::getSexe).setHeader("Sexe");
        gridAllJoueurs.addColumn(Joueur::getDateNaissance).setHeader("Date de Naissance").setWidth("180px");
        gridAllJoueurs.setHeight("100%");
        gridAllJoueurs.setWidthFull();
        
        gridUsers = new Grid<>(Utilisateur.class, false);
        gridUsers.addClassName("glass-grid-v2");
        gridUsers.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_NO_BORDER);
        gridUsers.addColumn(Utilisateur::getIdentifiant).setHeader("Identifiant");
        gridUsers.addColumn(Utilisateur::getRole).setHeader("Rôle");
        gridUsers.addComponentColumn(user -> {
            Button btnPromouvoir = new Button("Admin");
            btnPromouvoir.setIcon(new Icon(VaadinIcon.ARROW_UP));
            btnPromouvoir.addClassName("bouton-ajout"); // Ou votre style bouton
            // Si déjà admin, on désactive le bouton
            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                btnPromouvoir.setEnabled(false);
                btnPromouvoir.setText("Admin");
                btnPromouvoir.setIcon(new Icon(VaadinIcon.CHECK));
            } else {
                // Action au clic
                btnPromouvoir.addClickListener(e -> {
                    try {
                        user.devenirAdmin();
                        Notification.show(user.getIdentifiant() + " est maintenant Administrateur !");
                        rafraichirToutesLesDonnees(); 
                    } catch (SQLException ex) {
                        Notification.show("Erreur : " + ex.getMessage());
                        ex.printStackTrace();
                    }
                });
            }
            return btnPromouvoir;
        }).setHeader("Action"); 
        gridUsers.setWidthFull();
        gridUsers.setHeight("150px");
        detailsid.add(gridUsers);
      
        try {
            gridAllJoueurs.setItems(Joueur.getAllPlayers());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        
        
        contenuGauche.add(detailsRonde,detailsMatch,detailsEquipe,detailsJoueur,detailsCompo,detailsid);
        contenuMid.add(hjoueur,gridAllJoueurs);
        contenuMid2.add(hjoueurdeléquipe,grilleJoueurs,héquipe,gridEquipes);
        contenuDroit.add(hronde,gridRondes,hmatch,gridMatchs);
       
        // Ajout de tous les composants dans le VerticalLayout (Vue principale)
        this.add(tabs,titre,mepGT);
        this.setPadding(false);
        this.setSpacing(false);
        this.setSizeFull();
        this.setAlignItems(Alignment.CENTER);
        mepGT.setPadding(true);
        mepGT.setWidthFull();
        mepGT.setFlexGrow(1, contenuGauche);
        mepGT.setFlexGrow(0.5, contenuMid);
        mepGT.setFlexGrow(3,contenuMid2);
        mepGT.setFlexGrow(4,contenuDroit);
        mepGT.setJustifyContentMode(JustifyContentMode.CENTER);
        mepS.setPadding(true);
        rafraichirToutesLesDonnees();
        
     
        
        //Création des rondes
        try {
        selecteurRonde.setItems(Ronde.getAllRondes());
        } catch (SQLException ex) {
         ex.printStackTrace(); }
        selecteurRonde.setItemLabelGenerator(ronde -> 
        "Ronde n°" + ronde.getNumero() + " (" + ronde.getStatut() + ") " + "id : " + ronde.getId()
        );
        
        
        selecteurRonde.addValueChangeListener(event -> {
        Ronde rondeSelectionnee = event.getValue();
        if (rondeSelectionnee != null) {
            try {
                int idRonde = rondeSelectionnee.getId();
                rafraichirToutesLesDonnees();
                selecteurMatch.clear();
                selecteurEquipe.clear(); 
                grilleJoueurs.setItems(new ArrayList<>());
                List<Match> matchsDeLaRonde = Match.getMatchsDeLaRonde(idRonde);
                List<Equipe> equipesDeLaRonde = Equipe.getEquipesDeLaRonde(idRonde);

            // 3. On met à jour les sélecteurs
                selecteurMatch.setItems(matchsDeLaRonde);
                selecteurEquipe.setItems(equipesDeLaRonde);
                if (gridEquipes != null) {
                    gridEquipes.setItems(Equipe.getEquipesDeLaRonde(idRonde));
                }
                if (equipesDeLaRonde.isEmpty()) {
                Notification.show("Aucune équipe dans cette ronde pour l'instant.");
              }

            } catch (SQLException e) { 
                e.printStackTrace(); 
        }
        } else {

            selecteurMatch.setItems(new ArrayList<>());
            selecteurEquipe.setItems(new ArrayList<>());
            rafraichirToutesLesDonnees();
        }
      });

          
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
        selecteurMatchAlea.setItems(Match.getAllMatchs());
        selecteurMatchAlea.setItemLabelGenerator(match -> 
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
            if (match == null) {
                Notification.show("Veuillez sélectionner un match d'abord !");
                return;
            }

        try {
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
            grilleJoueurs.setItems();
        }
    });
       
    //Composition des équipes
       
    // fixer le nombre de joueur au début d'une ronde   
        fieldTailleEquipe.addValueChangeListener(e -> {
        if (e.getValue() != null) {
            Equipe.TAILLE_MAX_PAR_EQUIPE = e.getValue();
            Notification.show("Taille d'équipe fixée à " + e.getValue() + " joueurs.");
        }
        
        boutonAleatoire.addClickListener(click -> {
        Match match = selecteurMatchAlea.getValue();
        rafraichirToutesLesDonnees();

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
                int placesLibres = e.getValue() - nbJoueursActuels;

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
            if (compteurAjouts > 0) {
                Notification.show(compteurAjouts + " joueurs répartis aléatoirement (Max " + e.getValue() + "/équipe) !");

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
    });
    
    //Bouton supprimer un joueur 
    grilleJoueurs.addComponentColumn(joueur -> {
    
    Button boutonSupprimer = new Button(new Icon(VaadinIcon.TRASH));
    boutonSupprimer.addClassName("black-glass-button");
    
    boutonSupprimer.addClickListener(e -> {
        Dialog confirmDialog = new Dialog();
        confirmDialog.setHeaderTitle("Supprimer " + joueur.getSurnom() + " ?");
        confirmDialog.add("Êtes-vous sûr ? Cette action est irréversible.");
        
        Button btnOui = new Button("Oui, supprimer", click -> {
            try (Connection con = ConnectionPool.getConnection()) {
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
        rafraichirToutesLesDonnees();
    });
    
    return boutonSupprimer;
    });
    
    // Modification des infos d'un joueur   
        gridAllJoueurs.addSelectionListener(selection -> {
        // Si un joueur est sélectionné...
        if (selection.getFirstSelectedItem().isPresent()) {
            Joueur j = selection.getFirstSelectedItem().get();

            tfsurnom.setValue(j.getSurnom());
            tfcatégorie.setValue(j.getCategorie());
            tfprénom.setValue(j.getPrénom());
            tfnom.setValue(j.getNom());
            tfsexe.setValue(j.getSexe());
            if (j.getDateNaissance() != null) {
                dateN.setValue(j.getDateNaissance());
            } else {
                dateN.clear();
            }

            // 3. On active le mode "Modification"
            btnModifierJoueur.setEnabled(true);
            ajoutjoueurbtn.setEnabled(false); // On bloque l'ajout pour éviter les confusions

        } else {
            tfsurnom.clear(); tfcatégorie.clear(); tfprénom.clear(); 
            tfnom.clear(); tfsexe.clear(); dateN.clear();

            // On revient au mode "Ajout" par défaut
            btnModifierJoueur.setEnabled(false);
            ajoutjoueurbtn.setEnabled(true);
        }
    });
    btnModifierJoueur.addClickListener(click -> {
        // 1. On récupère le joueur sélectionné
        Joueur joueurSelectionne = gridAllJoueurs.asSingleSelect().getValue();

        if (joueurSelectionne != null) {
            try (Connection con = ConnectionPool.getConnection()) {

                // 2. On met à jour l'objet Java avec ce qui est écrit dans les champs
                joueurSelectionne.setSurnom(tfsurnom.getValue());
                joueurSelectionne.setCategorie(tfcatégorie.getValue());
                joueurSelectionne.setPrénom(tfprénom.getValue());
                joueurSelectionne.setNom(tfnom.getValue());
                joueurSelectionne.setSexe(tfsexe.getValue());
                joueurSelectionne.setDateNaissance(dateN.getValue());
                joueurSelectionne.update(con);

                Notification.show("Joueur modifié avec succès !");

                // 4. On rafraîchit l'affichage
                rafraichirToutesLesDonnees();

                // 5. On vide la sélection pour repartir à zéro
                gridAllJoueurs.deselectAll();

            } catch (SQLException ex) {
                Notification.show("Erreur de modification : " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    });
    
    gridEquipes.addSelectionListener(selection -> {
    if (selection.getFirstSelectedItem().isPresent()) {
        Equipe equipe = selection.getFirstSelectedItem().get();
        
        // 1. On remplit le champ de texte avec le nom actuel
        tfnomEquipe.setValue(equipe.getNomEquipe());
        
        // 2. On active le bouton de modification
        btnModifierEquipe.setEnabled(true);
        boutonGenererEquipes.setEnabled(false); // On évite de générer pendant qu'on modifie
    } else {
        // Désélection : On vide et on remet à l'état initial
        tfnomEquipe.clear();
        btnModifierEquipe.setEnabled(false);
        boutonGenererEquipes.setEnabled(true);
    }
});

    // modifier le nom d'une équipe
    btnModifierEquipe.addClickListener(click -> {
        Equipe equipeSelectionnee = gridEquipes.asSingleSelect().getValue();

        if (equipeSelectionnee != null) {
            try (Connection con = ConnectionPool.getConnection()) {
                // 1. Mise à jour de l'objet Java
                equipeSelectionnee.setNomEquipe(tfnomEquipe.getValue());

                // 2. Mise à jour en Base de Données
                equipeSelectionnee.update(con);

                Notification.show("Nom de l'équipe modifié !");

                // 3. Rafraîchissement
                rafraichirToutesLesDonnees();
                gridEquipes.deselectAll();

            } catch (SQLException ex) {
                Notification.show("Erreur : " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    });
    
    // Saisis des scors, cloture des matchs et de la ronde 
    gridMatchs.addComponentColumn(match -> {
    // Si le match est déjà fini, on affiche juste un texte ou un bouton désactivé
    if ("Terminé".equals(match.getStatut())) {
        Button btnFini = new Button("Terminé");
        btnFini.setEnabled(false);
        return btnFini;
    }

    // Sinon, on affiche le bouton pour saisir le score
    Button btnScore = new Button("Score");
    btnScore.addClassName("black-glass-button");
    
    btnScore.addClickListener(e -> {
        // 1. On ouvre une boite de dialogue (Pop-up)
        Dialog dialogScore = new Dialog();
        dialogScore.setHeaderTitle("Résultat du Match " + match.getId());
        
        VerticalLayout content = new VerticalLayout();
        
        try {
            List<Equipe> lesEquipes = Equipe.getEquipesDuMatch(match.getId());
            
            if (lesEquipes.size() < 2) {
                Notification.show("Erreur : Il faut 2 équipes pour saisir un score !");
                return;
            }
            
            Equipe eq1 = lesEquipes.get(0);
            Equipe eq2 = lesEquipes.get(1);
            TextField score1 = new TextField(eq1.getNomEquipe());
            score1.setPlaceholder("Points");
            
            TextField score2 = new TextField(eq2.getNomEquipe());
            score2.setPlaceholder("Points");
            
            content.add(score1, score2);
            dialogScore.add(content);
            
            // 4. Bouton Valider
            Button valider = new Button("Enregistrer & Clôturer", event -> {
                try {
                    int s1 = Integer.parseInt(score1.getValue());
                    int s2 = Integer.parseInt(score2.getValue());
                    
                    // A. On sauvegarde les scores
                    Equipe.setScoreEquipe(eq1.getId(), s1);
                    Equipe.setScoreEquipe(eq2.getId(), s2);
                    
                    // B. On clôture le match
                    Match.setStatutTermine(match.getId());
                    
                    Notification.show("Scores enregistrés ! Match terminé.");
                    dialogScore.close();
                    rafraichirToutesLesDonnees(); // Mise à jour des grilles
                    
                } catch (NumberFormatException ex) {
                    Notification.show("Veuillez entrer des nombres valides !");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });
            valider.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            dialogScore.getFooter().add(new Button("Annuler", c -> dialogScore.close()), valider);
            dialogScore.open();
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    });
    
    return btnScore;
}).setHeader("Actions");
    
    // cloturer une ronde
   
        btnCloturerRonde.addClickListener(click -> {
            Ronde ronde = selecteurRonde.getValue();
            if (ronde == null) {
                Notification.show("Sélectionnez une ronde d'abord !");
                return;
            }
            Dialog confirm = new Dialog();
            confirm.setHeaderTitle("Clôturer la Ronde " + ronde.getNumero() + " ?");
            confirm.add("Êtes-vous sûr ? Cela validera tous les résultats.");

            Button oui = new Button("Oui, Clôturer", e -> {
                try {
                    // Appel SQL
                    Ronde.setStatutTermine(ronde.getId());

                    Notification.show("Ronde clôturée avec succès !");
                    confirm.close();
                    rafraichirToutesLesDonnees();

                } catch (SQLException ex) {
                    Notification.show("Erreur : " + ex.getMessage());
                }
            });

            confirm.getFooter().add(new Button("Annuler", e -> confirm.close()), oui);
            confirm.open();
        });
       
// Affichage de tous les identifiants et modification de leur role 


    }
    private void rafraichirToutesLesDonnees() {
        try {
            if(gridRondes != null) gridRondes.setItems(Ronde.getAllRondes());
            if(gridMatchs != null) gridMatchs.setItems(Match.getAllMatchs());
            if(gridEquipes != null) gridEquipes.setItems(Equipe.getAllTeams());
            if(gridAllJoueurs != null) gridAllJoueurs.setItems(Joueur.getAllPlayers());
            if(gridUsers != null) gridUsers.setItems(Utilisateur.getAllUtilisateurs());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
    
