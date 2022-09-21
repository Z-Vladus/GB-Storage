package Lesson2.NIO_webinar_code;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Server implements Runnable {

    private final int port;
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;

    public Server(int port) {
        this.port = port;
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(port));
            // неблокирующий режим включаем
            serverSocketChannel.configureBlocking(false);
            // регистрируем serverSocketChannel в selector
            // сселектор постоянно опрашивает serverSocketChannel на предмет доступности клиента.
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public static void main(String[] args) {
        new Thread(new Server(9000)).start();
    }

    @Override
    public void run() {
        while (true) {
            // бокирующий метод. Ждём, пока не зарегистрированохотя бы одно событие.
            // в нашем случае - хотя бы 1 клиент (OP_ACCEPT)
            try {

                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isValid()) {
                        break;
                    }
                    if (key.isValid() && key.isAcceptable()) {
                        acceptClient();
                    }

                    if (key.isValid() && key.isReadable()) {
                        if (key.attachment() == null) {
                            key.attach(new ChannelReader((SocketChannel) key.channel()));
                        }
                        ((ChannelReader) key.attachment()).read();
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void acceptClient(){
        try {
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector,SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            System.out.println("accepted new client");
        } catch (IOException e) {
            //throw new RuntimeException(e);
            System.out.println("Client accept failed");
        }
    }
    
}
