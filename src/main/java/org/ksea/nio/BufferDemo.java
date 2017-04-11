package org.ksea.nio;

import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * Created by mexican on 2017/4/10.
 * Buffer缓冲区：在java NIO中负责数据的存取，缓冲区就是数组，用于存储不同数据类型的数据
 * 一种用于特定的基本类型数据的容器。
 * <p>
 * 缓冲区是特定的基本类型元素的线性、有限序列。缓冲区的基本属性除其内容外还包括容量、限制和位置：
 * <p>
 * 缓冲区的容量 是它所包含的元素的数量。缓冲区的容量永远不会为负并且从不会更改。
 * <p>
 * 缓冲区的限制 是不应读取或写入的第一个元素的索引。缓冲区的限制永远不会为负，并且永远不会大于其容量。
 * <p>
 * 缓冲区的位置 是下一个要读取或写入的元素的索引。缓冲区的位置永远不会为负，并且永远不会大于其限制。
 * <p>
 * ByteBuffer,
 * CharBuffer,
 * ShortBuffer,
 * IntBuffer,
 * FloatBuffer,
 * DoubleBuffer,
 * LongBuffer
 * 注意这里的缓冲区是没boolean的缓冲区的
 * <p>
 * 清除、反转和重绕
 * 除了访问位置、限制、容量值以及做标记和重置等方法外，此类还对缓冲区定义了以下操作：
 * <p>
 * clear() 使缓冲区做好了新序列信道读取或相对 put 操作的准备：它将限制设置为容量大小，将位置设置为零。
 * <p>
 * flip() 使缓冲区做好了新序列信道读取或相对 get 操作的准备：它将限制设置为当前位置，然后将该位置设置为零。
 * <p>
 * rewind() 使缓冲区做好了重新读取已包含的数据的准备：它使限制保持不变，并将位置设置为零。
 * <p>
 * 只读缓冲区
 * 每个缓冲区都是可读取的，但并非每个缓冲区都是可写入的。每个缓冲区类的 mutation 方法都被指定为可选操作，当调用只读缓冲区时，将抛出 ReadOnlyBufferException。只读缓冲区不允许更改其内容，但其标记、位置和限制值可以改变。缓冲区是否为只读通过调用其 isReadOnly 方法决定。
 * <p>
 * 线程安全
 * 缓冲区由当前的多个线程使用是不安全的。如果一个缓冲区由不止一个线程使用，则应该通过适当的同步来控制对该缓冲区的访问。
 * <p>
 * 调用连接
 * 指定此类中的方法（它们不返回其他值）以返回这些方法被调用时所在的缓冲区。此操作允许方法调用被连接；例如，语句序列
 * <p>
 * b.flip();
 * b.position(23);
 * b.limit(42);
 */
public class BufferDemo {

