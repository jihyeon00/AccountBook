package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import dto.goalVO;
import main.ABStart;
import util.DBManager;

public class Goal {
	public static void main(String[] args) {
		Goal.goalManu();
	}
	
	// 소목표 메뉴
		public static void goalManu() {
			Scanner sc = new Scanner(System.in);
			ABStart abStart = new ABStart();
			
			System.out.println("\n[소목표]");
			System.out.println("------------------------------------------------------------------");
			System.out.println("1. 조회 | 2. 작성 | 3. 수정 | 4. 삭제 | 5. 달성 | 6. 돌아가기");
			System.out.println("------------------------------------------------------------------");
			System.out.println("들어갈 메뉴의 번호를 입력해주세요.");		
			System.out.println("------------------------------------------------------------------");
			System.out.print("입력 > ");
			int goalManuSeq = sc.nextInt();
			
			switch(goalManuSeq) {
				case 1:
					selectGoal();
					break;
				case 2:
					insertGoal();
					break;
				case 3:
					updateGoal();
					break;
				case 4:
					deleteGoal();
					break;
				case 5:
					finishGoal();
					break;
				case 6:
					abStart.stManu();
					break;
				default:
					System.out.println("------------------------------------------------------");
					System.out.println("다시 입력해주세요.");
					System.out.println("------------------------------------------------------");
					Goal.goalManu();
					break;
			}
			sc.close();
			
		}
		
