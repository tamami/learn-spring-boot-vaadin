package lab.aikibo.started;

import com.jaunt.*;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Item;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import lab.aikibo.component.Greeter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.LinkedList;


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
        LinkedList<String> label = new LinkedList<>();
        LinkedList<String> value = new LinkedList<>();

        try {
            userAgent.visit("http://www.bkn.go.id/profil-pns");
        } catch(ResponseException e) { e.printStackTrace(); }

        try {
            userAgent.sendPOST("http://www.bkn.go.id/profil-pns", "nip=" + nip);

            Elements el = userAgent.doc.findEvery("<span>");
            for(Element data : el) {

                if(data.getAt("class").equals("label")) {
                    label.add(data.innerHTML());
                }
                if(data.getAt("class").equals("value")) {
                    value.add(data.innerHTML().substring(2));
                }
            }

        } catch(NotFound e) {}
          catch(SearchException e) { e.printStackTrace(); }
          catch(ResponseException e) { e.printStackTrace(); }

        System.out.println(label.size());
        System.out.println(value.size());

        for(int j=0; j<label.size(); j++) {
            /*
            Object newItem = table.addItem();
            Item row = table.getItem(newItem);
            row.getItemProperty("LABEL").setValue(label.get(j));
            row.getItemProperty("KETERANGAN").setValue(value.get(j));
            */
            table.addItem(new Object[]{label.get(j), value.get(j)}, j+1);
            System.out.println(label.get(j));
        }

        table.setPageLength(table.size());
    }

}
