package org.ksea.nio;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by mexican on 2017/4/12.
 */
public class SocketBlockingTest1 {
    /**
     * 客户端向服务端传输数据
     */
    @Test
    public void client() throws IOException {
        //获取客户端网络通道
        SocketChannel clientSocketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8888));
        //文件读取通道
        FileChannel inputChannel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);

        //分配缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //读取文件到缓冲区
        while (inputChannel.read(byteBuffer) != -1) {
            //改变读取模式
            byteBuffer.flip();
            //将数据写入到服务端
            clientSocketChannel.write(byteBuffer);
            byteBuffer.clear();
        }

        //关闭资源
        inputChannel.close();
        clientSocketChannel.close();


    }

    /**
     * 服务端接受来自Client端的数据
     */
    @Test
    public void server() throws IOException {
        //创建服务端通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //绑定端口
        serverSocketChannel.bind(new InetSocketAddress(8888));
        //创建输出文件通道
        FileChannel outputChannel = FileChannel.open(Paths.get("blocking_1.jpg"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);

        //监听客户端通道
        SocketChannel clientSocketChannel = serverSocketChannel.accept();

        //分配缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        //读取客户端的数据到缓冲区
        while (clientSocketChannel.read(byteBuffer) != -1) {
            //转换读取模式
            byteBuffer.flip();
            //服务端保存客户端的文件
            outputChannel.write(byteBuffer);
            byteBuffer.clear();
        }

        //关闭资源
        outputChannel.close();
        serverSocketChannel.close();

    }

}
