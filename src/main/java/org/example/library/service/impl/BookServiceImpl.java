package org.example.library.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.library.config.CacheConfig;
import org.example.library.domain.Author;
import org.example.library.domain.Book;
import org.example.library.domain.Genre;
import org.example.library.dto.request.CreateBookRequest;
import org.example.library.dto.response.BookDto;
import org.example.library.exception.BusinessException;
import org.example.library.exception.EntityNotFoundException;
import org.example.library.repository.AuthorRepository;
import org.example.library.repository.BookRepository;
import org.example.library.repository.GenreRepository;
import org.example.library.service.BookService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    @Override
    @Cacheable(cacheNames = CacheConfig.BOOKS_CACHE, key = "'all'")
    public List<BookDto> getAllBooksDetailed() {
        return bookRepository.findAllWithAuthorsAndGenres()
                .stream()
                .map(BookDto::from)
                .toList();
    }

    @Override
    public BookDto getBookById(Long id) {
        Book book = bookRepository.findByIdWithAuthorsAndGenres(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found: " + id));
        return BookDto.from(book);
    }

    @Override
    @Transactional
 //@CacheEvict(cacheNames = CacheConfig.BOOKS_CACHE, allEntries = true)
    public BookDto createBook(CreateBookRequest req) {
        if (req.isbn() != null && bookRepository.existsByIsbn(req.isbn())) {
            throw new BusinessException("Book with ISBN already exists: " + req.isbn());
        }

        Book book = new Book();
        book.setTitle(req.title());
        book.setIsbn(req.isbn());
        book.setPublicationYear(req.publicationYear());
        book.setPublisher(req.publisher());
        book.setTotalCopies(req.totalCopies());
        book.setAvailableCopies(req.totalCopies());

        if (req.authorIds() != null && !req.authorIds().isEmpty()) {
            Set<Author> authors = new HashSet<>(authorRepository.findAllById(req.authorIds()));
            if (authors.size() != req.authorIds().size()) {
                throw new EntityNotFoundException("Some authors not found");
            }
            book.setAuthors(authors);
        }

        if (req.genreIds() != null && !req.genreIds().isEmpty()) {
            Set<Genre> genres = new HashSet<>(genreRepository.findAllById(req.genreIds()));
            if (genres.size() != req.genreIds().size()) {
                throw new EntityNotFoundException("Some genres not found");
            }
            book.setGenres(genres);
        }

        Book saved = bookRepository.save(book);
        // перезагружаем с fetch, чтобы DTO собрался без N+1
        return BookDto.from(bookRepository.findByIdWithAuthorsAndGenres(saved.getId()).orElseThrow());
    }

    @Override
    public List<BookDto> searchByTitle(String q) {
        return bookRepository.searchByTitle(q)
                .stream()
                .map(this::toDtoWithFetch)
                .toList();
    }

    @Override
    public List<BookDto> findByAuthorLastName(String lastName) {
        return bookRepository.findByAuthorLastName(lastName)
                .stream()
                .map(this::toDtoWithFetch)
                .toList();
    }

    @Override
    public List<BookDto> findByGenre(String genreName) {
        return bookRepository.findByGenreName(genreName)
                .stream()
                .map(this::toDtoWithFetch)
                .toList();
    }

    @Override
    public List<BookDto> getAvailableBooks() {
        return bookRepository.findAllAvailable()
                .stream()
                .map(BookDto::from)
                .toList();
    }

    /**
     * searchByTitle/findByAuthorLastName/findByGenreName возвращают Book
     * без fetch коллекций (иначе декартово произведение). Для DTO нужны
     * авторы/жанры — подтягиваем их отдельным запросом.
     */
    private BookDto toDtoWithFetch(Book b) {
        Book withFetch = bookRepository.findByIdWithAuthorsAndGenres(b.getId())
                .orElseThrow();
        return BookDto.from(withFetch);
    }
}