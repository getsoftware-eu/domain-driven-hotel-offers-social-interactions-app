package eu.getsoftware.hotelico.hotelapp.adapter.out.checkin.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.common.collect.ImmutableSet;
import eu.getsoftware.hotelico.clients.common.utils.HibernateUtils;
import eu.getsoftware.hotelico.hotelapp.adapter.out.customer.model.CustomerRootEntity;
import eu.getsoftware.hotelico.hotelapp.adapter.out.hotel.model.HotelRootEntity;
import eu.getsoftware.hotelico.hotelapp.application.checkin.domain.model.ICustomerHotelCheckinEntity;
import eu.getsoftware.hotelico.hotelapp.application.customer.domain.model.ICustomerRootEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

import static lombok.Lombok.checkNotNull;


@Data
@Entity
@Table(name = "customer_hotel_checkin", schema = "hotel")
@AssociationOverrides({
		@AssociationOverride(name = "pk.customer",
				joinColumns = @JoinColumn(name = "CUSTOMER_ID")),
		@AssociationOverride(name = "pk.hotel",
				joinColumns = @JoinColumn(name = "HOTEL_ID")) })
public class CustomerHotelCheckin implements ICustomerHotelCheckinEntity, java.io.Serializable {

	private static final long serialVersionUID = -2949611288215768311L;

	@JsonView(CheckinPartialUpdateView.class)
	private Integer tenantId;

	@JsonView(ProductPartialUpdateView.class)
	private boolean visible;

	@JsonView(ProductPartialUpdateView.class)
	private String sku;

	@JsonView(ProductPartialUpdateView.class)
	private String name;

	@JsonView(ProductPartialUpdateView.class)
	private String description;

	@JsonView(ProductPartialUpdateView.class)
	private LocalDateTime createdAt;

	@Singular
	@JsonView(ProductPartialUpdateView.class)
	private ImmutableSet<String> tags;

	@Singular
	@JsonView(AttributePartialUpdateView.class)
	private ImmutableSet<Attribute> attributes;
	
	@Column(name = "active", columnDefinition = HibernateUtils.ColumnDefinition.BOOL_DEFAULT_TRUE)
	private boolean active = true;

	private CustomerHotelCheckinId pk = new CustomerHotelCheckinId();
	
	@Column(name = "fullCheckin", columnDefinition = HibernateUtils.ColumnDefinition.BOOL_DEFAULT_FALSE)
	private boolean fullCheckin = false;	
	
	@Column(name = "staffCheckin", columnDefinition = HibernateUtils.ColumnDefinition.BOOL_DEFAULT_FALSE)
	private boolean staffCheckin = false;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "validFrom", nullable = false, length = 10)
	private Date validFrom;

	@Temporal(TemporalType.DATE)
	@Column(name = "validTo", nullable = false, length = 10)
	private Date validTo;
	
	public CustomerHotelCheckin() {
		super();
	}

	@EmbeddedId
	public CustomerHotelCheckinId getPk() {
		return pk;
	}

	public void setPk(CustomerHotelCheckinId pk) {
		this.pk = pk;
	}
	
	public void setCustomer(CustomerRootEntity customerEntity) {
		getPk().setCustomer(customerEntity);
	}

	@Transient
	public HotelRootEntity getHotel() {
		return getPk().getHotel();
	}
	
	@Transient
	public ICustomerRootEntity getCustomer() {
		return getPk().getCustomer();
	}
	
	public void setHotel(HotelRootEntity category) {
		getPk().setHotel(category);
	}

	public int hashCode() {
		return (getPk() != null ? getPk().hashCode() : 0);
	}
	
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		
		CustomerHotelCheckin that = (CustomerHotelCheckin) o;
		
		if (getPk() != null ? !getPk().equals(that.getPk())
				: that.getPk() != null)
			return false;
		
		return true;
	}

	/**
	 * eu: entity options!
	 * @param originalSet
	 * @param elementToRemove
	 * @return
	 * @param <T>
	 */

	private static <T> ImmutableSet.Builder<T> remove(ImmutableSet<T> originalSet, T elementToRemove) {
		ImmutableSet.Builder<T> builder = ImmutableSet.builder();
		originalSet.forEach(element -> {
			if (!element.equals(elementToRemove)) {
				builder.add(element);
			}
		});
		return builder;
	}

	/**
	 * @param tag tag to be added to this product
	 * @return <tt>true</tt> if this product did not already contain the specified
	 * tag
	 */
	public boolean addOrReplaceTag(@NotNull String tag) {
		boolean contained = tags.contains(checkNotNull(tag));
		tags = remove(tags, tag).add(tag).build();
		return !contained;
	}

	/**
	 * @param tag tag to be removed from this product, if present
	 * @return <tt>true</tt> if this product contained the specified tag
	 */
	public boolean removeTag(@NotNull String tag) {
		boolean contained = tags.contains(checkNotNull(tag));
		tags = remove(tags, tag).build();
		return contained;
	}

	/**
	 * @param attribute attribute to be added to this product
	 * @return <tt>true</tt> if this product did not already contain the specified
	 * attribute
	 */
	public boolean addOrReplaceAttribute(@NotNull Attribute attribute) {
		boolean contained = attributes.contains(checkNotNull(attribute));
		attributes = remove(attributes, attribute).add(attribute).build();
		return !contained;
	}

	/**
	 * @param attribute attribute to be removed from this product, if present
	 * @return <tt>true</tt> if this product contained the specified attribute
	 */
	public boolean removeAttribute(@NotNull Attribute attribute) {
		boolean contained = attributes.contains(checkNotNull(attribute));
		attributes = remove(attributes, attribute).build();
		return contained;
	}

	@JsonPOJOBuilder(withPrefix = "")
	public static class ProductBuilder {
	}
}