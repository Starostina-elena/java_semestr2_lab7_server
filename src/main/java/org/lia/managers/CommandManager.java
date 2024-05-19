package org.lia.managers;

import org.lia.commands.*;
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
            while (true) {
                getInputFromConsole();
                SocketAddress address = dc.receive(buf);
                if (address != null) {
                    buf.flip();
                    try {
                        ByteArrayInputStream in = new ByteArrayInputStream(buf.array());
                        ObjectInputStream is = new ObjectInputStream(in);
                        Command command = (Command) is.readObject();
                        command.setCollectionManager(collectionManager);
                        command.setCommandManager(this);
                        command.setSqlManager(sqlManager);
                        Response response;
                        if (sqlManager.checkUser(command.getLogin(), command.getPassword()) > 0 | command.getClass()==SignUpCommand.class) {
                            response = command.execute();
                        } else {
                            response = new Response();
                            response.addAnswer("Access denied. Please login");
                        }
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(baos);
                        oos.writeObject(response);
                        byte[] secondaryBuffer = baos.toByteArray();
                        ByteBuffer mainBuffer = ByteBuffer.wrap(secondaryBuffer);
                        dc.send(mainBuffer, address);
                    } catch (StreamCorruptedException | UTFDataFormatException e) {
                        System.out.println("Bad packet");
                        buf.clear();
                        Response response = new Response();
                        response.addAnswer("Protocol error. Please try again");
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(baos);
                        oos.writeObject(response);
                        byte[] secondaryBuffer = baos.toByteArray();
                        ByteBuffer mainBuffer = ByteBuffer.wrap(secondaryBuffer);
                        dc.send(mainBuffer, address);
                    } catch (EOFException | ClassNotFoundException e) {
                        System.out.println(e);
                    }
                }
                buf.clear();
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
                if (command.equals("save")) {
                    //this.fileManager.writeCollection(collectionManager.getProductCollection());
                } else if (command.equals("exit")) {
                    System.out.println("Do you want to save your collection? Y/n");
                    System.out.print("> ");
                    String answer = in.nextLine().toUpperCase();
                    while (!answer.equals("Y") & !answer.equals("N")) {
                        System.out.println("Wrong input, try again. Do you want to save the collection? Y/n");
                        System.out.print("> ");
                        answer = in.nextLine().toUpperCase();
                    }
                    if (answer.equals("Y")) {
                        //this.fileManager.writeCollection(collectionManager.getProductCollection());
                    }
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