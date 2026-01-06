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

import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.théo.model.Equipe;
import fr.insa.théo.model.Joueur;
import fr.insa.théo.model.Match;
import fr.insa.théo.model.Ronde;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PageTitle("Classement Général")
@Route(value = "Statistiques") // L'URL sera .../statistiques
public class Statistiques extends VerticalLayout {

    public Statistiques() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        addClassName("page-stats");

        H1 titre = new H1("🏆 Classement Général");
        titre.getStyle().set("filter", "drop-shadow(0 0 10px rgba(99, 102, 241, 0.5))");
        // --- 1. CHARGEMENT ET TRI DES DONNÉES ---
        List<Joueur> tousLesJoueurs;
        try {
            tousLesJoueurs = Joueur.getAllPlayers();
            // On trie du plus grand score au plus petit
            tousLesJoueurs.sort(Comparator.comparingInt(Joueur::getScoreCalculé).reversed());
        } catch (SQLException e) {
            tousLesJoueurs = new ArrayList<>();
            e.printStackTrace();
        }

        // --- 2. CALCUL DES RANGS (NOUVEAU) ---
        // On crée une map pour savoir instantanément que le Joueur X est Nème
        Map<Integer, Integer> rangs = new HashMap<>();
        for (int i = 0; i < tousLesJoueurs.size(); i++) {
            // i + 1 car l'index commence à 0 mais le classement à 1
            rangs.put(tousLesJoueurs.get(i).getId(), i + 1);
        }

