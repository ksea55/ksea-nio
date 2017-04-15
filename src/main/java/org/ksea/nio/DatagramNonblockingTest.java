package org.ksea.nio;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Created by mexican on 2017/4/14.
 * 基于UDP的非阻塞模式
 */
public class DatagramNonblockingTest {

    @Test
    public void send() throws IOException {
        //获取客户端UDP网络通道
        DatagramChannel datagramChannel = DatagramChannel.open();
        //转换非阻塞模式
        datagramChannel.configureBlocking(false);
        //分配缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        byteBuffer.put("你好UDP".getBytes());
        byteBuffer.flip();
        datagramChannel.send(byteBuffer, new InetSocketAddress("127.0.0.1", 8888));
        byteBuffer.clear();


        datagramChannel.close();

    }

    @Test
    public void receive() throws IOException {

        //服务端接收网络通道
        DatagramChannel datagramChannel = DatagramChannel.open();
        //绑定应
        datagramChannel.bind(new InetSocketAddress(8888));
        //转换非阻塞模式
        datagramChannel.configureBlocking(false);

        //获取选择器
        Selector selector = Selector.open();
        //注册读的选择器
        datagramChannel.register(selector, SelectionKey.OP_READ);

        while (selector.select() > 0) {

            //获取当前注册的所有选择器key
            Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
            while (selectionKeyIterator.hasNext()) {

                SelectionKey selectionKey = selectionKeyIterator.next();
                if (selectionKey.isReadable()) {
                    //分配缓冲区
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    //读取数据到缓冲区
                    datagramChannel.receive(byteBuffer);
                    //转换模式
                    byteBuffer.flip();
                    //打印结果
                    System.out.println(new String(byteBuffer.array(), 0, byteBuffer.limit()));

                }


            }


        }


    }

}
