package org.lia;

import org.lia.managers.CollectionManager;
import org.lia.managers.CommandManager;
import org.lia.managers.FileManager;

import java.io.*;
import java.util.Scanner;

import org.lia.managers.SqlManager;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {
    public static void main(String[] args) {

        String username = "";
        String password = "";

        try {
            Scanner loginInfo = new Scanner(new FileReader("secret.txt"));
            username = loginInfo.nextLine().trim();
            password = loginInfo.nextLine().trim();
        } catch (FileNotFoundException e) {
            System.out.println("Please rename file secret_example.txt to secret.txt and fill it with your data");
            System.exit(-1);
        } catch (NoSuchElementException e) {
            System.out.println("Please fill file example.txt with your data");
            System.exit(-1);
        }

        SqlManager sqlManager = new SqlManager(username, password);

        CollectionManager collection = sqlManager.getCollectionFromDatabase();

        CommandManager commandManager = new CommandManager(collection, sqlManager);
        System.out.println("program initialization: successful");

        commandManager.listen();

    }
}