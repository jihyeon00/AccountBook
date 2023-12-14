//루틴 관리
package accountbook;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class ABRoutine {
	Scanner scan = new Scanner(System.in);
	Connection conn;
	
	
	public ABRoutine() {
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
	
	public void rtManu() {
		System.out.println();
		System.out.println("[루틴 관리]");
		System.out.println("------------------------------------------------------------");
		System.out.println("들어갈 메뉴의 번호를 입력하세요.");
		System.out.println("------------------------------------------------------------");
		System.out.println("메뉴 : 1.루틴입력 | 2.루틴조회 | 3.루틴수정 | 4.루틴삭제 | 5.돌아가기");
		System.out.println("------------------------------------------------------------");
		System.out.print("선택: ");
		String manuno = scan.next();
		System.out.println();

		switch (manuno) {
		case "1":
			rtInsert();
			break;
		case "2":
			rtSelect();
			break;
		case "5":
			rtExit();
			break;
		default :
			rtManu();
			break;
		}
	}
	
	public void rtInsert() {
		Routine routine = new Routine();
		System.out.println("[루틴 입력]");
		System.out.println("----------------------------------");
		System.out.println("해야 할일이 언제마다 해야되나요?");
		System.out.println("----------------------------------");
		System.out.println("메뉴: 1.일일 | 2.주간 | 3.월간 ");
		System.out.print("선택: ");
		int divnum = scan.nextInt();
		System.out.println();
		if(divnum==1) {
			routine.setROUTINE_DIV("일일");
		} else if(divnum==2) {
			routine.setROUTINE_DIV("주간");
		} else if(divnum==3) {
			routine.setROUTINE_DIV("월간");
		} else {
			rtInsert();
		}
		System.out.println("----------------------------------");
		System.out.println("해야 할일의 내용을 적어주세요.");
		System.out.println("----------------------------------");
		System.out.print("내용: ");
		
		scan.nextLine();
		routine.setROUTINE_CONTENTS(scan.nextLine());
		
		//scan.nextLine();
		
		System.out.println("----------------------------------");
		System.out.println("\n할일을 완료했을때 받을 토큰의 개수를 적어주세요..");
		System.out.println("----------------------------------");
		System.out.print("지급토큰수: ");
		int token = Integer.parseInt(scan.nextLine());
		routine.setROUTINE_TOKEN(token);
		
		System.out.println("----------------------------------------------------------------");
		System.out.println("위 내용을 저장한다. : 1.확인 | 2.취소");
		System.out.print("선택 : ");
		String menu = scan.next();
		
		if(menu.equals("1")) {
			try {
				String sql=	""+
							"INSERT INTO routine (ROUTINE_SEQ, ROUTINE_DIV, ROUTINE_CONTENTS, ROUTINE_TOKEN)"+
							"VALUES (tk_seq.nextval, ?, ?, ?)";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, routine.getROUTINE_DIV());
				pstmt.setString(2, routine.getROUTINE_CONTENTS());
				pstmt.setInt(3, routine.getROUTINE_TOKEN());
				
				pstmt.executeUpdate();
				pstmt.close();
				System.out.println("\n루틴을 저장 했습니다.");
			}catch(Exception e) {
				e.printStackTrace();
				rtExit();
			}
		}
		rtManu();
	}
	
	public void rtSelect() {
		rtManu();
	}
	
	public void rtExit() {
		System.out.println("루틴을 종료합니다.");
		System.exit(0);
	}
	
	public static void main(String[] args) {
		ABRoutine rt = new ABRoutine();
		rt.rtManu();
	}

}
