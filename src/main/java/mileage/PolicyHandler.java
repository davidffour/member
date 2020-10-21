package mileage;

import mileage.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PolicyHandler{

    @Autowired
    MemberRepository memberRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverMsgSent_UpdateStatus(@Payload MsgSent msgSent){

        if(msgSent.isMe()){
            System.out.println("##### listener UpdateStatus : " + msgSent.toJson());

            Optional<Member> memberOptional = memberRepository.findByMemberId(msgSent.getMemberId());
            Member member = memberOptional.get();

            if("SUCCESS".equals(msgSent.getMessageStatus())){
                member.setMemberStatus("SKT");
            }
            else {
                member.setMemberStatus("NON_SKT");
            }

            memberRepository.save(member);
        }
    }
}
