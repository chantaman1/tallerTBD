package backend.services;

import backend.models.Genre;

import backend.repositories.GenreRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

import java.text.ParseException;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/genres")
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;

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
}
