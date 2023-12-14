//코드 테스트
package accountbook;

import java.util.Scanner;

public class Test {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		Category[] cg = Category.values();
		
		System.out.println("[카테고리 선택]");
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
		int b = scan.nextInt();
		System.out.println("입력값 : "+b+"."+cg[b-1]);

//		for(int i=0;i<cg.length;i++) {
//			if(i%5==0) {
//				System.out.println("\n");
//			}
//			System.out.print((i+1)+"."+cg[i]+" ");
//		}
//		System.out.println();
	}

}
