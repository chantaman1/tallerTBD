package backend.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="genre_statistic")
public class GenreStatistic implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="genre_statistic_id", nullable = false)
    private Integer id;
    @Column(name = "positives", nullable = false)
    private Integer positives;
    @Column(name = "negatives", nullable = false)
    private Integer negatives;
    @Column(name = "genre_id", nullable = false)
    private Integer genreId;



    public GenreStatistic(Integer id,
                  Integer negatives,
                  Integer positives,
                  Integer genreId){
        this.id = id;
        this.positives = positives;
        this.negatives = negatives;
        this.genreId = genreId;
    }

    public GenreStatistic(){
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPositives() {
        return this.positives;
    }

    public void setPositives(Integer positives) {
        this.positives = positives;
    }

    public Integer getNegatives(){
        return this.negatives;
    }

    public void setNegatives(Integer negatives){
        this.negatives = negatives;
    }

    public Integer getGenreId(){
    	return this.genreId;
    }

    public void setGenreId(Integer genreId){
    	this.genreId = genreId;
    }

}
