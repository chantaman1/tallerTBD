package backend.models;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
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

    @OneToMany(targetEntity = StatsMusicalGenre.class, mappedBy = "musicalGenre", cascade = CascadeType.ALL)
    @JsonBackReference("keyword-musicalGenre")
    private List<MusicalGenre> statsGenre;

    @OneToMany(targetEntity = Keywords.class, mappedBy = "musicalGenre", cascade = CascadeType.ALL)
    @JsonBackReference("musicalGenre-statsMusicalGenre")
    private List<StatsMusicalGenre> keywordsGenre;
}
