package NIO.demo.SockerChannelDemo.BIODemo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ServerSocketChannelBIODemo {
    private ServerSocketChannel serverSocketChannel;
    private int port = 8888;
    private ByteBuffer buffer = ByteBuffer.allocate(1024);
    private IntBuffer intBuffer = buffer.asIntBuffer();

    private SocketChannel clientSocketChannel;
    public void bindServer() throws IOException {
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        System.out.println("Socket连接已经创建完成,绑定端口为: " + port);
    }

    public void waitReqConn() throws IOException {
        while(true) {
            if ((clientSocketChannel = serverSocketChannel.accept()) != null) {
                System.out.println(clientSocketChannel + "接入请求,并开始处理数据");
                clientSocketChannel.read(buffer);
                int result = intBuffer.get(0) + intBuffer.get(1);
                buffer.flip();
                buffer.clear();
                intBuffer.put(result);
                clientSocketChannel.write(buffer);
                System.out.println(clientSocketChannel + "关闭");

            }
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocketChannelBIODemo server = new ServerSocketChannelBIODemo();
        server.bindServer();
        server.waitReqConn();
    }
}
