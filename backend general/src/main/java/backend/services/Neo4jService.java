package backend.services;

import backend.Neo4J.Neo4J;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import backend.models.Artist;
import backend.models.ArtistStatistic;
import backend.repositories.ArtistRepository;
import backend.repositories.ArtistStatisticRepository;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

@CrossOrigin
@RestController
@RequestMapping("/neo4j")
public class Neo4jService{
    @Autowired
    Neo4J neo;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ArtistStatisticRepository artistStatisticRepository;

    @GetMapping("/artistaPopular")
    @ResponseBody
    public List<HashMap<String, Object>> getArtistaPopular(){
        neo.connect();
        List<HashMap<String, Object>> result = neo.obtenerUsuariosArtistaPopular();
        neo.disconnect();
        return result;
    }

    @GetMapping("/generoPopular")
    @ResponseBody
    public List<HashMap<String, Object>> getGeneroPopular(){
        neo.connect();
        List<HashMap<String, Object>> result = neo.obtenerUsuariosGeneroPopular();
        neo.disconnect();
        return result;
    }

    @GetMapping("/usuarioGenero")
    @ResponseBody
    public List<HashMap<String, Object>> getUsuarioGenero(){
        neo.connect();
        List<HashMap<String, Object>> result = neo.obtenerUsuarioGenero();
        neo.disconnect();
        return result;
    }

    @GetMapping("/usuarioArtista")
    @ResponseBody
    public List<HashMap<String, Object>> getUsuarioArtista(){
        neo.connect();
        List<HashMap<String, Object>> result = neo.obtenerUsuarioArtista();
        neo.disconnect();
        return result;
    }

    @GetMapping("/artistasGenero")
    @ResponseBody
    public List<HashMap<String, Object>> getArtistGenre(){
        HashMap<String, Object> map = new HashMap<>();
        List<HashMap<String, Object>> result = new ArrayList<>();
        List<ArtistStatistic> data = artistStatisticRepository.findAll();
        for(ArtistStatistic artist : data){
            Artist artistData = artistRepository.findArtistById(artist.getArtistId());
            map.put("artist", artistData.getName());
            map.put("genre", artistData.getGenre());
            map.put("total", artist.getPositives() + artist.getNegatives());
            result.add(map);
            map = new HashMap<>();
        }
        return result;
    }
}
