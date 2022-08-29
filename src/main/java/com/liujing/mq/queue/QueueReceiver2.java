package com.liujing.mq.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Date;

public class QueueReceiver2 {
    public static void main(String[] args) throws JMSException, InterruptedException {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                "admin",
                "admin",
                "tcp://172.16.157.132:61616");
        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);

        Destination destination = session.createQueue("testlj");
        MessageConsumer consumer = session.createConsumer(destination);
        for (int i = 0;; i++) {
            Message message = consumer.receive();
            Thread.sleep(1000);
            System.out.println("--------------------");
            if (message instanceof TextMessage) {
                System.out.println(new Date()+"destination2:"+destination.toString()+"TextMessage:"+((TextMessage) message).getText());
            }
            if ( i % 3 == 0){
                session.commit();
            } else {
                session.rollback();
            }
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