    @Test
    public void test1() {
        /*
        * 在buffer中有四个特定的属性
        *      // Invariants: mark <= position <= limit <= capacity
                private int mark = -1; 标记,表示记录当前position的位置，可以通过reset()恢复到mark的位置
                                                                     reset() 将此缓冲区的位置重新设置成以前标记的位置。
                private int position = 0;//位置，表示缓冲区中正在操作数据的位置,缓冲区的位置 是下一个要读取或写入的元素的索引。缓冲区的位置永远不会为负，并且永远不会大于其限制。
                private int limit; //表示界限，表示缓冲区中可以操作数据的大小(limit后的数据不能进行读写操作),缓冲区的限制 是不应读取或写入的第一个元素的索引。缓冲区的限制永远不会为负，并且永远不会大于其容量。
                private int capacity; //缓冲区的容量，表示缓冲区中最大存储的数据容量，一旦声明将不能改变, 它所包含的元素的数量,缓冲区的容量永远不会为负并且从不会更改。
        * */

        // 分配一个指定容量大小的缓冲区
        System.out.println("---------------------调用allocate()-------------------------");
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        System.out.println(buffer.position()); //运行结果:0 表示现在操作的位置
        System.out.println(buffer.limit()); //运行结果:1024 表示现在可以操作的数据
        System.out.println(buffer.capacity()); //运行结果:1024 表示数据的容量

        System.out.println("-------------------调用put()---------------------------");
        //开始写入数据
        buffer.put("abcde".getBytes());
        System.out.println(buffer.position()); //运行结果:5 此刻写入数据，我们插如5个字节的数据，0,1,2,3,4 接下来操作的就是下标5的空间地址
        System.out.println(buffer.limit()); //运行结果:1024 此刻界限不变
        System.out.println(buffer.capacity()); //运行结果:1024 数据的容量不改变


        System.out.println("------------------调用flip()--------------------------------");
        //反转此缓冲区。
        buffer.flip();//该模式会将写模式改变读取模式
        System.out.println(buffer.position()); //运行结果:0 表示以下标为0的当前位置开始读取
        System.out.println(buffer.limit()); //运行结果:5 表示目前可操作的数据下标为5
        System.out.println(buffer.capacity()); //运行结果:1024 数据的容量不改变

        //以上模式的数据结果请查看  图解NIO中position，limit，capacity的关系.jpg


        //读取数据
        byte[] resultByte = new byte[buffer.limit()];
        buffer.get(resultByte);//将数去读取到resultByte中
        System.out.println(new String(resultByte, 0, resultByte.length)); //打印结果
        System.out.println("------------------调用get()--------------------------------");
        System.out.println(buffer.position()); //运行结果:5 当读取之后数据的下标会开始移动 现在我们读取了五个所以是5
        System.out.println(buffer.limit()); //运行结果:5
        System.out.println(buffer.capacity()); //运行结果:1024 数据的容量不改变

        //重绕此缓冲区
        buffer.rewind(); //运行rewind()方法之后，数据有可以开始重头开始读取，也就是重复读取数据
        System.out.println("------------------调用rewind()--------------------------------");
        System.out.println(buffer.position()); //运行结果:0
        System.out.println(buffer.limit()); //运行结果:5
        System.out.println(buffer.capacity()); //运行结果:1024 数据的容量不改变


        buffer.clear(); //清空缓冲区，但是缓冲区的数据依旧还存在，此刻的数据处于被遗忘状态，此刻就不知道数据到底有多少个
        System.out.println("------------------调用clear()--------------------------------");
        System.out.println(buffer.position()); //运行结果:0
        System.out.println(buffer.limit()); //运行结果:1024
        System.out.println(buffer.capacity()); //运行结果:1024
    }

    @Test
    public void test2() {
        // Invariants: mark <= position <= limit <= capacity
      /* private int mark = -1;
        标记, 表示记录当前position的位置，可以通过reset() 恢复到mark的位置
        reset() 将此缓冲区的位置重新设置成以前标记的位置。*/

        //分配容量
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //写入数据
        buffer.put("abcde".getBytes());//此刻写入5个字节的数量
        //转换读取模式
        buffer.flip();//此刻转换到读取模式
        //读取数据
        byte[] resultByte = new byte[buffer.limit()];

        buffer.get(resultByte, 0, 2);//将0,2之间的数据读取到resultByte中
        System.out.println(new String(resultByte, 0, 2)); //打印结果：ab
        System.out.println(buffer.position()); //查看此刻的position=2

        //对当前位置进行标记
        buffer.mark();//因为此刻position=2调用buffer.mark()方法之后此刻的mark=2

        //继续读取
        buffer.get(resultByte, 2, 2);//将2-4的数据读取到resultByte中
        System.out.println(new String(resultByte, 2, 2));//打印结果:cd
        System.out.println(buffer.position()); //此刻的position=4

        //调用重置方法 reset()将此缓冲区的位置重新设置成以前标记的mark位置。
        buffer.reset();
        System.out.println(buffer.position()); //此刻position=2

        /*hasRemaining()
          判断在当前位置和限制之间是否有任何元素。*/
        if (buffer.hasRemaining()) {
            //  remaining()返回当前位置与限制之间的元素数量。
            int num = buffer.remaining();
            System.out.println(num); //打印结果是:3 因为当前的position的值是2，因此还有3个
        }


    }
}
