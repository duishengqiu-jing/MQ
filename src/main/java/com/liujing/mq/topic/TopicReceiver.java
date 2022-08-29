package com.liujing.mq.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Date;

public class TopicReceiver {
    public static void main(String[] args) throws JMSException, InterruptedException {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                "admin",
                "admin",
                "tcp://172.16.157.132:61616");
        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

        Destination destination = session.createTopic("testlj-tpk");
        MessageConsumer consumer = session.createConsumer(destination);
        for (int i = 0; ; i++) {
            Message message = consumer.receive();
            System.out.println("--------------------");
//            Thread.sleep(1000);
            if (message instanceof TextMessage) {
                System.out.println(new Date()+"TextMessage:"+((TextMessage) message).getText());
                message.acknowledge();
            }
//            if ( i % 3 == 0){
//                session.commit();
//            } else {
//                session.rollback();
//            }
//            session.commit();
        }
//        while (true) {
//            Message message = consumer.receive();
//            System.out.println("--------------------");
//            if (message instanceof TextMessage) {
//                System.out.println("destination:"+destination.toString()+"TextMessage:"+((TextMessage) message).getText());
//            }
//        }
//        consumer.close();
//        session.close();
//        connection.close();
    }
}
