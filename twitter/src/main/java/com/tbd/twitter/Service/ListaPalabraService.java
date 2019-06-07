package com.tbd.twitter.Service;

import com.tbd.twitter.Models.ListaPalabra;
import com.tbd.twitter.Repository.ListaPalabraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListaPalabraService {
    @Autowired
    ListaPalabraRepository listaPalabraRepository;

    public List<ListaPalabra> getAllPalabras(){
        return listaPalabraRepository.findAll();
    }

    public ListaPalabra getById(int id){
        return listaPalabraRepository.findListaPalabraById(id);
    }
}
