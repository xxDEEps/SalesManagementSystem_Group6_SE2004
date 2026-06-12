package Repositories;

import java.util.ArrayList;
import java.util.List;

import Model.SalesTransaction;

public class TransactionRepository extends AbstractFileRepository implements IRepository {
    private List<SalesTransaction> transactions = new ArrayList<>();
}
