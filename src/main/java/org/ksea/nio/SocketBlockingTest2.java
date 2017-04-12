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
public class SocketBlockingTest2 {
    @Test
    public void client() throws IOException {
        //获取客户端网络传输通道
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9999));
        //获取文件通道
        FileChannel inputChannel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);

        //分配缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //读取源文件到缓冲区
        while (inputChannel.read(buffer) != -1) {
            //改变buffer的读取模式
            buffer.flip();
            //将src信息写入到服务端
            socketChannel.write(buffer);
            buffer.clear();
        }

        //通知服务端客户端数据已经传输完毕，如果不写此句，服务端将处于阻塞Blocking
        socketChannel.shutdownOutput();


        //读取服务端反馈的信息
        while (socketChannel.read(buffer) != -1) {
            buffer.flip();
            System.out.println(new String(buffer.array(), 0, buffer.limit()));
            buffer.clear();
        }


        //关闭资源
        inputChannel.close();
        socketChannel.close();
    }

    @Test
    public void server() throws IOException {
        //创建服务端网通通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //绑定服务端应用程序端口
        serverSocketChannel.bind(new InetSocketAddress(9999));
        //监听客户端的网络通道
        SocketChannel socketChannel = serverSocketChannel.accept();
        //创建文件传输通道
        FileChannel outputChannel = FileChannel.open(Paths.get("blocking_2.jpg"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);

        //分配缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        //读取客户端的数据信息
        while (socketChannel.read(buffer) != -1) {
            //转换缓冲区模式
            buffer.flip();
            //将文件写入到服务端
            outputChannel.write(buffer);
            buffer.clear();
        }

        //数据写入完毕之后给客户端反馈信息
        buffer.put("您的数据已经成功被接受！".getBytes());
        buffer.flip();
        socketChannel.write(buffer);

        //关闭资源
        outputChannel.close();
        socketChannel.close();
        serverSocketChannel.close();

    }
}
