package br.eng.rodrigogml.mysteryrealms.ui;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Mystery Realms")
@Route("")
public class MainView extends VerticalLayout {

    public MainView() {
        add(new H1("Mystery Realms - Ambiente inicial pronto!"));
    }
}
