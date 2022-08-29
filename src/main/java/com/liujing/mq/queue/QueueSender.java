package com.liujing.mq.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

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
        Connection connection = connectionFactory.createConnection();
        connection.start();

        // 3、获取session
        Session session = connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
        // 4、找目的地，获取destination，和数据库最大区别会消失。提供了存储的功能，数据库的概念，不能瞎写，redis也有分库的概念，目的地
        Destination destination = session.createQueue("testlj");
        // 5、从session获取生产者
        MessageProducer producer = session.createProducer(destination);
//        producer.setDeliveryMode(DeliveryMode.PERSISTENT);
        // 6、向目的地写入消息，写完之后让consumer消费
        for (int i = 0; i < 30; i++) {
            TextMessage textMessage = session.createTextMessage("test activemq:i="+i);
            System.out.println(new Date()+"textMessage:"+textMessage.getText());
//            if (i % 4 == 0) {
//                producer.send(textMessage);
//            }
            if (i % 4 == 0) {
                // 对producer整体设置优先级，改了成员变量了
                producer.setPriority(9);
//                textMessage.setJMSPriority(9);
            } else {
                producer.setPriority(4);
//                textMessage.setJMSPriority(4);
            }
            producer.send(textMessage);

//            if (i % 3 == 0) {
//                session.commit();
//            } else {
//                session.rollback();
//            }
//            Thread.sleep(300);
        }
        session.commit();
        // 6、关闭连接
//        producer.close();
//        session.close();
        connection.close();
        System.out.println("system exit....");
    }
}