        // --- 3. CONFIGURATION DE LA GRILLE ---
        Grid<Joueur> grid = new Grid<>(Joueur.class, false);
        grid.addClassName("glass-grid-v2");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_ROW_STRIPES);

        // Colonnes standards
        grid.addColumn(Joueur::getSurnom).setHeader("Surnom").setSortable(true);
        grid.addColumn(Joueur::getPrénom).setHeader("Prénom");
        grid.addColumn(Joueur::getNom).setHeader("Nom");
        grid.addColumn(Joueur::getCategorie).setHeader("Catégorie");
        
        // Colonne Score
        grid.addColumn(Joueur::getScoreCalculé).setHeader("Total Points").setSortable(true);

        // --- 4. LA COLONNE CLASSEMENT (MODIFIÉE) ---
        grid.addComponentColumn(joueur -> {
            Span badge = new Span();
            badge.addClassName("glass-badge");
            
            // On récupère le rang calculé plus haut
            int rang = rangs.getOrDefault(joueur.getId(), -1);
            
            if (rang == 1) {
                badge.setText("🥇");
                badge.addClassName("gold");
                
            } else if (rang == 2) {
                badge.setText("🥈");
                badge.addClassName("silver");
                
            } else if (rang == 3) {
                badge.setText("🥉");
                badge.addClassName("bronze");
                
            } else {
                // Pour les autres : "4ème", "5ème", etc.
                badge.setText(rang + "ème");
                // On garde l'opacité réduite pour ne pas voler la vedette au podium
                badge.getStyle().set("opacity", "0.7"); 
            }
            
            return badge;
        }).setHeader("Classement").setWidth("180px");

        // --- 5. REMPLISSAGE FINAL ---
        grid.setItems(tousLesJoueurs);
        add(titre, grid);
        
        H2 titreMatchs = new H2("⚔️ Résultats des Matchs");
        titreMatchs.getStyle().set("font-size", "2em").set("margin-top", "40px");
        add(titreMatchs);

        // 1. Création d'un conteneur flexible pour aligner les boites horizontalement
        com.vaadin.flow.component.orderedlayout.FlexLayout conteneurRondes = new com.vaadin.flow.component.orderedlayout.FlexLayout();
        conteneurRondes.setWidthFull();
        conteneurRondes.setFlexWrap(com.vaadin.flow.component.orderedlayout.FlexLayout.FlexWrap.WRAP); // Passe à la ligne si pas de place
        conteneurRondes.setJustifyContentMode(JustifyContentMode.CENTER); // Centre les blocs au milieu de la page
        conteneurRondes.getStyle().set("gap", "20px"); // Espace de 20px entre chaque bloc

        try {
            List<Ronde> rondes = Ronde.getAllRondes();

            for (Ronde r : rondes) {
                Details detailsRonde = new Details();
                detailsRonde.setSummaryText("Ronde " + r.getNumero());
                detailsRonde.addClassName("glass-details");
                detailsRonde.addThemeVariants(DetailsVariant.FILLED);

                // 2. IMPORTANT : On fixe la largeur pour qu'ils tiennent côte à côte
                detailsRonde.setWidthFull();

                // On laisse ouvert pour voir direct
                detailsRonde.setOpened(true);

                List<MatchResultatDTO> resultats = chargerResultatsDeLaRonde(r.getId());

                Grid<MatchResultatDTO> gridMatchs = new Grid<>(MatchResultatDTO.class, false);
                gridMatchs.addClassName("glass-grid-v2");
                gridMatchs.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_COMPACT);

                // On laisse la grille calculer sa hauteur selon le nombre de matchs
                gridMatchs.setAllRowsVisible(true); 

                // --- Colonnes (Identiques à avant) ---
                gridMatchs.addColumn(MatchResultatDTO::getNomEquipe1)
                          .setHeader("Equipe A").setTextAlign(com.vaadin.flow.component.grid.ColumnTextAlign.END).setAutoWidth(true);

                gridMatchs.addComponentColumn(dto -> {
                    Span scoreSpan = new Span(dto.getScore1() + " - " + dto.getScore2());
                    scoreSpan.addClassName("glass-badge");
                    if ("Terminé".equals(dto.getStatut())) scoreSpan.addClassName("success");
                    else scoreSpan.addClassName("pending");
                    return scoreSpan;
                }).setHeader("Score").setTextAlign(com.vaadin.flow.component.grid.ColumnTextAlign.CENTER).setWidth("110px");

                gridMatchs.addColumn(MatchResultatDTO::getNomEquipe2).setHeader("Equipe B").setAutoWidth(true);

                gridMatchs.setItems(resultats);
                detailsRonde.setContent(gridMatchs);

                // 3. On ajoute le Details dans le conteneur horizontal (et pas direct dans la page)
                conteneurRondes.add(detailsRonde);
            }

            // 4. On ajoute le conteneur final à la page
            add(conteneurRondes);

            } catch (SQLException e) {
                e.printStackTrace();
            }
       }
    

    // --- Méthode Helper pour charger les données sans casser la BDD ---
    private List<MatchResultatDTO> chargerResultatsDeLaRonde(int idRonde) {
        List<MatchResultatDTO> liste = new ArrayList<>();
        try {
            // On récupère les objets Match bruts
            List<Match> matchsBruts = Match.getMatchsDeLaRonde(idRonde);
            
            for (Match m : matchsBruts) {
                // Pour chaque match, on cherche les équipes
                // ATTENTION : Cette méthode doit exister dans Equipe.java
                List<Equipe> eqs = Equipe.getEquipesDuMatch(m.getId());
                
                String nom1 = "En attente...";
                int score1 = 0;
                String nom2 = "En attente...";
                int score2 = 0;
                
                if (eqs.size() >= 1) {
                    nom1 = eqs.get(0).getNomEquipe(); // ou getNom()
                    score1 = eqs.get(0).getScore();
                }
                if (eqs.size() >= 2) {
                    nom2 = eqs.get(1).getNomEquipe();
                    score2 = eqs.get(1).getScore();
                }
                
                liste.add(new MatchResultatDTO(nom1, score1, nom2, score2, m.getStatut()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }

    // --- Classe Interne pour l'affichage (DTO) ---
    public static class MatchResultatDTO {
        private String nomEquipe1;
        private int score1;
        private String nomEquipe2;
        private int score2;
        private String statut;

        public MatchResultatDTO(String n1, int s1, String n2, int s2, String st) {
            this.nomEquipe1 = n1; this.score1 = s1;
            this.nomEquipe2 = n2; this.score2 = s2;
            this.statut = st;
        }

        // Getters nécessaires pour la Grid
        public String getNomEquipe1() { return nomEquipe1; }
        public int getScore1() { return score1; }
        public String getNomEquipe2() { return nomEquipe2; }
        public int getScore2() { return score2; }
        public String getStatut() { return statut; }
    }
 }


