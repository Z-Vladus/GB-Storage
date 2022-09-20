package Lesson2.NIO;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class ChannelReader {

    public ChannelReader(SocketChannel channel) {
        this.channel = channel;
    }

    private final SocketChannel channel;

    public void read () {

    }
}
