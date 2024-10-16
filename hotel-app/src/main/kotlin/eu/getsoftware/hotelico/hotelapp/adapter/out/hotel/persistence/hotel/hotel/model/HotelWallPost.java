package eu.getsoftware.hotelico.hotelapp.adapter.out.hotel.persistence.hotel.hotel.model;

import eu.getsoftware.hotelico.clients.common.utils.HibernateUtils;
import eu.getsoftware.hotelico.hotelapp.adapter.out.hotel.persistence.hotel.customer.model.CustomerRootEntity;
import eu.getsoftware.hotelico.hotelapp.application.hotel.domain.model.IHotelWallPost;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Eugen on 16.07.2015.
 */
@Entity
@Getter @Setter
@Table(name = "hotel_wallpost", schema = "hotel")
public class HotelWallPost implements IHotelWallPost, Serializable
{
	private static final long serialVersionUID = -5478152926665631989L;

	@Id
	@Setter(AccessLevel.PROTECTED)
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private long id;

	@Column(name = "active", columnDefinition = HibernateUtils.ColumnDefinition.BOOL_DEFAULT_TRUE)
	private boolean active = true;

	@Column
	private String message;	
	
	@Column
	private String specialWallContent;

	@Column(name = "initId", columnDefinition = "BIGINT(20) DEFAULT 0")
	private long initId;	
	
	@Column
	private Integer replyOnWallPostId;

	@Column
	private Timestamp timestamp;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "validUntil", nullable = true, length = 10)
	private Date validUntil;
	
	@ManyToOne
	@JoinColumn(name="senderId")
	private CustomerRootEntity sender;

	@ManyToOne
	@JoinColumn(name="hotelId")
	private HotelRootEntity hotelRootEntity;

	public HotelWallPost()
	{
		this.timestamp = new Timestamp(new Date().getTime());

	}

	public HotelWallPost(String message, CustomerRootEntity sender, HotelRootEntity hotelRootEntity) {

		this();

		this.message = message;
		this.sender = sender;
		this.hotelRootEntity = hotelRootEntity;
	}

	public long getId()
	{
		return id;
	}
}

