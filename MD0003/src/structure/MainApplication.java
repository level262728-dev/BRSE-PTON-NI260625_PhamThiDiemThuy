package structure;

import structure.presentation.BorrowCardView;

import java.util.Scanner;

public class MainApplication {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BorrowCardView borrowCardView = new BorrowCardView();
        borrowCardView.showMenu(scanner);
        scanner.close();
    }
}