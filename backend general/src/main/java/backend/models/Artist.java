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
    @Column(name = "info", nullable = false, length = 500)
    private String info;
    @Column(name = "genre_id", nullable = false)
    private Integer genreId;
    @Column(name = "picture", nullable = false, length = 200)
    private String picture;



    public Artist(Integer id,
                  Integer wordId,
                  String  name,
                  String info,
                  Integer genreId,
                  String picture){
        this.id = id;
        this.name = name;
        this.wordId = wordId;
        this.info = info;
        this.genreId = genreId;
        this.picture = picture;
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

    public Integer getGenreId(){
      return this.genreId;
    }

    public String getGenre() {
      if(genreId == 1){
        return "rock";
      }
      if(genreId == 2){
        return "pop";
      }
      if(genreId == 3){
        return "hip-hop";
      }
      if(genreId == 4){
        return "electronica";
      }
      else if(genreId == 5){
        return "reggaeton";
      }
      else if(genreId == 6){
        return "cumbia";
      }
      else if(genreId == 7){
        return "trap";
      }
      else if(genreId == 8){
        return "metal";
      }
      else if(genreId == 9){
        return "dubstep";
      }
      else if(genreId == 10){
        return "techno";
      }
      else{
        return "rap";
      }
        //return this.genreId;
    }

    public void setGenreId(Integer genreId) {
        this.genreId = genreId;
    }

    public String getInfo() {
        return this.info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPicture() {
        return this.picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

}
