package org.lia.commands;

import org.lia.managers.CollectionManager;
import org.lia.managers.CommandManager;
import org.lia.managers.FileManager;
import org.lia.managers.SqlManager;
import org.lia.tools.Response;

import java.sql.SQLException;
import java.util.ArrayDeque;

public class SignUpCommand implements Command {
    private static final long serialVersionUID = 1785464768755190753L;


    private CollectionManager collectionManager;
    private SqlManager sqlManager;
    private CommandManager commandManager;
    private String login;
    private String passwordString;
    private byte[] passwordByte;
    private String password;
    public SignUpCommand(SqlManager sqlManager) {
        this.sqlManager = sqlManager;
    }

    public String description() {
        return "creates new user";
    }

    public Response execute() {
        Response response = new Response();
        try {
            long id = sqlManager.addUser(login, passwordString);

        } catch (SQLException e) {
            response.addAnswer(e.toString());
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
