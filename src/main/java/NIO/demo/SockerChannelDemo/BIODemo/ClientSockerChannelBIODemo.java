package NIO.demo.SockerChannelDemo.BIODemo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.SocketChannel;

public class ClientSockerChannelBIODemo {
    private ByteBuffer byteBuffer = ByteBuffer.allocate(8);
    private IntBuffer intBuffer = byteBuffer.asIntBuffer();
    private SocketChannel clientSocketChannel = null;

    public void connect() throws IOException {
        clientSocketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1",8888));
        System.out.println(clientSocketChannel + "已经连接");
    }

    public int getSum (Integer a, Integer b) throws IOException {
        connect();
        intBuffer.put(0,a);
        intBuffer.put(1,b);
        clientSocketChannel.write(byteBuffer);
        System.out.printf(clientSocketChannel + "发送请求 参数为: %d,%d%n",a,b);
        byteBuffer.clear();
        clientSocketChannel.read(byteBuffer);
        int result = intBuffer.get(0);
        clientSocketChannel.close();
        return result;
    }

    public static void main(String[] args) throws IOException {
        ClientSockerChannelBIODemo clientSockerChannelBIODemo = new ClientSockerChannelBIODemo();
        int sum = clientSockerChannelBIODemo.getSum(3, 6);
        System.out.println("求和结果为 : " + sum);
    }

}
