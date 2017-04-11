package org.ksea.nio;

import org.junit.Test;

import java.io.*;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by mexican on 2017/4/11.
 * 一、通道(Channel)用于数据源节点与数据目标节点的连接，在JAVA NIO中 Channel主要负责缓冲区Buffer中的数据
 * 传输，Channel本身是不存储数据的，因此它是需要配合缓冲区Buffer进行传输
 * <p>
 * 二、通道的主要实现类,常用的几个:
 * FileChannel 文件通道
 * SocketChannel 网络客户端通道
 * ServerSocketChannel 网络服务端通道
 * DatagramChannel 网络udp通道
 * <p>
 * 三、获取通道的几种方法
 * 1、Java针对支持通道的类提供了getChannel()方法
 * 本地IO：
 * FileInputStream/FileOutputStream 文件输入/输出流
 * RandomAccessFile
 * 网络IO:
 * Socket ServerSocket TCP网络
 * DatagramSocket UDP网络
 * <p>
 * 2、在JDK1.7中的NIO.2针对各个通道做了优化，对其提供了静态方法open()
 * 3、在JDK1.7中的NIO.2的Files工具类提供了newByteChannel()方法
 * 4、基于通道之间的数据传输
 * transferFrom()
 * transferTo()
 * <p>
 * <p>
 * 五、分散(Scatter)与聚集(Gather)
 * 分散读取(Scattering Reads)是指从Channel中读取的数据分散到多个Buffer中
 * 注意：分散读取是按照缓冲区的顺序，从Channel中读取的数据依次将Buffer缓冲区填满
 * <p>
 * 聚集写入(Gathering Writes)是指将多个Buffer中的数据聚集到Channel中
 * 注意：聚集写入是按照缓冲区Buffer的顺，写入position和limit之间的数据到Channel中
 */
public class ChannelDemo {


    /**
     * 基于分散读取与聚集写入
     */
    @Test
    public void copyFileToScatteringAndGathering() throws Exception {
        RandomAccessFile inputStream = new RandomAccessFile("stock.txt", "rw");
        //获取读取通道
        FileChannel inputChannel = inputStream.getChannel();

        //分配多个缓冲区
        ByteBuffer buffer1 = ByteBuffer.allocate(50);
        ByteBuffer buffer2 = ByteBuffer.allocate(1024);

        //缓冲区数组
        ByteBuffer[] buffers = {buffer1, buffer2};

        //将数据读取到多个缓冲区中
        inputChannel.read(buffers);

        for (ByteBuffer b : buffers) {
            b.flip(); //改变缓冲区数组中单个缓冲区的读取模式
        }
        //打印buffer1缓冲区填充的数据
        System.out.println("buffer1缓冲区1中的数据:" + new String(buffers[0].array(), 0, buffers[0].limit()));
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("buffer2缓冲区1中的数据:" + new String(buffers[1].array(), 0, buffers[1].limit()));
        /*
            此刻打印结果:
            "C:\Program Files\Java\jdk1.8.0_91\bin\java" -ea -Didea.launcher.port=7533 "-Didea.launcher.bin.path=D:\dev.ksea\dev.tools\IntelliJ IDEA 2016.3\bin" -Dfile.encoding=UTF-8 -classpath "D:\dev.ksea\dev.tools\IntelliJ IDEA 2016.3\lib\idea_rt.jar;D:\dev.ksea\dev.tools\IntelliJ IDEA 2016.3\plugins\junit\lib\junit-rt.jar;C:\Program Files\Java\jdk1.8.0_91\jre\lib\charsets.jar;C:\Program Files\Java\jdk1.8.0_91\jre\lib\deploy.jar;C:\Program Files\Java\jdk1.8.0_91\jre\lib\ext\access-bridge-64.jar;C:\Program Files\Java\jdk1.8.0_91\jre\lib\ext\cldrdata.jar;C:\Program Files\Java\jdk1.8.0_91\jre\lib\ext\dnsns.jar;C:\Program Files\Java\jdk1.8.0_91\jre\lib\ext\jaccess.jar;C:\Program Files\Java\jdk1.8.0_91\jre\lib\ext\jfxrt.jar;C:\Program Files\Java\jdk1.8.0_91\jre\lib\ext\localedata.jar;C:\Program Files\Java\jdk1.8.0_91\jre\lib\ext\nashorn.jar;C:\Program Files\Java\jdk1.8.0_91\jre\lib\ext\sunec.jar;C:\Program Files\Java\jdk1.8.0_91\jre\lib\ext\sunjce_provider.jar;C:\Program Files\Java\jdk1.8.0_91\jre\lib\ext\sunmscapi.jar;C:\Program Files\Java\jdk1.8.0_91\jre\lib\ext\sunpkcs11.jar;C:\Program Files\Java\jdk1.8.0_91\jre\lib\ext\zipfs.jar;C:\Program Files\Java\jdk1.8.0_91\jre\lib\javaws.jar;C:\Program Files\Java\jdk1.8.0_91\jre\lib\jce.jar;C:\Program Files\Java\jdk1.8.0_91\jre\lib\jfr.jar;C:\Program Files\Java\jdk1.8.0_91\jre\lib\jfxswt.jar;C:\Program Files\Java\jdk1.8.0_91\jre\lib\jsse.jar;C:\Program Files\Java\jdk1.8.0_91\jre\lib\management-agent.jar;C:\Program Files\Java\jdk1.8.0_91\jre\lib\plugin.jar;C:\Program Files\Java\jdk1.8.0_91\jre\lib\resources.jar;C:\Program Files\Java\jdk1.8.0_91\jre\lib\rt.jar;D:\dev.ksea\dev.file\ksea-nio\target\classes;D:\dev.ksea\dev.file\MavenRepository\repository\junit\junit\4.12\junit-4.12.jar;D:\dev.ksea\dev.file\MavenRepository\repository\org\hamcrest\hamcrest-core\1.3\hamcrest-core-1.3.jar" com.intellij.rt.execution.application.AppMain com.intellij.rt.execution.junit.JUnitStarter -ideVersion5 org.ksea.nio.ChannelDemo,copyFileToScatteringAndGathering
            buffer1缓冲区1中的数据:/*
            Navicat MySQL Data Transfer
            Source Server
            >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
            buffer2缓冲区1中的数据:     : localhost
            Source Server Version : 50717
            Source Host           : localhost:3306
            Source Database       : ksea
            Target Server Type    : MYSQL
            Target Server Version : 50717
            File Encoding         : 65001
            Date: 2017-04-09 17:25:47
                SET FOREIGN_KEY_CHECKS=0;
                -- ----------------------------
                        -- Table structure for stock
                        -- ----------------------------
                DROP TABLE IF EXISTS `stock`;
                CREATE TABLE `stock` (
          `id` int(11) NOT NULL AUTO_INCREMENT,
          `name` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
          `amount` int(255) DEFAULT NULL,
                PRIMARY KEY (`id`)
        ) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

         */


        //聚集写入
        RandomAccessFile outputStream = new RandomAccessFile("gather.txt", "rw");
        FileChannel outputChannel = outputStream.getChannel();
        outputChannel.write(buffers);

        //关闭资源
        inputChannel.close();
        outputChannel.close();
    }


