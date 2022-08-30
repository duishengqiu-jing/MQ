package com.liujing.mq;

import com.liujing.mq.queue.Person;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.ArrayList;

public class Sender {
    public static void main(String[] args) throws Exception {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
                "admin",
                "admin",
                "tcp://172.16.157.132:61616"
        );
        //相同系统的里边能够以对象传递能够更加方便，只要对面有实体类，传输效率很高，序列化中转成字节码
        //去发送小的配置文件，通过mq发送，可以用bytes string，小型文件的往mq里面去写数据和消费数据
        //字符串的传输效率比较低，效率比较低，序列化反序列化效率过程经过比较复杂计算
        //效率更高的json/xml小的文件图片写到mq里如果用object就开销没有意义，本质就是二进制，就直接用byte
        ArrayList<String> trustPackages = new ArrayList<>();
        trustPackages.add(Person.class.getPackage().getName());
        factory.setTrustedPackages(trustPackages);
        Connection connection = factory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination queue = session.createQueue("new-queue?consumer.exclusive=true");
        MessageProducer producer = session.createProducer(queue);
        //默认情况下不持久化的数据不会进入数据库，默认情况非持久化消息一旦过期之后也不会进去死信队列
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);
        //当ttl到期，进入死信队列，保证一些有时效性的消息
        producer.setTimeToLive(3000);
        for (int i = 0; i < 10; i++) {
            TextMessage textMessage = session.createTextMessage("测试新队列的text消息:"+i);
            producer.send(textMessage);
            //objectMessage
            //序列化对象
            Person person = new Person();
            person.setName("pname1");
            person.setAge(i);
            person.setIdCard("12233435346465"+i);
            ObjectMessage objectMessage = session.createObjectMessage(person);
            producer.send(objectMessage);

            //小文件用byteMessage传
            //字节流、图、文件小的，从网络也好从本地也好，
            // 读一些文件组装成字节数组，拿这个写到mq，
            // 接的时候也是字节数组可以写到文件中
            //发送文件有可能对方没有手机，此时放在内存中，当一直堆积可能会炸了
            //首先做限制，死信队列也在内存不及时处理也会出事
            //死信队列需要配合另外一个参数才能使用
            BytesMessage bytesMessage = session.createBytesMessage();
            bytesMessage.writeUTF("hihellosfdwsfwsf");
//            bytesMessage.writeBoolean(false);
//            bytesMessage.setFloatProperty("float1", 334.6456f);
//            bytesMessage.setDoubleProperty("double1", 4546.755d);
//            bytesMessage.setIntProperty("int1", 245);
//            bytesMessage.setLongProperty("long1", 2325l);
//            bytesMessage.setShortProperty("short1", (short)5);
            producer.send(bytesMessage);

            //map key value 方式发送接收数据
            //1、写一个对象扔进去，那边拿到之后就直接是个map
            //2、直接用给的数据类型MapMessage
            MapMessage mapMessage = session.createMapMessage();
            mapMessage.setBoolean("boolean", false);
            mapMessage.setInt("int", 353535);
            mapMessage.setFloat("float", 2234234f);
            mapMessage.setLong("long", 23454l);
            mapMessage.setString("string","string-test");
            producer.send(mapMessage);

            //所有setXXProperty的都是设置对于当前这条消息的元数据信息属性，不是消息体

        }

//        producer.close();
//        session.close();
//        connection.close();

    }
}
