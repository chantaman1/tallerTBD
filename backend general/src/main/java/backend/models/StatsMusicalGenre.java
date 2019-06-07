package backend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name="statsMusicalGenre")
public class StatsMusicalGenre {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "statsMusicalGenre_id", nullable = false)
    private Integer statsMusicalGenreId;

    @Column(name = "positive_ratio", nullable = false)
    private String positive_ratio;

    @Column(name = "negative_ratio", nullable = false)
    private String negative_ratio;

    @Column(name = "neutral_ratio", nullable = false)
    private String neutral_ratio;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name="statsMusicalGenre_id")
    @JsonBackReference("musicalGenre-statsMusicalGenre")
    private MusicalGenre musicalGenre;

    @ManyToOne
    @JoinColumn(name="location_id")
    @JsonBackReference("location-musical_genre_estadistic")
    private Location location;

    //Foreign Keys
    private transient Integer musicalGenreId;
    private transient Integer locationId;
}
