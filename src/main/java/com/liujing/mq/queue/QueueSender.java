package com.liujing.mq.queue;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ScheduledMessage;

import javax.jms.*;
import java.util.Date;

/**
 * 消息发送
 * @author liuchang
 */
public class QueueSender {
    public static void main(String[] args) throws Exception{
        // 1、获取连接工厂
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                "admin",
                "admin",
                "tcp://172.16.157.132:61616");
        // 2、获取一个ActiveMQ的连接
//        connectionFactory.setUseAsyncSend(true);
        Connection connection = connectionFactory.createConnection();
        ActiveMQConnection activeMQConnection = (ActiveMQConnection) connection;
        activeMQConnection.setUseAsyncSend(true);
        connection.start();

        // 3、获取session
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        // 4、找目的地，获取destination，和数据库最大区别会消失。提供了存储的功能，数据库的概念，不能瞎写，redis也有分库的概念，目的地
        Destination destination = session.createQueue("testlj?producer.windowSize=1");
        // 5、从session获取生产者
        MessageProducer producer = session.createProducer(destination);
//        producer.setDeliveryMode(DeliveryMode.PERSISTENT);

        // 6、向目的地写入消息，写完之后让consumer消费
//        TextMessage message = session.createTextMessage("test activemq");
//        //延迟发送时间
//        long delay = 5 * 1000;
//        //重复投递次数
//        int repeat = 9;
//        //重复投递间隔
//        long period = 2 * 1000;
//        message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, delay);
//        message.setIntProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT, repeat);
//        message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_PERIOD, period);
//        producer.send(message);
//        while (true) {
//            System.out.println(message);
//        }
//        selector 不能基于body的信息，只能基于header的信息
        for (int i = 0; i < 30; i++) {
            MapMessage mapMessage = session.createMapMessage();
            mapMessage.setIntProperty("age", i % 10);
            mapMessage.setStringProperty("name", "name"+ i%5);
            mapMessage.setDoubleProperty("price", i*9);
            producer.send(mapMessage);
            System.out.println(mapMessage.toString());
//            mapMessage.acknowledge();
        }

//        for (int i = 0; i < 100; i++) {
//            TextMessage textMessage = session.createTextMessage("test activemq:i="+i);
//            System.out.println(new Date()+"textMessage:"+textMessage.getText());
//            if (i % 4 == 0) {
//                producer.send(textMessage);
//            }

            //            if (i % 4 == 0) {
//                // 对producer整体设置优先级，改了成员变量了
//                producer.setPriority(9);
////                textMessage.setJMSPriority(9);
//            } else {
//                producer.setPriority(4);
////                textMessage.setJMSPriority(4);
//            }
//            textMessage.setLongProperty("week", i % 3);
//            producer.send(textMessage);
            //objectMessage
            //序列化对象
//            Person person = new Person();
//            person.setName("pname1");
//            person.setAge(i);
//            person.setIdCard("12233435346465"+i);
//            ObjectMessage objectMessage = session.createObjectMessage(person);
//            producer.send(objectMessage);

            //小文件用byteMessage传
            //字节流、图、文件小的，从网络也好从本地也好，
            // 读一些文件组装成字节数组，拿这个写到mq，
            // 接的时候也是字节数组可以写到文件中
            //发送文件有可能对方没有手机，此时放在内存中，当一直堆积可能会炸了
            //首先做限制，死信队列也在内存不及时处理也会出事
            //死信队列需要配合另外一个参数才能使用
//            BytesMessage bytesMessage = session.createBytesMessage();
//            bytesMessage.writeUTF("hihellosfdwsfwsf");
//            bytesMessage.writeBoolean(false);
//            bytesMessage.setFloatProperty("float1", 334.6456f);
//            bytesMessage.setDoubleProperty("double1", 4546.755d);
//            bytesMessage.setIntProperty("int1", 245);
//            bytesMessage.setLongProperty("long1", 2325l);
//            bytesMessage.setShortProperty("short1", (short)5);
//            producer.send(bytesMessage);

            //map key value 方式发送接收数据
            //1、写一个对象扔进去，那边拿到之后就直接是个map
            //2、直接用给的数据类型MapMessage
//            MapMessage mapMessage = session.createMapMessage();
//            mapMessage.setBoolean("boolean", false);
//            mapMessage.setInt("int", 353535);
//            mapMessage.setFloat("float", 2234234f);
//            mapMessage.setLong("long", 23454l);
//            mapMessage.setString("string","string-test");
//            producer.send(mapMessage);

            //所有setXXProperty的都是设置对于当前这条消息的元数据信息属性，不是消息体
//            if (i % 3 == 0) {
//                session.commit();
//            } else {
//                session.rollback();
//            }
//            Thread.sleep(300);
//        }
//        session.commit();
        // 6、关闭连接
//        producer.close();
//        session.close();
//        connection.close();
        System.out.println("system exit....");
    }
}
