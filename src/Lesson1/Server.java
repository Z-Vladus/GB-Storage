package Lesson1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Server implements Runnable {
    private final int port;
    private ServerSocket serverSocket;
    private boolean isStopped;

    public Server(int port) {
        this.port = port;
    }

    public void stopServer (){
        isStopped=true;
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        Socket socket;
        openServerSocket();
        System.out.println("Server started");

        while (!isStopped) {

            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                // если сервер уже остановлен, то сообщаем и не брсаем исключение
                if (isStopped) {
                    System.out.println("Server is stopped");
                    return;
                }
                throw new RuntimeException(e);
            }
            try {
                process(socket);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }
    }

    private void openServerSocket() {
        try {
            serverSocket = new ServerSocket(port);

        } catch (IOException e) {
            throw new RuntimeException("Cannot open server socket ");
        }
    }

    private void process(Socket socket) throws IOException {

        // DataInputStream позволяет читать сразу int , без раскладки и сборки int побайтно.
        DataInputStream inStream = new DataInputStream(socket.getInputStream());
        DataOutput outStream = new DataOutputStream(socket.getOutputStream());

        // идея в том, чтобы сначала узнать длину передаваемой строки, а потом считать эту длину.
        int size = inStream.readInt();
        // подготовим буфер данных
        byte[] data = new byte[size];
        // полезная фича! заполняет массив полностью, не надо в цикле по байтам читать
        inStream.readFully(data);

        System.out.println("Received data:" + new String(data));

        //строка ответа
        String response = "Hello, client!";

        // в массив строку переводим
        byte[] outBytes = response.getBytes(StandardCharsets.UTF_8);

        // пошлём обратно на сервер длину посылаемой позже строки (массива)
        outStream.writeInt(outBytes.length);

        // шлём строку (массив)
        outStream.write(outBytes);

        System.out.println("Response processed");

    }
}
