package eu.getsoftware.hotelico.clients.common.domain.domainIDs;

import eu.getsoftware.hotelico.clients.api.clients.dto.entity.CheckinRequestDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * eu: test DTO validation on parameters!!!
 */
class CheckinRequestDTOTest {

    @Test
    public void givenCheckinRequestDTO_whenWrongDates_thenError(){
        
        assertThrows(IllegalArgumentException.class, () -> new CheckinRequestDTO(new CustomerDomainEntityId("123"), new HotelDomainEntityId("2"), LocalDate.now(),  LocalDate.now(), 123, "Eugen"));
    }
    

}