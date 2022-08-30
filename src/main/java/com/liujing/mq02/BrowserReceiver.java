package com.liujing.mq02;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;

import javax.jms.*;
import java.util.Enumeration;

public class BrowserReceiver {
    public static void main(String[] args) throws Exception {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
                "admin",
                "admin",
//                "tcp://172.16.157.132:61616"
                "tcp://172.16.157.132:5671"
        );
        Connection connection = factory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        QueueBrowser browser = session.createBrowser(new ActiveMQQueue("new-queue"));
        Enumeration enumeration = browser.getEnumeration();
        //把当前destination没有消费的东西取出来
        while (enumeration.hasMoreElements()) {
            TextMessage textMessage = (TextMessage)enumeration.nextElement();
            System.out.println("textMessage:"+textMessage.getText());
        }
    }
}
