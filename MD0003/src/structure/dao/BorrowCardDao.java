package structure.dao;

import structure.model.BorrowCard;
import java.util.List;

public interface BorrowCardDao {
    List<BorrowCard> getAllBorrowCards();
    boolean addBorrowCard(BorrowCard card);
    boolean updateBorrowCard(BorrowCard card);
    boolean deleteBorrowCard(long cardId);
    List<BorrowCard> getBorrowCardsByBorrower(String borrowerName);
    List<BorrowCard> searchByBookTitle(String keyword);
}