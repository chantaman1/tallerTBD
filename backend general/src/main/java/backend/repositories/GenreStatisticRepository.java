package backend.repositories;
import backend.models.GenreStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreStatisticRepository extends JpaRepository<GenreStatistic, Integer> {
    GenreStatistic findGenreStatisticById(Integer id);
}

