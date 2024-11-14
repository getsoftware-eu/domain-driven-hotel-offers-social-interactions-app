package eu.getsoftware.hotelico.infrastructure.hotel.plugin.chat.adapter.in.web.controller;

import eu.getsoftware.hotelico.clients.api.adapter.in.web.controller.IChatApiController;
import eu.getsoftware.hotelico.clients.api.clients.common.dto.CustomerDTO;
import eu.getsoftware.hotelico.clients.api.clients.infrastructure.chat.dto.ChatMsgDTO;
import eu.getsoftware.hotelico.infrastructure.hotel.plugin.chat.adapter.out.persistence.model.ChatMessageEntity;
import eu.getsoftware.hotelico.infrastructure.hotel.plugin.chat.adapter.out.persistence.model.ChatUserEntity;
import eu.getsoftware.hotelico.infrastructure.hotel.plugin.chat.adapter.out.persistence.repository.ChatMessageRepository;
import eu.getsoftware.hotelico.infrastructure.hotel.plugin.chat.adapter.out.persistence.repository.ChatUserRepository;
import eu.getsoftware.hotelico.infrastructure.hotel.plugin.chat.application.port.out.IChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v0/chat")
@RequiredArgsConstructor
public class ChatMSCommunicationController implements IChatApiController {
	
	private final ChatMessageRepository chatMessageRepository;	
	private final ChatUserRepository chatUserRepository;
	private final IChatService chatService;
	
//	@PostMapping("/message")  in interface
	@Override public ChatMsgDTO postMessage(@RequestBody ChatMsgDTO msgDTO) {
		
		ChatMessageEntity createdEntity = chatService.createChatMessageFromDTO(msgDTO);
		return chatService.convertToDTO(createdEntity);
	}
	
	
//	@PostMapping("/customer") in interface
	public CustomerDTO updateUser(@RequestBody CustomerDTO customerDTO) {
		
		ChatUserEntity updateEntity = chatService.updateCustomerFromDTO(customerDTO);
		return chatService.convertToDTO(updateEntity);
	}
}
