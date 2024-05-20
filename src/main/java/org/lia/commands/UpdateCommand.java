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

public class UpdateCommand implements Command {
    private static final long serialVersionUID = 1785464768755190753L;

    private CollectionManager collectionManager;
    private SqlManager sqlManager;
    private CommandManager commandManager;
    private Product product;
    private String login;
    private String password;
    long productId;

    public UpdateCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public String description() {
        return "update product by it's id. Pattern: update (long)id";
    }

    public Response execute() {
        Response response = new Response();
        long userId = sqlManager.checkUser(login, password);
        Product oldProduct = null;
        for (Product c : collectionManager.getProductCollection()) {
            if (c.getId() == productId) {
                oldProduct = c;
                break;
            }
        }
        if (oldProduct == null | product == null) {
            response.addAnswer("no such element");
        } else if (oldProduct.getUserId() == userId) {
            try {
                product.setId(productId);
                product.setUserId(userId);
                product.setCreationDate(oldProduct.getCreationDate());
                sqlManager.updateElement(product);
                collectionManager.removeFromCollection(oldProduct);
                collectionManager.addToCollection(product);
                response.addAnswer("object was successfully updated");
            } catch (SQLException e) {
                response.addAnswer(e.toString());
            }
        } else {
            response.addAnswer("access denied: you can not modify objects created buy other users");
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
