package lab.aikibo.started;

import com.jaunt.*;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Item;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import lab.aikibo.component.Greeter;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by tamami on 04/01/17.
 */
@Theme("valo")
@SpringUI
public class MyFirstVaadin extends UI {

    @Autowired
    private Greeter greeter;

    Window win;
    VerticalLayout verLayout;
    Table table;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setContent(new Label(greeter.sayHello()));

        initComponent();

        addWindow(win);
    }

    private void initComponent() {
        win = new Window();
        verLayout = new VerticalLayout();
        win.setContent(verLayout);
        win.setSizeFull();

        final TextField nipTF = new TextField();
        nipTF.setCaption("NIP");

        Button btnSubmit = new Button("Proses");
        btnSubmit.addClickListener( e -> {
            getNip(nipTF.getValue());
        });

        verLayout.addComponent(nipTF);
        verLayout.addComponent(btnSubmit);
        verLayout.setMargin(true);
        verLayout.setSpacing(true);

        table = new Table("Info Pegawai");
        table.addContainerProperty("LABEL", String.class, null);
        table.addContainerProperty("KETERANGAN", String.class, null);
        verLayout.addComponent(table);
    }

    public void getNip(String nip) {
        UserAgent userAgent = new UserAgent();

        try {
            userAgent.visit("http://www.bkn.go.id/profil-pns");
        } catch(ResponseException e) { e.printStackTrace(); }

        try {
            userAgent.sendPOST("http://www.bkn.go.id/profil-pns", "nip=" + nip);

            Elements el = userAgent.doc.findEvery("<span>");
            int i=1;
            for(Element data : el) {
                String label ="";
                String value ="";
                Object newItem = table.addItem();
                Item row = table.getItem(newItem);
                if(data.getAt("class").equals("label")) {
                    label = data.innerHTML();
                }
                if(data.getAt("class").equals("value")) {
                    value = data.innerHTML().substring(2);
                }
                table.addItem(new Object[]{label,value},i++);
            }
        } catch(NotFound e) {}
          catch(SearchException e) { e.printStackTrace(); }
          catch(ResponseException e) { e.printStackTrace(); }

        table.setPageLength(table.size());
    }

}
