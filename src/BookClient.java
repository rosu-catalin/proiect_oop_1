import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class BookClient {
    private static Scanner scanner;
    private static Socket socket;
    private static BufferedWriter bufferedWriter;
    private static BufferedReader bufferedReader;

    public static void main(String[] args) {
        try {
            initializeClient();

            scanner = new Scanner(System.in);

            while (true) {
                displayMenu();
                System.out.print("Alege o optiune: ");
                int choice = getUserChoice();

                switch (choice) {
                    case 1, 2, 3, 4, 5, 6 -> {
                        String command = getCommand(choice);
                        System.out.println("Comanda trimisa: " + command);
                        sendCommandToServer(command);
                        processServerResponse();
                    }
                    case 9 -> {
                        System.out.println("La revedere!");
                        return;
                    }
                    default -> System.out.println("Optiune invalida. Te rog sa alegi din nou.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeClientResources();
        }
    }

    private static void initializeClient() throws IOException {
        socket = new Socket("localhost", 8080);
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    private static void closeClientResources() {
        try {
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void displayMenu() {
        System.out.println("\n\n--- Meniu Biblioteca ---");
        System.out.println("1. Listare carti");
        System.out.println("2. Cautare dupa titlu (metoda liniara)");
        System.out.println("3. Cautare dupa autor (metoda liniara)");
        System.out.println("4. Cautare dupa titlu sau autor (metoda liniara)");
        System.out.println("5. Cautare dupa titlu (metoda binara)");
        System.out.println("6. Cautare dupa autor (metoda binara)");
        System.out.println("9. Iesire");
        System.out.println("------------------------------");
    }

    private static int getUserChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Optiune invalida. Te rog sa alegi din nou.");
            return -1;
        }
    }

    private static String getCommand(int choice) {
        if (choice == 1) {
            return "GET_ALL_BOOKS";
        } else {
            System.out.print("Introdu titlul sau autorul cartii: ");
            String input = scanner.nextLine();
            return switch (choice) {
                case 2 -> "LINEAR_SEARCH_BY_TITLE:" + input;
                case 3 -> "LINEAR_SEARCH_BY_AUTHOR:" + input;
                case 4 -> "LINEAR_SEARCH_BY_TITLE_OR_AUTHOR:" + input;
                case 5 -> "BINARY_SEARCH_BY_TITLE:" + input;
                case 6 -> "BINARY_SEARCH_BY_AUTHOR:" + input;
                default -> "";
            };
        }
    }

    private static void sendCommandToServer(String command) throws IOException {
        bufferedWriter.write(command);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

    private static void processServerResponse() throws IOException {
        StringBuilder fullResponse = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if (line.isEmpty()) {
                break;
            }
            fullResponse.append(line).append("\n");
        }
        System.out.println("Raspuns de la server:\n" + fullResponse.toString());
    }
}
