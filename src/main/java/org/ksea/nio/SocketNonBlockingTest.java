package org.ksea.nio;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Created by ksea on 2017/4/14.
 * 非阻塞式网络、
 * Channel，Buffer，Selector
 */
public class SocketNonBlockingTest {

    @Test
    public void client() throws IOException {
        //获取客户端网络通道
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8888));

        //分配缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(2014);

        byteBuffer.put("你好".getBytes());
        //转换模式
        byteBuffer.flip();

        //将数据写入到服务端
        socketChannel.write(byteBuffer);

        //关闭资源
        socketChannel.close();

    }

    @Test
    public void server() throws IOException {
        //获取服务端网络通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //绑定应用程序端口
        serverSocketChannel.bind(new InetSocketAddress(8888));

        //转换非阻塞模式
        serverSocketChannel.configureBlocking(false);

        //分配缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        //获取选择器
        Selector selector = Selector.open();

        /**
         * 将通道注册到选择器上
         *
         * SelectionKey.OP_ACCEPT 监听
         * SelectionKey.OP_CONNECT 连接
         * SelectionKey.OP_READ 读
         * SelectionKey.OP_WRITE 写
         * 当有多个选项用 |进行连接
         * */
        SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //获取所有的在选择器selector中选中的key
        Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
        while (selectionKeyIterator.hasNext()) {
            SelectionKey key = selectionKeyIterator.next();
            /*
            * key.isAcceptable() //是否是监听
            * key.isConnectable() //是否是连接
            * key.isReadable() //是否是读
            * key.isWritable() //是否是写
            * */
            if (key.isAcceptable()) {
                SocketChannel socketChannel = serverSocketChannel.accept();
            }
        }

    }
}
