package backend.Neo4J;

import backend.models.Artist;
import backend.models.Genre;
import backend.models.ArtistStatistic;
import backend.repositories.ArtistRepository;
import backend.repositories.ArtistStatisticRepository;
import backend.repositories.GenreRepository;
import backend.repositories.GenreStatisticRepository;
import com.mongodb.*;
import com.mongodb.client.MongoClients;
import org.neo4j.driver.v1.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.plaf.nimbus.State;
import java.util.ArrayList;
import java.util.HashMap;
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

    public List<HashMap<String, Object>> obtenerUsuarioArtista(){
        List<Artist> artistas = artistRepository.findAll();
        List<HashMap<String, Object>> resultData = new ArrayList<>();
        HashMap<String, Object> data = new HashMap<>();
        List<HashMap<String, Object>> childrens = new ArrayList<>();
        HashMap<String, Object> childrenData = new HashMap<>();
        for(Artist artista : artistas){
            StatementResult result = this.session.run("match (n:Usuario)-[r:TwitteaArtista]-(v:Artista) WHERE v.name='"+artista.getName()+"' RETURN n,v,r ORDER BY r.weight DESC LIMIT 3");
            StatementResult totalArtista = this.session.run("match (n:Usuario)-[r:TwitteaArtista]-(v:Artista) WHERE v.name='"+artista.getName()+"' RETURN count(r.weight)");
            int total = totalArtista.single().get(0).asInt();
            data.put("name", artista.getName());
            data.put("value", total);
            List<Record> records = result.list();
            for(Record record : records){
                childrenData.put("name", record.get(0).get("name").asString());
                childrenData.put("followers", record.get(0).get("followers").asInt());
                childrenData.put("value", record.get(2).get("weight").asInt());
                childrens.add(childrenData);
                childrenData = new HashMap<>();
            }
            List<Object> linkWith = new ArrayList<>();
            if(artista.getName().equals("Slayer") || artista.getName().equals("Queen")){
                linkWith.add("xataka");
            }
            data.put("linkWith", linkWith);
            data.put("children", childrens);
            resultData.add(data);
            childrens = new ArrayList<>();
            data = new HashMap<>();
        }
        return resultData;
    }

    public List<HashMap<String, Object>> obtenerUsuarioGenero(){
        List<Genre> genres = genreRepository.findAll();
        List<HashMap<String, Object>> resultData = new ArrayList<>();
        HashMap<String, Object> data = new HashMap<>();
        List<HashMap<String, Object>> childrens = new ArrayList<>();
        HashMap<String, Object> childrenData = new HashMap<>();
        for(Genre genre : genres){
            StatementResult result = this.session.run("match (n:Usuario)-[r:TwitteaGenero]-(v:Genero) WHERE v.name='"+genre.getGenre()+"' RETURN n,v,r ORDER BY r.weight DESC LIMIT 3");
            StatementResult totalArtista = this.session.run("match (n:Usuario)-[r:TwitteaGenero]-(v:Genero) WHERE v.name='"+genre.getGenre()+"' RETURN count(r.weight)");
            int total = totalArtista.single().get(0).asInt();
            data.put("name", genre.getGenre());
            data.put("value", total);
            List<Record> records = result.list();
            for(Record record : records){
                childrenData.put("name", record.get(0).get("name").asString());
                childrenData.put("followers", record.get(0).get("followers").asInt());
                childrenData.put("value", record.get(2).get("weight").asInt());
                childrens.add(childrenData);
                childrenData = new HashMap<>();
            }
            List<Object> linkWith = new ArrayList<>();
            data.put("linkWith", linkWith);
            data.put("children", childrens);
            resultData.add(data);
            childrens = new ArrayList<>();
            data = new HashMap<>();
        }
        return resultData;
    }

    public List<HashMap<String, Object>> obtenerUsuariosArtistaPopular(){
        StatementResult mostPopular = this.session.run("MATCH (a:Usuario)-[r:TwitteaArtista]->(b:Artista) RETURN b, count(r.weight) AS weight ORDER BY weight DESC LIMIT 1");
        List<HashMap<String, Object>> resultData = new ArrayList<>();
        HashMap<String, Object> data = new HashMap<>();
        List<HashMap<String, Object>> childrens = new ArrayList<>();
        HashMap<String, Object> childrenData = new HashMap<>();
        Record record = mostPopular.list().get(0);
        String artista = record.get(0).get("name").asString();
        int peso = record.get("weight").asInt();
        data.put("name", artista);
        data.put("value", peso);
        StatementResult dataUsuarios = this.session.run("match (n:Usuario)-[r:TwitteaArtista]-(v:Artista) WHERE v.name='"+artista+"' RETURN n,r ORDER BY r.weight DESC LIMIT 100");
        List<Record> usuarios = dataUsuarios.list();
        for(Record usuario : usuarios){
            childrenData.put("name", usuario.get(0).get("name").asString());
            childrenData.put("followers", usuario.get(0).get("followers").asInt());
            childrenData.put("value", usuario.get(1).get("weight").asInt());
            childrens.add(childrenData);
            childrenData = new HashMap<>();
        }
        List<Object> linkWith = new ArrayList<>();
        data.put("linkWith", linkWith);
        data.put("children", childrens);
        resultData.add(data);
        return resultData;
    }

    public List<HashMap<String, Object>> obtenerUsuariosGeneroPopular(){
        StatementResult mostPopular = this.session.run("MATCH (a:Usuario)-[r:TwitteaGenero]->(b:Genero) RETURN b, count(r.weight) AS weight ORDER BY weight DESC LIMIT 1");
        List<HashMap<String, Object>> resultData = new ArrayList<>();
        HashMap<String, Object> data = new HashMap<>();
        List<HashMap<String, Object>> childrens = new ArrayList<>();
        HashMap<String, Object> childrenData = new HashMap<>();
        Record record = mostPopular.list().get(0);
        String genero = record.get(0).get("name").asString();
        int peso = record.get("weight").asInt();
        data.put("name", genero);
        data.put("value", peso);
        StatementResult dataUsuarios = this.session.run("match (n:Usuario)-[r:TwitteaGenero]-(v:Genero) WHERE v.name='"+genero+"' RETURN n,r ORDER BY r.weight DESC LIMIT 100");
        List<Record> usuarios = dataUsuarios.list();
        for(Record usuario : usuarios){
            childrenData.put("name", usuario.get(0).get("name").asString());
            childrenData.put("followers", usuario.get(0).get("followers").asInt());
            childrenData.put("value", usuario.get(1).get("weight").asInt());
            childrens.add(childrenData);
            childrenData = new HashMap<>();
        }
        List<Object> linkWith = new ArrayList<>();
        data.put("linkWith", linkWith);
        data.put("children", childrens);
        resultData.add(data);
        return resultData;
    }

    public List<HashMap<String, Object>> obtenerArtistasGenero(){
        List<Genre> genres = genreRepository.findAll();
        List<Artist> artistas = artistRepository.findAll();
        List<HashMap<String, Object>> resultData = new ArrayList<>();
        HashMap<String, Object> data = new HashMap<>();
        List<HashMap<String, Object>> childrens = new ArrayList<>();
        HashMap<String, Object> childrenData = new HashMap<>();
        for(Genre genre : genres){
            StatementResult result = this.session.run("match (n:Usuario)-[r:TwitteaGenero]-(v:Genero) WHERE v.name='"+genre.getGenre()+"' RETURN n,v,r ORDER BY r.weight DESC LIMIT 3");
            //StatementResult totalArtista = this.session.run("match (n:Usuario)-[r:TwitteaArtista]-(v:Artista) WHERE v.name='"+artistas.getName()+"' RETURN count(r.weight)");
            //String artista = record.get(0).get("name").asString();
            StatementResult totalGenero = this.session.run("match (n:Usuario)-[r:TwitteaGenero]-(v:Genero) WHERE v.name='"+genre.getGenre()+"' RETURN count(r.weight)");
            //int totalA = totalArtista.single().get(0).asInt();
            int totalG = totalGenero.single().get(0).asInt();
            data.put("name", genre.getGenre());
            data.put("value", totalG);
            List<ArtistStatistic> x = artistStatisticRepository.findAll();
            for(ArtistStatistic artist : x){
                Artist artistData = artistRepository.findArtistById(artist.getArtistId());
                if(artistData.getGenre().equals(genre.getGenre())){
                  childrenData.put("name", artistData.getName());
                  //childrenData.put("commentsA", totalA);
                  childrens.add(childrenData);
                  childrenData = new HashMap<>();
                }
            }
            List<Object> linkWith = new ArrayList<>();
            data.put("linkWith", linkWith);
            data.put("children", childrens);
            resultData.add(data);
            childrens = new ArrayList<>();
            data = new HashMap<>();
        }
        return resultData;
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

    public StatementResult countGenreInstances(String genre){
        return this.session.run("MATCH (v:Genero)<-[r:TwitteaGenero]-(u:Usuario) WHERE v.name='" + genre + "' RETURN COUNT(r)");
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

    public void linkUsersArtist(){
        List<HashMap<String, List<String>>> result = new ArrayList<>();
        List<String> artistName = new ArrayList<>();
        List<String> singleName = new ArrayList<>();
        List<String> usersName = new ArrayList<>();
        HashMap<String, List<String>> map = new HashMap<>();
        List<Artist> artists = artistRepository.findAll();
        for(Artist artist : artists){
            StatementResult data = this.session.run("match (n:Usuario)-[r:TwitteaArtista]-(v:Artista) WHERE v.name='"+artist.getName()+"' RETURN n ORDER BY r.weight DESC LIMIT 3");
            for(Record record : data.list()){
                StatementResult usersRelationship = this.session.run("match (n:Usuario)-[r:TwitteaArtista]-(v:Artista) WHERE n.name='"+ record.get(0).get("name").asString() +"' RETURN v");
                List<Record> usersRecord = usersRelationship.list();
                if(usersName.indexOf(record.get(0).get("name").asString()) == -1){
                    if(usersRecord.size() > 1){
                        usersName.add(record.get(0).get("name").asString());
                        singleName.add(record.get(0).get("name").asString());
                        for(Record artista : usersRecord){
                            artistName.add(artista.get(0).get("name").asString());
                        }
                    }
                }
                map.put("username", singleName);
                map.put("artists", artistName);
                artistName = new ArrayList<>();
                singleName = new ArrayList<>();
                result.add(map);
            }
        }
        for(HashMap<String, List<String>> dato : result){
            System.out.println("Usuario:" + dato.get("username").get(0) + " posee " + dato.get("artists").size() + "artistas.");

        }
    }

    public void linkUsersGenre() {
        List<HashMap<String, List<String>>> result = new ArrayList<>();
        List<String> artistName = new ArrayList<>();
        List<String> singleName = new ArrayList<>();
        List<String> usersName = new ArrayList<>();
        HashMap<String, List<String>> map = new HashMap<>();
        List<Genre> artists = genreRepository.findAll();
        for (Genre artist : artists) {
            StatementResult data = this.session.run("match (n:Usuario)-[r:TwitteaGenero]-(v:Genero) WHERE v.name='" + artist.getGenre() + "' RETURN n ORDER BY r.weight DESC LIMIT 3");
            for (Record record : data.list()) {
                StatementResult usersRelationship = this.session.run("match (n:Usuario)-[r:TwitteaGenero]-(v:Genero) WHERE n.name='" + record.get(0).get("name").asString() + "' RETURN v");
                List<Record> usersRecord = usersRelationship.list();
                if (usersName.indexOf(record.get(0).get("name").asString()) == -1) {
                    if (usersRecord.size() > 1) {
                        usersName.add(record.get(0).get("name").asString());
                        singleName.add(record.get(0).get("name").asString());
                        for (Record artista : usersRecord) {
                            artistName.add(artista.get(0).get("name").asString());
                        }
                    }
                }
                map.put("username", singleName);
                map.put("artists", artistName);
                artistName = new ArrayList<>();
                singleName = new ArrayList<>();
                result.add(map);
            }
        }
        for (HashMap<String, List<String>> dato : result) {
            //System.out.println("Usuario:" + dato.get("username").get(0) + " posee " + dato.get("artists").size() + "generos.");
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
