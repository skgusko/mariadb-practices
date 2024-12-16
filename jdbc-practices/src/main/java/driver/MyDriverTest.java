package driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDriverTest {

	public static void main(String[] args) {
		Connection conn = null;
		
		try {
			// 1. JDBC Driver 로딩
			Class.forName("driver.MyDriver"); //method Area에 올라옴. 로딩될 때 실행됨
			
			// 2. 연결하기
			String url = "jdbc:mydb://127.0.0.1:1234/webdb";
			conn = DriverManager.getConnection(url, "webdb", "webdb"); //getConeection 으로 MyDriver에게 연결 정보 줌

			
			System.out.println("연결 성공!!");
		} catch (ClassNotFoundException e) { //jar가 Classpath에 없으면 에러남
			System.out.println("드라이버 로딩 실패: " + e);
		} catch (SQLException e) { //jar가 Classpath에 없으면 에러남
			System.out.println("error: " + e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

}
