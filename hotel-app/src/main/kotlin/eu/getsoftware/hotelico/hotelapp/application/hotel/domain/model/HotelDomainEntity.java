package eu.getsoftware.hotelico.hotelapp.application.hotel.domain.model;

public class HotelDomainEntity implements MultitenantDocument, Scoreable {

    public static final String TYPE_NAME = "product";

    @Id
    private String id; // UUID can not be used as document ID yet. https://jira.spring.io/browse/DATAES-163

    @Version
    private Long version;

    @Wither
    private LocalDateTime indexedAt;

    @Setter
    private Float score;

    @JsonView(HotelPartialUpdateView.class)
    private Integer tenantId;

    @JsonView(HotelPartialUpdateView.class)
    private boolean visible;

    @JsonView(HotelPartialUpdateView.class)
    private String sku;

    @JsonView(HotelPartialUpdateView.class)
    private String name;

    @JsonView(HotelPartialUpdateView.class)
    private String description;

    @JsonView(HotelPartialUpdateView.class)
    private String manufacturer;

    @JsonView(HotelPartialUpdateView.class)
    private String essentialFeatures;

    @JsonView(HotelPartialUpdateView.class)
    private String defaultImageRelUri;

    @JsonView(HotelPartialUpdateView.class)
    private LocalDateTime createdAt;

    @JsonView(HotelPartialUpdateView.class)
    private SimpleMonetaryAmount salesPrice;

    @JsonView(HotelPartialUpdateView.class)
    private boolean onSale;

    @JsonView(HotelPartialUpdateView.class)
    private Dimension shippingDimension;

    @Wither
    @JsonView(AvailabilityPartialUpdateView.class)
    private String availabilityState;

    @Singular
    @JsonView(HotelPartialUpdateView.class)
    private ImmutableSet<String> tags;

    @Singular
    @JsonView(AttributePartialUpdateView.class)
    private ImmutableSet<Attribute> attributes;

    @Singular
    @JsonView(ImagePartialUpdateView.class)
    private ImmutableSet<Image> images;

    @JsonView(HotelPartialUpdateView.class)
    private RefPrice refPrice;

    @JsonView(HotelPartialUpdateView.class)
    private ShippingPeriod shippingPeriod;

    @JsonView(HotelPartialUpdateView.class)
    private SimpleMonetaryAmount listPrice;

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
     * @param image image to be added to this product
     * @return <tt>true</tt> if this product did not already contain the specified
     * image
     */
    public boolean addOrReplaceImage(@NotNull Image image) {
        boolean contained = images.contains(checkNotNull(image));
        images = remove(images, image).add(image).build();
        return !contained;
    }

    /**
     * @param image image to be removed from this product, if present
     * @return <tt>true</tt> if this product contained the specified image
     */
    public boolean removeImage(@NotNull Image image) {
        boolean contained = images.contains(checkNotNull(image));
        images = remove(images, image).build();
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
    public static class HotelBuilder {
    }
}
Copy
        Respository
@RepositoryRestResource(exported = false)
public interface HotelRepository extends ElasticsearchRepository<Hotel, String>, HotelRepositoryExtension {

    Optional<Hotel> findVisibleById(String id);
}

public interface HotelRepositoryExtension {

    Page<Hotel> findVisibleByTags(@NotNull Set<String> tags, @NotNull Pageable pageable);

    Page<Hotel> findVisibleBySearchTerm(@Nullable String searchTerm, @NotNull Pageable pageable);

    Page<Hotel> findVisibleByCategoryQuery(@NotNull CategoryQuery categoryQuery, @NotNull Pageable pageable);

    Optional<Hotel> findOneIgnoringVisibility(@NotNull String id);

    Hotel partialUpdateHotel(@NotNull Hotel product);

    Hotel partialUpdateAttributes(@NotNull Hotel product);

    Hotel partialUpdateImages(@NotNull Hotel product);

    Hotel partialUpdateAvailability(@NotNull Hotel product);
}
 