		// 소목표 조회
		public static goalVO selectGoal() {
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			goalVO selectGoal = new goalVO();

			try {
				String sql = "SELECT GOAL_SEQ , GOAL_CONTENTS , GOAL_TOKEN FROM Goal order by GOAL_SEQ";
				conn = DBManager.getConnection();
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				System.out.println("--------------------------------------------------");
				System.out.println("번호 | 차감코인 | 내용");
				System.out.println("--------------------------------------------------");
				if (rs.next()) {
					do{
						System.out.printf("%3d  |   %3d    | %s\n",rs.getInt("goal_seq"),rs.getInt("goal_token"),rs.getString("goal_contents"));
					}while(rs.next());
				} else {
					System.out.println("입력된 목표가 없습니다.");
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			Goal.goalBack();
			DBManager.selectClose(conn, pstmt, rs);
			return selectGoal;
		}
		
		// 소목표 조회 (돌아가기 X)
		public static goalVO selectGoalNoBack() {
			Scanner sc = new Scanner(System.in);
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			
			goalVO selectGoal = new goalVO();
			
			try {
				String sql = "SELECT GOAL_SEQ , GOAL_CONTENTS , GOAL_TOKEN FROM Goal order by GOAL_SEQ";
				conn = DBManager.getConnection();
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				System.out.println("--------------------------------------------------");
				System.out.println("번호 | 차감코인 | 내용");
				System.out.println("--------------------------------------------------");
				if (rs.next()) {
					do{
						System.out.printf("%3d  |   %3d    | %s\n",rs.getInt("goal_seq"),rs.getInt("goal_token"),rs.getString("goal_contents"));
					}while(rs.next());
				} else {
					System.out.println("입력된 목표가 없습니다.");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			DBManager.selectClose(conn, pstmt, rs);
			return selectGoal;
		}

		// 소목표 작성
		public static goalVO insertGoal() {
			goalVO insertGoal = new goalVO();
			Scanner sc = new Scanner(System.in);
			
			System.out.println("--------------------------------------------------");
			System.out.println("내용을 입력하세요 (30자 이하)");
			System.out.println("--------------------------------------------------");
			System.out.print("입력 > ");
			insertGoal.setGoalContents(sc.nextLine());
			
			System.out.println("--------------------------------------------------");
			System.out.println("차감할 코인의 개수를 입력하세요");
			System.out.println("--------------------------------------------------");
			System.out.print("입력 > ");
			insertGoal.setGoalToken(sc.nextInt());
			
			System.out.println("내용 : " + insertGoal.getGoalContents());
			System.out.println("차감코인 : " + insertGoal.getGoalToken());
			System.out.println("--------------------------------------------------");
			System.out.println("작성하시겠습니까?\n1.확인 | 2.취소");
			System.out.println("--------------------------------------------------");
			System.out.print("입력 > ");
			int yesOrNO = sc.nextInt();

			Connection conn = null;
			PreparedStatement pstmt = null;

			if(yesOrNO==1) {
				try {
					String sql = "INSERT INTO goal (GOAL_SEQ, GOAL_CONTENTS, GOAL_TOKEN) "
							+ "VALUES (GOAL_SEQ.NEXTVAL, ?, ?)";
					conn = DBManager.getConnection();
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, insertGoal.getGoalContents());
					pstmt.setInt(2, insertGoal.getGoalToken());
					
					pstmt.executeUpdate();
					System.out.println("--------------------------------------------------");
					System.out.println("작성되었습니다.");
					System.out.println("--------------------------------------------------");
					Goal.goalManu();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("--------------------------------------------------");
				System.out.println("작성을 취소하였습니다.");
				System.out.println("--------------------------------------------------");
				goalBack();
			}
			DBManager.IUDClose(conn, pstmt);
			return insertGoal;
		}

		// 소목표 수정
		public static goalVO updateGoal() {
			Scanner sc = new Scanner(System.in);
			goalVO updateGoal = new goalVO();
			
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			selectGoalNoBack();
				
			System.out.println("--------------------------------------------------");
			System.out.println("수정할 내용의 번호를 입력해주세요.");
			System.out.println("--------------------------------------------------");
			System.out.print("입력 > ");
			int updateNum = sc.nextInt();
			
			try{
				if(updateNum <= 0) {
					System.out.println("--------------------------------------------------");
					System.out.println("다시 입력해주세요.");
					System.out.println("--------------------------------------------------");
					updateGoal();
				} else {
					updateGoal.setGoalSeq(updateNum);
				}
			} catch(Exception e){
				ABStart abStart = new ABStart();
				abStart.exit();
			}
				
			int GOAL_SEQ = updateGoal.getGoalSeq();
			
			try {
				String sql = "SELECT GOAL_SEQ , GOAL_CONTENTS , GOAL_TOKEN FROM Goal WHERE GOAL_SEQ=? order by GOAL_SEQ";
				conn = DBManager.getConnection();
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setInt(1, updateGoal.getGoalSeq());
				rs = pstmt.executeQuery();
				System.out.println("--------------------------------------------------");
				System.out.println("번호 | 차감코인 | 내용");
				System.out.println("--------------------------------------------------");
				if (rs.next()) {
					System.out.printf("%3d  |   %3d    | %s\n",rs.getInt("goal_seq"),rs.getInt("goal_token"),rs.getString("goal_contents"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
				System.out.println("--------------------------------------------------");
				System.out.println("차감할 코인의 개수를 입력하세요"
								+ "\n수정을 원치 않을 경우 같은 내용을 입력해주세요.");
				System.out.println("--------------------------------------------------");
				System.out.print("입력 > ");
				int updateGoalToken = sc.nextInt();
				try{
					if(updateGoalToken <= 0) {
						System.out.println("--------------------------------------------------");
						System.out.println("다시 입력해주세요.");
						System.out.println("--------------------------------------------------");
						updateGoal();
					} else {
						updateGoal.setGoalSeq(updateGoalToken);
					}
				} catch(Exception e){
					ABStart abStart2 = new ABStart();
					abStart2.exit();
				}
				updateGoal.setGoalToken(updateGoalToken);
				
				System.out.println("--------------------------------------------------");
				System.out.println("내용을 입력하세요."
								+ "\n수정을 원치 않을 경우 같은 내용을 입력해주세요.");
				System.out.println("--------------------------------------------------");
				System.out.print("입력 > ");
				sc.nextLine();
				updateGoal.setGoalContents(sc.nextLine());
				
				
				System.out.println("번호 : " + updateGoal.getGoalSeq());
				System.out.println("차감코인 : " + updateGoal.getGoalToken());
				System.out.println("내용 : " + updateGoal.getGoalContents());
				System.out.println("--------------------------------------------------");
				System.out.println("수정하시겠습니까?\n1.확인 | 2.취소");
				System.out.println("--------------------------------------------------");
				System.out.print("입력 > ");
				int yesOrNO = sc.nextInt();

				if(yesOrNO==1) {
					try {
						String sql = "UPDATE GOAL SET GOAL_TOKEN = ?, GOAL_CONTENTS = ? WHERE GOAL_SEQ=?";
						conn = DBManager.getConnection();
						pstmt = conn.prepareStatement(sql);
						pstmt.setInt(1, updateGoal.getGoalToken());
						pstmt.setString(2, updateGoal.getGoalContents());
						pstmt.setInt(3, GOAL_SEQ);
						
						pstmt.executeUpdate();
						System.out.println("--------------------------------------------------");
						System.out.println("수정되었습니다.");
						System.out.println("--------------------------------------------------");
						selectGoal();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				} else {
					System.out.println("--------------------------------------------------");
					System.out.println("수정을 취소하였습니다.");
					System.out.println("--------------------------------------------------");
					goalManu();
				}
			sc.close();
			DBManager.selectClose(conn, pstmt, rs);
			return updateGoal;
		}
		
		// 소목표 삭제
		public static goalVO deleteGoal() {
			Scanner sc = new Scanner(System.in);
			goalVO deleteGoal = new goalVO();
			
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try {
				String sql = "SELECT GOAL_SEQ , GOAL_CONTENTS , GOAL_TOKEN FROM Goal order by GOAL_SEQ";
				conn = DBManager.getConnection();
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				System.out.println("--------------------------------------------------");
				System.out.println("번호 | 차감코인 | 내용");
				System.out.println("--------------------------------------------------");
				while (rs.next()) {
					System.out.printf("%3d  |   %3d    | %s\n",rs.getInt("goal_seq"),rs.getInt("goal_token"),rs.getString("goal_contents"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
				
				System.out.println("--------------------------------------------------");
				System.out.println("삭제할 내용의 번호를 입력해주세요.");
				System.out.println("--------------------------------------------------");
				System.out.print("입력 > ");
				
				int deleteNum = sc.nextInt();
				
				try{
					if(deleteNum <= 0) {
						System.out.println("--------------------------------------------------");
						System.out.println("다시 입력해주세요.");
						System.out.println("--------------------------------------------------");
						deleteGoal();
					} else {
						deleteGoal.setGoalSeq(deleteNum);
					}
				} catch(Exception e){
					ABStart abStart = new ABStart();
					abStart.exit();
				}
				int GOAL_SEQ = deleteGoal.getGoalSeq();

				try {
					String sql = "SELECT GOAL_SEQ , GOAL_CONTENTS , GOAL_TOKEN FROM Goal WHERE GOAL_SEQ=? order by GOAL_SEQ";
					conn = DBManager.getConnection();
					pstmt = conn.prepareStatement(sql);
					
					pstmt.setInt(1, GOAL_SEQ);
					rs = pstmt.executeQuery();
					System.out.println("--------------------------------------------------");
					System.out.println("번호 | 차감코인 | 내용");
					System.out.println("--------------------------------------------------");
					if (rs.next()) {
						System.out.printf("%3d  |   %3d    | %s\n",rs.getInt("goal_seq"),rs.getInt("goal_token"),rs.getString("goal_contents"));
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			System.out.println("--------------------------------------------------");
			System.out.println("삭제하시겠습니까?\n1.확인 | 2.취소");
			System.out.println("--------------------------------------------------");
			System.out.print("입력 > ");
			int yesOrNO = sc.nextInt();

			if(yesOrNO==1) {
				try {
					String sql="UPDATE TOKEN SET GOAL_SEQ=NULL WHERE GOAL_SEQ=? ";
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, GOAL_SEQ);
					pstmt.executeUpdate();
					sql = "DELETE FROM GOAL WHERE GOAL_SEQ=?";
					conn = DBManager.getConnection();
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, GOAL_SEQ);
					
					pstmt.executeUpdate();
					System.out.println("--------------------------------------------------");
					System.out.println("삭제되었습니다.");
					System.out.println("--------------------------------------------------");
					selectGoal();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("--------------------------------------------------");
				System.out.println("삭제를 취소하였습니다.");
				System.out.println("--------------------------------------------------");
				goalManu();
			}
			sc.close();
			DBManager.selectClose(conn, pstmt, rs);
			return deleteGoal;
		}
		
		// 소목표 달성
		public static goalVO finishGoal() {
			
			Scanner sc = new Scanner(System.in);
			goalVO finishGoal = new goalVO();
			
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
		
			token();
			
			selectGoalNoBack();
			System.out.println("--------------------------------------------------");
			System.out.println("달성할 소목표의 번호를 입력해주세요.");
			System.out.println("--------------------------------------------------");
			System.out.print("입력 > ");
			int finishNum = sc.nextInt();
			
			try{
				if(finishNum <= 0) {
					System.out.println("--------------------------------------------------");
					System.out.println("다시 입력해주세요.");
					System.out.println("--------------------------------------------------");
					finishGoal();
				} else {
					finishGoal.setGoalSeq(finishNum);
				}
			} catch(Exception e){
				ABStart abStart = new ABStart();
				abStart.exit();
			}
			int GOAL_SEQ = finishGoal.getGoalSeq();
			
			try {	
				String sql = "SELECT GOAL_SEQ , GOAL_CONTENTS , GOAL_TOKEN FROM Goal WHERE GOAL_SEQ=? order by GOAL_SEQ";
				conn = DBManager.getConnection();
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setInt(1, GOAL_SEQ);
				rs = pstmt.executeQuery();
				System.out.println("--------------------------------------------------");
				System.out.println("번호 | 차감코인 | 내용");
				System.out.println("--------------------------------------------------");
				if (rs.next()) {
					System.out.printf("%3d  |   %3d    | %s\n",rs.getInt("goal_seq"),rs.getInt("goal_token"),rs.getString("goal_contents"));
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		System.out.println("--------------------------------------------------");
		System.out.println("달성하시겠습니까?\n1.확인 | 2.취소");
		System.out.println("--------------------------------------------------");
		System.out.print("입력 > ");
		int yesOrNO = sc.nextInt();

		if(yesOrNO==1) {
			try {
				
				String sql="INSERT INTO TOKEN(TOKEN_SEQ, TOKEN_AMOUNT, TOKEN_HISTORY, GOAL_SEQ) "
						+ "VALUES (TOKEN_SEQ.NEXTVAL, ?, '목표', ?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, rs.getInt("goal_token"));
				pstmt.setInt(2, GOAL_SEQ);
				pstmt.executeUpdate();
				
				System.out.println("--------------------------------------------------");
				System.out.println("완료되었습니다.");
				System.out.println("--------------------------------------------------");
				goalManu();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("--------------------------------------------------");
			System.out.println("취소하였습니다.");
			System.out.println("--------------------------------------------------");
			goalManu();
		}
		sc.close();
		DBManager.selectClose(conn, pstmt, rs);
		return finishGoal;
			
		}
				
		// 소목표 돌아가기
		public static void exitGoal() {
			ABStart abStart = new ABStart();
			abStart.stManu();
		}
		
		// 소목표 조회, 작성, 수정, 삭제의 돌아가기
		public static void goalBack() {
			System.out.println("--------------------------------------------------");
			System.out.println("그만보시려면 '돌아가기'를 입력해주세요.");
			System.out.println("--------------------------------------------------");
			System.out.print("입력 > ");
			
			Scanner sc = new Scanner(System.in);
			String goalBack = sc.nextLine();
			if(goalBack.equals("돌아가기")) {
				Goal.goalManu();
			} else {
				System.out.println("다시 입력해주세요.");
				goalBack();
			}
		}
		// 토큰 수
		public static void token() {
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
		
			
			try {
				String sql = "SELECT SUM(TOKEN_AMOUNT) FROM token WHERE TOKEN_HISTORY='루틴'";
				conn = DBManager.getConnection();
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
				sql = "SELECT SUM(TOKEN_AMOUNT) FROM token WHERE TOKEN_HISTORY ='목표'";
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
				System.out.println("--------------------------------------------------");
				System.out.printf("현재토큰수: %s개\n",plus-miners);
				
				if((plus-miners)<=0) {
					System.out.println("달성할 토큰의 수가 부족합니다.");
					System.out.println("--------------------------------------------------");
					goalManu();
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
}
