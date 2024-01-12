import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class BookServer {
    private static final int PORT = 8080;
    private static final String EXIT_COMMAND = "exit";

    private static BookRepository bookRepository = new BookRepository();

    public static void main(String[] args) {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(PORT);
            bookRepository.loadBooksFromFile(BookRepository.BOOKS_FILE_PATH);

            System.out.println("Serverul ruleaza pe portul " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client conectat.");

                handleClient(socket);
            }
        } catch (IOException e) {
            System.err.println("Eroare la pornirea serverului: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void handleClient(Socket socket) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

            String messageReceived;
            while ((messageReceived = bufferedReader.readLine()) != null) {
                if (messageReceived.equalsIgnoreCase(EXIT_COMMAND)) {
                    System.out.println("Client disconnected");
                    break;
                }

                System.out.println("DEBUG: Am primit comanda: " + messageReceived);
                processCommandAndSendToClient(messageReceived, bufferedWriter);
            }
        } catch (IOException e) {
            System.err.println("Eroare la comunicare cu clientul: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void processCommandAndSendToClient(String command, BufferedWriter writer) throws IOException {
        String[] commandParts = command.split(":", 2);

        String commandType = commandParts[0];
        String commandArgument = (commandParts.length > 1) ? commandParts[1] : "";

        switch (commandType) {
            case "GET_ALL_BOOKS" -> {
                List<Book> books = bookRepository.getBooks();
                sendBookListResponse(books, writer);
            }
            case "LINEAR_SEARCH_BY_TITLE" -> {
                List<Book> books = bookRepository.linearSearch(commandArgument, BookRepository.SearchType.TITLE);
                sendBookListResponse(books, writer);
            }
            case "LINEAR_SEARCH_BY_AUTHOR" -> {
                List<Book> books = bookRepository.linearSearch(commandArgument, BookRepository.SearchType.AUTHOR);
                sendBookListResponse(books, writer);
            }
            case "LINEAR_SEARCH_BY_TITLE_OR_AUTHOR" -> {
                List<Book> books = bookRepository.linearSearch(commandArgument, BookRepository.SearchType.TITLE_OR_AUTHOR);
                sendBookListResponse(books, writer);
            }
            case "BINARY_SEARCH_BY_TITLE" -> {
                Book book = bookRepository.binarySearch(commandArgument, BookRepository.SearchType.TITLE);
                sendSingleBookResponse(book, writer);
            }
            case "BINARY_SEARCH_BY_AUTHOR" -> {
                Book book = bookRepository.binarySearch(commandArgument, BookRepository.SearchType.AUTHOR);
                sendSingleBookResponse(book, writer);
            }
            default -> {
                writer.write("Comanda necunoscuta");
                writer.newLine();
                writer.flush();
            }
        }
    }

    private static void sendBookListResponse(List<Book> books, BufferedWriter writer) throws IOException {

        if (books.isEmpty()) {
            writer.write("Nu s-au gasit carti sau lista de carti este goala.");
            writer.newLine();
        } else {
            for (Book book : books) {
                writer.write(book.toString());
                writer.newLine();
            }
        }

        writer.newLine(); // Trimite o linie goala ca marker de sfarsit al raspunsului
        writer.flush();
    }

    private static void sendSingleBookResponse(Book book, BufferedWriter writer) throws IOException {
        if (book != null) {
            writer.write(book.toString());
            writer.newLine();
        } else {
            writer.write("Nu s-a gasit cartea.");
            writer.newLine();
        }
        writer.newLine(); // Trimite o linie goala ca marker de sfarsit al raspunsului
        writer.flush();
    }
}
