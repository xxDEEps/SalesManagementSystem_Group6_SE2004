
import View.MenuGenerator;
import java.util.Scanner;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


/**
 *
 * @author LENOVO
 */
public class SalesManagementSystem {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int choice = 0;
        Scanner scanner = new Scanner(System.in);
        do {
            View.MenuGenerator.displayMainMenu();
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    MenuGenerator.displayProductMenu();
                    break;
                case 2:
                    MenuGenerator.displayCustomerMenu();
                    break;
                case 3:
                    MenuGenerator.displayOrderMenu();
                    break;
                case 4:
                    System.out.println("Exiting the system. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (choice != 4);
    }
    
}
