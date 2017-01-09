package lab.aikibo.started;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;


/**
 * Created by tamami on 04/01/17.
 */
@SpringUI
public class MyFirstVaadin extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setContent(new Label("Hello! I'm the root UI!"));
    }

}
