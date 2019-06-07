package com.tbd.twitter.Repository;

import com.tbd.twitter.Models.ListaPalabra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListaPalabraRepository extends JpaRepository<ListaPalabra, String> {
    ListaPalabra findListaPalabraById(int id);
}
