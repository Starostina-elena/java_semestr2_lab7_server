package org.lia.multithreading;

import org.lia.commands.SignUpCommand;
import org.lia.tools.Response;

public class WorkRequest implements Runnable {

    GetRequest request;
    public WorkRequest(GetRequest getRequest) {
        request = getRequest;
    }

    @Override
    public void run() {
        request.command.setCollectionManager(request.collectionManager);
        request.command.setCommandManager(request.commandManager);
        request.command.setSqlManager(request.sqlManager);
        if (request.sqlManager.checkUser(request.command.getLogin(), request.command.getPassword()) > 0 |
                request.command.getClass()== SignUpCommand.class) {
            synchronized (request.collectionManager) {
                request.response = request.command.execute();
            }
        } else {
            request.response = new Response();
            request.response.addAnswer("Access denied. Please login");
        }
        request.answer = new SendRequest(request);
        request.executor.submit(request.answer);
    }
}
