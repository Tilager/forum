package ru.fa.forum.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fa.forum.models.NewsModel;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<NewsModel, Integer> {
    List<NewsModel> findAllByOrderByAddedDateDesc();
}
