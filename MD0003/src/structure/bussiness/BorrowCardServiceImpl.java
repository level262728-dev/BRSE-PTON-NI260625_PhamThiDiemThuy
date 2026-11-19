package structure.bussiness;
import structure.dao.BorrowCardDao;
import structure.dao.BorrowCardDaoImpl;
import structure.model.BorrowCard;

import java.util.List;

public class BorrowCardServiceImpl implements BorrowCardService {
    private final BorrowCardDao borrowCardDao = new BorrowCardDaoImpl();

    @Override
    public List<BorrowCard> getAllBorrowCards() {
        return borrowCardDao.getAllBorrowCards();
    }

    @Override
    public boolean addBorrowCard(BorrowCard card) {
        if (card == null) {
            return false;
        }
        return borrowCardDao.addBorrowCard(card);
    }

    @Override
    public boolean updateBorrowCard(BorrowCard card) {
        if (card == null || card.getCardId() <= 0) {
            return false;
        }
        return borrowCardDao.updateBorrowCard(card);
    }

    @Override
    public boolean deleteBorrowCard(long cardId) {
        if (cardId <= 0) {
            return false;
        }
        return borrowCardDao.deleteBorrowCard(cardId);
    }

    @Override
    public List<BorrowCard> getBorrowCardsByBorrower(String borrowerName) {
        return borrowCardDao.getBorrowCardsByBorrower(borrowerName);
    }

    @Override
    public List<BorrowCard> searchByBookTitle(String keyword) {
        return borrowCardDao.searchByBookTitle(keyword);
    }
}