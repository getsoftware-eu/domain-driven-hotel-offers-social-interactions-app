package eu.getsoftware.hotelico.hotelapp.adapter.out.persistence.checkin.messaging;

import eu.getsoftware.hotelico.clients.api.clients.dto.entity.CheckinDTO;
import eu.getsoftware.hotelico.clients.api.clients.infrastructure.domainEvents.CheckinUpdatedEventPayload;
import eu.getsoftware.hotelico.clients.api.clients.infrastructure.domainEvents.domainMessage.DomainMessage;
import eu.getsoftware.hotelico.hotelapp.adapter.out.persistence.checkin.model.CheckinDBEntity;
import eu.getsoftware.hotelico.hotelapp.adapter.out.persistence.checkin.repository.CheckinRepository;
import eu.getsoftware.hotelico.hotelapp.application.checkin.domain.CheckinRootDomainEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CheckinKafkaSubscriber {

    private final CheckinRepository checkinRepository;
    private final ModelMapper modelMapper;

    @KafkaListener(topics = {"checkin.checkin.created.event", 
                             "checkin.checkin.updated.event"}, 
                   groupId = "checkin.notification.processor.dev")
     void listener(DomainMessage<?> data){
        System.out.println("Kafka listener : " + data.toString());
    }

    @KafkaListener(topics = {"checkin.checkin.created.event"}, groupId = "checkin.notification.processor.dev")
    public void createCheckin(DomainMessage<CheckinUpdatedEventPayload> message) {
        CheckinUpdatedEventPayload payload = message.getPayload();

        {
            log.info("Processing event {}", message.getMessageType());
            
            CheckinDTO checkinDTO = toCheckinDTO(payload);//.build();
            
            if(checkinRepository.existsByDomainEntityId(checkinDTO.getInitId()))
                throw new RuntimeException("not found");

            CheckinDBEntity entity = modelMapper.map(checkinDTO, CheckinDBEntity.class);
            
            checkinRepository.save(entity);
        }
    }

    //    @DomainMessageHandler("checkin.checkin.updated.event")
    @KafkaListener(topics = {"checkin.checkin.updated.event"}, groupId = "checkin.notification.processor.dev")
    public void updateCheckin(DomainMessage<CheckinUpdatedEventPayload> message) {
        CheckinUpdatedEventPayload payload = message.getPayload();

        {
            log.info("Processing event {}", message.getMessageType());
            CheckinDTO checkinDTO = toCheckinDTO(payload);//.build();
//            checkinRepository.partialUpdateCheckin(checkinDTO);

             CheckinDBEntity entity = checkinRepository.findByDomainEntityId(checkinDTO.getInitId()).orElseThrow(()-> new RuntimeException("not found"));
             
             applyDtoUpdates(entity, checkinDTO);
             
             checkinRepository.save(entity);
        }
    }

    private void applyDtoUpdates(CheckinRootDomainEntity entity, CheckinDTO checkin) {
        //TODO
    }

    //    @DomainMessageHandler("checkin.checkin.deleted.event")
    @KafkaListener(topics = {"checkin.checkin.deleted.event"}, groupId = "checkin.notification.processor.dev")
    public void deleteCheckin(DomainMessage<CheckinUpdatedEventPayload> message) {
        CheckinUpdatedEventPayload payload = message.getPayload();

        {
            log.info("Processing event {}", message.getMessageType());
            checkinRepository.deleteByDomainEntityId(payload.getEntityId());
        }
    }

    /**
     * Re-create an entity from received Event-Payload
     *
     * @param payload
     * @return
     */
    private CheckinDTO toCheckinDTO(CheckinUpdatedEventPayload payload) {
        return CheckinDTO.builder()
                .initId(payload.getEntityId())
                .checkinFrom(payload.getCheckinFrom())
                .checkinTo(payload.getCheckinTo())
                .hotelId(payload.getHotelId())
                .customerId(payload.getCustomerId())
                .build();
    }
    
}