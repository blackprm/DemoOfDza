package NIO.demo.SockerChannelDemo.NIODemo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class SumServerNonBlocking {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress(8080));
        serverSocket.configureBlocking(false);

        Selector selector = Selector.open();

        serverSocket.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            if (selector.select() > 0) {
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();

                    if (key.isAcceptable()) {
                        SocketChannel client = serverSocket.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ);
                    } else if (key.isReadable()) {
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);

                        // Read two integers
                        int totalRead = 0;
                        while (totalRead < 8) { // Assuming 4 bytes per integer
                            int readBytes = client.read(buffer);
                            if (readBytes == -1) {
                                throw new IOException("Connection closed");
                            }
                            totalRead += readBytes;
                        }

                        buffer.flip();
                        int a = buffer.getInt();
                        int b = buffer.getInt();

                        // Calculate result
                        int sum = a + b;

                        // Write result back
                        buffer.clear();
                        buffer.putInt(sum);
                        buffer.flip();
                        while (buffer.hasRemaining()) {
                            client.write(buffer);
                        }

                        // Close the connection after one request
                        client.close();
                    }
                }
            }
        }
    }
}