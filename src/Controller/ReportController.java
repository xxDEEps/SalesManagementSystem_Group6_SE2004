package Controller;

import Services.ReportService;

public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    public String handleSalesReport(String period) {
        return reportService.getSalesReportByPeriod(period);
    }

    public String handleBestSellingProducts() {
        return reportService.getBestSellingProductReport();
    }

    public String handleTopCustomers(String period) {
        return reportService.getTopCustomerReport(period);
    }
}
