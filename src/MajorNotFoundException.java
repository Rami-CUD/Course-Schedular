// Custom exception class for MajorNotFoundException
    public class MajorNotFoundException extends Exception {
        // Constructor with a message indicating the major not found
        public MajorNotFoundException(String major) {
            super("Major not found: " + major);
        }
    }