package backend.services;

import backend.models.Artist;

import backend.repositories.ArtistRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

import java.text.ParseException;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/artists")
public class ArtistService {

    @Autowired
    private ArtistRepository artistRepository;

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
}
