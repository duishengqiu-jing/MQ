package com.liujing.mq.queue;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class Mylistener implements MessageListener {

    @Override
    public void onMessage(Message message) {
        // 通过onmessage方法收到broker给推送过来的消息，当有消息的时候就回调该方法
        if (message instanceof TextMessage){
            try {
                System.out.println("myListener:"+((TextMessage) message).getText());
            } catch (JMSException e) {
                e.printStackTrace();
            }
            try {
                message.acknowledge();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
