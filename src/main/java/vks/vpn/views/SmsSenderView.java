package vks.vpn.views;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.SelectionEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.declarative.Design;
import com.vaadin.ui.themes.ValoTheme;
import vks.vpn.backend.Contact;
import vks.vpn.backend.ContactUtils;
import vks.vpn.backend.DbExceptions;
import vks.vpn.backend.SmsUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


/**
 * Created by upitis on 13.07.2016.
 */
@DesignRoot
public class SmsSenderView extends VerticalLayout {
    private Label headLabel;
    private TextField phonesField;
    private TextArea messageArea;
    private Button sendBtn;
    private Grid contactsGrid;


    public SmsSenderView() {
        super();

        Design.read(this);

        messageArea.setImmediate(true);

        messageArea.addValueChangeListener((Property.ValueChangeListener) event -> {
            sendBtn.setEnabled(true);
        });

        messageArea.addTextChangeListener((FieldEvents.TextChangeListener) event -> {
            String s = " / Всего: " + event.getText().length() + " символов";
            messageArea.setCaption("Тескт СМС (не более 900 символов)"  + s);
        });

        phonesField.setImmediate(true);

        phonesField.addTextChangeListener( (FieldEvents.TextChangeListener) event -> {
            sendBtn.setEnabled(true);
        });


        sendBtn.setIcon(FontAwesome.MAIL_FORWARD);
        sendBtn.setCaption("Отправить");

        sendBtn.addClickListener((Button.ClickListener) event -> {
            try {

                SmsUtils.sendSms(Arrays.asList(phonesField.getValue().split(";")),messageArea.getValue());
                Notification.show("Отправлено");
            } catch (DbExceptions ex) {
                Notification.show("Возникли проблемы с оправкой сообщения");
            }
            sendBtn.setEnabled(false);
        });

        contactsGrid.setContainerDataSource(ContactUtils.getContactsContainer());
        contactsGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        Grid.FooterRow footerRow = contactsGrid.appendFooterRow();
        for (Grid.Column c : contactsGrid.getColumns()) {
            c.setSortable(true);

            TextField textField = new TextField();
            textField.setStyleName(ValoTheme.TEXTFIELD_TINY);

            if (c.getPropertyId().equals("name")) {
                Button addRowButton = new Button();


                addRowButton.setIcon(FontAwesome.PLUS_SQUARE);
                addRowButton.setStyleName(ValoTheme.BUTTON_TINY);
                addRowButton.addClickListener((Button.ClickListener) event -> {
                    Contact contact = getFooterTextFieldsValues(contactsGrid, footerRow);
                    ContactUtils.addContactToContainer(contactsGrid.getContainerDataSource(), contact);
                    try {
                        ContactUtils.addNewContactToDb(contact);
                    } catch (SQLException e) {
                        Notification.show("Не удалось сохранить новый контакт в БД");
                    }
                });
                textField.setId("addingName");
                HorizontalLayout hLayout = new HorizontalLayout();
                hLayout.addComponents(addRowButton, textField);
                footerRow.getCell(c.getPropertyId()).setComponent(hLayout);
            } else {
                footerRow.getCell(c.getPropertyId()).setComponent(textField);
                c.setHidable(true);
            }
        }
        addFilterToContactsGrid(contactsGrid);
        contactsGrid.setEditorEnabled(true);
        contactsGrid.getColumn("id").setHidden(true);
        contactsGrid.getColumn("title").setHidden(true);
        contactsGrid.setFrozenColumnCount(2);

        contactsGrid.addSelectionListener((SelectionEvent.SelectionListener) event -> {
            contactsGrid.focus();
            String addedPhN = phonesField.getValue().toString();
            for (Object object: event.getSelected()) {
                Item item = contactsGrid.getContainerDataSource().getItem(object);
                if (item != null) {
                    String phoneNumber = item.getItemProperty("phone").getValue().toString();
                    if (addedPhN != null && !addedPhN.contains(phoneNumber)) {
                        phonesField.setValue(phonesField.getValue() + ";" + phoneNumber);
                    }
                }
                }
            for (Object object: event.getRemoved()) {
                String phoneNumber = contactsGrid.getContainerDataSource().getItem(object).getItemProperty("phone").getValue().toString();;
                phonesField.setValue(phonesField.getValue().toString().replace(phoneNumber,""));
            }

            phonesField.setValue(phonesField.getValue().toString().replaceAll(";{2,}",";"));
            if (phonesField.getValue().toString().startsWith(";")) {
                phonesField.setValue(phonesField.getValue().toString().substring(1));
            }
        });

        contactsGrid.getEditorFieldGroup().addCommitHandler(new FieldGroup.CommitHandler() {
            @Override
            public void preCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
                Notification.show("Изменения сохраняются...");
            }

            @Override
            public void postCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {

                Contact contact = new Contact();
                contact.setId((Integer) commitEvent.getFieldBinder().getItemDataSource().getItemProperty("id").getValue());
                contact.setName(commitEvent.getFieldBinder().getItemDataSource().getItemProperty("name").getValue().toString());
                contact.setTitle(commitEvent.getFieldBinder().getItemDataSource().getItemProperty("title").getValue().toString());
                contact.setDepartment(commitEvent.getFieldBinder().getItemDataSource().getItemProperty("department").getValue().toString());
                contact.setPhone(commitEvent.getFieldBinder().getItemDataSource().getItemProperty("phone").getValue().toString());
                contact.setCompany(commitEvent.getFieldBinder().getItemDataSource().getItemProperty("company").getValue().toString());
                try {
                    ContactUtils.saveEditedRowToDb(contact);
                } catch (SQLException e) {
                    Notification.show("Ошибка при сохранении изменений в базе");
                }
            }
        });


    }


    private void addFilterToContactsGrid(Grid grid) {
        Grid.HeaderRow filterRow = grid.appendHeaderRow();

        for (Object pid: grid.getContainerDataSource().getContainerPropertyIds()) {

            Grid.HeaderCell cell = filterRow.getCell(pid);

            TextField filterField = new TextField();

            filterField.setColumns(14);
            filterField.setInputPrompt("поиск...");
            filterField.addStyleName(ValoTheme.TEXTFIELD_TINY);

            filterField.addTextChangeListener(changes -> {
                IndexedContainer container = ((IndexedContainer)grid.getContainerDataSource());
                container.removeContainerFilters(pid);
                if (! changes.getText().isEmpty()) {
                    container.addContainerFilter(
                            new SimpleStringFilter(pid, changes.getText(), true, false)
                    );
                }
            });

            cell.setComponent(filterField);
        }
    }


    private Contact getFooterTextFieldsValues(Grid grid, Grid.FooterRow footerRow) {
        Contact contact = new Contact();
        for (Grid.Column c : contactsGrid.getColumns()) {
            Component component = footerRow.getCell(c.getPropertyId()).getComponent();;
            switch (c.getPropertyId().toString()) {
                case "id":
                    contact.setId(0);
                    break;
                case "name":
                    component = findComponentById((HasComponents) footerRow.getCell(c.getPropertyId()).getComponent(), "addingName");
                    contact.setName(((TextField) component).getValue());
                    break;
                case "company":
                    contact.setCompany(((TextField)component).getValue().toString());
                    break;
                case "department":
                    contact.setDepartment(((TextField)component).getValue().toString());
                    break;
                case "title":
                    contact.setTitle(((TextField)component).getValue().toString());
                    break;
                case "phone":
                    contact.setPhone(((TextField)component).getValue().toString());
                    break;
            }
            ((TextField)component).clear();
        }
        return contact;
    }

    private Component findComponentById(HasComponents root, String id) {
        Iterator<Component> iterate = root.iterator();
        while (iterate.hasNext()) {
            Component c = iterate.next();
            if (id.equals(c.getId())) {
                return c;
            }
            if (c instanceof HasComponents) {
                Component cc = findComponentById((HasComponents) c, id);
                if (cc != null)
                    return cc;
            }
        }
        return null;
    }

}
