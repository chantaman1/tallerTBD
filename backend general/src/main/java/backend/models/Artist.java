package backend.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="artist")
public class Artist implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="artist_id", nullable = false)
    private Integer id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "word_id", nullable = false)
    private Integer wordId;



    public Artist(Integer id,
                  Integer wordId,
                  String  name){
        this.id = id;
        this.name = name;
        this.wordId = wordId;
    }

    public Artist(){
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getwordId(){
        return this.wordId;
    }

    public void setwordId(Integer wordId){
        this.wordId = wordId;
    }

}
