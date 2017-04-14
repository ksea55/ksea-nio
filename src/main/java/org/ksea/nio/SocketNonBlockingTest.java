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

        //切换非阻塞模式
        socketChannel.configureBlocking(false);


        //分配缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(2014);

        byteBuffer.put("你好".getBytes());
        //转换模式
        byteBuffer.flip();

        //将数据写入到服务端
        socketChannel.write(byteBuffer);
        byteBuffer.clear();
        //关闭资源
        socketChannel.close();

    }

    @Test
    public void server() throws IOException {
        //获取服务端网络通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //绑定应用程序端口
        serverSocketChannel.bind(new InetSocketAddress(8888));

        //切换非阻塞模式
        serverSocketChannel.configureBlocking(false);

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
        //轮询式获取选择器上已经"准备就绪"的时间
        while (selector.select() > 0) {
            //获取当前选择器中所有注册的选择键(已就绪的监听事件)
            Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
            while (selectionKeyIterator.hasNext()) {
                //获取准备就绪的事件
                SelectionKey key = selectionKeyIterator.next();
            /*
            * key.isAcceptable() //是否是监听
            * key.isConnectable() //是否是连接
            * key.isReadable() //是否是读
            * key.isWritable() //是否是写
            * */
                //判断是什么事件已经准备就绪
                if (key.isAcceptable()) {
                    //若"接受就绪",获取客户端链接
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    //切换非阻塞模式
                    socketChannel.configureBlocking(false);
                    //将该通道注册到选择器上
                    socketChannel.register(selector, SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    //获取当前选择器上"读就绪"状态的通道
                    SocketChannel clientSockentChannel = (SocketChannel) key.channel();

                    //读取数据 分配缓冲区
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

                    int len = 0;
                    while ((len = clientSockentChannel.read(byteBuffer)) > 0) {
                        //转换模式
                        byteBuffer.flip();
                        //获取数据并打印
                        System.out.println(new String(byteBuffer.array(), 0, len));
                        ;
                        byteBuffer.clear();
                    }
                }

                //取消选择键
                selectionKeyIterator.remove();
            }
        }


    }
}
