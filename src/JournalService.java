import java.util.List;

public interface JournalService {

    // Create: Takes a Thought object and saves it to the database
    void addEntry(Thought thought);

    // Read: Returns everything found in the storage
    List<Thought> getAllEntries();

    // Search: Returns only thoughts that match a specific category (e.g., "Work")
    List<Thought> getEntriesByCategory(String category);

    // Search: Returns thoughts containing a specific word (e.g., "Coffee")
    List<Thought> searchEntries(String keyword);

    // Update: Changes the text of an existing thought using its unique ID
    void updateEntry(int id, String newContent);

    // Delete: Removes a thought forever based on its ID
    void deleteEntry(int id);
}
