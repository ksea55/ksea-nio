package org.java.nio;

import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 学习管理FileChannel的知识点
 */
public class FileChannelDemo {

    @Test //1.4 这是jdk1.4的时候使用nio的通道进行文件copy
    public void test1() throws IOException {

        FileInputStream inputStream = new FileInputStream(new File("E:\\hzw.jpg"));
        FileOutputStream outputStream = new FileOutputStream(new File("hzw1.jpg"));

        FileChannel src = inputStream.getChannel();
        FileChannel target = outputStream.getChannel();

        //将源文件写入到目标文件，实现文件copy
        // src.transferTo(0, src.size(), target);
        target.transferFrom(src, 0, src.size()); //两种方式等效


    }


    @Test //1.7 jdk1.7对与channel有做了进一步优化
    public void test2() throws IOException {

        FileChannel input = FileChannel.open(Paths.get("E:\\hzw.jpg"), StandardOpenOption.READ);

        FileChannel output = FileChannel.open(Paths.get("hzw2.jpg"), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);

        input.transferTo(0, input.size(), output);

    }


    @Test
    public void test3() throws IOException {


        FileChannel input = FileChannel.open(Paths.get("E:\\hzw.jpg"), StandardOpenOption.READ);
        FileChannel output = FileChannel.open(Paths.get("hzw3.jpg"), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);


        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(10 * 1024);


        while (input.read(byteBuffer) != -1) {
            byteBuffer.flip();
            output.write(byteBuffer);
            byteBuffer.clear();
        }


    }

    @Test
    public void test4() throws IOException {
        FileChannel input = FileChannel.open(Paths.get("E:\\hzw.jpg"), StandardOpenOption.READ);
        FileChannel output = FileChannel.open(Paths.get("hzw4.jpg"), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);

        MappedByteBuffer srcBuffer = input.map(FileChannel.MapMode.READ_ONLY, 0, input.size());
        output.write(srcBuffer);


    }


    @Test
    public void test5() throws IOException {
        FileChannel input = FileChannel.open(Paths.get("E:\\hzw.jpg"), StandardOpenOption.READ);
        FileChannel output = FileChannel.open(Paths.get("hzw5.jpg"), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);

        MappedByteBuffer srcBuffer = input.map(FileChannel.MapMode.READ_ONLY, 0, input.size());
        MappedByteBuffer targetBuffer = output.map(FileChannel.MapMode.READ_WRITE, 0, input.size());

        byte[] bytes = new byte[srcBuffer.limit()];

        srcBuffer.get(bytes);
        targetBuffer.put(bytes);

    }
}
