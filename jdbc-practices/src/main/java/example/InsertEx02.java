package example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertEx02 {
	public static void main(String[] args) {
		insert("개발1팀");
		insert("개발2팀");
	}

	public static boolean insert(String departmentName) {
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
			String sql = "insert into department values(null, ?)"; //PreparedStatement가 Statement와 다른 점! +) 이외 보안에 더 좋음
			pstmt = conn.prepareStatement(sql);
			
			// 4. Parameter Binding
			pstmt.setString(1, departmentName); //DB에선 1부터 인덱스 시작
			
			// 5. SQL 실행 
			int count = pstmt.executeUpdate(); //주의! JDBC가 바인딩 시켜놓은 SQL로 쿼리 날려야 하므로 인자값 x
			
			result = count == 1;
			
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
