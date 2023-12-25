package util;

import java.sql.Connection; //DB 연결
import java.sql.DriverManager; //DB 관리
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//DB 관리(연결/닫기)
public class DBManager {

	// DB 연결
	public static Connection getConnection() {
		Connection conn = null;
		try {
//			(1단계) JDBC 드라이버 로드
			Class.forName("oracle.jdbc.OracleDriver");

//			(2단계) 데이터 베이스 연결 객체 생성
			// 데이터베이스 연결 정보
			String url = "jdbc:oracle:thin:@localhost:1521/orcl";
			String uid = "moneybook";
			String pass = "1234";

			conn = DriverManager.getConnection(url, uid, pass);

			// catch(){} : 예외가 발생했을 때 처리 과정
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	// DB 닫기
	public static void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (conn != null) {
				conn.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	// select DB 닫기
	public static void selectClose(Connection conn, PreparedStatement pstmt, ResultSet rs) {
			try {
				if(rs != null) {
					rs.close();
				}
				if(pstmt != null) {
					pstmt.close();
				}
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
					System.out.println("예외 발생 시 처리할 코드 : select 조회");
			}
		
	}
	// insert, update, delete DB 닫기
	public static void IUDClose(Connection conn, PreparedStatement pstmt) {
		try {
			if(pstmt != null) {
				pstmt.close();
			}
			if(conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("예외 발생 시 처리할 코드 : insert, update, delete");
		}
		
	}
	
}
