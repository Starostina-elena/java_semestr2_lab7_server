package org.lia.commands;

import org.lia.managers.CollectionManager;
import org.lia.managers.CommandManager;
import org.lia.managers.FileManager;
import org.lia.managers.SqlManager;
import org.lia.models.Organization;
import org.lia.models.Product;
import org.lia.tools.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PrintFieldAscendingManufacturerCommand implements Command {
    private static final long serialVersionUID = 1785464768755190753L;

    private CollectionManager collectionManager;
    private SqlManager sqlManager;
    private CommandManager commandManager;
    private String login;
    private String password;
    public PrintFieldAscendingManufacturerCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public String description() {
        return "shows all manufacturers in ascending orders";
    }

    public Response execute() {
        Response response = new Response();
        List<Organization> orgList = new ArrayList<>();
        for (Product c : collectionManager.getProductCollection()) {
            orgList.add(c.getManufacturer());
        }
        Collections.sort(orgList);
        for (Organization c : orgList) {
            response.addAnswer(c.toString());
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
