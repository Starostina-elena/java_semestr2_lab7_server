package org.lia.commands;

import org.lia.managers.CollectionManager;
import org.lia.managers.CommandManager;
import org.lia.managers.FileManager;
import org.lia.managers.SqlManager;
import org.lia.models.Product;
import org.lia.tools.Response;

public class RemoveHeadCommand implements Command {
    private static final long serialVersionUID = 1785464768755190753L;

    private CollectionManager collectionManager;
    private SqlManager sqlManager;
    private CommandManager commandManager;
    private String login;
    private String password;

    public RemoveHeadCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public String description() {
        return "shows first element of collection and deletes it";
    }

    public Response execute() {
        Response response = new Response();
        long userId = sqlManager.checkUser(login, password);
        boolean status = false;
        for (Product c: collectionManager.getProductCollection()) {
            if (c.getUserId() == userId) {
                response.addAnswer(c.toString());
                response.addAnswer("Object was successfully deleted");
                sqlManager.deleteElementById(c.getId(), userId);
                collectionManager.removeFromCollection(c);
                status = true;
                break;
            }
        }
        if (!status) {
            response.addAnswer("There are no elements created by you in the collection");
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
