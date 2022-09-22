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
            try {
                // бокирующий метод select(). Ждём, пока не произойдёт хотя бы одно событие
                // в нашем случае - хотя бы 1 клиент (OP_ACCEPT)

                selector.select();
                // это набор, содержащий данные по каналу, и др. служ. инф.
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                // итератор нужен для удаления обработанных ключей, т.е. selector продолжает добавлять
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                // обходим коллекцию, достаём каналы, в которых произошли события.
                while (iterator.hasNext()) {
                    //извлечение из итератора
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    // нужна проверка на некую валидность...
                    if (key.isValid()) {
                        System.out.println("key is not valid, exiting");
                        break;
                    }
                    System.out.println("key is valid, proceeding");
                    // проверяем категорию, к которой относится key (какой channel содержит)
                    // и если это подключение клиента, уходим в метод инициализации подключения
                    if (key.isValid() && key.isAcceptable()) {
                        System.out.println("key is acceptable, proceeding");
                        acceptClient();
                    }
                    // .. а если это событие чтения, то читаем.
                    // проблема в том, что обработчик общий, а каналы - разные.
                    // поэтому надо создавать экземпляры класса, для работы только со своим каналом.
                    // назовём его ChannelReader
                    if (key.isValid() && key.isReadable()) {
                        // работаем со своим экземпляром класса ChannelReader
                        // если его ещё нет, то создаём его
                        if (key.attachment() == null) {
                            key.attach(new ChannelReader((SocketChannel) key.channel()));
                        }
                        // после прикрепления, читаем из ключа, скастовав ChannelReader
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
            // принимаем клиента, получаем ассоциированный с ним socketChannel
            SocketChannel socketChannel = serverSocketChannel.accept();
            // Далее нужно его также в selector зарегистрировать, сконфигурировать в неблок. режим
            socketChannel.configureBlocking(false);
            // Регистрируем аналогично, но для мониторинга других событий.
            // Будем писать или читать.
            // Конфигурация через битовую маску. OP_READ = 001 ,  OP_WRITE = 100
            // складываем их побитно, получаем 001 + 100 = 101
            socketChannel.register(selector,SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            System.out.println("accepted new client ");


        } catch (IOException e) {
            //throw new RuntimeException(e);
            System.out.println("Client accept failed");
        }
    }
    
}
