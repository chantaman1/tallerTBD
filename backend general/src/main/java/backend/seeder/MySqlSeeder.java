package backend.seeder;

import backend.Elastic;

import backend.models.Artist;
import backend.models.ArtistStatistic;
import backend.models.Genre;
import backend.models.GenreStatistic;

import backend.repositories.ArtistRepository;
import backend.repositories.ArtistStatisticRepository;
import backend.repositories.GenreRepository;
import backend.repositories.GenreStatisticRepository;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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