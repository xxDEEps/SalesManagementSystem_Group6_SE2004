package Repositories;

import java.util.ArrayList;
import java.util.List;

import Model.Transaction;

public class TransactionRepository extends AbstractFileRepository implements IRepository {
    private List<Transaction> transactions = new ArrayList<>();
}
