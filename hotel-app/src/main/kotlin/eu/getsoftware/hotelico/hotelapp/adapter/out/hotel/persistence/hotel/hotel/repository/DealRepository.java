package eu.getsoftware.hotelico.hotelapp.adapter.out.hotel.persistence.hotel.hotel.repository;

import eu.getsoftware.hotelico.hotelapp.adapter.out.hotel.persistence.hotel.deal.model.CustomerDeal;
import eu.getsoftware.hotelico.hotelapp.application.deal.domain.infrastructure.utils.DealStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DealRepository extends JpaRepository<CustomerDeal, Long> {

	public final static String FIND_BY_INIT_ID_QUERY = "SELECT c " +
			"FROM CustomerDeal c " +
			"WHERE c.initId = :initId " ;
	
	public final static String FIND_ACTIVE_BY_CUSTOMER_QUERY = "SELECT c " +
			"FROM CustomerDeal c " +
			"WHERE c.active = true " +
			"AND c.pk.customer.id = :customerId " +
			"AND c.validFrom <= :checkDate " +
			"AND c.validTo >= :checkDate ";	
	
	public final static String FIND_ACTIVE_BY_GUEST_CUSTOMER_QUERY = "SELECT c " +
			"FROM CustomerDeal c " +
			"WHERE c.active = true " +
			"AND c.guestCustomerId = :guestCustomerId " +
			"AND c.validFrom <= :checkDate " +
			"AND c.validTo >= :checkDate ";		
	
	public final static String COUNT_ACTIVE_BY_GUEST_OR_CUSTOMER_QUERY = "SELECT count(c) " +
			"FROM CustomerDeal c " +
			"WHERE c.active = true " +
			"AND (c.guestCustomerId = :guestCustomerId OR c.pk.customer.id = :customerId)" +
			"AND c.validFrom <= :checkDate " +
			"AND c.validTo >= :checkDate ";	
	
	public final static String FIND_ANONYM_DEALS_FOR_GUEST_ID_QUERY = "SELECT c " +
			"FROM CustomerDeal c " +
			"WHERE c.active = true " +
			"AND c.guestCustomerId = :guestCustomerId " +
			"AND c.pk.customer.email = '[anonym]' ";			
	
	public final static String EXIST_ANONYM_BY_GUEST_CUSTOMER_QUERY = "SELECT count(c)>0 " +
			"FROM CustomerDeal c " +
			"WHERE c.active = true " +
			"AND c.guestCustomerId = :guestCustomerId " +
			"AND c.pk.customer.email = '[anonym]' ";		
	
	public final static String FIND_ACTIVE_BY_CUSTOMER_AND_ACTIVITY_QUERY = "SELECT c " +
			"FROM CustomerDeal c " +
			"WHERE c.active = true " +
			"AND c.pk.customer.id = :customerId " +
			"AND c.pk.activity.id = :activityId " +
			"AND c.status in (:statusList) " +
			"AND (:checkDateFrom is NULL OR c.validFrom <= :checkDateFrom) " +
			"AND (:checkDateTo is NULL OR c.validTo >= :checkDateTo) ";	
	
	public final static String FIND_ACTIVE_BY_GUEST_CUSTOMER_AND_ACTIVITY_QUERY = "SELECT c " +
			"FROM CustomerDeal c " +
			"WHERE c.active = true " +
			"AND c.guestCustomerId = :guestCustomerId " +
			"AND c.pk.activity.id = :activityId " +
			"AND c.status in (:statusList) " +
			"AND (:checkDateFrom is NULL OR c.validFrom <= :checkDateFrom) " +
			"AND (:checkDateTo is NULL OR c.validTo >= :checkDateTo) ";

	public final static String FIND_ACTIVE_BY_CUSTOMER_AND_HOTEL_QUERY = "SELECT c " +
			"FROM CustomerDeal c " +
			"WHERE c.active = true " +
			"AND c.pk.customer.id = :customerId " +
			"AND c.pk.activity.hotel.id = :hotelId " +
			"AND c.status in (:statusList) " +
			"AND (:checkDateFrom is NULL OR c.validFrom <= :checkDateFrom) " +
			"AND (:checkDateTo is NULL OR c.validTo >= :checkDateTo) ";
	
	public final static String FIND_ACTIVE_BY_GUEST_AND_HOTEL_QUERY = "SELECT c " +
			"FROM CustomerDeal c " +
			"WHERE c.active = true " +
			"AND c.guestCustomerId = :guestCustomerId " +
			"AND c.pk.activity.hotel.id = :hotelId " +
			"AND c.status in (:statusList) " +
			"AND (:checkDateFrom is NULL OR c.validFrom <= :checkDateFrom) " +
			"AND (:checkDateTo is NULL OR c.validTo >= :checkDateTo) ";		
	
	public final static String FIND_ACTIVE_DEALS_BY_ACTIVITY_QUERY = "SELECT c " +
			"FROM CustomerDeal c " +
			"WHERE c.active = true " +
//			"AND c.pk.customer.id = :customerId " +
			"AND c.pk.activity.id = :activityId " +
			"AND c.status in (:statusList) " +
			"AND (:checkDateFrom is NULL OR c.validFrom <= :checkDateFrom) " +
			"AND (:checkDateTo is NULL OR c.validTo >= :checkDateTo) ";
	
	public final static String FIND_ACTIVE_DEALS_BY_HOTEL_QUERY = "SELECT c " +
			"FROM CustomerDeal c " +
			"WHERE c.active = true " +
//			"AND c.pk.customer.id = :customerId " +
			"AND c.pk.activity.hotel.id = :hotelId " +
			"AND c.status in (:statusList) " +
			"AND (:checkDateFrom is NULL OR c.validFrom <= :checkDateFrom) " +
			"AND (:checkDateTo is NULL OR c.validTo >= :checkDateTo) ";	
	
	public final static String EXIST_ACTIVE_DEALS_FOR_CUSTOMER_BY_ACTIVITY_QUERY = "SELECT count(c)>0 " +
			"FROM CustomerDeal c " +
			"WHERE c.active = true " +
			"AND c.pk.activity.id = :activityId " +
			"AND c.pk.customer.id = :customerId " +
			"AND (" +
			  "( " +
//				" c.fullCheckin = true AND" +
				"c.validFrom <= :checkDate " +
				"AND c.validTo >= :checkDate " +
			  " ) " +
//				"OR ( c.pk.customer.activityStaff = TRUE ) " 
			" ) "
			;

	/**
	 * Find customer active checkIn.
	 */
	@Query(FIND_ACTIVE_BY_CUSTOMER_QUERY)
	public List<CustomerDeal> getActiveByCustomerId(@Param("customerId") Long customerId, @Param("checkDate") Date checkDate);		
		
	@Query(FIND_ACTIVE_BY_GUEST_CUSTOMER_QUERY)
	public List<CustomerDeal> getActiveByGuestId(@Param("guestCustomerId") Long guestCustomerId, @Param("checkDate") Date checkDate);
	
	@Query(COUNT_ACTIVE_BY_GUEST_OR_CUSTOMER_QUERY)
	public Integer countActiveDealsByCustomerOrGuest(@Param("customerId") Long customerId, @Param("guestCustomerId") Long guestCustomerId, @Param("checkDate") Date checkDate);
	
	@Query(FIND_ANONYM_DEALS_FOR_GUEST_ID_QUERY)
	public List<CustomerDeal> getAnonymDealsByGuestId(@Param("guestCustomerId") Long guestCustomerId);	
	
	
	@Query(EXIST_ANONYM_BY_GUEST_CUSTOMER_QUERY)
	public boolean existAnonymDelasByGuestId(@Param("guestCustomerId") Long guestCustomerId);		
	
	/**
	 * Find Deals by customer and activity
	 */
	@Query(FIND_ACTIVE_BY_CUSTOMER_AND_ACTIVITY_QUERY)
	public List<CustomerDeal> getActiveByCustomerAndActivityId(@Param("customerId") Long customerId, @Param("activityId") Long activityId, @Param("statusList") List<DealStatus> statusList, @Param("checkDateFrom") Date checkDateFrom, @Param("checkDateTo") Date checkDateTo);	
		
	/**
	 * Find Deals by guestId and activity
	 */
	@Query(FIND_ACTIVE_BY_GUEST_CUSTOMER_AND_ACTIVITY_QUERY)
	public List<CustomerDeal> getActiveByGuestAndActivityId(@Param("guestCustomerId") Long guestCustomerId, @Param("activityId") Long activityId, @Param("statusList") List<DealStatus> statusList, @Param("checkDateFrom") Date checkDateFrom, @Param("checkDateTo") Date checkDateTo);	
			
	/**
	 * Find Deals by customer and whole Hotel
	 */
	@Query(FIND_ACTIVE_BY_CUSTOMER_AND_HOTEL_QUERY)
	public List<CustomerDeal> getActiveByCustomerAndHotelId(@Param("customerId") Long customerId, @Param("activityId") Long activityId, @Param("statusList") List<DealStatus> statusList, @Param("checkDateFrom") Date checkDateFrom, @Param("checkDateTo") Date checkDateTo);	
		
	/**
	 * Find Deals by customer and whole Hotel
	 */
	@Query(FIND_ACTIVE_BY_GUEST_AND_HOTEL_QUERY)
	public List<CustomerDeal> getActiveByGuestAndHotelId(@Param("guestCustomerId") Long guestCustomerId, @Param("activityId") Long activityId, @Param("statusList") List<DealStatus> statusList, @Param("checkDateFrom") Date checkDateFrom, @Param("checkDateTo") Date checkDateTo);	
		
	@Query(EXIST_ACTIVE_DEALS_FOR_CUSTOMER_BY_ACTIVITY_QUERY)
	public boolean countActiveDealsForActivity(@Param("customerId") Long customerId, @Param("activityId") Long activityId, @Param("checkDate") Date checkDate);

	@Query(FIND_ACTIVE_DEALS_BY_ACTIVITY_QUERY)
	public List<CustomerDeal> getActivityDeals(@Param("activityId") Long activityId, @Param("statusList") List<DealStatus> statusList, @Param("checkDateFrom") Date checkDateFrom, @Param("checkDateTo") Date checkDateTo);
	
	@Query(FIND_ACTIVE_DEALS_BY_HOTEL_QUERY)
	public List<CustomerDeal> getHotelDeals(@Param("hotelId") Long hotelId, @Param("statusList") List<DealStatus> statusList, @Param("checkDateFrom") Date checkDateFrom, @Param("checkDateTo") Date checkDateTo);	
	
	@Query(FIND_BY_INIT_ID_QUERY)
	List<CustomerDeal> getByInitId(@Param("initId") Long initId);
}
