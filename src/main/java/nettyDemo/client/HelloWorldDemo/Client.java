package nettyDemo.client.HelloWorldDemo;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    public static void main(String[] args) {
        ClientBootstrap clientBootstrap = new ClientBootstrap();

        ExecutorService boss = Executors.newCachedThreadPool();
        ExecutorService worker = Executors.newCachedThreadPool();

        clientBootstrap.setFactory(new NioClientSocketChannelFactory(boss,worker));
        clientBootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("decoder",new StringDecoder());
                pipeline.addLast("encoder",new StringEncoder());
                pipeline.addLast("clientHandler",new ClientHandler());
                return pipeline;
            }
        });

        ChannelFuture connect = clientBootstrap.connect(new InetSocketAddress("127.0.0.1", 8888));
        Channel channel = connect.getChannel();
        Scanner in = new Scanner(System.in);
        while(true) {
            System.out.println("输入指令");
            channel.write(in.next());
        }

    }
}
