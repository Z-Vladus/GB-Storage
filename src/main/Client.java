package main;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 4445);
        String response = client.sendMsg("Hello, Server!!!");
        System.out.println("Response="+response);

    }

private final String host;
    private final int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String sendMsg (String msg){
        try (Socket socket = new Socket(host,port)){
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            byte[] sendData = msg.getBytes(StandardCharsets.UTF_8);

            DataOutput dataOutput = new DataOutputStream(outputStream);
            dataOutput.writeInt(sendData.length);
            outputStream.write(sendData);

            System.out.println("Message is sent to server");

            DataInput dataInput  =  new DataInputStream(inputStream);

            int size = dataInput.readInt();

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
