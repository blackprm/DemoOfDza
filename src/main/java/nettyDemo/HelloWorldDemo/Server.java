package nettyDemo.HelloWorldDemo;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String[] args) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        ExecutorService boss = Executors.newCachedThreadPool();
        ExecutorService workers = Executors.newCachedThreadPool();
        serverBootstrap.setFactory(new NioServerSocketChannelFactory(boss, workers));
        serverBootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            /**
             * Returns a newly created {@link ChannelPipeline}.
             */
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("stringDecoder", new StringDecoder());
                pipeline.addLast("stringEncoder", new StringEncoder());
                pipeline.addLast("handler",new MyHandler());
                return pipeline;
            }
        });
        serverBootstrap.bind(new InetSocketAddress("127.0.0.1",8888));
    }
}
