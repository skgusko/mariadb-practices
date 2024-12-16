package bookmall.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bookmall.vo.CartVo;

public class CartDao {

	public int insert(CartVo vo) {
		int count = 0;
		
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt1 = conn.prepareStatement("insert into cart (book_no, user_no, quantity) values (?, ?, ?)");
//			PreparedStatement pstmt2 = conn.prepareStatement("select last_insert_id() from dual");
		) {
			pstmt1.setLong(1, vo.getBookNo());
			pstmt1.setLong(2, vo.getUserNo());
			pstmt1.setInt(3, vo.getQuantity());
			count = pstmt1.executeUpdate();
			
//			ResultSet rs = pstmt2.executeQuery();
//			vo.set(rs.next() ? rs.getLong(1) : null);
//			rs.close();
		} catch (SQLException e) {
			System.out.println("error: " + e);
		} 
		return count;
	}

	public List<CartVo> findByUserNo(Long no) {
		List<CartVo> result = new ArrayList<>();
		
		try ( 
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select a.book_no, b.title, a.quantity from cart a join book b on a.book_no = b.no where a.user_no = ?");
		) {
			pstmt.setLong(1, no);
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Long bookNo = rs.getLong(1);
				String bookTitle = rs.getString(2);
				int quantity = rs.getInt(3);
				
				CartVo vo = new CartVo();
				vo.setBookNo(bookNo);
				vo.setBookTitle(bookTitle);
				vo.setQuantity(quantity);
				
				result.add(vo);
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		return result;
	}

	public int deleteByUserNoAndBookNo(Long userNo, Long bookNo) {
		int count = 0;
		
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt1 = conn.prepareStatement("delete from cart where user_no = ? and book_no = ?");
		) {
			pstmt1.setLong(1, userNo);
			pstmt1.setLong(2, bookNo);
			count = pstmt1.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("error: " + e);
		} 
		
		return count;
		
	}

	private Connection getConnection() throws SQLException{
		Connection conn = null;
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			
			String url = "jdbc:mariadb://192.168.56.5:3306/bookmall";
			conn = DriverManager.getConnection(url, "bookmall", "bookmall");
			
		} catch (ClassNotFoundException e) { 
			System.out.println("드라이버 로딩 실패: " + e);
		} 
		return conn;
	}

	
}
