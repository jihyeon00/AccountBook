//루틴 관리
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import dto.Routine;
import main.ABStart;
import util.DBManager;

public class ABRoutine {
	Scanner scan = new Scanner(System.in);
	Routine routine = Routine.getInstance();
	
	public void rtManu() {
		ABStart back = new ABStart();
		System.out.println();
		System.out.println("[루틴 관리]");
		System.out.println("------------------------------------------------------------------------");
		System.out.println("들어갈 메뉴의 번호를 입력하세요.");
		System.out.println("------------------------------------------------------------------------");
		System.out.println("메뉴 : 1.루틴입력 | 2.루틴조회 | 3.돌아가기");
		System.out.println("------------------------------------------------------------------------");
		System.out.print("선택: ");
		String manuno = scan.next();
		switch (manuno) {
		case "1":
			rtInsert();
			break;
		case "2":
			rtManageMent();
			break;
		case "3":
			back.stManu();
			break;
		default :
			rtManu();
			break;
		}
	}
	
	public void rtInsert() {
		Connection conn = DBManager.getConnection();
		PreparedStatement pstmt = null;
		System.out.println("\n[루틴 입력]");
		System.out.println("------------------------------------------------------------------------");
		System.out.println("[해야 할일의 주기]");
		System.out.println("메뉴: 1.일일 | 2.주간 | 3.월간 ");
		System.out.print("선택: ");
		int divnum = scan.nextInt();
		while(divnum>3 || divnum<=0) {
			System.out.print("재입력: ");
			divnum = scan.nextInt();
		}
		if(divnum==1) {
			routine.setROUTINE_DIV("일일");
		} else if(divnum==2) {
			routine.setROUTINE_DIV("주간");
		} else if(divnum==3) {
			routine.setROUTINE_DIV("월간");
		}
		
		System.out.println("\n[해야 할일의 내용]");
		System.out.print("내용: ");
		scan.nextLine();
		String contents = scan.nextLine();
		while(contents.length()<=0 || contents.length()>30) {
			System.out.print("내용: ");
			scan.nextLine();
			contents = scan.nextLine();
		}
		routine.setROUTINE_CONTENTS(contents);
		
		System.out.println("\n[완료시 받을 토큰수]");
		System.out.print("지급토큰수: ");
		int token = Integer.parseInt(scan.nextLine());
		routine.setROUTINE_TOKEN(token);
		
		System.out.println("\n[입력된 루틴]");
		System.out.println("------------------------------------------------------------------------");
		System.out.printf("%s\t%s\t\t\t\t\t\t%s\n","주기","내용","지급토큰수");
		System.out.println("------------------------------------------------------------------------");
		if(routine.getROUTINE_CONTENTS().length()>=27) {
			System.out.printf("%s\t%s\t\t%s\n",routine.getROUTINE_DIV(), routine.getROUTINE_CONTENTS(), routine.getROUTINE_TOKEN());
		} else if(routine.getROUTINE_CONTENTS().length()>=24) {
			System.out.printf("%s\t%s\t\t\t%s\n",routine.getROUTINE_DIV(), routine.getROUTINE_CONTENTS(), routine.getROUTINE_TOKEN());
		} else if(routine.getROUTINE_CONTENTS().length()>=18) {
			System.out.printf("%s\t%s\t\t\t\t%s\n",routine.getROUTINE_DIV(), routine.getROUTINE_CONTENTS(), routine.getROUTINE_TOKEN());
		} else if(routine.getROUTINE_CONTENTS().length()>=12) {
			System.out.printf("%s\t%s\t\t\t\t%s\n",routine.getROUTINE_DIV(), routine.getROUTINE_CONTENTS(), routine.getROUTINE_TOKEN());
		} else if(routine.getROUTINE_CONTENTS().length()>=6) {
			System.out.printf("%s\t%s\t\t\t\t\t%s\n",routine.getROUTINE_DIV(), routine.getROUTINE_CONTENTS(), routine.getROUTINE_TOKEN());
		} else {
			System.out.printf("%s\t%s\t\t\t\t\t\t%s\n",routine.getROUTINE_DIV(), routine.getROUTINE_CONTENTS(), routine.getROUTINE_TOKEN());
		}
		
		System.out.println("------------------------------------------------------------------------");
		System.out.println("위 내용을 저장한다. : 1.확인 | 2.취소");
		System.out.print("선택 : ");
		String manu = scan.next();
		
		if(manu.equals("1")) {
			try {
				String sql=	""+
							"INSERT INTO routine (ROUTINE_SEQ, ROUTINE_DIV, ROUTINE_CONTENTS, ROUTINE_TOKEN)"+
							"VALUES (ROUTINE_SEQ.nextval, ?, ?, ?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, routine.getROUTINE_DIV());
				pstmt.setString(2, routine.getROUTINE_CONTENTS());
				pstmt.setInt(3, routine.getROUTINE_TOKEN());
				
				pstmt.executeUpdate();
				System.out.println("\n루틴을 저장 했습니다.\n");
				DBManager.close(conn, pstmt, null);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		rtManu();
	}
	
	public int[] rtSelect(String div) {
		Connection conn = DBManager.getConnection();
		String sql = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int array[]=null;
		
		try {
			sql = ""
					+ "SELECT ROUTINE_SEQ, ROUTINE_CONTENTS, ROUTINE_TOKEN "
					+ "FROM ROUTINE " 
					+ "WHERE ROUTINE_DIV = ? "
					;
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, routine.getROUTINE_DIV());
			rs = pstmt.executeQuery();

			PreparedStatement pstmt2 = conn.prepareStatement(sql);
			pstmt2.setString(1, routine.getROUTINE_DIV());
			ResultSet rs2 = pstmt2.executeQuery();
			int num = 0;
			while (rs2.next()) {
				++num;
			}
			array = new int[num];
			
			if(array.length==0) {
				System.out.println("\n입력된 루틴이 없습니다.\n");
				rtManu();
			}
			
			rs2.close();
			pstmt2.close();
			
			routine.setROUTINE_DIV(div);
			System.out.printf("\n[%s 루틴 조회]\n",routine.getROUTINE_DIV());
			System.out.println("------------------------------------------------------------------------");
			System.out.printf("%s\t%s\t\t\t\t\t\t%s\n","번호","내용","지급토큰수");
			System.out.println("------------------------------------------------------------------------");
			
			int num2 = 0;
			while (rs.next()) {
				array[num2] = rs.getInt("ROUTINE_SEQ");
				++num2;
				routine.setROUTINE_SEQ(rs.getInt("ROUTINE_SEQ"));
				routine.setROUTINE_CONTENTS(rs.getString("ROUTINE_CONTENTS"));
				routine.setROUTINE_TOKEN(rs.getInt("ROUTINE_TOKEN"));
				
				if(routine.getROUTINE_CONTENTS().length()>=25) {
					System.out.printf("%s\t%s\t\t%s\n",num2, routine.getROUTINE_CONTENTS(), routine.getROUTINE_TOKEN());
				} else if(routine.getROUTINE_CONTENTS().length()>=17) {
					System.out.printf("%s\t%s\t\t\t%s\n",num2, routine.getROUTINE_CONTENTS(), routine.getROUTINE_TOKEN());
				} else if(routine.getROUTINE_CONTENTS().length()>=12) {
					System.out.printf("%s\t%s\t\t\t\t%s\n",num2, routine.getROUTINE_CONTENTS(), routine.getROUTINE_TOKEN());
				} else if(routine.getROUTINE_CONTENTS().length()>=6) {
					System.out.printf("%s\t%s\t\t\t\t\t%s\n",num2, routine.getROUTINE_CONTENTS(), routine.getROUTINE_TOKEN());
				} else {
					System.out.printf("%s\t%s\t\t\t\t\t\t%s\n",num2, routine.getROUTINE_CONTENTS(), routine.getROUTINE_TOKEN());
				}
			}
			System.out.println("------------------------------------------------------------------------");

			
			DBManager.close(conn, pstmt, rs);
			}catch(Exception e) {
				e.printStackTrace();
			}
		return array;
	}
	
	public void rtManageMent() {
		Connection conn = DBManager.getConnection();
		String sql = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		System.out.println("\n[루틴 조회]");
		System.out.println("------------------------------------------------------------------------");
		System.out.println("[조회할 할일의 주기]");
		System.out.println("메뉴: 1.일일 | 2.주간 | 3.월간 ");
		System.out.print("선택: ");
		int divnum = scan.nextInt();
		while(divnum>3 || divnum<=0) {
			System.out.print("재입력: ");
			divnum = scan.nextInt();
		}
		if(divnum==1) {
			routine.setROUTINE_DIV("일일");
		} else if(divnum==2) {
			routine.setROUTINE_DIV("주간");
		} else if(divnum==3) {
			routine.setROUTINE_DIV("월간");
		}
		String div = routine.getROUTINE_DIV();
		int array[]=rtSelect(routine.getROUTINE_DIV());
		
		try {
			System.out.println("메뉴: 1.완료 | 2.수정 | 3.삭제 | 4.돌아가기");
			System.out.print("입력: ");
			int manu = scan.nextInt();
			while(manu>5 || manu<=0) {
				System.out.print("재입력: ");
				manu = scan.nextInt();
			}
			if(manu==1) {
				System.out.println("달성한 루틴 번호를 입력하세요.");
				System.out.print("입력: ");
				int seq = scan.nextInt();
				while(seq>array.length || seq<=0) {
					System.out.print("재입력: ");
					seq = scan.nextInt();
				}
				routine.setROUTINE_SEQ(array[seq-1]);
				
				
				sql = "SELECT ROUTINE_CONTENTS, ROUTINE_TOKEN FROM ROUTINE "
						+ "WHERE ROUTINE_SEQ=? ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, routine.getROUTINE_SEQ());
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					routine.setROUTINE_CONTENTS(rs.getString("ROUTINE_CONTENTS"));
					routine.setROUTINE_TOKEN(rs.getInt("ROUTINE_TOKEN"));
				}
				
				System.out.printf("\n[선택한 %s 루틴]\n",div);
				System.out.println("------------------------------------------------------------------------");
				System.out.printf("%s\t%s\t\t\t\t\t\t%s\n","번호","내용","지급토큰수");
				System.out.println("------------------------------------------------------------------------");
				if(routine.getROUTINE_CONTENTS().length()>=25) {
					System.out.printf("%s\t%s\t\t%s\n",seq, routine.getROUTINE_CONTENTS(), routine.getROUTINE_TOKEN());
				} else if(routine.getROUTINE_CONTENTS().length()>=19) {
					System.out.printf("%s\t%s\t\t\t%s\n",seq, routine.getROUTINE_CONTENTS(), routine.getROUTINE_TOKEN());
				} else if(routine.getROUTINE_CONTENTS().length()>=12) {
					System.out.printf("%s\t%s\t\t\t\t%s\n",seq, routine.getROUTINE_CONTENTS(), routine.getROUTINE_TOKEN());
				} else if(routine.getROUTINE_CONTENTS().length()>=6) {
					System.out.printf("%s\t%s\t\t\t\t\t%s\n",seq, routine.getROUTINE_CONTENTS(), routine.getROUTINE_TOKEN());
				} else {
					System.out.printf("%s\t%s\t\t\t\t\t\t%s\n",seq, routine.getROUTINE_CONTENTS(), routine.getROUTINE_TOKEN());
				}
				System.out.println("------------------------------------------------------------------------");
				System.out.println("위 내용을 완료한다. : 1.확인 | 2.취소");
				System.out.print("선택 : ");
				String manu2 = scan.next();
				
				if(manu2.equals("1")) {
					sql = "INSERT INTO TOKEN(TOKEN_SEQ, TOKEN_AMOUNT, TOKEN_HISTORY, ROUTINE_SEQ) "+
							"VALUES (TOKEN_SEQ.NEXTVAL, ?, '루틴', ?) "
							;
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, routine.getROUTINE_TOKEN());
					pstmt.setInt(2, routine.getROUTINE_SEQ());
					pstmt.executeUpdate();
					System.out.printf("\n%s번의 토큰을 얻었습니다.\n",seq);
				}
			} else if(manu==2) {
				System.out.println("수정할 루틴 번호를 입력하세요.");
				System.out.print("입력: ");
				int seq = scan.nextInt();
				while(seq>array.length || seq<=0) {
					System.out.print("재입력: ");
					seq = scan.nextInt();
				}
				routine.setROUTINE_SEQ(array[seq-1]);
				
				System.out.println("\n할일의 주기를 수정하시겠습니까?");
				System.out.println("1.네 | 2.아니오");
				System.out.print("입력: ");
				int sel = scan.nextInt();
				
				while(sel>2 || sel<=0) {
					System.out.print("재입력: ");
					sel=scan.nextInt();
				}
				
				if(sel==1) {
					System.out.println("\n[해야 할일의 주기]");
					System.out.println("메뉴: 1.일일 | 2.주간 | 3.월간 ");
					System.out.print("선택: ");
					divnum = scan.nextInt();
					while(divnum>3 || divnum<=0) {
						System.out.print("재입력: ");
						divnum = scan.nextInt();
					}
					if(divnum==1) {
						routine.setROUTINE_DIV("일일");
					} else if(divnum==2) {
						routine.setROUTINE_DIV("주간");
					} else if(divnum==3) {
						routine.setROUTINE_DIV("월간");
					}
				} else if(sel==2) {
					routine.setROUTINE_DIV(div);
				}
				
				System.out.println("\n할일의 내용을 수정하시겠습니까?");
				System.out.println("1.네 | 2.아니오");
				System.out.print("입력: ");
				sel = scan.nextInt();
				
				while(sel>2 || sel<=0) {
					System.out.print("재입력: ");
					sel=scan.nextInt();
				}
				
				if(sel==1) {
					System.out.println("\n[해야 할일의 내용]");
					System.out.print("내용: ");
					scan.nextLine();
					String contents = scan.nextLine();
					while(contents.length()<=0 || contents.length()>30) {
						System.out.print("내용: ");
						scan.nextLine();
						contents = scan.nextLine();
					}
					routine.setROUTINE_CONTENTS(contents);
				} else if(sel==2) {
					sql = "SELECT ROUTINE_CONTENTS FROM ROUTINE WHERE ROUTINE_SEQ=? ";
					
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, routine.getROUTINE_SEQ());
					rs = pstmt.executeQuery();
					if(rs.next()) {
					routine.setROUTINE_CONTENTS(rs.getString("ROUTINE_CONTENTS"));	;
					}
				}
				
				System.out.println("\n지급받을 토큰수를 수정하시겠습니까?");
				System.out.println("1.네 | 2.아니오");
				System.out.print("입력: ");
				sel = scan.nextInt();
				
				if(sel==1) {
					System.out.println("\n[완료시 받을 토큰수]");
					System.out.print("지급토큰수: ");
					int token = scan.nextInt();
					routine.setROUTINE_TOKEN(token);
				} else if(sel==2) {
					sql = "SELECT ROUTINE_TOKEN FROM ROUTINE WHERE ROUTINE_SEQ=? ";
					
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, routine.getROUTINE_SEQ());
					rs = pstmt.executeQuery();
					if(rs.next()) {
					routine.setROUTINE_TOKEN(rs.getInt("ROUTINE_TOKEN"));
					}
				}
				
				
				System.out.printf("\n[선택한 %s번 루틴]\n", seq);
				System.out.println("------------------------------------------------------------------------");
				System.out.printf("%s\t%s\t%s\t\t\t\t\t\t%s\n","상태", "주기","내용", "지급토큰수");
				System.out.println("------------------------------------------------------------------------");
				sql = "SELECT ROUTINE_CONTENTS, ROUTINE_TOKEN FROM ROUTINE "
						+ "WHERE ROUTINE_SEQ=? ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, routine.getROUTINE_SEQ());
				rs = pstmt.executeQuery();
				
				
				String contents;
				if (rs.next()) {
					contents = rs.getString("ROUTINE_CONTENTS");
					if (contents.length() >= 25) {
						System.out.printf("%s\t%s\t%s\t\t%s\n","[수정전]", div, contents,
								rs.getInt("ROUTINE_TOKEN"));
					} else if (contents.length() >= 19) {
						System.out.printf("%s\t%s\t%s\t\t\t%s\n","[수정전]", div, contents,
								rs.getInt("ROUTINE_TOKEN"));
					} else if (contents.length() >= 12) {
						System.out.printf("%s\t%s\t%s\t\t\t\t%s\n","[수정전]", div, contents,
								rs.getInt("ROUTINE_TOKEN"));
					} else if (contents.length() >= 6) {
						System.out.printf("%s\t%s\t%s\t\t\t\t\t%s\n","[수정전]",  div, contents,
								rs.getInt("ROUTINE_TOKEN"));
					} else {
						System.out.printf("%s\t%s\t%s\t\t\t\t\t\t%s\n", "[수정전]", div, contents,
								rs.getInt("ROUTINE_TOKEN"));
					}
				}
				
