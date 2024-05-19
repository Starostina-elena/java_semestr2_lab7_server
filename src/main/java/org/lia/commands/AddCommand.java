package org.lia.commands;

import org.lia.managers.CollectionManager;
import org.lia.managers.CommandManager;
import org.lia.managers.FileManager;
import org.lia.managers.SqlManager;
import org.lia.models.Coordinates;
import org.lia.models.Organization;
import org.lia.models.Product;
import org.lia.models.UnitOfMeasure;
import org.lia.tools.Response;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class AddCommand implements Command {
    private static final long serialVersionUID = 1785464768755190753L;


    private CollectionManager collectionManager;
    private SqlManager sqlManager;
    private CommandManager commandManager;
    private Product product;
    private String login;
    private String password;

    public AddCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public String description() {
        return "adds an element to collection. " +
                "Pattern: add (String)name (Integer)price(may be null, use empty string) (String)partNumber (Integer)manufactureCost";
    }

    public Response execute() {
        Response response = new Response();
        try {
            product.setup();
            product.setUserId(sqlManager.checkUser(login, password));
            try {
                long id = sqlManager.addElement(product);
                product.setId(id);
                collectionManager.addToCollection(product);
                response.addAnswer("object was successfully added");
            } catch (SQLException e) {
                response.addAnswer(e.toString());
            }
        } catch (IllegalArgumentException e) {
            response.addAnswer(e + ". Please try again");
        } catch (ArrayIndexOutOfBoundsException e) {
            response.addAnswer("Incorrect number of arguments for add command. Please try again");
        }
        return response;
    }

    public void setCollectionManager(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public void setSqlManager(SqlManager sqlManager) {
        this.sqlManager = sqlManager;
    }

    public void setCommandManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }
    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
