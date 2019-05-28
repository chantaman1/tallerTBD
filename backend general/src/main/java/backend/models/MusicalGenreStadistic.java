package java.backend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "musical_genre_stadistic")
public class MusicalGenreStadistic implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "musical_genre_stadistic_id", nullable = false)
    private Integer musicalGenreStadisticId;

	@Column(name = "positive_ratio", nullable = false)
	private String positive_ratio;

    @Column(name = "negative_ratio", nullable = false)
    private String negative_ratio;

    @Column(name = "neutral_ratio", nullable = false)
    private String neutral_ratio;

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
    private transient Integer musicalGenreId;
    private transient Integer locationId;
}
