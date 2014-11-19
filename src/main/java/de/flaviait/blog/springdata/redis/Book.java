package de.flaviait.blog.springdata.redis;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

@Entity
@Table(name = "books")
public class Book implements Serializable {

  private static final long serialVersionUID = -6285119545181616629L;

  @Id
  @Column(nullable = false)
  private String isbn;

  @Column(nullable = false)
  private String title;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "book_authors",
                   joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "isbn", nullable = false))
  @Column(name = "author", nullable = false)
  private final Set<String> authors = Sets.newHashSet();

  protected Book() { }

  public Book(String isbn, String title, Set<String> authors) {

    Preconditions.checkNotNull(isbn);
    Preconditions.checkNotNull(title);
    Preconditions.checkNotNull(authors);

    Preconditions.checkArgument(isbn.length() == 13);


    this.isbn = isbn;
    this.title = title;
    this.authors.addAll(authors);
  }

  public Book(String isbn, String title, String author) {
    this(isbn, title, Sets.newHashSet(author));
  }


  public String getIsbn() {
    return isbn;
  }

  public String getTitle() {
    return title;
  }

  public Set<String> getAuthors() {
    return Sets.newHashSet(authors);
  }

  protected void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  protected void setTitle(String title) {
    this.title = title;
  }

  public void setAuthors(Set<String> authors) {
    this.authors.clear();
    this.authors.addAll(authors);
  }

  @Override
  public boolean equals(Object obj) {
    if(obj == null) return false;
    if(obj == this) return true;
    if(obj.getClass() != getClass()) return false;

    final Book other = (Book) obj;
    return Objects.equal(isbn, other.isbn);
  }
}
