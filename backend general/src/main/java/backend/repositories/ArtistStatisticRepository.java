package backend.repositories;

import backend.models.ArtistStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistStatisticRepository extends JpaRepository<ArtistStatistic, Integer> {
    ArtistStatistic findArtistStatisticById(Integer id);
}

