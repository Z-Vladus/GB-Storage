package Lesson2.NIO_webinar_code;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class ChannelReader {



    private final SocketChannel channel;
    private final ByteBuffer buffer;
    // т.к. данные могут приходить рандомно, будем действовать также,
    // т.е. сначала передавая размер данных(заголовок со служ.инфой).
    // определим некий флаг, который будет разделять данные и заголовок
    private boolean headerReadFlag = true;
    private byte[] data;

    public ChannelReader(SocketChannel channel) {
        buffer = ByteBuffer.allocate(1024);

        this.channel = channel;
    }

    public void read () {
        // если мы сюда попали, значит, селектор определил что канал доступен для чтения
        // и в буфере гарантированно , что есть данные
        try {
            // читаем
            channel.read(buffer);
            // и определяем, первое ли это чтение (чтение заголовка?)
            if (headerReadFlag) {
                // если первое, то проверяем, есть ли в буфере 4 байта,
                // которые дадут нам int - размер данных.
                System.out.println("ChannelReader.read: expecting header data");
                if (buffer.position() < 4 ) {
                    // если данных заголовка мало, уходим, и ждём когда будут 4 байта данных заголовка.
                    System.out.println("Header not full, waiting for data...");
                    return;
                }
                // если заголовок уже получен, то читаем. Но до этого буфер переводим в режим чтения.
                buffer.flip();
                // парсим заголовок в переменную.
                int size = buffer.getInt();
                // определяем размер
                data = new byte[size];
                // отметим, что флаг уже прочитан.
                headerReadFlag = false;
                //TODO 1:45 записи вебинара
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
