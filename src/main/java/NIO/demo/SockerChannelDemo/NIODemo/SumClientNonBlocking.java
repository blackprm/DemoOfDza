package NIO.demo.SockerChannelDemo.NIODemo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class SumClientNonBlocking {
    public static void main(String[] args) throws IOException {
        SocketChannel client = SocketChannel.open();
        client.configureBlocking(false);

        InetSocketAddress serverAddress = new InetSocketAddress("localhost", 8080);
        if (!client.connect(serverAddress)) {
            while (!client.finishConnect()) {
                // Wait until the connection is established
            }
        }

        Selector selector = Selector.open();

        // Register for write to send integers
        client.register(selector, SelectionKey.OP_WRITE);

        int a = 5, b = 7;
        ByteBuffer requestBuffer = ByteBuffer.allocate(8); // Assuming 4 bytes per integer
        requestBuffer.putInt(a);
        requestBuffer.putInt(b);
        requestBuffer.flip();

        while (true) {
            if (selector.select() > 0) {
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();

                    if (key.isWritable()) {
                        SocketChannel channel = (SocketChannel) key.channel();

                        // Send the integers
                        while (requestBuffer.hasRemaining()) {
                            channel.write(requestBuffer);
                        }

                        // Unregister write and register read
                        key.interestOps(SelectionKey.OP_READ);
                    } else if (key.isReadable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer responseBuffer = ByteBuffer.allocate(4); // For result

                        // Read the sum from the server
                        while (responseBuffer.remaining() > 0) {
                            if (channel.read(responseBuffer) == -1) {
                                throw new IOException("Connection closed");
                            }
                        }

                        responseBuffer.flip();
                        int sum = responseBuffer.getInt();

                        System.out.println("Received sum: " + sum);

                        // Close the connection after receiving the result
                        channel.close();
                        selector.close();
                        return;
                    }
                }
            }
        }
    }
}