package NIO.demo;

import java.nio.IntBuffer;

public class BufferDemo {
    public static void main(String[] args) {
        int [] data = new int[] {3,5,7};
        // 创建缓冲区
        IntBuffer dataBuffer = IntBuffer.wrap(data);
        // duplicate底层数组的数据是共享的，但是buffer属性是不共享的
        IntBuffer dataBufferDuplicate = dataBuffer.duplicate();
        System.out.println(dataBuffer.get() + " : " + dataBufferDuplicate.get());
        // 修改指定位置的值
        dataBuffer.put(0, 7);
        // 遍历缓冲区数据
//        System.out.println("遍历缓冲区");
//        for (int i = 0;i < dataBuffer.limit();i++) {
//            System.out.print(dataBuffer.get());
//        }
        // Invariants: mark <= position <= limit <= capacity
        dataBuffer.flip();// {limit = position;position = 0}
        // 清空缓冲区
        dataBuffer.clear();

    }
}
