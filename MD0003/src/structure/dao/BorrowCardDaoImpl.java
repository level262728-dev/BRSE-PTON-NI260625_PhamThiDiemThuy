package structure.dao;

import structure.model.BorrowCard;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BorrowCardDaoImpl implements BorrowCardDao {

    private BorrowCard mapRow(ResultSet rs) throws SQLException {
        return new BorrowCard(
                rs.getLong("card_id"),
                rs.getString("book_title"),
                rs.getString("borrower_name"),
                rs.getTimestamp("borrow_date"),
                rs.getTimestamp("return_deadline"),
                rs.getInt("quantity"),
                rs.getString("status")
        );
    }

    private List<BorrowCard> queryCards(String sql, String param) {
        List<BorrowCard> list = new ArrayList<>();
        try (Connection conn = MySQLConnection.openConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            if (param != null) {
                cstmt.setString(1, param);
            }
            try (ResultSet rs = cstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<BorrowCard> getAllBorrowCards() {
        return queryCards("{CALL sp_get_all_borrow_cards()}", null);
    }

    @Override
    public List<BorrowCard> getBorrowCardsByBorrower(String borrowerName) {
        return queryCards("{CALL sp_get_borrow_cards_by_borrower(?)}", borrowerName);
    }

    @Override
    public List<BorrowCard> searchByBookTitle(String keyword) {
        return queryCards("{CALL sp_search_borrow_cards_by_book_title(?)}", keyword);
    }

    @Override
    public boolean addBorrowCard(BorrowCard card) {
        String sql = "{CALL sp_add_borrow_card(?, ?, ?, ?, ?, ?)}";
        try (Connection conn = MySQLConnection.openConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            cstmt.setString(1, card.getBookTitle());
            cstmt.setString(2, card.getBorrowerName());
            cstmt.setTimestamp(3, card.getBorrowDate());
            cstmt.setTimestamp(4, card.getReturnDeadline());
            cstmt.setInt(5, card.getQuantity());
            cstmt.setString(6, card.getStatus());
            return cstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateBorrowCard(BorrowCard card) {
        String sql = "{CALL sp_update_borrow_card(?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conn = MySQLConnection.openConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            cstmt.setLong(1, card.getCardId());
            cstmt.setString(2, card.getBookTitle());
            cstmt.setString(3, card.getBorrowerName());
            cstmt.setTimestamp(4, card.getBorrowDate());
            cstmt.setTimestamp(5, card.getReturnDeadline());
            cstmt.setInt(6, card.getQuantity());
            cstmt.setString(7, card.getStatus());
            return cstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteBorrowCard(long cardId) {
        String sql = "{CALL sp_delete_borrow_card(?)}";
        try (Connection conn = MySQLConnection.openConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            cstmt.setLong(1, cardId);
            return cstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}