package kz.iitu.springlab.catalog;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class BookService {
    private final BookRepository repository;
    public BookService(BookRepository repository) { this.repository = repository; }

    public List<Book> findAll(String author) {
        return repository.findAll().stream()
                .filter(b -> author == null || b.author().toLowerCase().contains(author.toLowerCase()))
                .sorted(Comparator.comparing(Book::id)).toList();
    }
    public Optional<Book> findById(long id) { return repository.findById(id); }
    public Book create(Book b) { return repository.save(new Book(null, b.title(), b.author(), b.year())); }
    public Optional<Book> update(long id, Book b) {
        return repository.findById(id).map(e -> repository.save(new Book(id, b.title(), b.author(), b.year())));
    }
    public boolean delete(long id) { return repository.deleteById(id); }

    public BookStats stats() {
        List<Book> all = repository.findAll();
        Integer min = all.stream().map(Book::year).min(Integer::compare).orElse(null);
        Integer max = all.stream().map(Book::year).max(Integer::compare).orElse(null);
        return new BookStats(all.size(), min, max);
    }
}