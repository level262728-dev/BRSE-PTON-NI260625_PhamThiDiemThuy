package structure.presentation;

import structure.bussiness.BorrowCardService;
import structure.bussiness.BorrowCardServiceImpl;
import structure.model.BorrowCard;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class BorrowCardView {
    private final BorrowCardService borrowCardService = new BorrowCardServiceImpl();

    public void showMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n================= LIBRARY MANAGEMENT =================");
            System.out.println("1. Danh sách tất cả phiếu mượn");
            System.out.println("2. Thêm mới phiếu mượn");
            System.out.println("3. Cập nhật thông tin phiếu mượn");
            System.out.println("4. Xóa phiếu mượn");
            System.out.println("5. Tìm kiếm phiếu mượn theo tên độc giả");
            System.out.println("6. Tìm kiếm phiếu mượn theo tên sách");
            System.out.println("7. Thoát");
            System.out.print("Chọn chức năng: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    displayAllCards();
                    break;
                case "2":
                    addCard(scanner);
                    break;
                case "3":
                    updateCard(scanner);
                    break;
                case "4":
                    deleteCard(scanner);
                    break;
                case "5":
                    findByBorrower(scanner);
                    break;
                case "6":
                    searchByBookTitle(scanner);
                    break;
                case "7":
                    System.out.println("Đã thoát chương trình.");
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ, vui lòng chọn lại!");
            }
        }
    }

    private void displayAllCards() {
        List<BorrowCard> list = borrowCardService.getAllBorrowCards();
        if (list.isEmpty()) {
            System.out.println("Hiện chưa có phiếu mượn nào.");
            return;
        }
        printCardList(list);
    }

    private BorrowCard inputCardInfo(Scanner scanner, Long cardId) {
        String bookTitle = inputNonEmptyString(scanner,
                cardId == null ? "Nhập tên sách: " : "Nhập tên sách mới: ");
        String borrowerName = inputNonEmptyString(scanner,
                cardId == null ? "Nhập tên độc giả: " : "Nhập tên độc giả mới: ");
        Timestamp borrowDate = new Timestamp(System.currentTimeMillis());
        Timestamp returnDeadline = inputDeadline(scanner);
        int quantity = inputPositiveInt(scanner,
                cardId == null ? "Nhập số lượng sách: " : "Nhập số lượng sách mới: ");
        String status = inputNonEmptyString(scanner,
                cardId == null ? "Nhập trạng thái (ví dụ: Đang mượn / Đã trả): " : "Nhập trạng thái mới: ");

        if (cardId == null) {
            return new BorrowCard(bookTitle, borrowerName, borrowDate, returnDeadline, quantity, status);
        }
        return new BorrowCard(cardId, bookTitle, borrowerName, borrowDate, returnDeadline, quantity, status);
    }

    private void addCard(Scanner scanner) {
        BorrowCard card = inputCardInfo(scanner, null);
        boolean success = borrowCardService.addBorrowCard(card);
        System.out.println(success ? "Thêm phiếu mượn thành công!" : "Thêm phiếu mượn thất bại!");
    }

    private void updateCard(Scanner scanner) {
        long cardId = inputPositiveLong(scanner, "Nhập mã phiếu mượn (card_id) cần cập nhật: ");
        BorrowCard card = inputCardInfo(scanner, cardId);
        boolean success = borrowCardService.updateBorrowCard(card);
        System.out.println(success ? "Cập nhật phiếu mượn thành công!" : "Cập nhật thất bại hoặc không tìm thấy mã phiếu!");
    }

    private void deleteCard(Scanner scanner) {
        long cardId = inputPositiveLong(scanner, "Nhập mã phiếu mượn (card_id) cần xóa: ");
        System.out.print("Bạn có chắc chắn muốn xóa phiếu mượn có mã " + cardId + " không? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (!confirm.equals("y") && !confirm.equals("yes")) {
            System.out.println("Đã hủy thao tác xóa phiếu mượn.");
            return;
        }

        boolean success = borrowCardService.deleteBorrowCard(cardId);
        System.out.println(success ? "Xóa phiếu mượn thành công!" : "Không tìm thấy phiếu mượn để xóa!");
    }

    private void findByBorrower(Scanner scanner) {
        String borrowerName = inputNonEmptyString(scanner, "Nhập tên độc giả cần tìm: ");
        List<BorrowCard> list = borrowCardService.getBorrowCardsByBorrower(borrowerName);
        if (list.isEmpty()) {
            System.out.println("Không tìm thấy phiếu mượn nào cho độc giả này.");
            return;
        }
        printCardList(list);
    }

    private void searchByBookTitle(Scanner scanner) {
        String keyword = inputNonEmptyString(scanner, "Nhập từ khóa tên sách cần tìm: ");
        List<BorrowCard> list = borrowCardService.searchByBookTitle(keyword);
        if (list.isEmpty()) {
            System.out.println("Không tìm thấy phiếu mượn nào khớp với tên sách.");
            return;
        }
        printCardList(list);
    }

    private String inputNonEmptyString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Dữ liệu không được để trống, vui lòng nhập lại!");
        }
    }

    private Timestamp inputDeadline(Scanner scanner) {
        while (true) {
            System.out.print("Nhập hạn trả (định dạng YYYY-MM-DD, ví dụ 2026-12-01): ");
            String raw = scanner.nextLine().trim();
            try {
                LocalDate date = LocalDate.parse(raw);
                return Timestamp.valueOf(date.atTime(23, 59, 59));
            } catch (DateTimeParseException e) {
                System.out.println("Sai định dạng ngày! Vui lòng nhập theo dạng YYYY-MM-DD.");
            }
        }
    }

    private int inputPositiveInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String raw = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(raw);
                if (value > 0) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
            }
            System.out.println("Vui lòng nhập một số nguyên dương (> 0)!");
        }
    }

    private long inputPositiveLong(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String raw = scanner.nextLine().trim();
            try {
                long value = Long.parseLong(raw);
                if (value > 0) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
            }
            System.out.println("Vui lòng nhập mã ID hợp lệ (> 0)!");
        }
    }

    private void printCardList(List<BorrowCard> list) {
        System.out.println("----------------------------------------------------------------------------------------------------");
        for (BorrowCard card : list) {
            System.out.printf("ID: %d | Sách: %s | Độc giả: %s | SL: %d | Trạng thái: %s | Mượn: %s | Hạn trả: %s%n",
                    card.getCardId(),
                    card.getBookTitle(),
                    card.getBorrowerName(),
                    card.getQuantity(),
                    card.getStatus(),
                    card.getBorrowDate(),
                    card.getReturnDeadline());
        }
        System.out.println("----------------------------------------------------------------------------------------------------");
    }
}