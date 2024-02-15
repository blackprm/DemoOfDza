package nettyDemo.client.HelloWorldDemo;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

public class ClientHandler extends SimpleChannelHandler {
    /**
     * Invoked when a message object (e.g: {@link ChannelBuffer}) was received
     * from a remote peer.
     *
     * @param ctx
     * @param e
     */
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        super.messageReceived(ctx, e);
        System.out.println(e.getMessage());
    }
}
