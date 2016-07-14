package vks.vpn.views;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.data.Property;
import com.vaadin.event.FieldEvents;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.declarative.Design;
import vks.vpn.backend.SmsUtils;

import java.sql.SQLException;
import java.util.Arrays;


/**
 * Created by upitis on 13.07.2016.
 */
@DesignRoot
public class SmsSenderView extends VerticalLayout {
    private Label headLabel;
    private TextField phonesField;
    private TextArea messageArea;
    private Button sendBtn;

    public SmsSenderView() {
        super();
        Design.read(this);
        sendBtn.setIcon(FontAwesome.MAIL_FORWARD);
        sendBtn.setCaption("Отправить");

        sendBtn.addClickListener((Button.ClickListener) event -> {
            try {

                SmsUtils.sendSms(Arrays.asList(phonesField.getValue().split(";")),messageArea.getValue());
                Notification.show("Отправлено");
            } catch (ClassNotFoundException | SQLException ex) {
                Notification.show("Возникли проблемы с оправкой сообщения");
            }
            sendBtn.setEnabled(false);
        });

        messageArea.addValueChangeListener((Property.ValueChangeListener) event -> {
            sendBtn.setEnabled(true);
        });

        messageArea.addTextChangeListener((FieldEvents.TextChangeListener) event -> {
            String s = " / Всего: " + event.getText().length() + " символов";
            messageArea.setCaption("Тескт СМС (не более 900 символов)"  + s);
        });

        phonesField.addValueChangeListener((Property.ValueChangeListener) event -> {
            sendBtn.setEnabled(true);
        });

    }

}
