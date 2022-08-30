package com.liujing.mq05.mq04;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQMessageProducer;
import org.apache.activemq.AsyncCallback;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;
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
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination queue = session.createQueue("new-queue");
        ActiveMQMessageProducer producer = (ActiveMQMessageProducer) session.createProducer(queue);
        //countDownLatch可以异步
        CountDownLatch countDownLatch = new CountDownLatch(100);
        for (int i = 0; i < 100; i++) {
            producer.send(session.createTextMessage("ssss"), new AsyncCallback() {
                @Override
                public void onSuccess() {
                    //成功的时候回调消息
                }

                @Override
                public void onException(JMSException e) {
                    //出异常回调消息
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();


//        for (int i = 0; i < 10; i++) {
//            TextMessage textMessage = session.createTextMessage("hello");
//            textMessage.setJMSCorrelationID(""+i);
//            System.out.println("producer:"+textMessage.getText());
//        }
    }
}