				contents=routine.getROUTINE_CONTENTS();
				if (contents.length() >= 25) {
					System.out.printf("%s\t%s\t%s\t\t%s\n","[수정후]", routine.getROUTINE_DIV(), contents,
							routine.getROUTINE_TOKEN());
				} else if (contents.length() >= 19) {
					System.out.printf("%s\t%s\t%s\t\t\t%s\n","[수정후]", routine.getROUTINE_DIV(), contents,
							routine.getROUTINE_TOKEN());
				} else if (contents.length() >= 12) {
					System.out.printf("%s\t%s\t%s\t\t\t\t%s\n","[수정후]", routine.getROUTINE_DIV(), contents,
							routine.getROUTINE_TOKEN());
				} else if (contents.length() >= 6) {
					System.out.printf("%s\t%s\t%s\t\t\t\t\t%s\n","[수정후]", routine.getROUTINE_DIV(), contents,
							routine.getROUTINE_TOKEN());
				} else {
					System.out.printf("%s\t%s\t%s\t\t\t\t\t\t%s\n", "[수정후]", routine.getROUTINE_DIV(), contents,
							routine.getROUTINE_TOKEN());
				}
				System.out.println("------------------------------------------------------------------------");
				System.out.println("위 내용을 수정한다. : 1.확인 | 2.취소");
				System.out.print("선택 : ");
				String manu2 = scan.next();
				
