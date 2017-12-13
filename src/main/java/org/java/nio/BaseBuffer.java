package org.java.nio;

import org.junit.Before;
import org.junit.Test;

import javax.sound.midi.Soundbank;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

/**
 * 此类用于记录 在Buffer中的基本属性和方法的探讨和记录
 */
public class BaseBuffer {

    private ByteBuffer buffer;

    @Before
    public void init() {
        //初始化容器
        buffer = ByteBuffer.allocate(10);
    }

    @Test //初识 容器的四大属性
    public void test() {
        System.out.println("初始化容器:" + buffer.position() + "," + buffer.limit() + "," + buffer.capacity());

        //向容器中填充数据
        buffer.put("hello11111".getBytes());
        System.out.println("向容器中填充数据之后:" + buffer.position() + "," + buffer.limit() + "," + buffer.capacity());


        boolean hasRemaining = buffer.hasRemaining(); //当前是否还有剩余元素
        int remaining = buffer.remaining(); //剩余元素的个数
        System.out.println(hasRemaining + "," + remaining);

        //转换模式
        buffer.flip();
        System.out.println("调用flip之后:" + buffer.position() + "," + buffer.limit() + "," + buffer.capacity());


    }


    private String[] strings = {
            "A random string value",
            "The product of an infinite number of monkeys",
            "Hey hey we're the Monkees",
            "Opening act for the Monkees: Jimi Hendrix",
            "'Scuse me while I kiss this fly",  // Sorry Jimi ;-)
            "Help Me!  Help Me!",
    };


    private int index = 0;


    //声明字符容器
    private CharBuffer charBuffer;


    //向容器中填充数据
    public boolean fillBuffer() {

        if (index >= strings.length) {

            return (false);
        }

        //获取字符串数组中的元素
        String currStr = strings[index++];

        for (int i = 0; i < currStr.length(); i++) {
            //获取当前字符
            char c = currStr.charAt(i);
            //填充数据
            charBuffer.put(c);
            //charBuffer.put(currStr.toCharArray());
            //charBuffer.put(currStr);
        }

        return (true);
    }


    //释放容器中的数据
    public void liberateBuffer(CharBuffer charBuffer) {
        //判定容器中是否还有剩余元素
        while (charBuffer.hasRemaining()) {

            //获取当前的元素
            char c = charBuffer.get();

            System.out.print(c);
        }
        System.out.println("");

    }


    @Test //基于nio的填充与释放写一个demo
    public void demoTest() {

        //给容器分配大小
        charBuffer = CharBuffer.allocate(100);

        //先填充数据
        while (fillBuffer()) {
            //转换容器模式
            charBuffer.flip();
            //释放容器元素
            liberateBuffer(charBuffer);

            //执行清空
            charBuffer.clear();

        }


    }


}
