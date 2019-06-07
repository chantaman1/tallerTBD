package backend.repositories;

import backend.models.ListaPalabra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListaPalabraRepository extends JpaRepository<ListaPalabra, String> {
    ListaPalabra findListaPalabraById(int id);
}
