package java.backend.models;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Entity
@Getter
@Setter
@Table(name="keywords")
public class Keywords {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "keyword_id", nullable = false)
    private Integer keywordId;

    @Column(name = "keyword_word", nullable = false)
    private String word;

    @ManyToOne
    @JoinColumn(name="keyword_id")
    @JsonBackReference("keyword-musicalGenre")
    private MusicalGenre musicalGenre;

    //Muchas palabras clave pertenecen a un artista
    @ManyToOne
    @JoinColumn(name="artist_id")
    @JsonBackReference("keyword-artist")
    private Artist artist;

    private transient Integer musicalGenreId;
    private transient Integer artistId;
}
