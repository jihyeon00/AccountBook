//카테고리 값 데이터베이스에 저장하는 클래스
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Category_Low {

	public static void main(String[] args) {
		Connection conn;
		Category[] cg = Category.values();
		
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			
			conn = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521/orcl",
					"moneybook",
					"1234"
					);
				for(int i=0;i<cg.length;i++) {	
				String sql=	""+
							"INSERT INTO category (CATEGORY_SEQ, CATEGORY_NAME) "+
							"VALUES (?, ?)";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, i+1);
				pstmt.setString(2, cg[i].name());
				
				
				pstmt.executeUpdate();
				pstmt.close();
				}
				
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
