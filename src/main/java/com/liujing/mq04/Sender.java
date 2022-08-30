package com.liujing.mq04;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Sender {
    public static void main(String[] args) throws Exception {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
                "admin",
                "admin",
                "nio://172.16.157.132:5671"
        );
        ActiveMQConnection connection = (ActiveMQConnection) factory.createConnection();
        connection.start();
        QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("new-queue");
        QueueRequestor queueRequestor = new QueueRequestor(session, queue);
        System.out.println("before");
        Message message = queueRequestor.request(session.createTextMessage("sendertextmessa2222"));
        System.out.println(message.toString());
        System.out.println("after");
//        for (int i = 0; i < 10; i++) {
//            TextMessage textMessage = session.createTextMessage("hello");
//            textMessage.setJMSCorrelationID(""+i);
//            System.out.println("producer:"+textMessage.getText());
//        }
    }
}
