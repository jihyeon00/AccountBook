package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import util.DBManager;

public class Test {
	
	
	public static void main(String[] args) {
		Connection conn = DBManager.getConnection();
		String sql=null;
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		
		try {
			sql = "SELECT * FROM MONEYBOOK";
			String sql2 = "SELECT * FROM ROUTINE ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			
			
			if(rs.next()) {
				System.out.println(rs.getInt(1));
				
				PreparedStatement pstmt2 = conn.prepareStatement(sql2);
				ResultSet rs2 = pstmt2.executeQuery();
				if(rs2.next()) {
					System.out.println(rs2.getInt(1));
				}
			
			}
			
			
		}catch(Exception e){}
		
		
		
	}
	
}
