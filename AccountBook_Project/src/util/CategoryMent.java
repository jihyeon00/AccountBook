//카테고리 멘트 입력하는 코드


package util;

import java.sql.Connection;
import java.sql.PreparedStatement;

import java.util.Scanner;

public class CategoryMent {
	
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		Connection conn = DBManager.getConnection();
		String sql=null;
		PreparedStatement pstmt=null;
		Category[] cg = Category.values();
		
		int a = 1;
		while(a==1) {
		System.out.println("\n[카테고리 선택]");
		System.out.println("--------------------------------------------------------------");
		int i = 0;
		for (Category type : cg) {
			++i;
			System.out.print(i + "." + type + " ");
			if (i % 5 == 0) {
				System.out.println("\n");
			}
		}
		System.out.println("--------------------------------------------------------------");
		System.out.println("카테고리 번호를 입력하세요. ");
		System.out.print("입력: ");
		int cnum = scan.nextInt();

		while (cnum > 20 || cnum <= 0) {
			System.out.print("재입력: ");
			cnum = scan.nextInt();
		}

		System.out.println("--------------------------------------------------------------");
		System.out.println("내용을 입력하세요");
		System.out.print("입력: ");
		scan.nextLine();
		String na =scan.nextLine();
		
		

		
		try {
			sql = "INSERT INTO CATEGORY_MENT VALUES (?, ?) ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, cnum);
			pstmt.setString(2, na);
			pstmt.executeUpdate();
			
			System.out.println("저장됨");
			System.out.print("1.다시입력: ");
			a=scan.nextInt();
			
		}catch(Exception e){}

	}
		scan.close();
	}
}
