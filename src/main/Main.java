package main;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Server server = new Server(4445);
        new Thread(server).start();

        Scanner scanner = new Scanner(System.in);
        while (scanner.nextLine().equals("stop")) {
            server.stopServer();
        }

    }
}
