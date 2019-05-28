package java.backend.models;

import javax.persistence.*;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "location")
public class Location implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id", nullable = false)
    private Integer locationId;

	@Column(name = "location_name", nullable = false)
	private String location_name;

	//One artist has many keywords.
    @OneToMany(targetEntity = ArtistEstadistic.class, mappedBy = "artist_estadistic", cascade = CascadeType.ALL)
    @JsonManagedReference("location-artist_estadistic")
    private List<ArtistEstadistic> artist_estadistic;

    //One artist has many estadistics.
    @OneToMany(targetEntity = MusicalGenreStadistic.class, mappedBy = "musical_genre_stadistic", cascade = CascadeType.ALL)
    @JsonManagedReference("location-musical_genre_estadistic")
    private List<ArtistEstadistic> musical_genre_estadistic;
}
