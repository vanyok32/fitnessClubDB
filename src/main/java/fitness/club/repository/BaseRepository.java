package fitness.club.repository;

import fitness.club.entity.BaseEntity;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<T, Integer> {
     void delete(Integer id);
     T update(T entity);
     T add(T entity);
     Optional<T> findById(Integer id);
     List<T> findAll();
}
