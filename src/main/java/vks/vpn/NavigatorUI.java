package vks.vpn;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import vks.vpn.backend.Authenticator;
import vks.vpn.views.LoginView;
import vks.vpn.views.SmsSenderView;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
@Widgetset("vks.vpn.DefaultWidgetset")
public class NavigatorUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        getPage().setTitle("SMS Sender");

        setSizeFull();
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        layout.addComponent((new LoginView(user -> {
            if (Authenticator.isCredentialRight(user.getUsername(), user.getPassword())) {
                setContent(new SmsSenderView());
            } else {
                Notification.show("Неправильные логин или пароль");
            }
        })));

        setContent(layout);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = NavigatorUI.class, productionMode = true, widgetset="vks.vpn.DefaultWidgetset")
    public static class MyUIServlet extends VaadinServlet {
    }
}
