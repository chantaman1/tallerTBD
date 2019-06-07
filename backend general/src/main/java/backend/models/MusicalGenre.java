package backend.models;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name="musicalGenre")
public class MusicalGenre {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "musicalGenre_id", nullable = false)
    private Integer musicalGenreId;

    @Column(name = "musicalGenre_genre", nullable = false)
    private String genre;

    @OneToMany(targetEntity = java.backend.models.StatsMusicalGenre.class, mappedBy = "musicalGenre", cascade = CascadeType.ALL)
    @JsonBackReference("keyword-musicalGenre")
    private List<MusicalGenre> statsGenre;

    @OneToMany(targetEntity = java.backend.models.Keywords.class, mappedBy = "musicalGenre", cascade = CascadeType.ALL)
    @JsonBackReference("musicalGenre-statsMusicalGenre")
    private List<java.backend.models.StatsMusicalGenre> keywordsGenre;
}
