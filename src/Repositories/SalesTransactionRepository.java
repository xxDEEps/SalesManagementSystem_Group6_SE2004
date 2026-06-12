package Repositories;

import java.util.ArrayList;
import java.util.List;

import Model.SalesTransaction;

public class SalesTransactionRepository extends AbstractFileRepository implements IRepository {
    private List<SalesTransaction> salesTransactionsList = new ArrayList<>();
}
