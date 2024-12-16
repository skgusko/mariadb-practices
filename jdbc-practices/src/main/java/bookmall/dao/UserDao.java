package bookmall.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bookmall.vo.UserVo;

public class UserDao {
	
	public int insert(UserVo vo) {
		int count = 0;
		
		Connection conn = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();

			String sql = 
					" insert" + 
					"   into user" +
					" values (null, ?, ?, ?, ?)"; 
			
			pstmt1 = conn.prepareStatement(sql);
			
			pstmt1.setString(1, vo.getName()); 
			pstmt1.setString(2, vo.getEmail());
			pstmt1.setString(3, vo.getPassword());
			pstmt1.setString(4, vo.getPhone());
			
			count = pstmt1.executeUpdate();
			
			// Auto Increment 인 'no' 컬럼의 데이터값 vo 객체의 no 필드에 할당
            sql = "select last_insert_id()";

            pstmt2 = conn.prepareStatement(sql);
            rs = pstmt2.executeQuery();

            Long no = 0L;
            while (rs.next()) {
                no = rs.getLong(1);
            }
            vo.setNo(no);
			
		} catch (SQLException e) {
			System.out.println("error: " + e);
		} finally {
			try { 
				if (rs != null) {
					rs.close();
				}
				if (pstmt2 != null) {
					pstmt2.close();
				}
				if (pstmt1 != null) {
					pstmt1.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return count;
	}
	
	public List<UserVo> findAll() {
		
		List<UserVo> result = new ArrayList<UserVo>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();

			String sql = 
					" select no, name, email, password, phone" +
					"   from user";
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Long no = rs.getLong(1);
				String name = rs.getString(2);
				String email = rs.getString(3);
				String password = rs.getString(4);
				String phone = rs.getString(5);
				
				UserVo vo = new UserVo(no, name, email, password, phone);
				vo.setNo(no);
				vo.setName(name);
				vo.setEmail(email);
				vo.setPassword(password);
				vo.setPhone(phone);
				
				result.add(vo);
			}
			
		} catch (SQLException e) {
			System.out.println("error: " + e);
		} finally {
			try { 
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}

	public boolean deleteByNo(Long no) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();

			String sql = 
					" delete" + 
					"   from user" +
					" where no = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, no);
			
			int count = pstmt.executeUpdate();
			
			result = count == 1;
			
		} catch (SQLException e) {
			System.out.println("error: " + e);
		} finally {
			try { 
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
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

}
