package eu.getsoftware.hotelico.hotelapp.adapter.out.persistence.checkin.repository;

import eu.getsoftware.hotelico.clients.common.adapter.out.persistence.GenericRepositoryAdapter;
import eu.getsoftware.hotelico.clients.common.domain.domainIDs.CheckinDomainEntityId;
import eu.getsoftware.hotelico.hotelapp.adapter.out.persistence.checkin.mapper.CheckinEntityMapper;
import eu.getsoftware.hotelico.hotelapp.adapter.out.persistence.checkin.model.CheckinDBEntity;
import eu.getsoftware.hotelico.hotelapp.application.checkin.domain.CheckinRootDomainEntity;
import org.springframework.stereotype.Component;

@Component
public class CheckinRepositoryAdapter
        extends GenericRepositoryAdapter<CheckinRootDomainEntity, CheckinDBEntity, CheckinDomainEntityId> {

    public CheckinRepositoryAdapter(CheckinRepository repository, CheckinEntityMapper mapper) {
        super(repository, mapper);
    }
}