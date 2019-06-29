package backend.services;

import backend.Neo4J.Neo4J;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/neo4j")
public class Neo4jService{
    @Autowired
    Neo4J neo;

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
}
