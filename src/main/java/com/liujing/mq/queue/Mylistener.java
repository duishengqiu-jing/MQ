package com.liujing.mq.queue;

import javax.jms.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Mylistener implements MessageListener {

    @Override
    public void onMessage(Message message) {
        // 通过onmessage方法收到broker给推送过来的消息，当有消息的时候就回调该方法
//        if (message instanceof TextMessage){
//            try {
//                System.out.println("myListener:"+((TextMessage) message).getText());
//                message.acknowledge();
//            } catch (JMSException e) {
//                e.printStackTrace();
//            }
//        }
        try {
            if (message instanceof TextMessage) {
                System.out.println("consumer textmessage:"+((TextMessage) message).getText());
            } else if (message instanceof ObjectMessage) {
                Person person = (Person) ((ObjectMessage) message).getObject();
                System.out.println("consumer ObjectMessage:"+person.getName()+"\n"+person.getAge()+"\n"+person.getIdCard());
            } else if (message instanceof BytesMessage) {
                //顺序不正确就会读取报错，只有按照推送消息的顺序来进行
                BytesMessage bytesMessage = (BytesMessage) message;
                FileOutputStream outputStream = new FileOutputStream("/Users/liuchang/Downloads/byte.txt");
                byte[] bytes = new byte[2048];
                int len = 0;
                while ((len = bytesMessage.readBytes(bytes)) != -1) {
                    try {
                        outputStream.write(bytes, 0, len);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
//                        System.out.println("ByteMessage-readUTF:"+((BytesMessage) message).readUTF());
//                        System.out.println("ByteMessage-readBoolean:"+((BytesMessage) message).readBoolean());
//                        System.out.println("ByteMessage-readFloat:"+((BytesMessage) message).readFloat());
//                        System.out.println("ByteMessage-readDouble:"+((BytesMessage) message).readDouble());
//                        System.out.println("ByteMessage-readInt:"+((BytesMessage) message).readInt());
//                        System.out.println("ByteMessage-readLong:"+((BytesMessage) message).readLong());
//                        System.out.println("ByteMessage-readShort:"+((BytesMessage) message).readShort());
            } else if (message instanceof MapMessage) {
                boolean aBoolean = ((MapMessage) message).getBoolean("boolean");
                String string = ((MapMessage) message).getString("string");
                int anInt = ((MapMessage) message).getInt("int");
                float aFloat = ((MapMessage) message).getFloat("float");
                long aLong = ((MapMessage) message).getLong("long");
                System.out.println("aBoolean:"+aBoolean+",string:"+string+",anInt:"+anInt+",aFloat:"+aFloat+",aLong:"+aLong);
            }
            message.acknowledge();
        } catch (JMSException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
