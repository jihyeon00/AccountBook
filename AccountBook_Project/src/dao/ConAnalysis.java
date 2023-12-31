package dao;

import java.sql.Connection;				// DB 연결
import java.sql.PreparedStatement;		// 3. DB 쿼리문 사용
import java.sql.ResultSet;				// 4. DB 쿼리문 수행 결과 관리
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import dto.conAnalysisVO;
import main.ABStart;
import util.Category;
import util.DBManager;

public class ConAnalysis {
	Scanner sc = new Scanner(System.in);
	static Category[] cg = Category.values();
	static DecimalFormat decFor = new DecimalFormat("###,###");
	
	public static void main(String[] args) {
		
		selectCategoryManu();
	}
	
	// 소비분석 메뉴를 불러오는 메서드
	public static void conAnalysisManu() {
		Scanner sc = new Scanner(System.in);
		ABStart abStart = new ABStart();
		System.out.println("==============================================================================");
		System.out.println("[소비분석]");
		System.out.println("------------------------------------------------------------------------------");
		System.out.println("1. 일별확인 | 2. 월별확인 | 3. 연별확인 | 4. 카테고리별확인 | 5. 돌아가기");
		System.out.println("------------------------------------------------------------------------------");
		System.out.println("들어갈 메뉴의 번호를 입력해주세요.");		
		System.out.println("------------------------------------------------------------------------------");
		System.out.print("입력 > ");
		int conAnalSeq = sc.nextInt();
		
		switch(conAnalSeq) {
			case 1:
				selectDayManu();
				break;
			case 2:
				selectMonthManu();
				break;
			case 3:
				selectYearManu();
				break;
			case 4:
				selectCategoryManu();
				break;
			case 5:
				abStart.stManu();
				break;
			default:
				System.out.println("------------------------------------------------------");
				System.out.println("다시 입력해주세요.");
				System.out.println("------------------------------------------------------");
				ConAnalysis.conAnalysisManu();
		}
		sc.close();
	}
	
	// 일별 내역 메뉴
	static void selectDayManu() {
		Scanner sc = new Scanner(System.in);

		System.out.println("============================================================");
		System.out.println("[일별확인]");
		System.out.println("------------------------------------------------------------");
		System.out.println("1. 모든내역 | 2. 수입내역 | 3. 지출내역 | 4. 돌아가기");
		System.out.println("------------------------------------------------------------");
		System.out.println("들어갈 메뉴의 번호를 입력해주세요.");		
		System.out.println("------------------------------------------------------------");
		System.out.print("입력 > ");
		int selectManu = sc.nextInt();
		
		switch(selectManu) {
		case 1:
			selectDayAll();
			break;
		case 2:
			selectDayIn();
			break;
		case 3:
			selectDayIn();
			break;
		case 4:
			conAnalysisManu();
			break;
		default:
			System.out.println("------------------------------------------------------------");
			System.out.println("다시 입력해주세요.");
			System.out.println("------------------------------------------------------------");
			selectDayManu();
		}
		sc.close();
	}
	
	// 일별 모든 내역
	public static String selectDayAll() {
		String selectDay;
		Scanner sc = new Scanner(System.in);
		
		System.out.println("------------------------------------------------------------");
		System.out.println("확인할 날짜를 입력해주세요.");		
		System.out.println("------------------------------------------------------------");
		System.out.print("입력(예시: 20231207) > ");
		
		selectDay = sc.next();
		
		// selectDay의 길이 8이 아닐경우와 숫자가 아닐 경우 재입력
		while (selectDay.length()!=8 | selectDay=="^[\\D]*$") {
			System.out.println("------------------------------------------------------------");
			System.out.println("확인할 날짜를 다시 입력해주세요.");		
			System.out.println("------------------------------------------------------------");

			System.out.print("입력(예시: 20231207) > ");
			selectDay = sc.next();
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			// 현재 금액을 구하기 위한 SQL
			String sql = "SELECT SUM(MONEY_WON) FROM MONEYBOOK WHERE MONEY_INOUT='수입' AND TO_CHAR(TO_DATE(MONEY_DATE),'YYYYMMDD') = ?";
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, selectDay);
			rs = pstmt.executeQuery();
			int plus = 0;
			if(rs.next()) {
				if(rs.getString(1) != null) {
				plus=rs.getInt(1);
				}else if(rs.getString(1) == null){
					plus=0;
				}
			}
			sql = "SELECT SUM(MONEY_WON) FROM MONEYBOOK WHERE MONEY_INOUT='지출' AND TO_CHAR(TO_DATE(MONEY_DATE),'YYYYMMDD') = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, selectDay);
			rs = pstmt.executeQuery();
			
			int miners=0;
			if(rs.next()) {
				if(rs.getString(1) != null) {
					miners=rs.getInt(1);
				} else if(rs.getString(1) == null){
					miners=0;
				}
			}

			sql = "SELECT rownum,TO_CHAR(M.MONEY_DATE,'YYYY/MM/DD') MONEY_DATE, "
					+ "C.CATEGORY_NAME, M.MONEY_INOUT, M.MONEY_WON "
					+ "FROM MONEYBOOK M, CATEGORY C "
					+ "WHERE M.CATEGORY_seq = C.CATEGORY_seq"
					+ " AND TO_CHAR(TO_DATE(MONEY_DATE),'YYYYMMDD') = ? "
					+ "ORDER BY MONEY_DATE";
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, selectDay);
			rs = pstmt.executeQuery();
			
