package vks.vpn.views;

import com.ejt.vaadin.loginform.DefaultVerticalLoginForm;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Notification;

import java.util.function.Consumer;

/**
 * Created by upitis on 13.07.2016.
 */
public class LoginView extends DefaultVerticalLoginForm implements View{

    public LoginView(Consumer<User> loginCallBack) {
        super();

        this.addLoginListener((LoginListener) loginEvent -> {
            Notification.show("Logining...");
            loginCallBack.accept(new User(loginEvent.getUserName(),loginEvent.getPassword()));
        });
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
