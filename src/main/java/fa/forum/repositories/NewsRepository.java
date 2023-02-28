package fa.forum.repositories;

import fa.forum.models.NewsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<NewsModel, String> {
    List<NewsModel> findAllByOrderByAddedDateDesc();
}