			System.out.println("============================================================");
			System.out.printf("현재금액: %s원 | 수입금액: %s원 | 지출금액: %s원 \n",decFor.format(plus-miners),decFor.format(plus),decFor.format(miners));
			System.out.println("------------------------------------------------------------");
			System.out.printf("%6s %7s %7s %15s", "날짜" ,"수입/지출","금액", "카테고리\n");
			System.out.println("------------------------------------------------------------");
				if(rs.next()) {
					do{
						System.out.printf("%s %4s %13s %15s", rs.getString("money_date"), rs.getString("money_inout"),decFor.format(rs.getInt("money_won")),rs.getString("category_name"));
						System.out.println();
					}while (rs.next());
				} else {
					System.out.println("입력된 내역이 없습니다.");
				}
			System.out.println("============================================================");
			ConAnalysis.conAnalysisBack();
		} catch (Exception e) {
			e.printStackTrace();
		}
		sc.close();
		DBManager.selectClose(conn, pstmt, rs);
		
		return selectDay;
	}
	
	// 일별 수입 내역
	public static String selectDayIn() {
		String selectDay;
		Scanner sc = new Scanner(System.in);
		
		System.out.println("------------------------------------------------------------");
		System.out.println("확인할 날짜를 입력해주세요.");		
		System.out.println("------------------------------------------------------------");
		System.out.print("입력(예시: 20231207) > ");
		
		selectDay = sc.next();
		
		// selectDay의 길이 8이 아닐경우와 숫자가 아닐 경우 재입력
		while (selectDay.length()!=8 | selectDay=="^[\\D]*$") {
				System.out.println("------------------------------------------------------------");
				System.out.println("확인할 날짜를 다시 입력해주세요.");		
				System.out.println("------------------------------------------------------------");
				System.out.print("입력(예시: 20231207) > ");
				selectDay = sc.next();
			}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			
			// 수입 금액을 구하기 위한 SQL
			String sql = "SELECT SUM(MONEY_WON) FROM MONEYBOOK WHERE MONEY_INOUT='수입' AND TO_CHAR(TO_DATE(MONEY_DATE),'YYYYMMDD') = ?";
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, selectDay);
			rs = pstmt.executeQuery();
			int plus = 0;
			if(rs.next()) {
				if(rs.getString(1) != null) {
				plus=rs.getInt(1);
				}else if(rs.getString(1) == null){
					plus=0;
				}
			}
			sql = "SELECT TO_CHAR(M.MONEY_DATE,'YYYY/MM/DD') MONEY_DATE, "
					+ "C.CATEGORY_NAME, M.MONEY_INOUT, M.MONEY_WON "
					+ "FROM MONEYBOOK M, CATEGORY C "
					+ "WHERE M.CATEGORY_seq = C.CATEGORY_seq"
					+ " AND M.MONEY_INOUT = '수입'"
					+ " AND TO_CHAR(TO_DATE(MONEY_DATE),'YYYYMMDD') = ? "
					+ "ORDER BY MONEY_DATE";
			
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, selectDay);
			rs = pstmt.executeQuery();
			System.out.println("============================================================");
			System.out.printf("수입금액: %s원\n",decFor.format(plus));
			System.out.println("------------------------------------------------------------");
			System.out.printf("%6s %7s %7s %15s", "날짜" ,"수입/지출","금액", "카테고리\n");
			System.out.println("------------------------------------------------------------");
			if(rs.next()) {
				do{
					System.out.printf("%s %4s %13s %15s", rs.getString("money_date"), rs.getString("money_inout"),decFor.format(rs.getInt("money_won")),rs.getString("category_name"));
					System.out.println();
				}while (rs.next());
			} else {
				System.out.println("입력된 내역이 없습니다.");
			}
			System.out.println("============================================================");
			ConAnalysis.conAnalysisBack();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		sc.close();
		DBManager.selectClose(conn, pstmt, rs);
		
		return selectDay;
	}

	// 일별 지출 내역
	public static String selectDayOut() {
		String selectDay;
		Scanner sc = new Scanner(System.in);
		
		System.out.println("------------------------------------------------------------");
		System.out.println("확인할 날짜를 입력해주세요.");		
		System.out.println("------------------------------------------------------------");
		System.out.print("입력(예시: 20231207) > ");
		
		selectDay = sc.next();
		
		// selectDay의 길이 8이 아닐경우와 숫자가 아닐 경우 재입력
		while (selectDay.length()!=8 | selectDay=="^[\\D]*$") {
				System.out.println("------------------------------------------------------------");
				System.out.println("확인할 날짜를 다시 입력해주세요.");		
				System.out.println("------------------------------------------------------------");
				System.out.print("입력(예시: 20231207) > ");
				selectDay = sc.next();
			}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			// 지출 금액을 구하기 위한 SQL
			String sql = "SELECT SUM(MONEY_WON) FROM MONEYBOOK WHERE MONEY_INOUT='지출' AND TO_CHAR(TO_DATE(MONEY_DATE),'YYYYMMDD') = ?";
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, selectDay);
			rs = pstmt.executeQuery();
			
			int miners=0;
			if(rs.next()) {
				if(rs.getString(1) != null) {
					miners=rs.getInt(1);
				} else if(rs.getString(1) == null){
					miners=0;
				}
			}
			
			sql = "SELECT TO_CHAR(M.MONEY_DATE,'YYYY/MM/DD') MONEY_DATE, "
					+ "C.CATEGORY_NAME, M.MONEY_INOUT, M.MONEY_WON "
					+ "FROM MONEYBOOK M, CATEGORY C "
					+ "WHERE M.CATEGORY_seq = C.CATEGORY_seq"
					+ " AND M.MONEY_INOUT = '지출'"
					+ " AND TO_CHAR(TO_DATE(MONEY_DATE),'YYYYMMDD') = ? "
					+ "ORDER BY MONEY_DATE";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, selectDay);
			rs = pstmt.executeQuery();
			System.out.println("============================================================");
			System.out.printf("지출금액: %s원 \n",decFor.format(miners));
			System.out.println("------------------------------------------------------------");
			System.out.printf("%6s %7s %7s %15s", "날짜" ,"수입/지출","금액", "카테고리\n");
			System.out.println("------------------------------------------------------------");
			if(rs.next()) {
				do{
					System.out.printf("%s %4s %13s %15s", rs.getString("money_date"), rs.getString("money_inout"),decFor.format(rs.getInt("money_won")),rs.getString("category_name"));
					System.out.println();
				}while (rs.next());
			} else {
				System.out.println("입력된 내역이 없습니다.");
			}
			System.out.println("============================================================");
			ConAnalysis.conAnalysisBack();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		sc.close();
		DBManager.selectClose(conn, pstmt, rs);
		
		return selectDay;
	}
	
	// 월별 내역 메뉴
	public static void selectMonthManu() {
		Scanner sc = new Scanner(System.in);

		System.out.println("============================================================");
		System.out.println("[월별확인]");
		System.out.println("------------------------------------------------------------");
		System.out.println("1. 모든내역 | 2. 수입내역 | 3. 지출내역 | 4. 돌아가기");
		System.out.println("------------------------------------------------------------");
		System.out.println("들어갈 메뉴의 번호를 입력해주세요.");		
		System.out.println("------------------------------------------------------------");
		System.out.print("입력 > ");
		int selectManu = sc.nextInt();
		
		switch(selectManu) {
		case 1:
			selectMonthAll();
			break;
		case 2:
			selectMonthIn();
			break;
		case 3:
			selectMonthIn();
			break;
		case 4:
			conAnalysisManu();
			break;
		default:
			System.out.println("------------------------------------------------------------");
			System.out.println("다시 입력해주세요.");
			System.out.println("------------------------------------------------------------");
			selectMonthManu();
		}
		sc.close();
	}
	
	// 월별 모든 내역
	public static String selectMonthAll() {
		String selectMonth;
		Scanner sc = new Scanner(System.in);
		
		System.out.println("------------------------------------------------------------");
		System.out.println("확인할 날짜를 입력해주세요.");		
		System.out.println("------------------------------------------------------------");
		System.out.print("입력(예시: 202312) > ");
		
		selectMonth = sc.next();
		
		// 		selectMonth의 길이 6이 아닐경우와 숫자가 아닐 경우 재입력
		while (selectMonth.length()!=6 | selectMonth=="^[\\D]*$") {
			System.out.println("------------------------------------------------------------");
			System.out.println("확인할 날짜를 다시 입력해주세요.");		
			System.out.println("------------------------------------------------------------");

			System.out.print("입력(예시: 202312) > ");
			selectMonth = sc.next();
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			// 현재 금액을 구하기 위한 SQL
			String sql = "SELECT SUM(MONEY_WON) FROM MONEYBOOK WHERE MONEY_INOUT='수입' AND TO_CHAR(TO_DATE(MONEY_DATE),'YYYYMM') = ?";
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, selectMonth);
			rs = pstmt.executeQuery();
			int plus = 0;
			if(rs.next()) {
				if(rs.getString(1) != null) {
				plus=rs.getInt(1);
				}else if(rs.getString(1) == null){
					plus=0;
				}
			}
			sql = "SELECT SUM(MONEY_WON) FROM MONEYBOOK WHERE MONEY_INOUT='지출' AND TO_CHAR(TO_DATE(MONEY_DATE),'YYYYMM') = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, selectMonth);
			rs = pstmt.executeQuery();
			
			int miners=0;
			if(rs.next()) {
				if(rs.getString(1) != null) {
					miners=rs.getInt(1);
				} else if(rs.getString(1) == null){
					miners=0;
				}
			}
			
			sql = "SELECT TO_CHAR(M.MONEY_DATE,'YYYY/MM/DD') MONEY_DATE, C.CATEGORY_NAME, M.MONEY_INOUT, M.MONEY_WON "
					+ "FROM MONEYBOOK M, CATEGORY C "
					+ "WHERE M.CATEGORY_seq = C.CATEGORY_seq "
					+ " AND TO_CHAR(TO_DATE(MONEY_DATE),'YYYYMM') = ? "
					+ "ORDER BY MONEY_DATE";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, selectMonth);
			rs = pstmt.executeQuery();
			
			System.out.println("============================================================");
			System.out.printf("현재금액: %s원 | 수입금액: %s원 | 지출금액: %s원 \n",decFor.format(plus-miners),decFor.format(plus),decFor.format(miners));
			System.out.println("------------------------------------------------------------");
			System.out.printf("%6s %7s %7s %15s", "날짜" ,"수입/지출","금액", "카테고리\n");
			System.out.println("------------------------------------------------------------");
			if(rs.next()) {
				do{
					System.out.printf("%s %4s %13s %15s", rs.getString("money_date"), rs.getString("money_inout"),decFor.format(rs.getInt("money_won")),rs.getString("category_name"));
					System.out.println();
				}while (rs.next());
			} else {
				System.out.println("입력된 내역이 없습니다.");
			}
			System.out.println("============================================================");
			ConAnalysis.conAnalysisBack();
		} catch (Exception e) {
			e.printStackTrace();
		}
		sc.close();
		DBManager.selectClose(conn, pstmt, rs);
		
		return selectMonth;
	}
	
	// 월별 수입 내역
	public static String selectMonthIn() {
		String selectMonth;
		Scanner sc = new Scanner(System.in);
		
		System.out.println("------------------------------------------------------------");
		System.out.println("확인할 날짜를 입력해주세요.");		
		System.out.println("------------------------------------------------------------");
		System.out.print("입력(예시: 202312) > ");
		
		selectMonth = sc.next();
		
		// selectMonth의 길이 6이 아닐경우와 숫자가 아닐 경우 재입력
		while (selectMonth.length()!=6 | selectMonth=="^[\\D]*$") {
			System.out.println("------------------------------------------------------------");
			System.out.println("확인할 날짜를 다시 입력해주세요.");		
			System.out.println("------------------------------------------------------------");

			System.out.print("입력(예시: 202312) > ");
			selectMonth = sc.next();
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT SUM(MONEY_WON) FROM MONEYBOOK WHERE MONEY_INOUT='수입' AND TO_CHAR(TO_DATE(MONEY_DATE),'YYYYMM') = ?";
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, selectMonth);
			rs = pstmt.executeQuery();
			int plus = 0;
			if(rs.next()) {
				if(rs.getString(1) != null) {
				plus=rs.getInt(1);
				}else if(rs.getString(1) == null){
					plus=0;
				}
			}
			
			sql = "SELECT TO_CHAR(M.MONEY_DATE,'YYYY/MM/DD') MONEY_DATE, C.CATEGORY_NAME, M.MONEY_INOUT, M.MONEY_WON "
					+ "FROM MONEYBOOK M, CATEGORY C "
					+ "WHERE M.CATEGORY_seq = C.CATEGORY_seq "
					+ " AND M.MONEY_INOUT = '수입'"
					+ "AND TO_CHAR(TO_DATE(MONEY_DATE),'YYYYMM') = ? "
					+ "ORDER BY MONEY_DATE";
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, selectMonth);
			rs = pstmt.executeQuery();
			
			System.out.println("============================================================");
			System.out.printf("수입금액: %s원\n",decFor.format(plus));
			System.out.println("------------------------------------------------------------");
			System.out.printf("%6s %7s %7s %15s", "날짜" ,"수입/지출","금액", "카테고리\n");
			System.out.println("------------------------------------------------------------");
			if(rs.next()) {
				do{
					System.out.printf("%s %4s %13s %15s", rs.getString("money_date"), rs.getString("money_inout"),decFor.format(rs.getInt("money_won")),rs.getString("category_name"));
					System.out.println();
				}while (rs.next());
			} else {
				System.out.println("입력된 내역이 없습니다.");
			}
			System.out.println("============================================================");
			ConAnalysis.conAnalysisBack();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		sc.close();
		DBManager.selectClose(conn, pstmt, rs);
		
		return selectMonth;
	}
	
	// 월별 지출 내역
	public static String selectMonthOut() {
		String selectMonth;
		Scanner sc = new Scanner(System.in);
		
		System.out.println("------------------------------------------------------------");
		System.out.println("확인할 날짜를 입력해주세요.");		
		System.out.println("------------------------------------------------------------");
		System.out.print("입력(예시: 202312) > ");
		
		selectMonth = sc.next();
		
		// selectMonth의 길이 6이 아닐경우와 숫자가 아닐 경우 재입력
		while (selectMonth.length()!=6 | selectMonth=="^[\\D]*$") {
			System.out.println("------------------------------------------------------------");
			System.out.println("확인할 날짜를 다시 입력해주세요.");		
			System.out.println("------------------------------------------------------------");

			System.out.print("입력(예시: 202312) > ");
			selectMonth = sc.next();
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT SUM(MONEY_WON) FROM MONEYBOOK WHERE MONEY_INOUT='지출' AND TO_CHAR(TO_DATE(MONEY_DATE),'YYYYMM') = ?";
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, selectMonth);
			rs = pstmt.executeQuery();
			
			int miners=0;
			if(rs.next()) {
				if(rs.getString(1) != null) {
					miners=rs.getInt(1);
				} else if(rs.getString(1) == null){
					miners=0;
				}
			}
			
			sql = "SELECT TO_CHAR(M.MONEY_DATE,'YYYY/MM/DD') MONEY_DATE, C.CATEGORY_NAME, M.MONEY_INOUT, M.MONEY_WON "
					+ "FROM MONEYBOOK M, CATEGORY C "
					+ "WHERE M.CATEGORY_seq = C.CATEGORY_seq "
					+ " AND M.MONEY_INOUT = '지출'"
					+ "AND TO_CHAR(TO_DATE(MONEY_DATE),'YYYYMM') = ? "
					+ "ORDER BY MONEY_DATE";
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, selectMonth);
			rs = pstmt.executeQuery();
			
			System.out.println("============================================================");
			System.out.printf("지출금액: %s원 \n",decFor.format(miners));
			System.out.println("------------------------------------------------------------");
			System.out.printf("%6s %7s %7s %15s", "날짜" ,"수입/지출","금액", "카테고리\n");
			System.out.println("------------------------------------------------------------");
			if(rs.next()) {
				do{
					System.out.printf("%s %4s %13s %15s", rs.getString("money_date"), rs.getString("money_inout"),decFor.format(rs.getInt("money_won")),rs.getString("category_name"));
					System.out.println();
				}while (rs.next());
			} else {
				System.out.println("입력된 내역이 없습니다.");
			}
			System.out.println("============================================================");
			ConAnalysis.conAnalysisBack();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		sc.close();
		DBManager.selectClose(conn, pstmt, rs);
		
		return selectMonth;
	}
	
	// 연도별 내역 메뉴
	static void selectYearManu() {
		Scanner sc = new Scanner(System.in);
		
		System.out.println("============================================================");
		System.out.println("[월별확인]");
		System.out.println("------------------------------------------------------------");
		System.out.println("1. 모든내역 | 2. 수입내역 | 3. 지출내역 | 4. 돌아가기");
		System.out.println("------------------------------------------------------------");
		System.out.println("들어갈 메뉴의 번호를 입력해주세요.");		
		System.out.println("------------------------------------------------------------");
		System.out.print("입력 > ");
		int selectManu = sc.nextInt();
		
		switch(selectManu) {
		case 1:
			selectYearAll();
			break;
		case 2:
			selectYearIn();
			break;
		case 3:
			selectYearIn();
			break;
		case 4:
			conAnalysisManu();
			break;
		default:
			System.out.println("------------------------------------------------------------");
			System.out.println("다시 입력해주세요.");
			System.out.println("------------------------------------------------------------");
			selectYearManu();
		}
		sc.close();
	}
	
	// 연도별 모든 내역
	public static String selectYearAll() {
		String selectYear;
		Scanner sc = new Scanner(System.in);
		
		System.out.println("------------------------------------------------------------");
		System.out.println("확인할 날짜를 입력해주세요.");		
		System.out.println("------------------------------------------------------------");
		System.out.print("입력(예시: 2023) > ");
		
		selectYear = sc.next();
		
		// selectYear의 길이 4이 아닐경우와 숫자가 아닐 경우 재입력
		while (selectYear.length()!=4 | selectYear=="^[\\D]*$") {
			System.out.println("------------------------------------------------------------");
			System.out.println("확인할 날짜를 다시 입력해주세요.");		
			System.out.println("------------------------------------------------------------");

			System.out.print("입력(예시: 2023) > ");
			selectYear = sc.next();
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			// 현재 금액을 구하기 위한 SQL
			String sql = "SELECT SUM(MONEY_WON) FROM MONEYBOOK WHERE MONEY_INOUT='수입' AND TO_CHAR(TO_DATE(MONEY_DATE),'YYYY') = ?";
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, selectYear);
			rs = pstmt.executeQuery();
			int plus = 0;
			if(rs.next()) {
				if(rs.getString(1) != null) {
				plus=rs.getInt(1);
				}else if(rs.getString(1) == null){
					plus=0;
				}
			}
			sql = "SELECT SUM(MONEY_WON) FROM MONEYBOOK WHERE MONEY_INOUT='지출' AND TO_CHAR(TO_DATE(MONEY_DATE),'YYYY') = ?";
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, selectYear);
			rs = pstmt.executeQuery();
			
			int miners=0;
			if(rs.next()) {
				if(rs.getString(1) != null) {
					miners=rs.getInt(1);
				} else if(rs.getString(1) == null){
					miners=0;
				}
			}
			
			sql = "SELECT TO_CHAR(M.MONEY_DATE,'YYYY/MM/DD') MONEY_DATE, C.CATEGORY_NAME, M.MONEY_INOUT, M.MONEY_WON "
					+ "FROM MONEYBOOK M, CATEGORY C "
					+ "WHERE M.CATEGORY_seq = C.CATEGORY_seq "
					+ "AND TO_CHAR(TO_DATE(MONEY_DATE),'YYYY') = ? "
					+ "ORDER BY MONEY_DATE";
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, selectYear);
			rs = pstmt.executeQuery();
			
			System.out.println("============================================================");
			System.out.printf("현재금액: %s원 | 수입금액: %s원 | 지출금액: %s원 \n",decFor.format(plus-miners),decFor.format(plus),decFor.format(miners));
			System.out.println("------------------------------------------------------------");
			System.out.printf("%6s %7s %7s %15s", "날짜" ,"수입/지출","금액", "카테고리\n");
			System.out.println("------------------------------------------------------------");
			if(rs.next()) {
				do{
					System.out.printf("%s %4s %13s %15s", rs.getString("money_date"), rs.getString("money_inout"),decFor.format(rs.getInt("money_won")),rs.getString("category_name"));
					System.out.println();
				}while (rs.next());
			} else {
				System.out.println("입력된 내역이 없습니다.");
			}
			System.out.println("============================================================");
			ConAnalysis.conAnalysisBack();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		sc.close();
		DBManager.selectClose(conn, pstmt, rs);
		
		return selectYear;
	}
	
	// 연도별 수입 내역
	public static String selectYearIn() {
		String selectYear;
		Scanner sc = new Scanner(System.in);
		
		System.out.println("------------------------------------------------------------");
		System.out.println("확인할 날짜를 입력해주세요.");		
		System.out.println("------------------------------------------------------------");
		System.out.print("입력(예시: 2023) > ");
		
		selectYear = sc.next();
		
		// selectYear의 길이 4이 아닐경우와 숫자가 아닐 경우 재입력
		while (selectYear.length()!=4 | selectYear=="^[\\D]*$") {
			System.out.println("------------------------------------------------------------");
			System.out.println("확인할 날짜를 다시 입력해주세요.");		
			System.out.println("------------------------------------------------------------");

			System.out.print("입력(예시: 2023) > ");
			selectYear = sc.next();
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			// 수입 금액을 구하기 위한 SQL
			String sql = "SELECT SUM(MONEY_WON) FROM MONEYBOOK WHERE MONEY_INOUT='수입' AND TO_CHAR(TO_DATE(MONEY_DATE),'YYYY') = ?";
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, selectYear);
			rs = pstmt.executeQuery();
			int plus = 0;
			if(rs.next()) {
				if(rs.getString(1) != null) {
				plus=rs.getInt(1);
				}else if(rs.getString(1) == null){
					plus=0;
				}
			}
			
			sql = "SELECT TO_CHAR(M.MONEY_DATE,'YYYY/MM/DD') MONEY_DATE, C.CATEGORY_NAME, M.MONEY_INOUT, M.MONEY_WON "
					+ "FROM MONEYBOOK M, CATEGORY C "
					+ "WHERE M.CATEGORY_seq = C.CATEGORY_seq "
					+ " AND M.MONEY_INOUT = '수입'"
					+ "AND TO_CHAR(TO_DATE(MONEY_DATE),'YYYY') = ? "
					+ "ORDER BY MONEY_DATE";
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, selectYear);
			rs = pstmt.executeQuery();
			
			System.out.println("============================================================");
			System.out.printf("수입금액: %s원\n",decFor.format(plus));
			System.out.println("------------------------------------------------------------");
			System.out.printf("%6s %7s %7s %15s", "날짜" ,"수입/지출","금액", "카테고리\n");
			System.out.println("------------------------------------------------------------");
			if(rs.next()) {
				do{
					System.out.printf("%s %4s %13s %15s", rs.getString("money_date"), rs.getString("money_inout"),decFor.format(rs.getInt("money_won")),rs.getString("category_name"));
					System.out.println();
				}while (rs.next());
			} else {
				System.out.println("입력된 내역이 없습니다.");
			}
			System.out.println("============================================================");
			ConAnalysis.conAnalysisBack();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		sc.close();
		DBManager.selectClose(conn, pstmt, rs);
		
		return selectYear;
	}
	
	// 연도별 지출 내역
	public static String selectYearOut() {
		String selectYear;
		Scanner sc = new Scanner(System.in);
		
		System.out.println("------------------------------------------------------------");
		System.out.println("확인할 날짜를 입력해주세요.");		
		System.out.println("------------------------------------------------------------");
		System.out.print("입력(예시: 2023) > ");
		
		selectYear = sc.next();
		
		// selectYear의 길이 4이 아닐경우와 숫자가 아닐 경우 재입력
		while (selectYear.length()!=4 | selectYear=="^[\\D]*$") {
			System.out.println("------------------------------------------------------------");
			System.out.println("확인할 날짜를 다시 입력해주세요.");		
			System.out.println("------------------------------------------------------------");

			System.out.print("입력(예시: 2023) > ");
			selectYear = sc.next();
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT SUM(MONEY_WON) FROM MONEYBOOK WHERE MONEY_INOUT='지출' AND TO_CHAR(TO_DATE(MONEY_DATE),'YYYY') = ?";
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, selectYear);
			rs = pstmt.executeQuery();
			
			int miners=0;
			if(rs.next()) {
				if(rs.getString(1) != null) {
					miners=rs.getInt(1);
				} else if(rs.getString(1) == null){
					miners=0;
				}
			}
			
			sql = "SELECT TO_CHAR(M.MONEY_DATE,'YYYY/MM/DD') MONEY_DATE, C.CATEGORY_NAME, M.MONEY_INOUT, M.MONEY_WON "
					+ "FROM MONEYBOOK M, CATEGORY C "
					+ "WHERE M.CATEGORY_seq = C.CATEGORY_seq "
					+ " AND M.MONEY_INOUT = '지출'"
					+ "AND TO_CHAR(TO_DATE(MONEY_DATE),'YYYY') = ? "
					+ "ORDER BY MONEY_DATE";
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, selectYear);
			rs = pstmt.executeQuery();
			System.out.println("============================================================");
			System.out.printf("지출금액: %s원 \n",decFor.format(miners));
			System.out.println("------------------------------------------------------------");
			System.out.printf("%6s %7s %7s %15s", "날짜" ,"수입/지출","금액", "카테고리\n");
			System.out.println("------------------------------------------------------------");
			if(rs.next()) {
				do{
					System.out.printf("%s %4s %13s %15s", rs.getString("money_date"), rs.getString("money_inout"),decFor.format(rs.getInt("money_won")),rs.getString("category_name"));
					System.out.println();
				}while (rs.next());
			} else {
				System.out.println("입력된 내역이 없습니다.");
			}
			System.out.println("============================================================");
			ConAnalysis.conAnalysisBack();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		sc.close();
		DBManager.selectClose(conn, pstmt, rs);
		
		return selectYear;
	}
	
	// 카테고리별 내역 메뉴
	static void selectCategoryManu() {
		Scanner sc = new Scanner(System.in);
		
		System.out.println("============================================================");
		System.out.println("[카테고리별 확인]");
		System.out.println("------------------------------------------------------------");
		System.out.println("1. 월별확인 | 2. 연도별확인 | 3. 돌아가기");
		System.out.println("------------------------------------------------------------");
		System.out.println("들어갈 메뉴의 번호를 입력해주세요.");		
		System.out.println("------------------------------------------------------------");
		System.out.print("입력 > ");
		int selectManu = sc.nextInt();
		
		switch(selectManu) {
		case 1:
			selectCategoryMonth();
			break;
		case 2:
			selectCategoryYear();
			break;
		case 3:
			conAnalysisManu();
			break;
		default:
			System.out.println("------------------------------------------------------------");
			System.out.println("다시 입력해주세요.");
			System.out.println("------------------------------------------------------------");
			selectCategoryManu();
		}
		sc.close();
	}
	
	// 카테고리별 월별 내역
	public static conAnalysisVO selectCategoryMonth() {
		conAnalysisVO selectCategoryMonth = new conAnalysisVO();
		String selectMonth;
		Scanner sc = new Scanner(System.in);
		
		System.out.println("------------------------------------------------------------");
		System.out.println("확인할 날짜를 입력해주세요.");		
		System.out.println("------------------------------------------------------------");
		System.out.print("입력(예시: 202312) > ");
		
		selectMonth = sc.next();
		
		// selectMonth의 길이 6이 아닐경우와 숫자가 아닐 경우 재입력
		while (selectMonth.length()!=6 | selectMonth=="^[\\D]*$") {
			System.out.println("------------------------------------------------------------");
			System.out.println("확인할 날짜를 다시 입력해주세요.");		
			System.out.println("------------------------------------------------------------");

			System.out.print("입력(예시: 202312) > ");
			selectMonth = sc.next();
		}
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT c.category_seq, c.category_name , nvl(sumwon,0)"
					+ "FROM category C left join"
					+ " (select category_seq, sum(MONEY_WON) sumwon"
					+ " from moneybook"
					+ " where MONEY_INOUT='수입'"
					+ " AND TO_CHAR(TO_DATE(MONEY_DATE),'YYYYMM') = ?"
					+ " group by category_seq ) M"
					+ " on  c.category_seq = m.category_seq"
					+ " order by c.category_seq";
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, selectMonth);
			rs = pstmt.executeQuery();
			
			int[] plus = new int[20];
			while(rs.next()) {
				plus[rs.getInt(1)-1]=rs.getInt(3);
			}
			
			sql = "SELECT c.category_seq, c.category_name , nvl(sumwon,0)"
					+ "FROM category C left join"
					+ " (select category_seq, sum(MONEY_WON) sumwon"
					+ " from moneybook"
					+ " where MONEY_INOUT='지출'"
					+ " AND TO_CHAR(TO_DATE(MONEY_DATE),'YYYYMM') = ?"
					+ " group by category_seq ) M"
					+ " on  c.category_seq = m.category_seq"
					+ " order by c.category_seq";
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, selectMonth);
			rs = pstmt.executeQuery();
			System.out.println("============================================================");
			System.out.printf("%3s\t  %7s\t%15s", "번호", "금액", "카테고리\n");
			System.out.println("------------------------------------------------------------");
			
			int[] miners = new int[20];
			while(rs.next()) {
				miners[rs.getInt(1)-1] = rs.getInt(3);
				System.out.printf("%3d\t%12s\t%15s\n", rs.getInt(1), decFor.format(plus[rs.getInt(1)-1]-miners[rs.getInt(1)-1]), rs.getString(2));
			}
			System.out.println("============================================================");
			ConAnalysis.selectCategoryBack();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		sc.close();
		DBManager.selectClose(conn, pstmt, rs);
		
		return selectCategoryMonth;
	}

	// 카테고리별 연도별 내역
	public static conAnalysisVO selectCategoryYear() {
		conAnalysisVO selectCategoryYear = new conAnalysisVO();
		String selectYear;
		Scanner sc = new Scanner(System.in);
		
		System.out.println("------------------------------------------------------------");
		System.out.println("확인할 날짜를 입력해주세요.");		
		System.out.println("------------------------------------------------------------");
		System.out.print("입력(예시: 2023) > ");
		
		selectYear = sc.next();
		
		// selectYear의 길이 4이 아닐경우와 숫자가 아닐 경우 재입력
		while (selectYear.length()!=4 | selectYear=="^[\\D]*$") {
			System.out.println("------------------------------------------------------------");
			System.out.println("확인할 날짜를 다시 입력해주세요.");		
			System.out.println("------------------------------------------------------------");

			System.out.print("입력(예시: 2023) > ");
			selectYear = sc.next();
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT c.category_seq, c.category_name , nvl(sumwon,0)"
					+ "FROM category C left join"
					+ " (select category_seq, sum(MONEY_WON) sumwon"
					+ " from moneybook"
					+ " where MONEY_INOUT='수입'"
					+ " AND TO_CHAR(TO_DATE(MONEY_DATE),'YYYY') = ?"
					+ " group by category_seq ) M"
					+ " on  c.category_seq = m.category_seq"
					+ " order by c.category_seq";
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, selectYear);
			rs = pstmt.executeQuery();
			
			int[] plus = new int[20];
			while(rs.next()) {
				plus[rs.getInt(1)-1]=rs.getInt(3);
			}
			
			sql = "SELECT c.category_seq, c.category_name , nvl(sumwon,0)"
					+ "FROM category C left join"
					+ " (select category_seq, sum(MONEY_WON) sumwon"
					+ " from moneybook"
					+ " where MONEY_INOUT='지출'"
					+ " AND TO_CHAR(TO_DATE(MONEY_DATE),'YYYY') = ?"
					+ " group by category_seq ) M"
					+ " on  c.category_seq = m.category_seq"
					+ " order by c.category_seq";
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, selectYear);
			rs = pstmt.executeQuery();
			System.out.println("============================================================");
			System.out.printf("%3s\t  %7s\t%15s", "번호", "금액", "카테고리\n");
			System.out.println("------------------------------------------------------------");
			
			int[] miners = new int[20];
			while(rs.next()) {
				miners[rs.getInt(1)-1] = rs.getInt(3);
				System.out.printf("%3d\t%12s\t%15s\n", rs.getInt(1), decFor.format(plus[rs.getInt(1)-1]-miners[rs.getInt(1)-1]), rs.getString(2));
			}
			System.out.println("============================================================");
			ConAnalysis.selectCategoryBack();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		sc.close();
		DBManager.selectClose(conn, pstmt, rs);
		
		return selectCategoryYear;
	}
	
	// conAnalysisManu로 돌아가기
	public static void conAnalysisBack() {
		System.out.println("그만보시려면 '돌아가기'를 입력해주세요.");
		System.out.println("------------------------------------------------------------");
		System.out.print("입력 > ");
		
		Scanner sc = new Scanner(System.in);
		String conAnalysisBack = sc.nextLine();
		if(conAnalysisBack.equals("돌아가기")) {
			ConAnalysis.conAnalysisManu();
		} else {
			System.out.println("다시 입력해주세요.");
			ConAnalysis.conAnalysisBack();
		}
	}
	
	// selectCategoryManu로 돌아가기
	public static void selectCategoryBack() {
		System.out.println("그만보시려면 '돌아가기'를 입력해주세요.");
		System.out.println("------------------------------------------------------------");
		System.out.print("입력 > ");
		
		Scanner sc = new Scanner(System.in);
		String selectCategoryBack = sc.nextLine();
		if(selectCategoryBack.equals("돌아가기")) {
			ConAnalysis.selectCategoryManu();
		} else {
			System.out.println("다시 입력해주세요.");
			ConAnalysis.selectCategoryBack();
		}
	}

}