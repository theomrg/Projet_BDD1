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
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.beuvron.utils.database.ConnectionPool;
import fr.insa.théo.model.Utilisateur;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 *
 * @author francois
 */
@Route(value = "Connexion")
@PageTitle("Connexion")
public class PageConnection extends VerticalLayout {

    // Composants graphiques
    private TextField idField;
    private PasswordField mdpField;
    private Button actionButton;
    private Span disclaimer; // Le message d'avertissement
    
    private Tabs tabs;

    public PageConnection() {
        this.setAlignItems(Alignment.CENTER);
        this.setJustifyContentMode(JustifyContentMode.CENTER);
        this.setSizeFull();

        H1 titre = new H1("Bienvenue au Tournoi 🏐");
        
        // --- Onglets ---
        Tab tabLogin = new Tab("Se connecter");
        Tab tabSignup = new Tab("Créer un compte");
        tabs = new Tabs(tabLogin, tabSignup);
        
        // --- Disclaimer (Caché par défaut) ---
        disclaimer = new Span();
        disclaimer.add(new Icon(VaadinIcon.INFO_CIRCLE));
        disclaimer.add(" Important : Votre identifiant doit être au format 'Prénom.Nom' (ex: Jean.Dupont)");
        disclaimer.getElement().getThemeList().add("badge error"); // Style rouge/alerte
        disclaimer.setVisible(false); // On ne l'affiche que pour l'inscription
        disclaimer.getStyle().set("padding", "10px");
        
        // --- Champs ---
        idField = new TextField("Identifiant");
        idField.setWidth("300px");
        idField.setPlaceholder("prenom.nom"); // Petit indice visuel
        
        mdpField = new PasswordField("Mot de passe");
        mdpField.setWidth("300px");

        // --- Bouton ---
        actionButton = new Button("Connexion");
        actionButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        actionButton.setWidth("300px");
        
        // Logique de changement d'onglet
        tabs.addSelectedChangeListener(event -> {
            boolean isInscription = event.getSelectedTab().equals(tabSignup);
            
            if (isInscription) {
                actionButton.setText("S'inscrire");
                disclaimer.setVisible(true); // Afficher le disclaimer
                idField.setHelperText("Format requis : prénom.nom");
            } else {
                actionButton.setText("Connexion");
                disclaimer.setVisible(false); // Cacher le disclaimer
                idField.setHelperText(null);
            }
        });

        // Logique du clic
        actionButton.addClickListener(e -> {
            if (tabs.getSelectedTab().equals(tabLogin)) {
                login();
            } else {
                register();
            }
        });

        // Ajout des composants
        this.add(titre, tabs, disclaimer, idField, mdpField, actionButton);
    }

    private void login() {
        String identifiant = idField.getValue();
        String mdp = mdpField.getValue();

        if (identifiant.isEmpty() || mdp.isEmpty()) {
            Notification.show("Veuillez remplir tous les champs.");
            return;
        }

        try (Connection con = ConnectionPool.getConnection()) {
            String sql = "SELECT role FROM utilisateurs WHERE identifiant=? AND mdp=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, identifiant);
            pst.setString(2, mdp);
            
            ResultSet res = pst.executeQuery();
            
            if (res.next()) {
                String role = res.getString("role");
                Notification.show("Connexion réussie !");
                
                if ("ADMIN".equalsIgnoreCase(role)) {
                    UI.getCurrent().navigate("GestionTournoi"); 
                } else {
                    UI.getCurrent().navigate("Statistiques");
                }
            } else {
                Notification.show("Identifiant ou mot de passe incorrect.")
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        } catch (SQLException ex) {
            Notification.show("Erreur BDD : " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void register() {
        String identifiant = idField.getValue();
        String mdp = mdpField.getValue();

        // --- 1. Validation des champs vides ---
        if (identifiant.isEmpty() || mdp.isEmpty()) {
            Notification.show("Veuillez remplir tous les champs.");
            return;
        }

        // --- 2. Validation du format Prénom.Nom ---
        // On vérifie s'il y a un point ET si ce n'est pas au début ou à la fin
        if (!identifiant.contains(".") || identifiant.startsWith(".") || identifiant.endsWith(".")) {
            Notification.show("Refusé : L'identifiant doit être au format 'prénom.nom' !")
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        try (Connection con = ConnectionPool.getConnection()) {
            // Vérifier doublon
            String checkSql = "SELECT id FROM utilisateurs WHERE identifiant=?";
            PreparedStatement checkPst = con.prepareStatement(checkSql);
            checkPst.setString(1, identifiant);
            if (checkPst.executeQuery().next()) {
                Notification.show("Cet identifiant existe déjà !");
                return;
            }
            // On vérifie s'il y a un point ET si ce n'est pas au début ou à la fin
        if (!identifiant.contains(".") || identifiant.startsWith(".") || identifiant.endsWith(".")) {
            Notification.show("Refusé : L'identifiant doit être au format 'prénom.nom' !")
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }
            // Création compte
            Utilisateur.créerUtilisateur(identifiant, mdp);
            
            // Retour à la connexion
            tabs.setSelectedIndex(0); 
            mdpField.clear();
            idField.clear();

        } catch (SQLException ex) {
            Notification.show("Erreur : " + ex.getMessage());
        }
    }
}
