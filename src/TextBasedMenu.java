import java.util.List;
import java.util.Scanner;

public class TextBasedMenu {

    private Scanner scanner;
    private BookRepository bookRepository;

    public TextBasedMenu(BookRepository bookRepository) {
        scanner = new Scanner(System.in);
        this.bookRepository = bookRepository;
    }

    public void displayMenu() {
        while (true) {
            System.out.println("\n\n--- Meniu Bibliotecă ---");
            System.out.println("1. Listare cărți");
            System.out.println("2. Caută carte după titlu [CAUTARE LINIARĂ]");
            System.out.println("3. Caută carte după autor [CAUTARE LINIARĂ]");
            System.out.println("4. Caută carte după titlu sau autor [CAUTARE LINIARĂ]");
            System.out.println("5. Caută carte după titlu [CAUTARE BINARĂ]");
            System.out.println("6. Caută carte după autor [CAUTARE BINARĂ]");
            System.out.println("9. Ieșire");
            System.out.println("------------------------------");

            System.out.print("Alege o opțiune: ");
            int choice = getUserChoice();

            switch (choice) {
                case 1 -> listAllBooks();
                case 2 -> searchByTitleLinear();
                case 3 -> searchByAuthorLinear();
                case 4 -> searchByTitleOrAuthorLinear();
                case 5 -> searchByTitleBinary();
                case 6 -> searchByAuthorBinary();
                case 9 -> {
                    exitProgram();
                    return;
                }
                default -> System.out.println("Opțiune invalidă. Te rog să alegi din nou.");
            }
        }
    }

    private int getUserChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Opțiune invalidă. Te rog să alegi din nou.");
            return -1;
        }
    }

    private void listAllBooks() {
        List<Book> books = bookRepository.getBooks();
        bookRepository.listBooks(books);
    }

    private void searchByTitleLinear() {
        System.out.print("Introdu titlul cărții: ");
        String title = scanner.nextLine();
        List<Book> foundBooks = bookRepository.linearSearch(title, BookRepository.SearchType.TITLE);
        bookRepository.listBooks(foundBooks);
    }

    private void searchByAuthorLinear() {
        System.out.print("Introdu autorul cărții: ");
        String author = scanner.nextLine();
        List<Book> foundBooks = bookRepository.linearSearch(author, BookRepository.SearchType.AUTHOR);
        bookRepository.listBooks(foundBooks);
    }

    private void searchByTitleOrAuthorLinear() {
        System.out.print("Introdu titlul sau autorul cărții: ");

        String titleOrAuthor = scanner.nextLine();
        List<Book> foundBooks = bookRepository.linearSearch(titleOrAuthor, BookRepository.SearchType.TITLE_OR_AUTHOR);

        bookRepository.listBooks(foundBooks);
    }

    private void searchByTitleBinary() {
        System.out.print("Introdu titlul cărții: ");

        String title = scanner.nextLine();
        Book foundBook = bookRepository.binarySearch(title, BookRepository.SearchType.TITLE);

        printSearchResult(foundBook);
    }

    private void searchByAuthorBinary() {
        System.out.print("Introdu autorul cărții: ");

        String author = scanner.nextLine();
        Book foundBook = bookRepository.binarySearch(author, BookRepository.SearchType.AUTHOR);

        printSearchResult(foundBook);
    }

    private void printSearchResult(Book foundBook) {

        if (foundBook != null) {
            System.out.println("Cartea a fost găsită: " + foundBook.getTitle() + " de " + foundBook.getAuthor() + " (" + foundBook.getCopies() + " exemplare)");
            return;
        }

        System.out.println("Cartea nu a fost găsită.");
    }

    private void exitProgram() {
        System.out.println("Ieșire din program...");
    }
}