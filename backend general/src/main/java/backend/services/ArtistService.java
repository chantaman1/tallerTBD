package backend.services;

import backend.models.Artist;

import backend.models.ArtistStatistic;
import backend.repositories.ArtistRepository;

import backend.repositories.ArtistStatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

import java.text.ParseException;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/artists")
public class ArtistService {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ArtistStatisticRepository artistStatisticRepository;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Artist getArtistById(@PathVariable Integer id) {
        return artistRepository.findArtistById(id);
    }

    @PostMapping("/create")
    @ResponseBody
    public Artist create(@RequestBody Artist resource) throws ParseException {

        return artistRepository.save(resource);
    }

    @GetMapping("/valoracion")
    @ResponseBody
    public List<ArtistStatistic> getAllStatistic(){
        return artistStatisticRepository.findAll();
    }

    @GetMapping("/totalPositivoNegativo")
    @ResponseBody
    public HashMap<String, Object> getAllSentiment(){
        HashMap<String, Object> map = new HashMap<>();
        List<ArtistStatistic> data = artistStatisticRepository.findAll();
        int positive = 0;
        int negative = 0;
        for(ArtistStatistic artista : data){
            positive = positive + artista.getPositives();
            negative = negative + artista.getNegatives();
        }
        map.put("positive", positive);
        map.put("negative", negative);
        map.put("total", positive + negative);
        return map;
    }

    @GetMapping("/sortedArtist")
    @ResponseBody
    public HashMap<String, Integer> getSortedByAsc(){
        HashMap<String, Integer> map = new HashMap<>();
        List<ArtistStatistic> data = artistStatisticRepository.findAll();
        for(ArtistStatistic artist : data){
            Artist artistData = artistRepository.findArtistById(artist.getArtistId());
            map.put(artistData.getName(), artist.getPositives() + artist.getNegatives());
        }
        return sortByValue(map, false);
    }

    @GetMapping("/getArtistStadistic")
    @ResponseBody
    public List<HashMap<String, Object>> getArtistStadistic(){
        HashMap<String, Object> map = new HashMap<>();
        List<HashMap<String, Object>> result = new ArrayList<>();
        List<ArtistStatistic> data = artistStatisticRepository.findAll();
        for(ArtistStatistic artist : data){
            Artist artistData = artistRepository.findArtistById(artist.getArtistId());
            map.put("artist", artistData.getName());
            map.put("positive", artist.getPositives());
            map.put("negative", artist.getNegatives());
            map.put("total", artist.getPositives() + artist.getNegatives());
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
