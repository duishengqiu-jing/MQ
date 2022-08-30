package com.liujing.mq.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class QueueReceiver {
    public static void main(String[] args) throws JMSException, InterruptedException {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                "admin",
                "admin",
                "tcp://172.16.157.132:61616");
        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

        Destination destination = session.createQueue("testlj");
        MessageConsumer consumer = session.createConsumer(destination);
        // 方式一，匿名类
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                // 通过onmessage方法收到broker给推送过来的消息，当有消息的时候就回调该方法
                System.out.println(message.toString());
                if (message instanceof TextMessage){
                    try {
                        message.acknowledge();
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Thread.sleep(1000);
        // 方式二：自己创建一个实现了了一个MessageListener接口的类
//        consumer.setMessageListener(new Mylistener());
//        session.commit();
//        for (int i = 0;; i++) {
//            Message message = consumer.receive();
////            Thread.sleep(1000);
//            System.out.println("--------------------");
//            if (message instanceof TextMessage) {
//                System.out.println(new Date()+"destination1:"+destination.toString()+"TextMessage:"+((TextMessage) message).getText());
//            }
//            if ( i % 3 == 0){
//                session.commit();
//            } else {
//                session.rollback();
//            }
//        }
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
