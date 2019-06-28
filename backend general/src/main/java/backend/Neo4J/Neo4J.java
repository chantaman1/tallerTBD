package backend.Neo4J;

import backend.models.Artist;
import backend.models.Genre;
import backend.repositories.ArtistRepository;
import backend.repositories.ArtistStatisticRepository;
import backend.repositories.GenreRepository;
import backend.repositories.GenreStatisticRepository;
import com.mongodb.*;
import com.mongodb.client.MongoClients;
import org.neo4j.driver.v1.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
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

    public void connect(){
        String url = "bolt://134.209.166.153:7687";
        String username = "neo4j";
        String password = "tallertbd";
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
        System.out.println("Connecting to mongo.");
        MongoClient mongoo = new MongoClient("18.220.76.177", 27017);
        DB db= mongoo.getDB("twitter");
        DBCollection col=db.getCollection("tweet");
        System.out.println("Connected.");
        DBObject group = new BasicDBObject("$group", new BasicDBObject("_id", "$userName")
                .append("seguidores", new BasicDBObject("$avg", "$followersCount")));
        System.out.println("Creating group of items.");
        DBObject sort = new BasicDBObject("$sort", new BasicDBObject("seguidores", -1));
        DBObject limit= new BasicDBObject("$limit",300);
        System.out.println("Sorting items.");
        AggregationOutput output = col.aggregate(group,sort,limit);
        int cantidad =output.hashCode();
        int i=0;
        System.out.println("Starting proccess.");
        for (DBObject result : output.results()) {
            long followers = convertToLong(result.get("seguidores").toString());
            if(followers > 10){
                System.out.println(result);
                i++;
                session.run("create (a:Usuario {name:'"+limpiar(result.get("_id").toString())+"', followers:"+followers+"})");
            }
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

    public void crearNodoUsuario(String usuario, Long seguidores){
        this.session.run("create (a:Usuario {name:'"+usuario+"', followers:"+seguidores+"})");
        System.out.println("Se creo nodo usuario");
    }

    public void crearRelacionUsuarioGenero(String genre, String usuario, int peso){
        this.session.run("MATCH (u:Usuario),(v:Genero) WHERE u.name='"+usuario+"' AND v.name='"+genre+"'"
                + " CREATE (u)-[r:TwitteaGenero {weight:"+peso+"}]->(v)");
    }

    public void crearRelacionUsuarioArtista(String artist, String usuario, int peso){
        this.session.run("MATCH (u:Usuario),(v:Artista) WHERE u.name='"+usuario+"' AND v.name='"+artist+"'"
                + " CREATE (u)-[r:TwitteaArtista {weight:"+peso+"}]->(v)");
    }

    public StatementResult obtenerUsuarios(){
       return this.session.run("match (n:Usuario) RETURN n LIMIT 400");
    }

    public boolean existeUsuario(String usuario){
        StatementResult res = this.session.run("match (n:Usuario) WHERE n.name='"+usuario+"' RETURN n");
        List<Record> listaUsuarios = res.list();
        if(listaUsuarios.size() > 0){
            return true;
        }
        else{
            return false;
        }
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

    public Long convertToLong(String o){
        String[] split = o.split("\\.");
        if(split.length > 0){
            Long convertedLong = Long.parseLong(split[0]);
            return convertedLong;
        }
        else{
            return 0L;
        }
    }
}
