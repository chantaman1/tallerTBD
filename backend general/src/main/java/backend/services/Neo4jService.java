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
      return neo.obtenerUsuariosArtistaPopular();
    }

    @GetMapping("/generoPopular")
    @ResponseBody
    public List<HashMap<String, Object>> getGeneroPopular(){
      return neo.obtenerUsuariosGeneroPopular();
    }

    @GetMapping("/usuarioGenero")
    @ResponseBody
    public List<HashMap<String, Object>> getUsuarioGenero(){
      return neo.obtenerUsuarioGenero();
    }

    @GetMapping("/usuarioArtista")
    @ResponseBody
    public List<HashMap<String, Object>> getUsuarioArtista(){
      return neo.obtenerUsuarioArtista();
    }
}
