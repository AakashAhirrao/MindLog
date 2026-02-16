import java.util.Scanner;
import java.util.List;

public class MindLogApp {

    private static JournalService journal = new SqlJournalService();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        boolean running = true;

        while (running) {
            System.out.println("\n===MIND LOG===");
            System.out.println("1. New Thought ✍️");
            System.out.println("2. View History 📖");
            System.out.println("3. Exit 🚪");
            System.out.print("Choice: ");
            String input = sc.nextLine();

            switch (input) {

                case "1" -> handleWrite();
                case "2" -> handleRead();
                case "3" -> running = false;
                default -> System.out.println("Try again!");
            }
        }
    }

    private static void handleWrite() {

        System.out.print("Category (Work, Personal, Idea): ");
        String cat = sc.nextLine();
        System.out.println("Enter your thoughts below.");
        System.out.println("(Type \"Q\" on the next line and press enter to save)");
        System.out.println("-------------------------------------------------------");

        StringBuilder sb =  new StringBuilder();
        while (true) {
            String line = sc.nextLine();

            if (line.equalsIgnoreCase("Q")){
                break;
            }

            sb.append(line).append(System.lineSeparator());
        }

        String msg = sb.toString().trim();

        if (msg.isEmpty()){
            System.out.println("Thought was empty. Nothing saved.");
        }
        else {
            Thought t = new Thought(cat, msg);
            journal.addEntry(t);
//            System.out.println("Saved to Database!");
        }
    }

    private static void handleRead() {
        List<Thought> entries = journal.getAllEntries();
        System.out.println("\n---- YOUR JOURNAL ENTRIES ----");
        for (Thought t : entries) {
            System.out.println("ID: " + t.getId() + " | Category: " + t.getCategory());
            System.out.println("Content: " + t.getContent());
            System.out.println("------------------------------------");
        }
    }
}
