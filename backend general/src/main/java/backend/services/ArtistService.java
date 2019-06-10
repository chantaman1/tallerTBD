package backend.services;

import backend.models.Artist;

import backend.models.ArtistStatistic;
import backend.repositories.ArtistRepository;

import backend.repositories.ArtistStatisticRepository;
import backend.seeder.MySqlSeeder;
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

    @Autowired
    MySqlSeeder sqlSeeder;

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

    @GetMapping("/popularArtists")
    @ResponseBody
    public List<Map> getPopular(){
      HashMap<String, Integer> map = new HashMap<>();
      List<ArtistStatistic> data = artistStatisticRepository.findAll();
      for(ArtistStatistic artist : data){
          Artist artistData = artistRepository.findArtistById(artist.getArtistId());
          map.put(artistData.getName(), artist.getPositives() + artist.getNegatives());
      }
      HashMap<String,Integer> sorted = sortByValue(map, false);
      //Keep the top 3
      Iterator<String> iteration = sorted.keySet().iterator();
      int top = 0;
      while (iteration.hasNext()){
        String key = iteration.next();
        if (top >= 5){
          iteration.remove();
        }
        top++;
      }

      List<Map> list = new ArrayList<>();
      Iterator it = sorted.entrySet().iterator();
      while(it.hasNext()){
        HashMap<String,Object> out = new HashMap<>();
        Map.Entry pair = (Map.Entry)it.next();
        out.put("artista",pair.getKey());
        out.put("total",pair.getValue());
        list.add(out);
        //System.out.println(pair.getKey() + "=" + pair.getValue());
      }
      return list;
    }

    @GetMapping("/obtenerPorFecha")
    @ResponseBody
    public List<HashMap<String, Object>> getArtistByDate(/*@RequestBody Map<String, Object> jsonData*/){
        List<Artist> artists = artistRepository.findAll();
        //String startDate = transformDate(jsonData.get("fechaInicio").toString());
        //String endDate = transformDate(jsonData.get("fechaTermino").toString());
        List<HashMap<String, Object>> result = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();

        for(Artist artist : artists){
            map.put("artist", artist.getName());
            map.put("positive", sqlSeeder.dateSeed("positive", artist.getName(), "20190520", "*"));
            map.put("negative", sqlSeeder.dateSeed("negative", artist.getName(), "20190520", "*"));
            map.put("startDate", "20190520");
            map.put("endDate", "20190610");
            result.add(map);
            map = new HashMap<>();
        }
        return result;
    }

    private String transformDate(String date){
        if(date != null){
            String[] temp = date.split("/");
            String out = "";
            for(int i = 0; i < temp.length; i++){
                out = out + temp[i];
            }
            return out;
        }
        else{
            return date;
        }
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
