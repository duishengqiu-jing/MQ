package com.liujing.mq.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Date;

/**
 * 消息发送
 * @author liuchang
 */
public class TopicSender {
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
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        // 4、找目的地，获取destination，和数据库最大区别会消失。提供了存储的功能，数据库的概念，不能瞎写，redis也有分库的概念，目的地
        Destination destination = session.createTopic("testlj-tpk");
        // 临时节点，针对当前的connection有效，在启动的时候如果后边公用一个conn则可见，不是同一个conn则不可见
        // 有些时候在消息中间件上面临时节点，生命周期就是在connection里面，隔离性也在connection里，同步消息带消息确认的
        // 消费端有没有拿到消息以及ack就是根据临时节点实现
//        session.createTemporaryQueue();
//        session.createTemporaryTopic();
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
            textMessage.setJMSTimestamp(1000);
            producer.send(textMessage);

//            if (i % 3 == 0) {
//                session.commit();
//            } else {
//                session.rollback();
//            }
//            Thread.sleep(300);
        }
//        session.commit();
        // 6、关闭连接
//        producer.close();
//        session.close();
        connection.close();
        System.out.println("system exit....");
    }
}
