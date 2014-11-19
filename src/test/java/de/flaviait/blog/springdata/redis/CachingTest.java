package de.flaviait.blog.springdata.redis;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class CachingTest {

  private static final String ISBN = "1234567890123";

  @Configuration
  static class Config {

    @Bean
    @Primary
    public CacheManager cacheManager(CacheManager cacheManager) {
      return Mockito.spy(cacheManager);
    }
  }

  @Autowired
  private CacheManager cacheManager;

  @Autowired
  private BookRepository repository;

  @Test
  public void caching() throws InterruptedException {

    final Book book = new Book(ISBN, "Sample Book", "Waldemar Biller");
    repository.save(book);

    // load book from DB
    final Book loaded = repository.findOne(ISBN);
    assertThat(loaded, not(nullValue()));

    // ensure it gets cached and the cached one
    // is the same as the one from the DB
    final Cache cache = cacheManager.getCache("books");
    assertThat(cache, not(nullValue()));
    final SimpleValueWrapper wrapper =  (SimpleValueWrapper) cache.get(ISBN);
    assertThat(wrapper.get(), equalTo(loaded));

    Thread.sleep(5000);

    // ensure the book is evicted after 5 seconds
    assertThat(cache.get(ISBN), nullValue());
  }

}
