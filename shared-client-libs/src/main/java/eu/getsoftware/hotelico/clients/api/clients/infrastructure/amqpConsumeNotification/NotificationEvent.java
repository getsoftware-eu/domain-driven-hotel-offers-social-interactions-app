package eu.getsoftware.hotelico.clients.api.clients.infrastructure.amqpConsumeNotification;

import eu.getsoftware.hotelico.clients.common.domain.IDomainRequestDTO;

public record NotificationEvent(
        long requesterId,
        String name,
        long toCustomerId,
        String toCustomerName,
        String message
) implements IDomainRequestDTO {
}
