import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BookRepository {

    public static final String BOOKS_FILE_PATH = "C:\\Users\\zonet\\IdeaProjects\\OOP_PROJECT\\src\\carti.txt";

    private List<Book> books;
    private List<Book> booksSortedByTitle;
    private List<Book> booksSortedByAuthor;
    public enum SearchType {
        TITLE,
        AUTHOR,
        TITLE_OR_AUTHOR
    }

    public BookRepository() {
        this.books = new ArrayList<>();
        this.booksSortedByTitle = new ArrayList<>();
        this.booksSortedByAuthor = new ArrayList<>();
    }

    public void loadBooksFromFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String title = line.trim();
                String author = reader.readLine().trim();
                int copies = Integer.parseInt(reader.readLine().trim());

                Book book = new Book(title, author, copies);
                books.add(book);
                booksSortedByTitle.add(book);
                booksSortedByAuthor.add(book);
            }

            Collections.sort(booksSortedByTitle, Comparator.comparing(Book::getTitle));
            Collections.sort(booksSortedByAuthor, Comparator.comparing(Book::getAuthor));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Book> linearSearch(String searchText, SearchType searchType) {
        List<Book> foundBooks = new ArrayList<>();

        searchText = searchText.toLowerCase();

        for (Book book : books) {

            String bookAttribute = switch (searchType) {
                case TITLE -> book.getTitle().toLowerCase();
                case AUTHOR -> book.getAuthor().toLowerCase();
                case TITLE_OR_AUTHOR -> book.getTitle().toLowerCase() + " " + book.getAuthor().toLowerCase();
            };

            if (bookAttribute.contains(searchText)) {
                foundBooks.add(book);
            }
        }

        return foundBooks;
    }

    public Book binarySearch(String searchText, SearchType searchType) {

        List<Book> searchList;

        if (searchType == SearchType.TITLE) {
            searchList = booksSortedByTitle;
        } else if (searchType == SearchType.AUTHOR) {
            searchList = booksSortedByAuthor;
        } else {
            return null; // Tratați în mod corespunzător pentru alte tipuri de căutare
        }

        int low = 0;
        int high = searchList.size() - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            Book midBook = searchList.get(mid);
            String midAttribute = (searchType == SearchType.TITLE) ? midBook.getTitle() : midBook.getAuthor();
            int result = midAttribute.compareToIgnoreCase(searchText);

            if (result == 0) {
                return midBook;
            } else if (result < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return null;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void listBooks(List<Book> books) {

        if (books.size() == 0) {
            System.out.println("Nu s-au găsit cărți.");
            return;
        }

        int maxTitleLength = "Title".length();
        int maxAuthorLength = "Author".length();
        int maxCopiesLength = "Copies".length();

        // Determină lățimea maximă pentru fiecare coloană
        for (Book book : books) {
            if (book.getTitle().length() > maxTitleLength) {
                maxTitleLength = book.getTitle().length();
            }
            if (book.getAuthor().length() > maxAuthorLength) {
                maxAuthorLength = book.getAuthor().length();
            }
            if (String.valueOf(book.getCopies()).length() > maxCopiesLength) {
                maxCopiesLength = String.valueOf(book.getCopies()).length();
            }
        }

        System.out.println(books.size() + " books found:\n");

        // Antetul tabelului
        String headerFormat = "%-" + maxTitleLength + "s | %-" + maxAuthorLength + "s | %-" + maxCopiesLength + "s%n";
        System.out.printf(headerFormat, "Title", "Author", "Copies");
        for (int i = 0; i < maxTitleLength + maxAuthorLength + maxCopiesLength + 6; i++) {
            System.out.print("-");
        }
        System.out.println();

        // Listarea fiecărei cărți
        String rowFormat = "%-" + maxTitleLength + "s | %-" + maxAuthorLength + "s | %" + maxCopiesLength + "d%n";
        for (Book book : books) {
            System.out.printf(rowFormat,
                    book.getTitle(),
                    book.getAuthor(),
                    book.getCopies());
        }
    }


}
