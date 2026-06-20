package Repositories;

import java.io.*;

public abstract class AbstractFileRepository {
    
    private static final String DATA_FOLDER = "src\\Data";
    
    private String getCurrentDirectory() {
        return System.getProperty("user.dir");
    }
    
    // Ensure Data folder exists
    private void ensureDataFolderExists() throws IOException {
        File dataFolder = new File(getCurrentDirectory() + "\\" + DATA_FOLDER);
        if (!dataFolder.exists()) {
            if (!dataFolder.mkdir()) {
                throw new IOException("Failed to create Data folder");
            }
        }
    }

    // Write binary file
    protected void writeDataToFile(Object data, String filePath) throws IOException {
        ensureDataFolderExists();
        String fullPath = getCurrentDirectory() + "\\" + DATA_FOLDER + "\\" + filePath;
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fullPath))) {
            oos.writeObject(data);
        }
    }

    // Read binary file
    protected Object readDataFromFile(String filePath) throws IOException, ClassNotFoundException {
        String fullPath = getCurrentDirectory() + "\\" + DATA_FOLDER + "\\" + filePath;
        File file = new File(fullPath);
        if (!file.exists()) {
            return null; 
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return ois.readObject();
        }
    }
}