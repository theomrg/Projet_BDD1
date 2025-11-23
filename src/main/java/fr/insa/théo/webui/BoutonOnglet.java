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
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.dependency.CssImport;


/** v4
 *
 * @author theom
 */

public class BoutonOnglet extends Button {
    public BoutonOnglet(String label) {
        super(label);

        // Style de base (mauve)
        this.getStyle().set("background-color", "#8e44ad");
        this.getStyle().set("color", "white");
        this.getStyle().set("font-weight", "bold");
        this.getStyle().set("border-radius", "8px");
        this.getStyle().set("padding", "10px 20px");
        this.getStyle().set("transition", "all 0.3s ease");

        // Hover : couleur plus claire
        this.getElement().addEventListener("mouseover", e -> {
            this.getStyle().set("background-color", "#9b59b6");
            this.getStyle().set("transform", "scale(1.05)");
            this.getStyle().set("box-shadow", "0 6px 10px rgba(0,0,0,0.3)");
        });

        // Mouse out : revenir au style initial
        this.getElement().addEventListener("mouseout", e -> {
            this.getStyle().set("background-color", "#8e44ad");
            this.getStyle().remove("transform");
            this.getStyle().remove("box-shadow");
        });
    }
}
