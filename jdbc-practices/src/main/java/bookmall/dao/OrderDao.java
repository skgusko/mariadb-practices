package bookmall.dao;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bookmall.vo.OrderBookVo;
import bookmall.vo.OrderVo;

public class OrderDao {

	public int insert(OrderVo vo) {
		int count = 0;

		try (
			Connection conn = getConnection();
			PreparedStatement pstmt1 = conn.prepareStatement("insert into orders (user_no, number, payment, shipping, status) values (?, ?, ?, ?, ?)");
			PreparedStatement pstmt2 = conn.prepareStatement("select last_insert_id() from dual");
		) {
			pstmt1.setLong(1, vo.getUserNo());
			pstmt1.setString(2, vo.getNumber());
			pstmt1.setInt(3, vo.getPayment());
			pstmt1.setString(4, vo.getShipping());
			pstmt1.setString(5, vo.getStatus());
			
			count = pstmt1.executeUpdate();
			
			ResultSet rs = pstmt2.executeQuery();
			vo.setNo(rs.next() ? rs.getLong(1) : null);
			
			rs.close();
		} catch (SQLException e) {
			System.out.println("error: " + e);
		} 
		return count;
		
	}

	public OrderVo findByNoAndUserNo(Long orderNo, Long userNo) {
		OrderVo result = null;
		
		try ( 
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select number, payment, status, shipping from orders where no = ? and user_no = ?");
		) {
			pstmt.setLong(1, orderNo);
			pstmt.setLong(2, userNo);
			
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = new OrderVo();
				
				result.setNumber(rs.getString(1));
				result.setPayment(rs.getInt(2));
				result.setStatus(rs.getString(3));
				result.setShipping(rs.getString(4));
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		return result;
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

	public int deleteByNo(Long no) {
		int count = 0;
		
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt1 = conn.prepareStatement("delete from orders where no = ?");
		) {
			pstmt1.setLong(1, no);
			count = pstmt1.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("error: " + e);
		} 
		return count;
	}

	public int deleteBooksByNo(Long no) {
		int count = 0;
		
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt1 = conn.prepareStatement("delete from orders_book where orders_no = ?");
		) {
			pstmt1.setLong(1, no);
			count = pstmt1.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("error: " + e);
		} 
		return count;
	}

	public int insertBook(OrderBookVo vo) {
		int count = 0;

		try (
			Connection conn = getConnection();
			PreparedStatement pstmt1 = conn.prepareStatement("insert into orders_book (book_no, orders_no, quantity, price) values (?, ?, ?, ?)");
		) {
			pstmt1.setLong(1, vo.getBookNo());
			pstmt1.setLong(2, vo.getOrderNo());
			pstmt1.setInt(3, vo.getQuantity());
			pstmt1.setInt(4, vo.getPrice());
			
			count = pstmt1.executeUpdate();
	
		} catch (SQLException e) {
			System.out.println("error: " + e);
		} 
		return count;
		
	}

	public List<OrderBookVo> findBooksByNoAndUserNo(Long orderNo, Long userNo) {
		List<OrderBookVo> result = new ArrayList<>();
		
		try ( 
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(
					"select b.orders_no, b.quantity, b.price, a.no, a.title" +
					"  from book a, orders_book b, orders c" +
					" where a.no = b.book_no" +
					"   and b.orders_no = c.no" +
					"   and b.orders_no = ?" +
					"   and c.user_no = ?");
		) {
			pstmt.setLong(1, orderNo);
			pstmt.setLong(2, userNo);
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Long ordersNo = rs.getLong(1);
				int quantity = rs.getInt(2);
				int price = rs.getInt(3);
				Long bookNo = rs.getLong(4);
				String bookTitle = rs.getString(5);
				
				OrderBookVo vo = new OrderBookVo();
				vo.setOrderNo(ordersNo);
				vo.setQuantity(quantity);
				vo.setPrice(price);
				vo.setBookNo(bookNo);
				vo.setBookTitle(bookTitle);
				
				result.add(vo);
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		return result;
	}
}
