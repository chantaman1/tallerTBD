package backend.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="genre")
public class Genre implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="genre_id", nullable = false)
    private Integer id;
    @Column(name = "genre", nullable = false)
    private String genre;
    @Column(name = "word_id", nullable = false)
    private Integer wordId;



    public Genre(Integer id,
                  Integer wordId,
                  String  genre){
        this.id = id;
        this.genre = genre;
        this.wordId = wordId;
    }

    public Genre(){
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGenre() {
        return this.genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getWordId(){
        return this.wordId;
    }

    public void setWordId(Integer wordId){
        this.wordId = wordId;
    }

}
