package com.liujing.mq06;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQMessageProducer;
import org.apache.activemq.AsyncCallback;

import javax.jms.*;
import java.util.concurrent.CountDownLatch;

public class Sender {
    public static void main(String[] args) throws Exception {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
                "admin",
                "admin",
                "nio://172.16.157.132:5671"
        );
        ActiveMQConnection connection = (ActiveMQConnection) factory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        Destination queue = session.createQueue("new-queue");

        MessageProducer producer = session.createProducer(queue);
        TextMessage textMessage = session.createTextMessage("testddddmessage");
        textMessage.setJMSReplyTo(session.createTemporaryQueue());
        producer.send(textMessage);
        System.out.println("p:producer: 消息发送完成");
        MessageConsumer consumer = session.createConsumer(textMessage.getJMSReplyTo());
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    System.out.println("p onMessage:consumer:"+message.getJMSReplyTo().toString());
                    TemporaryQueue temporaryQueue = session.createTemporaryQueue();
                    MessageProducer producer1 = session.createProducer(temporaryQueue);
                    TextMessage textMessage1 = session.createTextMessage("再次replyto");
                    textMessage1.setJMSReplyTo(message.getJMSReplyTo());
                    producer1.send(textMessage1);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        while (true) {

        }
    }
}
