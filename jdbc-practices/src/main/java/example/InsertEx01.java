package example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

// 이 코드의 문제점 : 자바와 sql 두 개가 분리되어 있지 않음 
public class InsertEx01 {
	public static void main(String[] args) {
		insert("기획1팀");
		insert("기획2팀");
	}

	public static boolean insert(String departmentName) {
		boolean result = false;
		Connection conn = null;
		Statement stmt = null;
		
		try {
			// 1. JDBC Driver 로딩
			Class.forName("org.mariadb.jdbc.Driver"); //method Area에 올라옴
			
			// 2. 연결하기
			String url = "jdbc:mariadb://192.168.56.5:3306/webdb";
			conn = DriverManager.getConnection(url, "webdb", "webdb");

			// 3. Statement 생성하기
			stmt = conn.createStatement();
			
			// 4. SQL 실행
			String sql = "insert into department values(null, '" + departmentName + "')";
			int count = stmt.executeUpdate(sql); //insert, delete, update => executeUpdate. 데이터를 변경한다는 의미
			
			result = count == 1;
			
		} catch (ClassNotFoundException e) { //jar가 Classpath에 없으면 에러남
			System.out.println("드라이버 로딩 실패: " + e);
		} catch (SQLException e) {
			System.out.println("error: " + e);
		} finally {
			try { //생성 역순으로 자원 해제
				if (stmt != null) {
					stmt.close();
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
