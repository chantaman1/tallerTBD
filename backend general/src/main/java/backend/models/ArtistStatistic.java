package backend.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="artist_statistic")
public class ArtistStatistic implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="artist_statistic_id", nullable = false)
    private Integer id;
    @Column(name = "positives", nullable = false)
    private Integer positives;
    @Column(name = "negatives", nullable = false)
    private Integer negatives;
    @Column(name = "artist_id", nullable = false)
    private Integer artistId;



    public ArtistStatistic(Integer id,
                  Integer negatives,
                  Integer positives,
                  Integer artistId){
        this.id = id;
        this.positives = positives;
        this.negatives = negatives;
        this.artistId = artistId;
    }

    public ArtistStatistic(){
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

    public Integer getArtistId(){
    	return this.artistId;
    }

    public void setArtistId(Integer artistId){
    	this.artistId = artistId;
    }

}