    /**
     * 基于通道之间的数据传输
     * 这种方式也是基于直接缓冲区
     */
    @Test
    public void copyFileToTransfer() throws Exception {
        //读取模式通道
        FileChannel inputChannel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
        //写入通道
        FileChannel outputChannel = FileChannel.open(Paths.get("4.jpg"), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);

        //写入到目标通道 这两种方式其结果是一样的
        //  inputChannel.transferTo(0,inputChannel.size(),outputChannel);
        outputChannel.transferFrom(inputChannel, 0, inputChannel.size());


        //关闭通道
        inputChannel.close();
        outputChannel.close();

    }


    /**
     * 直接缓冲区方式通过open()的方式获取通道
     * 总结：直FileChannel的map()方法将文件区域直接映射到内存中来创建，该方法返回MappedByteBuffer，Java平台的实现有助于
     * 通过JNI从本机代码创建直接字节缓冲区，如果以上这些缓冲区中德某个缓冲区实例指的是不可访问的内存区域，则试图访问该区域
     * 不会更改缓冲区的内容，并且将会在访问期间或稍后的某个时间导致抛出不确定的异常
     * <p>
     * 直接缓冲区的内容可以驻留在常规的垃圾回收堆之外，因此它们对应用程序的内存需求量造成的影响可能并不明显，所以，建议将直接缓冲区
     * 主要分配给那些易受基础系统的本机I/O操作影响的大型、持久的缓冲区。一般情况下，最好仅在直接缓冲区能在程序性能方面带来明显好处时分配它们.
     */
    @Test
    public void copyFileToDriect() throws Exception {
        //获取文件读取通道,该通道是一个只读通道
        FileChannel inputChannel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
        /*
            该通道是一个读写通道
            StandardOpenOption.CREATE表示如果目标文件存在于不存在都创建
            StandardOpenOption.CREATE_NEW表示目标文件如果不存在则创建，如果目标文件已存在则会抛出异常
            */
        FileChannel outputChannel = FileChannel.open(Paths.get("3.jpg"), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);

        //内存映射文件
        //内存映射源该数据源是只读模式，其读取从0到容量大小
        MappedByteBuffer inputMappedBuffer = inputChannel.map(FileChannel.MapMode.READ_ONLY, 0, inputChannel.size());
        MappedByteBuffer outputMappedBuffer = outputChannel.map(FileChannel.MapMode.READ_WRITE, 0, inputChannel.size());

        //直接对缓冲区进行数据的读写操作
        byte[] bytes = new byte[inputMappedBuffer.limit()];
        inputMappedBuffer.get(bytes);//将数据读取到bytes中
        outputMappedBuffer.put(bytes);//将文件写入

        //关闭资源
        inputChannel.close();
        outputChannel.close();
    }


    /**
     * 该案列讲解基于IO流与NIO结合进行文件的copy
     * 它是基于getChannel()方法获取通道，这种方式是非直接缓冲区的方式
     */
    @Test
    public void copyFile() {
        //输入流
        FileInputStream inputStream = null;
        //输出流
        FileOutputStream outputStream = null;
        //缓冲区
        ByteBuffer buffer = null;
        //输入流通道
        FileChannel inputChannel = null;
        //输出流通道
        FileChannel outputChannel = null;
        try {
            inputStream = new FileInputStream("1.jpg");
            outputStream = new FileOutputStream("target.jpg");

            //分配2014字节缓冲区
            buffer = ByteBuffer.allocate(1024);
            inputChannel = inputStream.getChannel();
            outputChannel = outputStream.getChannel();

            //将数据读取到buffer缓冲区中
            while (inputChannel.read(buffer) != -1) {
                //转换读取模式
                buffer.flip();
                outputChannel.write(buffer);//写入目标数据
                buffer.clear();//清空缓冲区
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            try {
                if (null != inputChannel)
                    inputChannel.close();
                if (null != outputChannel)
                    outputChannel.close();
                if (null != inputStream)
                    inputStream.close();
                if (null != outputStream)
                    outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
