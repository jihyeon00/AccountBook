//가계부 내역 등록
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Scanner;

import dto.MoneyBook;
import main.ABStart;
import util.Category;
import util.DBManager;
import util.InOut;

public class ABCreate {
	Scanner scan = new Scanner(System.in);
	DecimalFormat decFor = new DecimalFormat("###,###");
	MoneyBook money = MoneyBook.getInstance();
	Category[] cg = Category.values();
	InOut[] io = InOut.values();

	public void mainManu() {
		ABStart back = new ABStart();
		System.out.println("\n[가계부 내역등록]");
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
			abInsert();
			break;
		case "2":
			abUpdate();
			break;
		case "3":
			abDelete();
			break;
		case "4":
			back.stManu();
			break;
		}
	}

	public void abInsert() {
		Connection conn = DBManager.getConnection();
		
		System.out.println("\n[가계부 내역입력]");
		System.out.println("--------------------------------------------------");
		System.out.println("20XX 12 14 | 20XX.12.14 | 20XX/12/14 | 20XX-12-14");
		System.out.println("[날짜 입력]");
		System.out.print("날짜: ");
		scan.nextLine();
		money.setMONEY_DATE(scan.nextLine());

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

		money.setCATEGORY_SEQ(cnum);

		System.out.println("\n[수입/지출 선택]");
		System.out.println("1.수입 | 2.지출 ");
		System.out.print("입력: ");
		int in = scan.nextInt();

		while (in > 2 || in <= 0) {
			System.out.print("재입력: ");
			in = scan.nextInt();
		}
		money.setMONEY_INOUT(io[in - 1].name());

		System.out.println("\n[금액 입력]");
		System.out.print("금액: ");
		money.setMONEY_WON(scan.nextInt());

		System.out.println("\n[입력된 내용]");
		System.out.println("------------------------------------------------------------------------");
		System.out.printf("%s\t\t %s \t %s \t %s\n", "날짜", "카테고리", "수입/지출", "금액");
		System.out.println("------------------------------------------------------------------------");

		if (cg[cnum - 1].name().length() >= 5) {
			System.out.printf("%s \t %s\t %s \t\t %s원\n", money.getMONEY_DATE(), cg[money.getCATEGORY_SEQ() - 1].name(),
					money.getMONEY_INOUT(), decFor.format(money.getMONEY_WON()));
		} else {
			System.out.printf("%s \t %s \t\t %s \t\t %s원\n", money.getMONEY_DATE(),
					cg[money.getCATEGORY_SEQ() - 1].name(), money.getMONEY_INOUT(),
					decFor.format(money.getMONEY_WON()));
		}
		System.out.println("------------------------------------------------------------------------");
		System.out.println("위 내용을 저장한다. : 1.확인 | 2.취소");
		System.out.print("선택 : ");
		String manu = scan.next();

		if (manu.equals("1")) {
			try {
				String sql = "" + "INSERT INTO moneybook (MONEY_SEQ, MONEY_DATE, CATEGORY_SEQ, MONEY_INOUT, MONEY_WON)"
						+ "VALUES (MONEY_SEQ.nextval, ?, ?, ?, ?)";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, money.getMONEY_DATE());
				pstmt.setInt(2, money.getCATEGORY_SEQ());
				pstmt.setString(3, money.getMONEY_INOUT());
				pstmt.setInt(4, money.getMONEY_WON());

				pstmt.executeUpdate();
				System.out.println("\n입력한 내용이 저장되었습니다.");
				DBManager.close(conn, pstmt, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		mainManu();
	}
	
	public int[] abList(String date) {
		Connection conn = DBManager.getConnection();
		int array[]=null;
		
		money.setMONEY_DATE(date);
		try {
			
			String sql = ""
					+ "SELECT MONEY_SEQ, CATEGORY_SEQ, MONEY_INOUT, MONEY_WON "
					+ "FROM moneybook " 
					+ "WHERE MONEY_DATE = ? "
					;
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, money.getMONEY_DATE());
			ResultSet rs = pstmt.executeQuery();

			PreparedStatement pstmt2 = conn.prepareStatement(sql);
			pstmt2.setString(1, money.getMONEY_DATE());
			ResultSet rs2 = pstmt2.executeQuery();
			int num = 0;
			while (rs2.next()) {
				++num;
			}
			array = new int[num];
			
			if(array.length==0) {
				System.out.println("\n내용이 없습니다.\n");
				mainManu();
			}
			
			rs2.close();
			pstmt2.close();
			
			int num2 = 0;
			System.out.println("------------------------------------------------------------------------");
			System.out.printf("\n[%s 가계부 내역]\n",money.getMONEY_DATE());
			System.out.println("------------------------------------------------------------------------");
			System.out.printf("%s \t %s \t %s \t %s\n", "번호", "카테고리", "수입/지출", "금액");
			System.out.println("------------------------------------------------------------------------");
			while (rs.next()) {
				array[num2] = rs.getInt("MONEY_SEQ");
				++num2;
				money.setMONEY_SEQ(rs.getInt("MONEY_SEQ"));
				money.setCATEGORY_SEQ(rs.getInt("CATEGORY_SEQ"));
				money.setMONEY_INOUT(rs.getString("MONEY_INOUT"));
				money.setMONEY_WON(rs.getInt("MONEY_WON"));
				if (cg[money.getCATEGORY_SEQ() - 1].name().length() >= 5) {
					System.out.printf("%s \t %s\t %s \t\t %s원\n", num2,
							cg[money.getCATEGORY_SEQ() - 1].name(), money.getMONEY_INOUT(),
							decFor.format(money.getMONEY_WON()));
				} else {
					System.out.printf("%s \t %s \t\t %s \t\t %s원\n", num2,
							cg[money.getCATEGORY_SEQ() - 1].name(), money.getMONEY_INOUT(),
							decFor.format(money.getMONEY_WON()));
				}
			}
			System.out.println("------------------------------------------------------------------------");	
			DBManager.close(conn, pstmt, rs);
			
			}catch(Exception e) {
				e.printStackTrace();
			}
		return array;
	}

	public void abUpdate() {
		Connection conn = DBManager.getConnection();
		String sql=null;
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		
		System.out.println("\n[가계부 수정]");
		System.out.println("------------------------------------------------------------------------");
		System.out.println("20XX 12 14 | 20XX.12.14 | 20XX/12/14 | 20XX-12-14");
		System.out.println("[수정할 날짜 입력]");
		System.out.print("날짜: ");
		scan.nextLine();
		String day = scan.nextLine();
		int array[]=abList(day);
		
		try {
			System.out.println("\n[수정할 번호]");
			System.out.print("번호: ");
			int num2 = scan.nextInt();
			while (num2 > array.length || num2 <= 0) {
				System.out.print("재입력: ");
				num2 = scan.nextInt();
			}
			money.setMONEY_SEQ(array[num2-1]);
			
			System.out.println("\n날짜를 수정하시겠습니까?");
			System.out.println("1.네 | 2.아니오");
			System.out.print("입력: ");
			int num3=scan.nextInt();
			
			while(num3>2 || num3<=0) {
				System.out.print("재입력: ");
				num3=scan.nextInt();
			}
			
			if(num3==1) {
				System.out.println("\n--------------------------------------------------------------");
				System.out.println("20XX 12 14 | 20XX.12.14 | 20XX/12/14 | 20XX-12-14");
				System.out.println("[수정할 날짜 입력]");
				System.out.print("날짜: ");
				scan.nextLine();
				money.setMONEY_DATE(scan.nextLine());
			} else if(num3==2) {
				money.setMONEY_DATE(day);
			}
			
			System.out.println("\n카테고리를 수정하시겠습니까?");
			System.out.println("1.네 | 2.아니오");
			System.out.print("입력: ");
			num3 = scan.nextInt();
			
			while(num3>2 || num3<=0) {
				System.out.print("재입력: ");
				num3=scan.nextInt();
			}
			
			int cnum=0;
			if(num3==1) {
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
				cnum = scan.nextInt();

				while (cnum > 20 || cnum <= 0) {
					System.out.print("재입력: ");
					cnum = scan.nextInt();
				}
				money.setCATEGORY_SEQ(cnum);
			} else if(num3==2) {
				sql = "SELECT CATEGORY_SEQ FROM MONEYBOOK WHERE MONEY_SEQ=? ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, money.getMONEY_SEQ());
				rs = pstmt.executeQuery();
				if(rs.next()) {
					money.setCATEGORY_SEQ(rs.getInt("CATEGORY_SEQ"));
					cnum = money.getCATEGORY_SEQ();
				}
			}
		
			System.out.println("\n[수입/지출 선택]");
			System.out.println("1.수입 | 2.지출 ");
			System.out.print("입력: ");
			int in = scan.nextInt();

			while (in > 2 || in <= 0) {
				System.out.print("재입력: ");
				in = scan.nextInt();
			}
			money.setMONEY_INOUT(io[in - 1].name());
			
			
			System.out.println("\n금액을 수정하시겠습니까?");
			System.out.println("1.네 | 2.아니오");
			System.out.print("입력: ");
			num3 = scan.nextInt();
			
			while(num3>2 || num3<=0) {
				System.out.print("재입력: ");
				num3=scan.nextInt();
			}
			
			if(num3==1) {
				System.out.println("\n[금액 입력]");
				System.out.print("금액: ");
				money.setMONEY_WON(scan.nextInt());
			} else if(num3==2) {
				sql = "SELECT MONEY_WON FROM MONEYBOOK WHERE MONEY_SEQ=? ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, money.getMONEY_SEQ());
				rs = pstmt.executeQuery();
				if(rs.next()) {
					money.setCATEGORY_SEQ(rs.getInt("MONEY_WON"));
				}
			}
			

			System.out.println("\n------------------------------------------------------------------------");
			System.out.printf("\n[%d번의 수정할 내용]\n",num2);
			System.out.println("------------------------------------------------------------------------");
			System.out.printf("%s \t %s \t\t %s \t %s \t %s\n", "비교", "날짜", "카테고리", "수입/지출", "금액");
			System.out.println("------------------------------------------------------------------------");

			sql = "SELECT TO_CHAR(MONEY_DATE,'YYYY-MM-DD') DATE1, CATEGORY_SEQ, MONEY_INOUT, MONEY_WON "
					+ "FROM moneybook " + "WHERE MONEY_SEQ=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, money.getMONEY_SEQ());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if (cg[rs.getInt("CATEGORY_SEQ")-1].name().length() >= 5) {
					System.out.printf("%s \t %s \t %s\t %s \t\t %s원\n", "[수정전]", rs.getString(1),
							cg[rs.getInt("CATEGORY_SEQ")-1].name(), rs.getString("MONEY_INOUT"),
							decFor.format(rs.getInt("MONEY_WON")));
				} else {
					System.out.printf("%s \t %s \t %s\t\t %s \t\t %s원\n", "[수정전]", rs.getString(1),
							cg[rs.getInt("CATEGORY_SEQ")-1].name(), rs.getString("MONEY_INOUT"),
							decFor.format(rs.getInt("MONEY_WON")));
				}

				
				if (cg[cnum - 1].name().length() >= 5) {
					System.out.printf("%s \t %s \t %s\t %s \t\t %s원\n", "[수정후]", money.getMONEY_DATE(),
							cg[cnum - 1].name(), money.getMONEY_INOUT(),
							decFor.format(money.getMONEY_WON()));
				} else {
					System.out.printf("%s \t %s \t %s \t\t %s \t\t %s원\n", "[수정후]", money.getMONEY_DATE(),
							cg[cnum - 1].name(), money.getMONEY_INOUT(),
							decFor.format(money.getMONEY_WON()));
				}
				System.out.println("------------------------------------------------------------------------");
				System.out.println("위 내용을 수정한다. : 1.확인 | 2.취소");
				System.out.print("선택 : ");
				String manu = scan.next();

				if (manu.equals("1")) {

					sql = "" + "UPDATE moneybook SET money_date=?, CATEGORY_SEQ=?, money_inout=?, money_won=? "
							+ "WHERE MONEY_SEQ=?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, money.getMONEY_DATE());
					pstmt.setInt(2, money.getCATEGORY_SEQ());
					pstmt.setString(3, money.getMONEY_INOUT());
					pstmt.setInt(4, money.getMONEY_WON());
					pstmt.setInt(5, money.getMONEY_SEQ());
					pstmt.executeUpdate();
					System.out.println("\n가계부가 수정되었습니다.\n");
				}

			}
			DBManager.close(conn, pstmt, rs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mainManu();
	}
	
	public void abDelete() {
		Connection conn = DBManager.getConnection();
		String sql = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		System.out.println("\n[가계부 삭제]");
		System.out.println("------------------------------------------------------------------------");
		System.out.println("20XX 12 14 | 20XX.12.14 | 20XX/12/14 | 20XX-12-14");
		System.out.println("[삭제할 날짜 입력]");
		System.out.print("날짜: ");
		scan.nextLine();
		String day = scan.nextLine();
		int array[]=abList(day);
		
		try {
			System.out.println("\n[삭제할 번호]");
			System.out.print("번호: ");
			int num2 = scan.nextInt();
			while (num2 > array.length || num2 <= 0) {
				System.out.print("재입력: ");
				num2 = scan.nextInt();
			}
			money.setMONEY_SEQ(array[num2-1]);
			
			System.out.println("\n------------------------------------------------------------------------");
			System.out.printf("\n[%d번의 삭제할 내용]\n",num2);
			System.out.println("------------------------------------------------------------------------");
			System.out.printf("%s \t %s \t\t %s \t %s \t %s\n", "비고", "날짜", "카테고리", "수입/지출", "금액");
			System.out.println("------------------------------------------------------------------------");

			sql = "" + "SELECT MONEY_SEQ,  TO_CHAR(MONEY_DATE,'YYYY-MM-DD') DATE1, CATEGORY_SEQ, MONEY_INOUT, MONEY_WON "
					+ "FROM moneybook " + "WHERE MONEY_SEQ=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, money.getMONEY_SEQ());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if (cg[rs.getInt("CATEGORY_SEQ")-1].name().length() >= 5) {
					System.out.printf("%s \t %s \t %s\t %s \t\t %s원\n", "[삭제]", rs.getString(2),
							cg[rs.getInt("CATEGORY_SEQ")-1].name(), rs.getString("MONEY_INOUT"),
							decFor.format(rs.getInt("MONEY_WON")));
				} else {
					System.out.printf("%s \t %s \t %s\t\t %s \t\t %s원\n", "[삭제]", rs.getString(2),
							cg[rs.getInt("CATEGORY_SEQ")-1].name(), rs.getString("MONEY_INOUT"),
							decFor.format(rs.getInt("MONEY_WON")));
				}
				System.out.println("------------------------------------------------------------------------");
				System.out.println("위 내용을 삭제한다. : 1.확인 | 2.취소");
				System.out.print("선택 : ");
				String manu = scan.next();
				
				if(manu.equals("1")) {
						sql = 	""+
										"DELETE FROM moneybook WHERE MONEY_SEQ=?";
						pstmt = conn.prepareStatement(sql);
						pstmt.setInt(1, money.getMONEY_SEQ());
						
						pstmt.executeUpdate();
						System.out.println("\n가계부가 삭제되었습니다.\n");
				}
			DBManager.close(conn, pstmt, rs);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		mainManu();
	}

	public static void main(String[] args) {
		ABCreate manager = new ABCreate();
		manager.mainManu();
	}
}