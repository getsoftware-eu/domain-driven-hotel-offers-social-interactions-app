package eu.getsoftware.hotelico.infrastructure.hotel.plugin.chat.adapter.out.persistence.model;

import eu.getsoftware.hotelico.clients.common.domain.domainIDs.CustomerDomainEntityId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Getter @Setter
@RequiredArgsConstructor
@Document(collection = "chat_users")
public class ChatUserMappedEntity implements Serializable
{
  private static final long serialVersionUID = -5478152926695631989L;
  
  @Id
  private long id;
  
//  @Convert(converter = CustomerDomainEntityId.class)
  //@Embedded @Column(unique = true, nullable = false)
  private final CustomerDomainEntityId userDomainId;
  
  private String firstName;
  
  private String lastName;
  
  private String email;
}