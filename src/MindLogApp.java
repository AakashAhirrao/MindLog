import java.util.Scanner;
import java.util.List;

public class MindLogApp {

    private static final JournalService journal = new SqlJournalService();
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        boolean running = true;

        while (running) {
            System.out.println("\n===MIND LOG===");
            System.out.println("1. New Thought ✍️");
            System.out.println("2. View History 📖");
            System.out.println("3. Search with word 🔍");
            System.out.println("4. Exit 🚪");
            System.out.print("Choice: ");
            String input = sc.nextLine();

            switch (input) {

                case "1" -> handleWrite();
                case "2" -> handleRead();
                case "3" -> handleSearch();
                case "4" -> running = false;
                default -> System.out.println("Try again!");
            }
        }
    }

    private static void handleWrite() {

        System.out.println("Select Category: ");
        System.out.println("1. Work | 2. Personal | 3. Idea | 4. Fitness | 5. Important");
        System.out.print("Choice (Enter Number): ");

        String input = sc.nextLine();
        int choice = Integer.parseInt(input);
        Category selectedCat = switch (choice) {
            case 1 -> Category.WORK;
            case 2 -> Category.PERSONAL;
            case 3 -> Category.IDEA;
            case 4 -> Category.FITNESS;
            case 5 -> Category.IMPORTANT;
            default -> Category.PERSONAL;
        };

//        String cat = sc.nextLine();
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
            Thought t = new Thought(selectedCat, msg);
            journal.addEntry(t);
//            System.out.println("Saved to Database!");
        }
    }

    private static void handleRead() {
        List<Thought> entries = journal.getAllEntries();
        System.out.println("\n---- YOUR JOURNAL ENTRIES ----");
        for (Thought t : entries) {
            System.out.println("ID: " + t.getId() + " | Category: " + t.getCategory().getPrettyName());
            System.out.println("Content: " + t.getContent());
            System.out.println("------------------------------------");
        }
    }

    private static void handleSearch() {
        System.out.print("Enter keyword to search: ");
        String keyword = sc.nextLine();

        List<Thought> results = journal.searchEntries(keyword);

        if (results.isEmpty()){
            System.out.println("🔍 No thoughts found containing: " + keyword);
        }
        else {
            System.out.println("🔍 Found " +results.size()+" matches: ");
            for (Thought t : results){
                System.out.println("[" + t.getCategory().getPrettyName() + "] "+t.getContent());
                System.out.println("---------------------------------------");
            }
        }
    }
}
