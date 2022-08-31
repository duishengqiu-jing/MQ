package com.liujing.mq06;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Reveiver {
    public static void main(String[] args) throws Exception {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
                "admin",
                "admin",
                "nio://172.16.157.132:5671"
        );
        Connection connection = factory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        Destination queue = session.createQueue("new-queue");
        MessageConsumer consumer = session.createConsumer(queue);
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    System.out.println("s onMessage:consumer 接收到消息:"+message.getJMSReplyTo().toString());
                    message.acknowledge();
                    MessageProducer producer = session.createProducer(message.getJMSReplyTo());
                    TextMessage textMessage = session.createTextMessage("s:返回来啦replyto");
                    textMessage.setJMSReplyTo(message.getJMSReplyTo());
                    producer.send(textMessage);
                    System.out.println("s omNessage replyto 返回");
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        while (true) {

        }
    }

}
