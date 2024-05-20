package org.lia.managers;

import org.lia.commands.*;
import org.lia.multithreading.GetRequest;
import org.lia.tools.Response;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**CommandManager class. Provides operations with commands*/
public class CommandManager {

    private Map<String, Command> commandsManager = new HashMap<>();
    private CollectionManager collectionManager;
    private SqlManager sqlManager;

    DatagramChannel dc;
    ByteBuffer buf = ByteBuffer.allocate(1 << 17 - 1);
    InetAddress host;
    int port = 6789;
    SocketAddress addr = new InetSocketAddress(port);

    /**Constructor. Loading of available commands*/
    public CommandManager(CollectionManager collectionManager, SqlManager sqlManager) {

        this.collectionManager = collectionManager;
        this.sqlManager = sqlManager;

    }

    public void listen() {
        try {
            dc = DatagramChannel.open();
            dc.bind(addr);
            dc.configureBlocking(false);
            ExecutorService executor = Executors.newCachedThreadPool();
            while (true) {
                getInputFromConsole();
                GetRequest request = new GetRequest(buf, dc, collectionManager, sqlManager, this, executor);
                executor.submit(request);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getInputFromConsole() {
        try {
            if (System.in.available() > 0) {
                Scanner in = new Scanner(System.in);
                String command = in.nextLine();
                if (command.equals("exit")) {
                    System.out.println("goodbye");
                    System.exit(0);
                } else {
                    System.out.println("Unknown command, please try again");
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public Map<String, Command> getCommandsList() {
        return commandsManager;
    }

    public CollectionManager getCollectionManager() {
        return collectionManager;
    }

    //public FileManager getFileManager() {
      //  return fileManager;
    //}

}