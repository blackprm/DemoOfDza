package nettyDemo.sourceDemo;

import nettyDemo.sourceDemo.pool.Boss;
import nettyDemo.sourceDemo.pool.NioSelectorRunnablePool;
import nettyDemo.sourceDemo.pool.Worker;

import java.io.IOException;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;
/**
 * boss实现类
 * @author -琴兽-
 *
 */
public class NioServerBoss extends AbstractNioSelector implements Boss {

	public NioServerBoss(Executor executor, String threadName, NioSelectorRunnablePool selectorRunnablePool) {
		super(executor, threadName, selectorRunnablePool);
	}

	@Override
	protected void process(Selector selector) throws IOException {
		Set<SelectionKey> selectedKeys = selector.selectedKeys();
        if (selectedKeys.isEmpty()) {
            return;
        }
        
        for (Iterator<SelectionKey> i = selectedKeys.iterator(); i.hasNext();) {
            SelectionKey key = i.next();
            i.remove();
            ServerSocketChannel server = (ServerSocketChannel) key.channel();
    		// 新客户端
    		SocketChannel channel = server.accept();
    		// 设置为非阻塞
    		channel.configureBlocking(false);
    		// 获取一个worker
    		Worker nextworker = getSelectorRunnablePool().nextWorker();
    		// 注册新客户端接入任务
    		nextworker.registerNewChannelTask(channel);
    		
    		System.out.println("新客户端链接");
        }
	}
	
	
	public void registerAcceptChannelTask(final ServerSocketChannel serverChannel){
		 final Selector selector = this.selector;
		 registerTask(new Runnable() {
			@Override
			public void run() {
				try {
					//注册serverChannel到selector
					serverChannel.register(selector, SelectionKey.OP_ACCEPT);
				} catch (ClosedChannelException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	@Override
	protected int select(Selector selector) throws IOException {
		return selector.select();
	}

}
