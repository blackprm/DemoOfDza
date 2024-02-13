package NIO.demo.fileChannelDemo;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelDemo {

    public static void main(String[] args) {
        String inputFile = "input.txt";
        String outputFile = "output.txt";

        try (
                // 打开一个可读的FileChannel
                RandomAccessFile inputRAFile = new RandomAccessFile(inputFile, "r");
                FileChannel inputChannel = inputRAFile.getChannel();

                // 打开一个可写的FileChannel
                RandomAccessFile outputRAFile = new RandomAccessFile(outputFile, "rw");
                FileChannel outputChannel = outputRAFile.getChannel()

        ) {
            // 分配一个缓冲区
            ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

            // 从输入通道读取数据到缓冲区
            while (inputChannel.read(buffer) > 0) {
                buffer.flip();

                // 将缓冲区内容写入输出通道
                while (buffer.hasRemaining()) {
                    outputChannel.write(buffer);
                }

                // 清空缓冲区以便再次读取
                buffer.clear();
            }

            // 如果需要确保所有数据都被写出（比如在文件关闭之前），可以调用force方法
            outputChannel.force(true);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}