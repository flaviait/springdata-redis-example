package de.flaviait.blog.springdata.redis;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, String> {

  @Cacheable(key = "#a0", value = "books")
  Book findOne(String isbn);

}
