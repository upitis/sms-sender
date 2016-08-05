package vks.vpn.backend;

import com.vaadin.data.Container;
import com.vaadin.data.util.IndexedContainer;

import java.sql.*;

/**
 * Created by upitis on 15.07.2016.
 */
public class ContactUtils {
    private static final String CONTACTS_SELECT_SQL_QUERY = "SELECT * FROM Contacts";
    private static final String CONTACTS_UPDATE_SQL_QUERY = "UPDATE Contacts SET name = ?, company = ?, department = ?, " +
            "                                                   title = ?, phone = ? WHERE id = ?";
    private static final String CONTACT_INSERT_SQL_QUERY = "INSERT INTO Contacts (name,company,department,title,phone) values(?,?,?,?,?)";

    public static IndexedContainer getContactsContainer() {
        IndexedContainer indexedContainer = new IndexedContainer();
        indexedContainer.addContainerProperty("id",Integer.class,"");
        indexedContainer.addContainerProperty("name",String.class,"");
        indexedContainer.addContainerProperty("company",String.class,"");
        indexedContainer.addContainerProperty("department",String.class,"");
        indexedContainer.addContainerProperty("title",String.class,"");
        indexedContainer.addContainerProperty("phone",String.class,"");

        Connection connection = null;
        try {
            connection = ScPhonesDb.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(CONTACTS_SELECT_SQL_QUERY);

            while (resultSet.next()) {
                Object item = indexedContainer.addItem();
                indexedContainer.getItem(item).getItemProperty("id").setValue(resultSet.getInt("id"));
                indexedContainer.getItem(item).getItemProperty("name").setValue(resultSet.getString("name"));
                indexedContainer.getItem(item).getItemProperty("company").setValue(resultSet.getString("company"));
                indexedContainer.getItem(item).getItemProperty("department").setValue(resultSet.getString("department"));
                indexedContainer.getItem(item).getItemProperty("title").setValue(resultSet.getString("title"));
                indexedContainer.getItem(item).getItemProperty("phone").setValue(resultSet.getString("phone"));
            }
        } catch (SQLException ex) {
            throw new DbExceptions();
        } finally {
            ScPhonesDb.closeConnection(connection);
        }
        return indexedContainer;
    }

    public static void addContactToContainer(Container container, Contact contact) {
        Object item = container.addItem();
        container.getItem(item).getItemProperty("id").setValue(contact.getId());
        container.getItem(item).getItemProperty("name").setValue(contact.getName());
        container.getItem(item).getItemProperty("company").setValue(contact.getCompany());
        container.getItem(item).getItemProperty("department").setValue(contact.getDepartment());
        container.getItem(item).getItemProperty("title").setValue(contact.getTitle());
        container.getItem(item).getItemProperty("phone").setValue(contact.getPhone());
    }

    public static void saveEditedRowToDb(Contact contact) throws SQLException {

        Connection connection = null;
        try {
            connection = ScPhonesDb.getConnection();
            PreparedStatement statement = connection.prepareStatement(CONTACTS_UPDATE_SQL_QUERY);

            statement.setString(1, contact.getName());
            statement.setString(2, contact.getCompany());
            statement.setString(3, contact.getDepartment());
            statement.setString(4, contact.getTitle());
            statement.setString(5, contact.getPhone());
            statement.setInt(6, contact.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DbExceptions();
        } finally {
            ScPhonesDb.closeConnection(connection);
        }

    }

    public static void addNewContactToDb(Contact contact) throws SQLException {
        Connection connection = null;
        try {
            connection = ScPhonesDb.getConnection();
            PreparedStatement statement = connection.prepareStatement(CONTACT_INSERT_SQL_QUERY);
            statement.setString(1, contact.getName());
            statement.setString(2, contact.getCompany());
            statement.setString(3, contact.getDepartment());
            statement.setString(4, contact.getTitle());
            statement.setString(5, contact.getPhone());
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DbExceptions();
        } finally {
            ScPhonesDb.closeConnection(connection);
        }

    }
}
