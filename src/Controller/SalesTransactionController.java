package Controller;

import Services.SalesTransactionService;

public class SalesTransactionController {
    private final SalesTransactionService salesTransactionService;
    public SalesTransactionController(SalesTransactionService salesTransactionService) {
        this.salesTransactionService = salesTransactionService;
    }
}
