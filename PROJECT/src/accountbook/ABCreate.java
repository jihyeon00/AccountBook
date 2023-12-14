//가계부 내역 등록
package accountbook;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ABCreate {
	Scanner scan = new Scanner(System.in);
	Connection conn;
	
	ABCreate(){
			try {
				Class.forName("oracle.jdbc.OracleDriver");
				
				conn = DriverManager.getConnection(
						"jdbc:oracle:thin:@localhost:1521/orcl",
						"moneybook",
						"1234"
						);
			}catch(Exception e) {
				e.printStackTrace();
			}
	}
	
	
	public void mainManu() {
		System.out.println();
		System.out.println("[가계부 내역등록]");
		System.out.println("--------------------------------------------------");
		System.out.println("들어갈 메뉴의 번호를 입력하세요.");
		System.out.println("--------------------------------------------------");
		System.out.println("메뉴 : 1.내역입력 | 2.내역수정 | 3.내역삭제 | 4.돌아가기 ");
		System.out.println("--------------------------------------------------");
		System.out.print("선택: ");
		String manuno = scan.next();
		System.out.println();

		switch (manuno) {
		case "1":
			inSert();
			break;
		case "2":
			update();
			break;
		case "3":
			delete();
			break;
		case "4":
			exit();
			break;
		}
	}
	
	public void inSert(){
		MoneyBook mb = new MoneyBook();
		Category[] cg = Category.values();
		InOut[] io = InOut.values();
		System.out.println("[가계부 내역입력]");
		System.out.println("--------------------------------------------------");
		System.out.println("[날짜 입력]");
		System.out.println("예시(20xx-10-25)");
		System.out.print("날짜: ");
		mb.setMONEY_DATE(scan.next());
		
		System.out.println("\n[카테고리 선택]");
		System.out.println("--------------------------------------------------------------");
		int i=0;
		for(Category type : cg) {
			++i;
			System.out.print(i+"."+type+" ");
			if(i%5==0) {
				System.out.println("\n");
			}
		}
		System.out.println("--------------------------------------------------------------");
		System.out.println("카테고리 번호를 입력하세요. ");
		System.out.print("입력: ");
		int c = scan.nextInt();
		System.out.println("선택한 카테고리: "+c+"."+cg[c-1]);
		mb.setMONEY_CATEGORY(cg[c-1].name());
		
		System.out.println("\n[수입/지출 선택]");
		System.out.println("1.수입 | 2.지출 ");
		System.out.print("입력: ");
		int in = scan.nextInt();
		mb.setMONEY_INOUT(io[in-1].name());
		
		System.out.println("\n[금액 입력]");
		System.out.print("금액: ");
		mb.setMONEY_WON(scan.nextInt());
		
		
		System.out.println("----------------------------------------------------------------");
		System.out.printf("%-13s %-15s %-8s %-10s\n", 
													mb.getMONEY_DATE(),
													mb.getMONEY_CATEGORY(),
													mb.getMONEY_INOUT(),
													mb.getMONEY_WON());
		System.out.println("--------------------------------------------------");
		System.out.println("위 내용을 저장한다. : 1.확인 | 2.취소");
		System.out.print("선택 : ");
		String menu = scan.next();
		
		if(menu.equals("1")) {
			try {
				String sql=	""+
							"INSERT INTO moneybook (MONEY_SEQ, MONEY_DATE, MONEY_CATEGORY, MONEY_INOUT, MONEY_WON)"+
							"VALUES (mon_seq.nextval, ?, ?, ?, ?)";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, mb.getMONEY_DATE());
				pstmt.setString(2, mb.getMONEY_CATEGORY());
				pstmt.setString(3, mb.getMONEY_INOUT());
				pstmt.setInt(4, mb.getMONEY_WON());
				
				
				pstmt.executeUpdate();
				System.out.println("\n입력한 내용이 저장되었습니다.");
				pstmt.close();
			}catch(Exception e) {
				e.printStackTrace();
				exit();
			}
		}
		mainManu();
	}
	
	public void update() {
		System.out.println();
		System.out.println("[가계부 일주일간 내역]");
		System.out.println("----------------------------------------------------------------");
		System.out.printf("%s \t %s \t\t %s \t %s \t %s\n","번호","날짜","카테고리","수입/지출","금액");
		System.out.println("----------------------------------------------------------------");
		
		try {
			String sql = 	""+
							"SELECT MONEY_SEQ,  TO_CHAR(MONEY_DATE,'YYYY-MM-DD') DATE1, MONEY_CATEGORY, MONEY_INOUT, MONEY_WON "+
							"FROM moneybook "+
							"WHERE MONEY_DATE BETWEEN SYSDATE-7 AND SYSDATE "+
							"ORDER BY MONEY_SEQ "
							;
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				MoneyBook money = new MoneyBook();
				money.setMONEY_SEQ(rs.getInt("MONEY_SEQ"));
				money.setMONEY_DATE(rs.getString(2));
				money.setMONEY_CATEGORY(rs.getString("MONEY_CATEGORY"));
				money.setMONEY_INOUT(rs.getString("MONEY_INOUT"));
				money.setMONEY_WON(rs.getInt("MONEY_WON"));
				System.out.printf("%s \t %s \t %s \t %s \t %s\n", 
																money.getMONEY_SEQ(), 
																money.getMONEY_DATE(),
																money.getMONEY_CATEGORY(),
																money.getMONEY_INOUT(),
																money.getMONEY_WON());
			}
			rs.close();
			pstmt.close();
		}catch(SQLException e) {
			e.printStackTrace();
			exit();
		}
		
		System.out.println("----------------------------------------------------------------");
		MoneyBook mb = new MoneyBook();
		System.out.println();
		System.out.println("[가계부 수정]");
		System.out.println("수정할 번호를 입력하세요.");
		System.out.print("번호: ");
		mb.setMONEY_SEQ(scan.nextInt());
		
		System.out.println("예시(20xx-10-25)");
		System.out.print("날짜: ");
		mb.setMONEY_DATE(scan.next());
		
		System.out.print("카테고리: ");
		mb.setMONEY_CATEGORY(scan.next());
		
		System.out.print("수입/지출: ");
		mb.setMONEY_INOUT(scan.next());
		
		System.out.print("금액: ");
		mb.setMONEY_WON(scan.nextInt());
		
		System.out.println("----------------------------------------------------------------");
		System.out.println("위 내용을 수정한다. : 1.확인 | 2.취소");
		System.out.print("선택 : ");
		String menu = scan.next();
		
		if(menu.equals("1")) {
			try {
				String sql = 	""+
								"UPDATE moneybook SET money_date=?, money_category=?, money_inout=?, money_won=? "+
								"WHERE MONEY_SEQ=?";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, mb.getMONEY_DATE());
				pstmt.setString(2, mb.getMONEY_CATEGORY());
				pstmt.setString(3, mb.getMONEY_INOUT());
				pstmt.setInt(4, mb.getMONEY_WON());
				pstmt.setInt(5, mb.getMONEY_SEQ());
				pstmt.executeUpdate();
				pstmt.close();
				System.out.println();
				System.out.println("가계부가 수정되었습니다.");
				System.out.println();
			}catch(Exception e) {
				e.printStackTrace();
				exit();
			}
		}
		mainManu();
	}
	
	public void delete() {
		System.out.println();
		System.out.println("[가계부 일주일간 내역]");
		System.out.println("----------------------------------------------------------------");
		System.out.printf("%-6s %-12s %-18s %-8s %-1s\n","번호","날짜","카테고리","수입/지출","금액");
		System.out.println("----------------------------------------------------------------");
		
		try {
			String sql = 	""+
							"SELECT MONEY_SEQ,  TO_CHAR(MONEY_DATE,'YYYY-MM-DD') DATE1, MONEY_CATEGORY, MONEY_INOUT, MONEY_WON "+
							"FROM moneybook "+
							"WHERE MONEY_DATE BETWEEN SYSDATE-7 AND SYSDATE "+
							"ORDER BY MONEY_SEQ "
							;
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				MoneyBook money = new MoneyBook();
				money.setMONEY_SEQ(rs.getInt("MONEY_SEQ"));
				money.setMONEY_DATE(rs.getString(2));
				money.setMONEY_CATEGORY(rs.getString("MONEY_CATEGORY"));
				money.setMONEY_INOUT(rs.getString("MONEY_INOUT"));
				money.setMONEY_WON(rs.getInt("MONEY_WON"));
				System.out.printf("%-7s %-13s %-20s %-8s %-10s\n", 
																money.getMONEY_SEQ(), 
																money.getMONEY_DATE(),
																money.getMONEY_CATEGORY(),
																money.getMONEY_INOUT(),
																money.getMONEY_WON());
			}
			rs.close();
			pstmt.close();
		}catch(SQLException e) {
			e.printStackTrace();
			exit();
		}
		
		MoneyBook mb = new MoneyBook();
		System.out.println();
		System.out.println("[가계부 내역 삭제]");
		System.out.println("삭제할 번호를 입력하세요.");
		System.out.print("번호: ");
		mb.setMONEY_SEQ(scan.nextInt());
		
		try {
			String sql = 	""+
							"SELECT MONEY_SEQ, TO_CHAR(MONEY_DATE,'YYYY-MM-DD') DATE1, MONEY_CATEGORY, MONEY_INOUT, MONEY_WON "+
							"FROM moneybook "+
							"WHERE MONEY_SEQ=? "
							;
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, mb.getMONEY_SEQ());
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				MoneyBook money = new MoneyBook();
				money.setMONEY_SEQ(rs.getInt("MONEY_SEQ"));
				money.setMONEY_DATE(rs.getString(2));
				money.setMONEY_CATEGORY(rs.getString("MONEY_CATEGORY"));
				money.setMONEY_INOUT(rs.getString("MONEY_INOUT"));
				money.setMONEY_WON(rs.getInt("MONEY_WON"));
				System.out.println("----------------------------------------------------------------");
				System.out.printf("%-7s %-13s %-20s %-8s %-10s\n", 
																money.getMONEY_SEQ(), 
																money.getMONEY_DATE(),
																money.getMONEY_CATEGORY(),
																money.getMONEY_INOUT(),
																money.getMONEY_WON());
			}
			rs.close();
			pstmt.close();
		}catch(SQLException e) {
			e.printStackTrace();
			exit();
		}
		
		
		System.out.println("----------------------------------------------------------------");
		System.out.println("위 내용을 삭제한다. : 1.확인 | 2.취소");
		System.out.print("선택 : ");
		String menu = scan.next();
		
		if(menu.equals("1")) {
			try {
				String sql = 	""+
								"DELETE FROM moneybook WHERE MONEY_SEQ=?";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, mb.getMONEY_SEQ());
				
				pstmt.executeUpdate();
				pstmt.close();
				System.out.println();
				System.out.println("가계부가 삭제되었습니다.");
				System.out.println();
			}catch(Exception e) {
				e.printStackTrace();
				exit();
			}
		}
		
		mainManu();
	}
	
	public void exit() {
		System.out.println("가계부가 종료됩니다.");
		System.exit(0);
	}
		
	public static void main(String[] args) {
		ABCreate insert = new ABCreate();
		insert.mainManu();
	}
}