				if(manu2.equals("1")) {
					sql = "" + "UPDATE ROUTINE SET ROUTINE_DIV=?, ROUTINE_CONTENTS=?, ROUTINE_TOKEN=? "
							+ "WHERE ROUTINE_SEQ=?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, routine.getROUTINE_DIV());
					pstmt.setString(2, routine.getROUTINE_CONTENTS());
					pstmt.setInt(3, routine.getROUTINE_TOKEN());
					pstmt.setInt(4, routine.getROUTINE_SEQ());
					pstmt.executeUpdate();
					System.out.printf("\n%s번 루틴이 수정되었습니다.\n",seq);
				}
			} else if(manu==3) {
				System.out.println("삭제할 루틴 번호를 입력하세요.");
				System.out.print("입력: ");
				int seq = scan.nextInt();
				while(seq>array.length || seq<=0) {
					System.out.print("재입력: ");
					seq = scan.nextInt();
				}
				routine.setROUTINE_SEQ(array[seq-1]);
				
				System.out.printf("\n[선택한 %s번 루틴]\n", seq);
				System.out.println("------------------------------------------------------------------------");
				System.out.printf("%s\t%s\t\t\t\t\t\t%s\n","주기","내용", "지급토큰수");
				System.out.println("------------------------------------------------------------------------");
				sql = "SELECT ROUTINE_DIV, ROUTINE_CONTENTS, ROUTINE_TOKEN FROM ROUTINE "
						+ "WHERE ROUTINE_SEQ=? ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, routine.getROUTINE_SEQ());
				rs = pstmt.executeQuery();
				String contents;
				if(rs.next()) {
					contents=rs.getString("ROUTINE_CONTENTS");
					if (contents.length() >= 25) {
						System.out.printf("%s\t%s\t\t%s\n",rs.getString("ROUTINE_DIV"), contents,
								rs.getInt("ROUTINE_TOKEN"));
					} else if (contents.length() >= 19) {
						System.out.printf("%s\t%s\t\t\t%s\n",rs.getString("ROUTINE_DIV"), contents,
								rs.getInt("ROUTINE_TOKEN"));
					} else if (contents.length() >= 12) {
						System.out.printf("%s\t%s\t\t\t\t%s\n",rs.getString("ROUTINE_DIV"), contents,
								rs.getInt("ROUTINE_TOKEN"));
					} else if (contents.length() >= 6) {
						System.out.printf("%s\t%s\t\t\t\t\t%s\n",rs.getString("ROUTINE_DIV"), contents,
								rs.getInt("ROUTINE_TOKEN"));
					} else {
						System.out.printf("%s\t%s\t\t\t\t\t\t%s\n",rs.getString("ROUTINE_DIV"), contents,
								rs.getInt("ROUTINE_TOKEN"));
					}
				}
				System.out.println("------------------------------------------------------------------------");
				System.out.println("위 내용을 삭제한다. : 1.확인 | 2.취소");
				System.out.print("선택 : ");
				String manu2 = scan.next();
				
				if(manu2.equals("1")) {
					sql="UPDATE TOKEN SET ROUTINE_SEQ=NULL WHERE ROUTINE_SEQ=? ";
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, routine.getROUTINE_SEQ());
					pstmt.executeUpdate();
					
					sql="DELETE FROM ROUTINE WHERE ROUTINE_SEQ=? ";
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, routine.getROUTINE_SEQ());
					pstmt.executeUpdate();
					System.out.printf("\n%s번 루틴이 삭제되었습니다.\n",seq);
					
				}
			}
			DBManager.close(conn, pstmt, rs);
			rtManu();
		}catch(Exception e) {e.printStackTrace();}
		
	}
}
