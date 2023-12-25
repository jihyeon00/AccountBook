package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.Scanner;

import dao.ABCreate;
import dao.ABRoutine;
import dao.Goal;
import dto.MoneyBook;
import util.Category;
import util.DBManager;

public class ABStart {
	Scanner scan = new Scanner(System.in);
	DecimalFormat decFor = new DecimalFormat("###,###");
	Category[] cg = Category.values();
	MoneyBook money = MoneyBook.getInstance();
	
	public void screen() {
		Connection conn = DBManager.getConnection();
		String sql = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			sql = "SELECT D.CATEGORY_SEQ, D.A "
					+ "FROM "
					+ "(SELECT MAX(A) S FROM "
					+ "(SELECT SUM(MONEY_WON) A, CATEGORY_SEQ "
					+ "FROM MONEYBOOK WHERE MONEY_INOUT='지출' GROUP BY CATEGORY_SEQ)) C, "
					+ "(SELECT SUM(MONEY_WON) A, CATEGORY_SEQ \r\n"
					+ "FROM MONEYBOOK WHERE MONEY_INOUT='지출' GROUP BY CATEGORY_SEQ) D "
					+ "WHERE D.A=C.S";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				money.setCATEGORY_SEQ(rs.getInt(1));
				money.setMONEY_WON(rs.getInt(2));
			}
			
			sql = "SELECT FROM CATEGORY_MENT WHERE CATEGORY_SEQ";
			System.out.printf("30일간 '%s'(지출하셨네요) 총 %s원 사용하셨습니다.\n\n",cg[money.getCATEGORY_SEQ()-1], decFor.format(money.getMONEY_WON()));
			
			
			sql = "SELECT SUM(MONEY_WON) FROM MONEYBOOK WHERE MONEY_INOUT='수입'";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			int plus=0;
			if(rs.next()) {
				if(rs.getString(1) != null) {
				plus=rs.getInt(1);
				}else if(rs.getString(1) == null){
					plus=0;
				}
			}
			sql = "SELECT SUM(MONEY_WON) FROM MONEYBOOK WHERE MONEY_INOUT='지출'";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			int miners=0;
			if(rs.next()) {
				if(rs.getString(1) != null) {
				miners=rs.getInt(1);
				} else if(rs.getString(1) == null){
					miners=0;
				}
			}
			
			System.out.printf("현재금액: %s원\n",decFor.format(plus-miners));
			
			sql = "SELECT SUM(TOKEN_AMOUNT) FROM token WHERE TOKEN_HISTORY='루틴'";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			plus=0;
			if(rs.next()) {
				if(rs.getString(1) != null) {
				plus=rs.getInt(1);
				}else if(rs.getString(1) == null){
					plus=0;
				}
			}
			sql = "SELECT SUM(TOKEN_AMOUNT) FROM token WHERE TOKEN_HISTORY !='루틴'";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			miners=0;
			if(rs.next()) {
				if(rs.getString(1) != null) {
				miners=rs.getInt(1);
				} else if(rs.getString(1) == null){
					miners=0;
				}
			}
			System.out.printf("현재토큰수: %s개\n",decFor.format(plus-miners));
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		DBManager.close(conn, pstmt, rs);
	}
	
	
	public void start() {
		
		System.out.println("\n[가계부 및 자기개발 프로그램]");
		System.out.println("------------------------------------------------------------------------");
		screen();
		System.out.println("------------------------------------------------------------------------");
		System.out.println("메뉴 : 1.시작하기 | 2.종료하기");
		System.out.print("입력: ");
		int manu = scan.nextInt();
		if(manu==1) {
			stManu();
		}
		exit();
	}
	
	public void stManu() {
		Connection conn = DBManager.getConnection();
		String sql = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ABCreate create = new ABCreate();
		ABRoutine routine = new ABRoutine();
		Goal goal = new Goal();
		
		try {
			sql = "SELECT TO_CHAR(MONEY_DATE,'YYYY-MM-DD') DATE1, CATEGORY_SEQ, MONEY_INOUT, MONEY_WON FROM MONEYBOOK WHERE to_char(SYSDATE,'yy-mm-dd') = to_char(MONEY_DATE,'yy-mm-dd')";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			System.out.printf("\n[오늘 등록한 내용]\n");
			System.out.println("------------------------------------------------------------------------");
			System.out.printf("%s \t\t %s \t %s \t %s\n", "날짜", "카테고리", "수입/지출", "금액");
			System.out.println("------------------------------------------------------------------------");
			
			while(rs.next()) {
				if (cg[rs.getInt("CATEGORY_SEQ")-1].name().length() >= 5) {
					System.out.printf("%s \t %s\t %s \t\t %s원\n", rs.getString(1),
							cg[rs.getInt("CATEGORY_SEQ")-1].name(), rs.getString("MONEY_INOUT"),
							decFor.format(rs.getInt("MONEY_WON")));
				} else {
					System.out.printf("%s \t %s\t\t %s \t\t %s원\n", rs.getString(1),
							cg[rs.getInt("CATEGORY_SEQ")-1].name(), rs.getString("MONEY_INOUT"),
							decFor.format(rs.getInt("MONEY_WON")));
				}
			}
			System.out.println("------------------------------------------------------------------------");
			System.out.println("메뉴: 1.내역관리 | 2.소비분석 | 3.루틴관리 | 4.목표관리 | 5.나가기");
			System.out.print("입력: ");
			int manu = scan.nextInt();
			switch(manu) {
				case 1:
					create.mainManu();
					break;
				case 2:
					exit();
					break;
				case 3:
					routine.rtManu();
					break;
				case 4:
					goal.goalManu();
					break;
				default:
					start();
					break;
			}
		}catch(Exception e){e.printStackTrace();}
		DBManager.close(conn, pstmt, rs);
	}
	
	public void exit() {
		System.out.println("종료합니다.");
		System.exit(0);
	}
	
	public static void main(String[] args) {
		ABStart ab = new ABStart();
		ab.start();
	}

}
