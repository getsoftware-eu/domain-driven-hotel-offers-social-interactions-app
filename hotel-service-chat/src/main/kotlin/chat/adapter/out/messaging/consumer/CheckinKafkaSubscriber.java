package chat.adapter.out.messaging.consumer;

import chat.application.port.in.process.ChatCheckinProcessManagerService;
import eu.getsoftware.hotelico.clients.api.clients.dto.entity.CheckinDTO;
import eu.getsoftware.hotelico.clients.api.clients.infrastructure.domainEvents.CheckinUpdatedEventPayload;
import eu.getsoftware.hotelico.clients.api.clients.infrastructure.domainEvents.domainMessage.DomainMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CheckinKafkaSubscriber {

    ChatCheckinProcessManagerService chatCheckinProcessManager;
    
    @KafkaListener(topics = {"checkin.checkin.created.event", 
                             "checkin.checkin.updated.event"}, 
                   groupId = "eu_group_1")
     void listener(DomainMessage<?> data){
        System.out.println("Kafka listener : " + data.toString());
    }

    @KafkaListener(topics = {"checkin.checkin.created.event"}, groupId = "eu_group_1")
    public void createCheckin(DomainMessage<CheckinUpdatedEventPayload> message) {
        CheckinUpdatedEventPayload payload = message.getPayload();

        {
            log.info("Processing event {}", message.getMessageType());
            CheckinDTO checkinDTO = toCheckin(payload);
//            checkinRepository.save(checkinDTO);
            chatCheckinProcessManager.handleCheckinCreated(checkinDTO);
        }
    }

    //    @DomainMessageHandler("checkin.checkin.updated.event")
    @KafkaListener(topics = {"checkin.checkin.updated.event"}, groupId = "eu_group_1")
    public void updateCheckin(DomainMessage<CheckinUpdatedEventPayload> message) {
        CheckinUpdatedEventPayload payload = message.getPayload();

        {
            log.info("Processing event {}", message.getMessageType());
            CheckinDTO checkin = toCheckin(payload);
//            checkinRepository.partialUpdateCheckin(checkin);
            chatCheckinProcessManager.handleCheckinUpdated(checkin);

        }
    }

    //    @DomainMessageHandler("checkin.checkin.deleted.event")
    @KafkaListener(topics = {"checkin.checkin.deleted.event"}, groupId = "eu_group_1")
    public void deleteCheckin(DomainMessage<CheckinUpdatedEventPayload> message) {
        CheckinUpdatedEventPayload payload = message.getPayload();

        {
            log.info("Processing event {}", message.getMessageType());
//            checkinRepository.deleteById(payload.getEntityId());
            chatCheckinProcessManager.handleCheckinClosed(payload.getEntityId());

        }
    }

    /**
     * Re-create an entity from received Event-Payload
     *
     * @param payload
     * @return
     */
    private CheckinDTO toCheckin(CheckinUpdatedEventPayload payload) {
        return CheckinDTO.builder()
                .initId(payload.getEntityId())
                .checkinFrom(payload.getCheckinFrom())
                .checkinTo(payload.getCheckinTo())
                .hotelId(payload.getHotelId())
                .customerId(payload.getCustomerId())
                .build();
    }
    
}
