package eu.getsoftware.hotelico.hotelapp.adapter.out.persistence.checkin.messaging;

import eu.getsoftware.hotelico.clients.api.clients.dto.entity.CheckinDTO;
import eu.getsoftware.hotelico.clients.api.clients.dto.entity.CheckinRequestDTO;
import eu.getsoftware.hotelico.clients.common.domain.mapper.IDomainMapper;
import eu.getsoftware.hotelico.hotelapp.application.checkin.domain.CheckinRootDomainEntity;
import eu.getsoftware.hotelico.hotelapp.application.checkin.port.out.CheckinPortService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
@RepositoryEventHandler
@RequiredArgsConstructor
public class CheckinRepositoryEventHandler {

//    @NonNull
//    private ResourceProcessor<Resource<CheckinDTO>> checkinResourceProcessor;

    @NonNull
    private final CheckinMessagePublisher checkinMessagePublisher;

    @NonNull
    private final CheckinPortService checkinService;

    private final IDomainMapper<CheckinRootDomainEntity, CheckinRequestDTO, CheckinDTO> checkinDomainMapper;

    @HandleAfterCreate //выполнить определенные действия сразу после создания нового объекта в базе данных
    public void sendCreatedEvent(CheckinRootDomainEntity checkin) {
        CheckinDTO checkinDTO = checkinDomainMapper.toResponseDTO(checkin);
        checkinMessagePublisher.publishCheckinCreatedEvent(checkinDTO);
    }

    @HandleAfterSave //выполнить определенные действия сразу после update объекта в базе данных
    public void sendUpdatedEvent(CheckinRootDomainEntity checkin) {
        CheckinDTO checkinDTO = checkinDomainMapper.toResponseDTO(checkin);
        checkinMessagePublisher.publishCheckinUpdatedEvent(checkinDTO);
    }

    @HandleAfterLinkSave
    public void onDefaultImageSavedEvent(CheckinRootDomainEntity checkin, Object image) {
        CheckinDTO checkinDTO = checkinDomainMapper.toResponseDTO(checkin);
        checkinMessagePublisher.publishCheckinUpdatedEvent(checkinDTO);
    }

    @HandleAfterLinkDelete
    public void onChildEntityDeleted(CheckinDTO checkinDTO, Object child) {
        if (child instanceof Image) {
            // for DELETE on default-image
            checkinMessagePublisher.publishCheckinUpdatedEvent(checkinDTO);
        }
    }

    @HandleBeforeDelete
    public void deleteImagesAndAttachmentsBeforeCheckinDeletion(CheckinRootDomainEntity checkin){
        CheckinDTO checkinDTO = checkinDomainMapper.toResponseDTO(checkin);
        checkinService.deleteAllImagesAndAttachments(checkinDTO);
    }

    @HandleAfterDelete
    public void sendDeletedEvent(CheckinRootDomainEntity checkin) {
        CheckinDTO checkinDTO = checkinDomainMapper.toResponseDTO(checkin);
        checkinMessagePublisher.publishCheckinDeletedEvent(checkinDTO);
    }
}
