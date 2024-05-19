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

public class AddIfMaxCommand implements Command {
    private static final long serialVersionUID = 1785464768755190753L;


    private CollectionManager collectionManager;
    private SqlManager sqlManager;
    private CommandManager commandManager;
    private Product product;
    private String login;
    private String password;
    public AddIfMaxCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public String description() {
        return "adds new element if this element is bigger than max element in collection. Pattern: " +
                "add_if_max (String)name (Integer)price(may be null, use empty string) (String)partNumber " +
                "(Integer)manufactureCost";
    }

    public Response execute() {
        Response response = new Response();
        try {

            Product max_product = collectionManager.getProductCollection().getFirst();
            for (Product c : collectionManager.getProductCollection()) {
                if (c.compareTo(max_product) > 0) {
                    max_product = c;
                }
            }
            if (product.compareTo(max_product) > 0) {
                product.setup();
                product.setUserId(sqlManager.checkUser(login, password));
                try {
                    long id = sqlManager.addElement(product);
                    product.setId(id);
                    collectionManager.addToCollection(product);
                } catch (SQLException e) {
                    response.addAnswer(e.toString());
                }
                response.addAnswer("object was successfully added");
                response.addAnswer(product.toString());
            } else {
                response.addAnswer("object is not max, it wasn't added");
            }
        } catch (IllegalArgumentException e) {
            response.addAnswer(e + ". Please try again");
        } catch (ArrayIndexOutOfBoundsException e) {
            response.addAnswer("Incorrect number of arguments for add_if_max command. Please try again");
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
