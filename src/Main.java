public class Main {
    public static void main(String[] args) {

        BookRepository bookRepository = new BookRepository();
        bookRepository.loadBooksFromFile(BookRepository.BOOKS_FILE_PATH);

        TextBasedMenu textBasedMenu = new TextBasedMenu(bookRepository);
        textBasedMenu.displayMenu();

    }
}