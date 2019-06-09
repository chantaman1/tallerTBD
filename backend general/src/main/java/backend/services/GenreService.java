package backend.services;

import backend.models.Genre;

import backend.models.GenreStatistic;
import backend.repositories.GenreRepository;
import backend.repositories.GenreStatisticRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

import java.text.ParseException;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/genres")
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private GenreStatisticRepository genreStatisticRepository;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Genre getGenreById(@PathVariable Integer id) {
        return genreRepository.findGenreById(id);
    }

    @PostMapping("/create")
    @ResponseBody
    public Genre create(@RequestBody Genre resource) throws ParseException {

        return genreRepository.save(resource);
    }

    @GetMapping("/valoracion")
    @ResponseBody
    public List<GenreStatistic> getAllStatistic(){
        return genreStatisticRepository.findAll();
    }

    @GetMapping("/totalPositivoNegativo")
    @ResponseBody
    public HashMap<String, Object> getAllSentiment(){
        HashMap<String, Object> map = new HashMap<>();
        List<GenreStatistic> data = genreStatisticRepository.findAll();
        int positive = 0;
        int negative = 0;
        for(GenreStatistic genre : data){
            positive = positive + genre.getPositives();
            negative = negative + genre.getNegatives();
        }
        map.put("positive", positive);
        map.put("negative", negative);
        map.put("total", positive + negative);
        return map;
    }

    @GetMapping("/sortedGenre")
    @ResponseBody
    public HashMap<String, Integer> getSortedByAsc(){
        HashMap<String, Integer> map = new HashMap<>();
        List<GenreStatistic> data = genreStatisticRepository.findAll();
        for(GenreStatistic genre : data){
            Genre genreData = genreRepository.findGenreById(genre.getGenreId());
            map.put(genreData.getGenre(), genre.getPositives() + genre.getNegatives());
        }
        return sortByValue(map, false);
    }

    @GetMapping("/popularGenres")
    @ResponseBody
    public HashMap<String,Integer> getPopular(){
      HashMap<String, Integer> map = new HashMap<>();
      List<GenreStatistic> data = genreStatisticRepository.findAll();
      for(GenreStatistic genre : data){
          Genre genreData = genreRepository.findGenreById(genre.getGenreId());
          map.put(genreData.getGenre(), genre.getPositives() + genre.getNegatives());
      }
      HashMap<String,Integer> sorted = sortByValue(map, false);
      //Keep the top 3
      Iterator<String> iteration = sorted.keySet().iterator();
      int top = 0;
      while (iteration.hasNext()){
        String key = iteration.next();
        if (top >= 3){
          iteration.remove();
        }
        top++;
      }
      return sorted;
    }


    @GetMapping("/getGenreStadistic")
    @ResponseBody
    public List<HashMap<String, Object>> getGenreStadistic(){
        HashMap<String, Object> map = new HashMap<>();
        List<HashMap<String, Object>> result = new ArrayList<>();
        List<GenreStatistic> data = genreStatisticRepository.findAll();
        for(GenreStatistic genre : data){
            Genre genreData = genreRepository.findGenreById(genre.getGenreId());
            map.put("genre", genreData.getGenre());
            map.put("positive", genre.getPositives());
            map.put("negative", genre.getNegatives());
            map.put("total", genre.getPositives() + genre.getNegatives());
            result.add(map);
            map = new HashMap<>();
        }
        return result;
    }

    private static HashMap<String, Integer> sortByValue(Map<String, Integer> unsortMap, final boolean order)
    {
        List<Map.Entry<String, Integer>> list = new LinkedList<>(unsortMap.entrySet());

        // Sorting the list based on values
        list.sort((o1, o2) -> order ? o1.getValue().compareTo(o2.getValue()) == 0
                ? o1.getKey().compareTo(o2.getKey())
                : o1.getValue().compareTo(o2.getValue()) : o2.getValue().compareTo(o1.getValue()) == 0
                ? o2.getKey().compareTo(o1.getKey())
                : o2.getValue().compareTo(o1.getValue()));
        return list.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b, LinkedHashMap::new));

    }
}
