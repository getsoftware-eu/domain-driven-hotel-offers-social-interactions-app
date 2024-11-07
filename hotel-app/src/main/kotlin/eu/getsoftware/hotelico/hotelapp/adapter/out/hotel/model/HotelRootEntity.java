package eu.getsoftware.hotelico.hotelapp.adapter.out.hotel.model;

import eu.getsoftware.hotelico.clients.common.utils.HibernateUtils;
import eu.getsoftware.hotelico.hotelapp.adapter.out.checkin.model.CustomerHotelCheckin;
import eu.getsoftware.hotelico.hotelapp.adapter.out.checkin.model.HotelActivity;
import eu.getsoftware.hotelico.hotelapp.application.hotel.domain.model.IHotelRootEntity;
import eu.getsoftware.hotelico.hotelapp.application.hotel.port.out.iPortService.IFileUploadable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter @Setter
@Table(name = "hotel", schema = "hotel")
public class HotelRootEntity implements IHotelRootEntity, Serializable, IFileUploadable
{
    private static final long serialVersionUID = -4429239383913774205L;
	
    @Id
    @Setter(AccessLevel.PROTECTED)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "hotel_id_generator")
    @SequenceGenerator(name="hotel_id_generator", sequenceName = "hotel_id_seq")
    private long id;

    @Column(columnDefinition = "BINARY(16)")
    private UUID hotelUUID;
    
    @Column(name = "rating", nullable = true)
    private Integer rating;

    @Column(name = "consistencyId", columnDefinition = "BIGINT(20) DEFAULT 0")
    private long consistencyId;    
    
    @Column(name = "creationTime", columnDefinition = "BIGINT(20) DEFAULT 0")
    private long creationTime;
    
    @Column(name = "active", columnDefinition = HibernateUtils.ColumnDefinition.BOOL_DEFAULT_TRUE)
    private boolean active = true;
    
    @Column(name = "virtual", columnDefinition = HibernateUtils.ColumnDefinition.BOOL_DEFAULT_FALSE)
    private boolean virtual = false;
    
    @Column(name = "name", nullable = false)
    private String name;    
    
    @Column(name = "wellcomeMessage", nullable = false)
    private String wellcomeMessage;
   
    @Column(name = "description", nullable = true, columnDefinition = "LONGTEXT")
    @JdbcTypeCode(SqlTypes.LONGVARCHAR) // Use LONGVARCHAR for long text fields

    private String description;    
    
    @Column(name = "info", nullable = true, columnDefinition = "LONGTEXT")
    @JdbcTypeCode(SqlTypes.LONGVARCHAR) // Use LONGVARCHAR for long text fields

    private String info;

    @Column(name = "city", nullable = true)
    private String city;

    @Column(name = "postalCode", nullable = true)
    private String postalCode;    
    
    @Column(name = "website", nullable = true)
    private String website;    
    
    @Column(name = "mapLink", nullable = true)
    private String mapLink;

    @Column(name = "street", nullable = true)
    private String street;

    @Column(name = "house", nullable = true)
    private String house;

    @Column(name = "phone", nullable = true)
    private String phone;

    @Column(name = "fax", nullable = true)
    private String fax;

    @Column(name = "email", nullable = true)
    private String email;

    @Column(name = "currentHotelAccessCode", nullable = true)
    private String currentHotelAccessCode;
    
    @Column(name = "previewPictureUrl", nullable = true, length = 250)
    private String previewPictureUrl;    
    
    @Column(name = "pictureUrl", nullable = true, length = 250)
    private String pictureUrl;    
    
	//TODO convert string data to own structure
    @Column(name = "guestPushIds", nullable = true, columnDefinition="LONGTEXT")
    private String guestPushIds = "";    
	
	@Column(name = "unsubscribeNotificationPushIds", nullable = true, columnDefinition="LONGTEXT")
    private String unsubscribeNotificationPushIds = "";

    @ElementCollection(targetClass= CustomerHotelCheckin.class)
    @OneToMany(/*fetch = FetchType.LAZY,*/ mappedBy = "pk.hotel")
    private Set<CustomerHotelCheckin> customerHotelHistories = new HashSet<CustomerHotelCheckin>(0);

//    @ElementCollection(targetClass=WallPost.class)
//    @OneToMany(/*fetch = FetchType.LAZY,*/ mappedBy = "pkw.hotel")
//    private Set<WallPost> wallPosts = new HashSet<WallPost>(0);

    //eugen: mappedBy entity!
    @OneToMany(mappedBy= "hotelRootEntity")
    private Set<HotelWallPost> hotelWallPosts;
    
    //eugen: mappedBy entity!
    @OneToMany(mappedBy= "hotelRootEntity")
    private Set<HotelActivity> hotelActivities;

    @Column(name = "mediaUploaded", columnDefinition = HibernateUtils.ColumnDefinition.BOOL_DEFAULT_FALSE)
    private boolean mediaUploaded = false;
	
	/**
	 * GPS-Koordinate X (geografische Breite) WGS84.<br />
	 * Format: 0 <= latitude <= 90. Dezimalkomma wird in Dezimalpunkt konvertiert;<br />
	 * Dezimalpunkt und Dezimalkomma sind erlaubt.
	 */
	@Column(name = "latitude", columnDefinition = "double DEFAULT '" + LOWER_BOUND_LATITUDE + "'")
	//	@Validate("min=0, max=90")
	private double latitude = LOWER_BOUND_LATITUDE;
	
	/**
	 * GPS-Koordinate Y (geografische Länge) WGS84.<br />
	 * Format: 0 <= longitude <= 180. Dezimalkomma wird in Dezimalpunkt konvertiert<br />
	 * Dezimalpunkt und Dezimalkomma sind erlaubt.
	 */
	@Column(name = "longitude", columnDefinition = "double DEFAULT '" + LOWER_BOUND_LONGITUDE + "'")
	//	@Validate("min=0, max=180")
	private double longitude = LOWER_BOUND_LONGITUDE;
    
    public HotelRootEntity() {
        super();
    }

    public long getId() {
        return id;
    }
    
    @Override
    public String getPlainFilePath(final int upperOrderId)
    {
        return "hotel/" + getId() + "";
    }

    @Override
    public void setMediaUploaded(boolean mediaUploaded)
    {
        this.mediaUploaded = mediaUploaded;
    }
	
	/**
	 * -180
	 */
	public static final double LOWER_BOUND_LONGITUDE = -180d;
	/**
	 * -90
	 */
	public static final double LOWER_BOUND_LATITUDE = -90d;
}
