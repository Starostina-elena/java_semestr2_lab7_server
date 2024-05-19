package org.lia.commands;

import org.lia.managers.CollectionManager;
import org.lia.managers.CommandManager;
import org.lia.managers.FileManager;
import org.lia.managers.SqlManager;
import org.lia.models.Product;
import org.lia.tools.Response;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayDeque;

public class ClearCommand implements Command {
    private static final long serialVersionUID = 1785464768755190753L;


    private CollectionManager collectionManager;
    private SqlManager sqlManager;
    private CommandManager commandManager;
    private String login;
    private String password;

    public ClearCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public String description() {
        return "clears collection";
    }

    public Response execute() {
        Response response = new Response();
        long userId = sqlManager.checkUser(login, password);
        sqlManager.deleteElementsByUserId(userId);
        for (Product c: collectionManager.getProductCollection()) {
            if (c.getUserId() == userId) {
                collectionManager.removeFromCollection(c);
            }
        }
        response.addAnswer("Products created buy you were deleted");
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
