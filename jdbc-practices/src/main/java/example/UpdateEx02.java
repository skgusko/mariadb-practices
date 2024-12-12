package example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateEx02 {
	public static void main(String[] args) {
		update(new DepartmentVo(1L, "경영지원팀"));
	}

	public static boolean update(DepartmentVo vo) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			// 1. JDBC Driver 로딩
			Class.forName("org.mariadb.jdbc.Driver"); //method Area에 올라옴
			
			// 2. 연결하기
			String url = "jdbc:mariadb://192.168.56.5:3306/webdb";
			conn = DriverManager.getConnection(url, "webdb", "webdb");

			// 3. Statement 준비하기
			String sql = "update department set name = ? where id = ?";
			pstmt = conn.prepareStatement(sql);
			
			// 4. Parameter Binding
			pstmt.setString(1, vo.getName()); //DB에선 1부터 인덱스 시작
			pstmt.setLong(2, vo.getId());
			
			// 5. SQL 실행 
			int count = pstmt.executeUpdate(); //주의! JDBC가 바인딩 시켜놓은 SQL로 쿼리 날려야 하므로 인자값 x
			
			result = count == 1;
			System.out.println("SQL 실행 성공");
		} catch (ClassNotFoundException e) { //jar가 Classpath에 없으면 에러남
			System.out.println("드라이버 로딩 실패: " + e);
		} catch (SQLException e) {
			System.out.println("error: " + e);
		} finally {
			try { //생성 역순으로 자원 해제
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

}
