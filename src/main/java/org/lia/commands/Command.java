package org.lia.commands;

import org.lia.managers.CollectionManager;
import org.lia.managers.CommandManager;
import org.lia.managers.FileManager;
import org.lia.managers.SqlManager;
import org.lia.tools.Response;

import java.io.Serializable;

public interface Command extends Serializable {
    Response execute();
    void setCollectionManager(CollectionManager collectionManager);
    void setSqlManager(SqlManager sqlManager);
    void setCommandManager(CommandManager commandManager);
    String description();
    public String getLogin();
    public String getPassword();
}
