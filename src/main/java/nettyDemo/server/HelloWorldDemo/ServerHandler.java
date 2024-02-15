package nettyDemo.server.HelloWorldDemo;

import org.jboss.netty.channel.*;

public class ServerHandler extends SimpleChannelHandler {

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        String message = (String)e.getMessage();
        Channel channel = e.getChannel();
        channel.write(message + "pong");
        System.out.println(message);
        super.messageReceived(ctx, e);
    }


    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        super.channelConnected(ctx, e);
        System.out.println("连接来了"+ e);
        Channel clientChannel = e.getChannel();
        clientChannel.write("hello world");
    }
}
