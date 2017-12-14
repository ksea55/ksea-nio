package org.java.nio;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;

/**
 * 通道拷贝
 */
public class ChannelCopy {

    @Test
    public void copy1() throws IOException {
        ReadableByteChannel readableByteChannel = Channels.newChannel(new FileInputStream("E:\\io.txt"));
        WritableByteChannel writableByteChannel = Channels.newChannel(System.out);

        ByteBuffer byteBuffer = ByteBuffer.allocate(16 * 1024);
        while ((readableByteChannel.read(byteBuffer)) != -1) {
            byteBuffer.flip();
            writableByteChannel.write(byteBuffer);
            byteBuffer.clear();
        }


    }

    @Test
    public void copy2() throws IOException {

        ReadableByteChannel readableByteChannel = Channels.newChannel(new FileInputStream("E:\\io.txt"));
        WritableByteChannel writableByteChannel = Channels.newChannel(System.out);

        ByteBuffer byteBuffer = ByteBuffer.allocate(16 * 1024);
        while (readableByteChannel.read(byteBuffer) != -1) {
            byteBuffer.flip();

            while (byteBuffer.hasRemaining()) {
                writableByteChannel.write(byteBuffer);
            }
            byteBuffer.clear();

        }

    }

    @Test
    public void copy3() throws IOException {


        ReadableByteChannel readableByteChannel = Channels.newChannel(new FileInputStream("E:\\hzw.jpg"));
        WritableByteChannel writableByteChannel = Channels.newChannel(new FileOutputStream("hzw_copy.jpg"));

        ByteBuffer byteBuffer = ByteBuffer.allocate(16 * 1024);

        while (readableByteChannel.read(byteBuffer)!=-1){
            byteBuffer.flip();
            writableByteChannel.write(byteBuffer);
            byteBuffer.clear();
        }


    }


}
