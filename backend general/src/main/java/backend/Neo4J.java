package backend;

import backend.models.Artist;
import backend.models.ArtistStatistic;
import backend.models.Genre;
import backend.repositories.ArtistRepository;
import backend.repositories.ArtistStatisticRepository;
import backend.repositories.GenreRepository;
import backend.repositories.GenreStatisticRepository;
import com.mongodb.*;
import org.neo4j.driver.v1.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class Neo4J {
    private Driver driver;
    private Session session;

    @Autowired
    ArtistStatisticRepository artistStatisticRepository;
    @Autowired
    ArtistRepository artistRepository;
    @Autowired
    GenreStatisticRepository genreStatisticRepository;
    @Autowired
    GenreRepository genreRepository;

    public void connect(String url, String username, String password){
        this.driver = GraphDatabase.driver(url, AuthTokens.basic(username, password));
        this.session = driver.session();
    }

    public void disconnect(){
        this.session.close();
        this.driver.close();
    }

    public void deleteAll(){
        this.session.run("match (a)-[r]->(b) delete r");
        this.session.run("match (n) delete n");
    }

    public void crearNodosUsuario(){
        MongoClient mongoo = new MongoClient("18.220.76.177", 27017);
        DB db= mongoo.getDB("twitter");
        DBCollection col=db.getCollection("tweet");

        DBObject group = new BasicDBObject("$group", new BasicDBObject("_id", "$userScreenName")
                .append("seguidores", new BasicDBObject("$avg", "$userFollowersCount")));

        DBObject sort = new BasicDBObject("$sort", new BasicDBObject("seguidores", -1));
        DBObject limit= new BasicDBObject("$limit",100);
        AggregationOutput output = col.aggregate(group,sort,limit);
        int cantidad =output.hashCode();
        int i=0;
        for (DBObject result : output.results()) {
            System.out.println(result);
            i++;
            session.run("create (a:User {name:'"+limpiar(result.get("_id").toString())+"', followers:"+result.get("seguidores")+"})");
        }
        System.out.println("Usuarios agregados");
        mongoo.close();
    }

    public void crearNodoArtista(String nombre){
        session.run("create (a:Artista {name:'"+nombre+"'})");
        System.out.println("Se creo nodo artista");
    }

    public void crearNodosArtista(){
        List<Artist> artists = artistRepository.findAll();
        for(Artist artist : artists){
            session.run("create (a:Artista {name:'"+artist.getName()+"'})");
            System.out.println("Se creo nodo artista " + artist.getName());
        }
        System.out.println("Creado nodos de artistas.");
    }

    public void crearNodosGenero(){
        List<Genre> genres = genreRepository.findAll();
        for(Genre genre : genres){
            session.run("create (a:Genero {name:'"+genre.getGenre()+"'})");
            System.out.println("Se creo nodo genero " + genre.getGenre());
        }
        System.out.println("Creado nodos de genero.");
    }

    public void crearNodoGenero(String genero){
        session.run("create (a:Genero {name:'"+genero+"'})");
        System.out.println("Se creo nodo genero");
    }

    public void crearNodoUsuario(String usuario, Double seguidores){
        session.run("create (a:Usuario {name:'"+usuario+"', followers:"+seguidores+"})");
        System.out.println("Se creo nodo usuario");
    }

    public void crearRelacionUsuarioGenero(String genre, String usuario){
        this.session.run("MATCH (u:Usuario),(v:Genero) WHERE u.name='"+usuario+"' AND v.name='"+genre+"'"
                + " CREATE (u)-[r:TwitteaGenero]->(v)");
        System.out.println("Se crea la relacion usuario-genero");
    }

    public void crearRelacionUsuarioArtista(String artist, String usuario){
        this.session.run("MATCH (u:Usuario),(v:Artista) WHERE u.name='"+usuario+"' AND v.name='"+artist+"'"
                + " CREATE (u)-[r:TwitteaArtista]->(v)");
        System.out.println("Se crea la relacion usuario-artista");
    }



    public String limpiar(String nombre) {
        nombre = nombre.replace("'", "");
        nombre = nombre.replace("/", "");
        nombre = nombre.replace("\"", "");
        nombre = nombre.replace("_", "");
        nombre = nombre.replace("¯(ツ)¯", "");
        nombre = nombre.replace("|", "");
        nombre = nombre.replace("°", "");
        nombre = nombre.replace("¬", "");
        nombre = nombre.replace("!", "");
        nombre = nombre.replace("#", "");
        nombre = nombre.replace("$", "");
        nombre = nombre.replace("%", "");
        nombre = nombre.replace("&", "");
        nombre = nombre.replace("/", "");
        nombre = nombre.replace("(", "");
        nombre = nombre.replace(")", "");
        nombre = nombre.replace("=", "");
        nombre = nombre.replace("?", "");
        nombre = nombre.replace("\\", "");
        nombre = nombre.replace("¡", "");
        nombre = nombre.replace("¿", "");
        nombre = nombre.replace("@", "");
        nombre = nombre.replace("*", "");
        nombre = nombre.replace("+", "");
        nombre = nombre.replace("~", "");
        nombre = nombre.replace("{", "");
        nombre = nombre.replace("}", "");
        nombre = nombre.replace("[", "");
        nombre = nombre.replace("]", "");
        nombre = nombre.replace(";", "");
        nombre = nombre.replace(",", "");
        nombre = nombre.replace(":", "");
        nombre = nombre.replace(".", "");
        nombre = nombre.replace("_", "");
        nombre = nombre.replace("-", "");

        nombre = nombre.replace("AND", "(and)");
        if (nombre.equals("AND Noticias")) {
            nombre = nombre.replace("AND", "aanndd");
        }
        return nombre;
    }
}
