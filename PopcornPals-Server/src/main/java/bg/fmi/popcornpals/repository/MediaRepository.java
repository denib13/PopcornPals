package bg.fmi.popcornpals.repository;

import bg.fmi.popcornpals.util.MediaType;
import bg.fmi.popcornpals.util.Genre;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import bg.fmi.popcornpals.model.Media;
import java.util.List;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long>{

    List<Media> findByTitleContainingIgnoreCase(String title);
    List<Media> findByType(MediaType type);

    List<Media> findByTypeContainingIgnoreCase(MediaType type);

    List<Media> findByGenre(Genre genre);

}
