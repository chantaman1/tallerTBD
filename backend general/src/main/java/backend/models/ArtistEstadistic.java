package backend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "artist_estadistic")
public class ArtistEstadistic implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "artist_estadistic_id", nullable = false)
    private Integer artistEstadisticId;

	@Column(name = "positive_ratio", nullable = false)
	private String positive_ratio;

    @Column(name = "negative_ratio", nullable = false)
    private String negative_ratio;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    //One estadistic belongs to one artist
    @ManyToOne
    @JoinColumn(name="artist_id")
    @JsonBackReference("artist-artist_estadistic")
    private Artist artist;

    //One estadistic belongs to one location
    @ManyToOne
    @JoinColumn(name="location_id")
    @JsonBackReference("location-artist_estadistic")
    private Location location;

    //Foreign keys
    private transient Integer artistId;
    private transient Integer locationId;
}
