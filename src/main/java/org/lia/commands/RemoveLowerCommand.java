package org.lia.commands;

import org.lia.managers.CollectionManager;
import org.lia.managers.CommandManager;
import org.lia.managers.FileManager;
import org.lia.managers.SqlManager;
import org.lia.models.Product;
import org.lia.tools.Response;

import java.util.ArrayDeque;

public class RemoveLowerCommand implements Command {
    private static final long serialVersionUID = 1785464768755190753L;

    private CollectionManager collectionManager;
    private SqlManager sqlManager;
    private CommandManager commandManager;
    private long id;
    private String login;
    private String password;

    public RemoveLowerCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public String description() {
        return "delete from collection all elements lower that selected. Pattern: remove_lower (long)id";
    }

    public Response execute() {
        Response response = new Response();
        long userId = sqlManager.checkUser(login, password);
        try {
            Product product = collectionManager.getById(id);
            int counter = 0;
            ArrayDeque<Product> collection = collectionManager.getProductCollection();
            for (Product c: collection) {
                if (c.getUserId() == userId & product.compareTo(c) > 0) {
                    counter++;
                    sqlManager.deleteElementById(c.getId(), userId);
                    collectionManager.removeFromCollection(c);
                }
            }
            response.addAnswer(counter + " products were successfully deleted");
        } catch (ArrayIndexOutOfBoundsException e) {
            response.addAnswer("Incorrect number of arguments for remove_lower command. Please try again");
        } catch (NumberFormatException e) {
            response.addAnswer("id is not integer. Please try again");
        } catch (IllegalArgumentException e) {
            response.addAnswer(e + ". Please try again");
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
