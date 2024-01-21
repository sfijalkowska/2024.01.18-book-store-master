package pl.comarch.camp.it.book.store.dao.memory;

import org.springframework.stereotype.Component;
import pl.comarch.camp.it.book.store.dao.IBookDAO;
import pl.comarch.camp.it.book.store.exceptions.BookAlreadyExistException;
import pl.comarch.camp.it.book.store.model.Book;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class BookRepository implements IBookDAO {
    private final List<Book> books = new ArrayList<>();

    public BookRepository() {
        this.books.add(new Book(1, "Java. Podręcznik na start",
                "Krzysztof Krocz", "978-83-283-9783-5", 44.85, 10));
        this.books.add(new Book(2, "Java. Kompendium programisty. Wydanie XII",
                "Herbert Schildt", "978-83-832-2156-4", 129.35, 10));
        this.books.add(new Book(3, "Java. Rusz głową! Wydanie III",
                "Kathy Sierra, Bert Bates, Trisha Gee",
                "978-83-283-9984-6", 96.85, 10));
        this.books.add(new Book(4, "Java. Przewodnik doświadczonego programisty. Wydanie III",
                "Cay S. Horstmann", "978-83-289-0140-7", 57.84, 10));
    }

    @Override
    public Book getById(int id) {
        for(Book book : this.books) {
            if(book.getId() == id) {
                return book.clone();
            }
        }
        return null;
    }

    @Override
    public List<Book> getAll() {
        List<Book> result = new ArrayList<>();
        for(Book book : this.books) {
            result.add(book.clone());
        }
        return result;
    }

    @Override
    public List<Book> getByPattern(String pattern) {
        List<Book> result = new ArrayList<>();
        for(Book book : this.books) {
            if(book.getTitle().toLowerCase().contains(pattern.toLowerCase()) ||
            book.getAuthor().toLowerCase().contains(pattern.toLowerCase())) {
                result.add(book.clone());
            }
        }
        return result;
    }

    @Override
    public void save(Book book) {
        Book bookFromDb = this.getById(book.getId());
        if(bookFromDb == null) {
            this.books.add(book);
        } else {
            throw new BookAlreadyExistException("Book with id: " + book.getId() + " already exist");
        }
    }

    @Override
    public void update(Book book) {
        Book bookFromDb = this.getById(book.getId());
        if(bookFromDb == null) {
            return;
        }
        bookFromDb.setTitle(book.getTitle());
        bookFromDb.setAuthor(book.getAuthor());
        bookFromDb.setIsbn(book.getIsbn());
        bookFromDb.setPrice(book.getPrice());
        bookFromDb.setQuantity(book.getQuantity());
    }

    @Override
    public void delete(int id) {
        Iterator<Book> iterator = this.books.iterator();
        while(iterator.hasNext()) {
            Book book = iterator.next();
            if(book.getId() == id) {
                iterator.remove();
                break;
            }
        }
    }
}
