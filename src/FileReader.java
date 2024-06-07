import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

// FileReader class is designed to read lines from a file
public abstract class FileReader implements FileOperations{
    // Path to the file to be read
    protected Path filePath;

    // Parameterized Constructor that initializes the filePath
    public FileReader(Path filePath) {
        this.filePath = filePath;
    }

    // Method to read all lines from the file and return them as a list of strings
    public List<String> getAllLines() throws IOException {
        try {
            // Attempt to read all lines from the file using Files.readAllLines
            return Files.readAllLines(filePath);
        } catch (IOException e) {
            // If an IOException occurs during file reading, create a new IOException with a custom message
            // This new exception is created to provide more specific information about the error
            IOException studyPlanNotFound = new IOException("Study plan path does not exist");
            
            // Set the original exception (e) as the cause of the new exception
            // This is to maintain the stack trace of the original exception
            studyPlanNotFound.initCause(e);
            
            // Throw the new exception with the original cause
            throw studyPlanNotFound;
        }
    }
}

// Interface that guarantees FileReader will have GetAllLines method
interface FileOperations {
    public List<String> getAllLines() throws IOException;
}