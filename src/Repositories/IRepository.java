package Repositories;

import java.util.List;

public interface IRepository {
    void saveToFile() throws Exception;
    void loadFromFile() throws Exception;
    //List<T> findAll();
}
