package backend.seeder;

import backend.Elastic;
import backend.Neo4J.Neo4J;
import backend.models.Artist;
import backend.models.ArtistStatistic;
import backend.models.Genre;
import backend.models.GenreStatistic;
import backend.repositories.ArtistRepository;
import backend.repositories.ArtistStatisticRepository;
import backend.repositories.GenreRepository;
import backend.repositories.GenreStatisticRepository;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.StatementResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MySqlSeeder{
	private Elastic e = new Elastic();

	@Autowired
	private ArtistRepository artistRepository;

	@Autowired
	private ArtistStatisticRepository artistStatisticRepository;

	@Autowired
	private GenreRepository genreRepository;

	@Autowired
	private GenreStatisticRepository genreStatisticRepository;

	@Autowired
	private Neo4J neo4J;

	public void test(){
		neo4J.connect();
		neo4J.disconnect();
	}
	
	/*public void testeoQueryCLI(){
		neo4J.connect();

		System.out.println("------------------QUERY BÚSQUEDA------------------");
		Map<String, Integer> resultados = new HashMap<String, Integer>();
		List<Record> records = neo4J.testeoQuery2().list();
		for (Record r : records){
			Integer j = resultados.get(r.get(0).get("name").asString());
			resultados.put(r.get(0).get("name").asString(), (j == null) ? 1 : j+1);
		}

        for (Map.Entry<String, Integer> val : resultados.entrySet()) { 
            System.out.println("Element " + val.getKey() + " "
                               + "occurs"
                               + ": " + val.getValue() + " times"); 
        }

        System.out.println("Cantidad que aparece el rock: " + neo4J.countGenreWeigth("rock").list().get(0).get("COUNT(r)"));
        System.out.println("Cantidad que aparece el pop: " + neo4J.countGenreWeigth("pop").list().get(0).get("COUNT(r)"));

		neo4J.disconnect();
	}*/

	public void createUserNodes(){
		neo4J.connect();
		neo4J.crearNodosUsuario();
		neo4J.disconnect();
	}

	public void feedNeo4JNodes(){
		neo4J.connect();
		neo4J.crearNodosArtista();
		neo4J.crearNodosGenero();
		neo4J.disconnect();
	}

	public void crearRelacionUserArtist(){
		List<Artist> artists = artistRepository.findAll();
		for(Artist artista : artists){
			System.out.println("Procesando: " + artista.getName());
			List<HashMap<String, Object>> usuarios = e.getUsersAndFollowers(artista.getName());
			neo4J.connect();
			for(HashMap user : usuarios){
				if(neo4J.existeUsuario(user.get("userName").toString())){
					int totalHits = e.getByGenreAndUser(artista.getName(), user.get("userName").toString());
					neo4J.crearRelacionUsuarioArtista(artista.getName(), user.get("userName").toString(), totalHits);
				}
				else{
					neo4J.crearNodoUsuario(user.get("userName").toString(), Long.parseLong(user.get("followers").toString()));
					int totalHits = e.getByGenreAndUser(artista.getName(), user.get("userName").toString());
					neo4J.crearRelacionUsuarioArtista(artista.getName(), user.get("userName").toString(), totalHits);
				}
			}
			neo4J.disconnect();
		}
		System.out.println("Terminado.");
	}

	public void crearRelacionUserGenre(){
		List<Genre> genres = genreRepository.findAll();
		for(Genre genero : genres){
			System.out.println("Procesando: " + genero.getGenre());
			List<HashMap<String, Object>> usuarios = e.getUsersAndFollowers(genero.getGenre());
			neo4J.connect();
			for(HashMap user : usuarios){
				if(neo4J.existeUsuario(user.get("userName").toString())){
					int totalHits = e.getByGenreAndUser(genero.getGenre(), user.get("userName").toString());
					neo4J.crearRelacionUsuarioGenero(genero.getGenre(), user.get("userName").toString(), totalHits);
				}
				else{
					neo4J.crearNodoUsuario(user.get("userName").toString(), Long.parseLong(user.get("followers").toString()));
					int totalHits = e.getByGenreAndUser(genero.getGenre(), user.get("userName").toString());
					neo4J.crearRelacionUsuarioGenero(genero.getGenre(), user.get("userName").toString(), totalHits);
				}
			}
			neo4J.disconnect();
		}
		System.out.println("Terminado.");
	}

	public void createRelationsUserArtist(){
		neo4J.connect();
		List<Artist> artists = artistRepository.findAll();
		StatementResult users = neo4J.obtenerUsuarios();
		List<Record> listaUsuarios = users.list();
		int i = 0;
		for(Artist artista : artists){
			for(Record usuario : listaUsuarios){
				int totalMatch = e.getByGenreAndUser(artista.getName(), usuario.get(0).get("name").asString());
				if(totalMatch > 0){
					neo4J.crearRelacionUsuarioArtista(artista.getName(), usuario.get(0).get("name").asString(), totalMatch);
					System.out.println("Creada una relacion usuario-artista: " + i);
					i++;
				}
			}
		}
		neo4J.disconnect();
	}

	public void createRelationsUserGenre(){
		neo4J.connect();
		List<Genre> genres = genreRepository.findAll();
		StatementResult users = neo4J.obtenerUsuarios();
		List<Record> listaUsuarios = users.list();
		int i = 0;
		for(Genre genre : genres){
			for(Record usuario : listaUsuarios){
				int totalMatch = e.getByGenreAndUser(genre.getGenre(), usuario.get(0).get("name").asString());
				if(totalMatch > 0){
					neo4J.crearRelacionUsuarioGenero(genre.getGenre(), usuario.get(0).get("name").asString(), totalMatch);
					System.out.println("Creada una relacion usuario-genero: " + i);
					i++;
				}
			}
		}
		neo4J.disconnect();
	}

	public void seedArtistsStatistic(){
		List<Artist> artists = artistRepository.findAll();

		for (Artist artist : artists){
			System.out.println("Artista: " + artist.getName().toLowerCase());
			System.out.println("Positivos: " + e.getGenreAndSentiment("positive", artist.getName().toLowerCase()));
			System.out.println("Negativos: " + e.getGenreAndSentiment("negative", artist.getName().toLowerCase()));

			ArtistStatistic statistic = artistStatisticRepository.findArtistStatisticById(artist.getId());
			if(statistic != null){
				statistic.setPositives(e.getGenreAndSentiment("positive", artist.getName().toLowerCase()));
				statistic.setNegatives(e.getGenreAndSentiment("negative", artist.getName().toLowerCase()));
				statistic.setArtistId(artist.getId());
				artistStatisticRepository.save(statistic);
			}
			else{
				ArtistStatistic stats = new ArtistStatistic();
				stats.setPositives(e.getGenreAndSentiment("positive", artist.getName().toLowerCase()));
				stats.setNegatives(e.getGenreAndSentiment("negative", artist.getName().toLowerCase()));
				stats.setArtistId(artist.getId());
				artistStatisticRepository.save(stats);
			}
		}
	}

	public void seedGenresStatistic(){
		List<Genre> genres = genreRepository.findAll();

		for (Genre genre : genres){
			System.out.println("Género: " + genre.getGenre().toLowerCase());
			System.out.println("Positivos: " + e.getGenreAndSentiment("positive", genre.getGenre().toLowerCase()));
			System.out.println("Negativos: " + e.getGenreAndSentiment("negative", genre.getGenre().toLowerCase()));

			GenreStatistic statistic = genreStatisticRepository.findGenreStatisticById(genre.getId());
			if(statistic != null){
				statistic.setPositives(e.getGenreAndSentiment("positive", genre.getGenre().toLowerCase()));
				statistic.setNegatives(e.getGenreAndSentiment("negative", genre.getGenre().toLowerCase()));
				statistic.setGenreId(genre.getId());
				genreStatisticRepository.save(statistic);
			}
			else{
				GenreStatistic stats = new GenreStatistic();
				stats.setPositives(e.getGenreAndSentiment("positive", genre.getGenre().toLowerCase()));
				stats.setNegatives(e.getGenreAndSentiment("negative", genre.getGenre().toLowerCase()));
				stats.setGenreId(genre.getId());
				genreStatisticRepository.save(stats);
			}
		}
	}

	public int dateSeed(String sentiment, String genre, String startDate, String endDate){
		return e.getGenreAndSentimentBetweenDates(sentiment, genre, startDate, endDate);
	}

	public int findByDate(String sentiment, String genre, String date){
		return e.getGenreAndSentimentByDate(sentiment, genre, date);
	}
}