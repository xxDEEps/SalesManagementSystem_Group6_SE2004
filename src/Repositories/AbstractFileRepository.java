package Repositories;

import java.io.*;

public abstract class AbstractFileRepository {
    
    // Write binary file
    protected void writeDataToFile(Object data, String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(data);
        }
    }

    // Read binary file
    protected Object readDataFromFile(String filePath) throws IOException, ClassNotFoundException {
        File file = new File(filePath);
        if (!file.exists()) {
            return null; 
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return ois.readObject();
        }
    }
}