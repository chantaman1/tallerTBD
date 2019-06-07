package backend.services;

import backend.models.ListaPalabra;
import backend.repositories.ListaPalabraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListaPalabraService {
    @Autowired
    ListaPalabraRepository listaPalabraRepository;

    public List<ListaPalabra> getAllPalabras(){
        System.out.println("ok");
        return listaPalabraRepository.findAll();
    }

    public ListaPalabra getById(int id){
        return listaPalabraRepository.findListaPalabraById(id);
    }
}