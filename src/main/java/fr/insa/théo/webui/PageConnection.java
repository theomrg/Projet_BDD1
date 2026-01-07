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

import com.vaadin.flow.component.Text;
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
import com.vaadin.flow.component.tabs.TabsVariant;
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

    private TextField idField;
    private PasswordField mdpField;
    private Button actionButton;
    private Span disclaimer; 
    private Tabs tabs;

    public PageConnection() {
        // --- 1. Configuration du Layout Principal (L'écran entier) ---
        this.setSizeFull(); // Prend tout l'écran
        this.setAlignItems(Alignment.CENTER); // Centre horizontalement
        this.setJustifyContentMode(JustifyContentMode.CENTER); // Centre verticalement
        H1 titre = new H1("Connexion au tournoi 🌐");
        Tab tabLogin = new Tab("Se connecter");
        Tab tabSignup = new Tab("Créer un compte");
        tabs = new Tabs(tabLogin, tabSignup);
        // On enlève le style inline précédent pour laisser le CSS gérer ou garder simple
        tabs.setWidthFull(); 
        tabs.addThemeVariants(TabsVariant.LUMO_CENTERED);
        
       
        disclaimer = new Span();
        Icon icon = new Icon(VaadinIcon.INFO_CIRCLE);
        disclaimer.add(icon);
        disclaimer.add(new Text(" Format requis pour l'identifiant : prénom.nom"));
        disclaimer.addClassName("glass-disclaimer"); // Votre classe existante
        disclaimer.setVisible(false);
        disclaimer.setWidthFull(); // Pour qu'il prenne la largeur de la carte
        
        idField = new TextField("Identifiant");
        idField.setWidthFull(); // Prend la largeur de la carte
        idField.setPlaceholder("prenom.nom");
        
        mdpField = new PasswordField("Mot de passe");
        mdpField.setWidthFull(); // Prend la largeur de la carte

        actionButton = new Button("Connexion");
        actionButton.addClassName("glass-button");
        actionButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        actionButton.setWidthFull(); // Bouton large
        
        // --- 3. Création du Sous-Layout "Glass Card" ---
        VerticalLayout glassCard = new VerticalLayout();
        glassCard.addClassName("glass-card"); // APPLIQUER LE STYLE CSS ICI
        
        
        glassCard.add(titre, tabs, disclaimer, idField, mdpField, actionButton);
        
      
        glassCard.setSpacing(false); 
        glassCard.setPadding(false); 
        glassCard.setJustifyContentMode(JustifyContentMode.CENTER);
        // --- 4. Logique des onglets ---
        tabs.addSelectedChangeListener(event -> {
            boolean isInscription = event.getSelectedTab().equals(tabSignup);
            if (isInscription) {
                actionButton.setText("S'inscrire");
                disclaimer.setVisible(true);
                idField.setHelperText("Ex: Jean.Dupont");
            } else {
                actionButton.setText("Connexion");
                disclaimer.setVisible(false);
                idField.setHelperText(null);
            }
        });

        actionButton.addClickListener(e -> {
            if (tabs.getSelectedTab().equals(tabLogin)) {
                login();
            } else {
                register();
            }
        });

       
        this.add(glassCard);
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

        if (identifiant.isEmpty() || mdp.isEmpty()) {
            Notification.show("Veuillez remplir tous les champs.");
            return;
        }

        if (!identifiant.contains(".") || identifiant.startsWith(".") || identifiant.endsWith(".")) {
            Notification.show("Refusé : L'identifiant doit être au format 'prénom.nom' !")
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        boolean existeDeja = false;
        try (Connection con = ConnectionPool.getConnection()) {
            String checkSql = "SELECT id FROM utilisateurs WHERE identifiant=?";
            PreparedStatement checkPst = con.prepareStatement(checkSql);
            checkPst.setString(1, identifiant);
            if (checkPst.executeQuery().next()) {
                existeDeja = true;
            }
        } catch (SQLException ex) {
            Notification.show("Erreur de vérification : " + ex.getMessage());
            return;
        }

        if (existeDeja) {
            Notification.show("Cet identifiant existe déjà !");
            return;
        }

        Utilisateur.créerUtilisateur(identifiant, mdp);
        
        tabs.setSelectedIndex(0); 
        mdpField.clear();
        idField.clear();
    }
}