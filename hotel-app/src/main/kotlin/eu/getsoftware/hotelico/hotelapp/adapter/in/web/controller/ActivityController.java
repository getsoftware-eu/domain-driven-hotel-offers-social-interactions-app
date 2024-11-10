package eu.getsoftware.hotelico.hotelapp.adapter.in.web.controller;

import eu.getsoftware.hotelico.clients.common.adapter.in.web.controller.BasicController;
import eu.getsoftware.hotelico.hotelapp.application.hotel.domain.infrastructure.aspects.NotifyClients;
import eu.getsoftware.hotelico.hotelapp.application.hotel.domain.infrastructure.dto.HotelActivityDTO;
import eu.getsoftware.hotelico.hotelapp.application.hotel.domain.infrastructure.dto.ResponseDTO;
import eu.getsoftware.hotelico.hotelapp.application.hotel.port.out.iPortService.IHotelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/activity")
public class ActivityController extends BasicController
{

  private Logger logger = LoggerFactory.getLogger(getClass());
  
  @Autowired
  private IHotelService hotelService;
	
  @RequestMapping(method = RequestMethod.GET)
  public String viewApplication() {
    return "activity";
  }

  @MessageMapping("/activity")
  //who should recieve result of this method
  @SendTo("/activitytopic/message")
  public HotelActivityDTO sendActivity(HotelActivityDTO activityDto) {
    logger.info("Activity sent");

    HotelActivityDTO savedActivity = hotelService.addUpdateHotelActivity(activityDto.getSenderId(), activityDto);
    
    return activityDto;
  }

  @RequestMapping(value = "/activities/customer/{customerId}/hotel/{hotelId}", method = RequestMethod.GET)
  public @ResponseBody
  List<HotelActivityDTO> getActivitiesByHotelId(@PathVariable long customerId, @PathVariable long hotelId) {
    //TODO Eugen: socket ConnectException: Connection timed out: connect
    return hotelService.getHotelActivitiesByHotelId(customerId, hotelId);
  }
  
  @RequestMapping(value = "/action/{action}/customer/{customerId}/activityId/{activityId}", method = RequestMethod.GET)
  public @ResponseBody
  HotelActivityDTO addActivityAction(@PathVariable String action, @PathVariable long customerId, @PathVariable long activityId) {
    return hotelService.addActivityAction(customerId, activityId, action);
  }
  
  @RequestMapping(value = "/activities/sender/{senderId}/hotel/{hotelId}", method = RequestMethod.GET)
  public @ResponseBody
  List<HotelActivityDTO> getActivitiesByCreatorAndHotelId(@PathVariable long senderId, @PathVariable long hotelId) {
    return hotelService.getHotelActivitiesBySenderAndHotelId(senderId, hotelId);
  }  
  
  @RequestMapping(value = "/activities/customer/{customerId}/activityId/{activityId}", method = RequestMethod.GET)
  public @ResponseBody
  HotelActivityDTO getActivityById(@PathVariable long customerId, @PathVariable long activityId) {
    return hotelService.getHotelActivityById(customerId, activityId);
  }
//  
//  @RequestMapping(value = "/activities/hotel/{id}", method = RequestMethod.GET)
//  public List<HotelActivityDto> getHotnelActivitiesByHotelId(@PathVariable int id) {
//    List<HotelActivityDto> out = hotelService.getHotelActivitiesByHotelId(id);
//    return out;
//  }

//  @NotifyClients
//  @RequestMapping(value = "/activity/{id}", method = RequestMethod.PUT)
//  public HotelActivityDto updateHotelActivity(@PathVariable int id, @RequestBody HotelActivityDto hotelActivityDto) {
//    hotelActivityDto.setId(id);
//    HotelActivityDto out = hotelService.updateHotelActivity(hotelActivityDto);
//    return out;
//  }
  
  @NotifyClients
  @RequestMapping(value = "/customer/{customerId}/activity/{activityId}", method = RequestMethod.DELETE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public @ResponseBody ResponseDTO deleteHotelActivity(@PathVariable long customerId, @PathVariable long activityId) throws Throwable {
    
    return hotelService.deleteHotelActivity(customerId, activityId);
  }

  @RequestMapping(value = "/customer/{customerId}/activity/{activityId}", method = RequestMethod.POST)//, headers ="Accept:*/*")
  public @ResponseBody
  HotelActivityDTO addUpdateActivity(@PathVariable long customerId, @PathVariable long activityId, @RequestBody HotelActivityDTO activityDto) throws Throwable {

//    activityDto.setInitId(activityId);
    HotelActivityDTO out = hotelService.addUpdateHotelActivity(customerId, activityDto);
    return out;
  }

//  @NotifyClients
//  @SendTo("/activitytopic/message")
//  @RequestMapping(value = "/customer/activity", method = RequestMethod.PUT, headers ="Accept:*/*")
//  public @ResponseBody HotelActivityDto addActivity(@RequestBody HotelActivityDto activityDto) {
////    activityDto.setInitId(activityId);
//    HotelActivityDto out = hotelService.addUpdateHotelActivity(activityDto.getSenderId(), activityDto);
//    return out;
//  }

}
