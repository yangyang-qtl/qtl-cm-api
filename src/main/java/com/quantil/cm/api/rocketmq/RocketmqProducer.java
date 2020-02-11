package com.quantil.cm.api.rocketmq;


import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RocketmqProducer {
    
    @Value("${rocketmq.producer.name}")
    private String producerName;

    @Value("${rocketmq.nameserver.address}")
    private String nameServerAddress;
    
    @Value("${rocketmq.tag}")
    private String tag;
    
    public final static String TOPIC = "task_topic";
    private static final Logger logger = LoggerFactory.getLogger(RocketmqProducer.class);
    public void sendMessage(String messageBody) throws Exception{
        DefaultMQProducer producer = new
            DefaultMQProducer(producerName);
            producer.setNamesrvAddr(nameServerAddress);
            producer.start();
            Message msg = new Message(TOPIC, tag, (messageBody).getBytes(RemotingHelper.DEFAULT_CHARSET) 
                    );
                    SendResult sendResult = producer.send(msg);
                    logger.info(sendResult.toString());
            producer.shutdown();
    }
        
   

}
