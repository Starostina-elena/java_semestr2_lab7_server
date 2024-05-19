package org.lia.commands;

import org.lia.managers.CollectionManager;
import org.lia.managers.CommandManager;
import org.lia.managers.FileManager;
import org.lia.managers.SqlManager;
import org.lia.tools.Response;

public class InfoCommand implements Command {
    private static final long serialVersionUID = 1785464768755190753L;
    private String login;
    private String password;


    private CollectionManager collectionManager;
    private SqlManager sqlManager;
    private CommandManager commandManager;

    public InfoCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public String description() {
        return "shows short information on collection";
    }

    public Response execute() {
        Response response = new Response();
        response.addAnswer(collectionManager.shortInfo());
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
