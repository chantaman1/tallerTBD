package java.backend.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "artist")
public class Artist implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "artist_id", nullable = false)
    private Integer artistId;

	@Column(name = "name", nullable = false)
	private String name;

	//One artist has many keywords.
    @OneToMany(targetEntity = Keywords.class, mappedBy = "keywords", cascade = CascadeType.ALL)
    @JsonManagedReference("keywords")
    private List<Keywords> keywords;

    //One artist has many estadistics.
    @OneToMany(targetEntity = ArtistEstadistic.class, mappedBy = "artist_estadistic", cascade = CascadeType.ALL)
    @JsonManagedReference("artist-artist_estadistic")
    private List<ArtistEstadistic> artist_estadistic;
}
