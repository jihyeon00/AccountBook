//루틴 테이블에 값을 입력하고 저장할때 도와줄 클래스
package accountbook;

public class Routine {
	int ROUTINE_SEQ;
    String ROUTINE_DIV;
    String ROUTINE_CONTENTS;
    int ROUTINE_TOKEN;
    
	public int getROUTINE_SEQ() {
		return ROUTINE_SEQ;
	}
	
	public void setROUTINE_SEQ(int ROUTINE_SEQ) {
		this.ROUTINE_SEQ = ROUTINE_SEQ;
	}
	
	public String getROUTINE_DIV() {
		return ROUTINE_DIV;
	}
	
	public void setROUTINE_DIV(String ROUTINE_DIV) {
		this.ROUTINE_DIV = ROUTINE_DIV;
	}
	
	public String getROUTINE_CONTENTS() {
		return ROUTINE_CONTENTS;
	}
	
	public void setROUTINE_CONTENTS(String ROUTINE_CONTENTS) {
		this.ROUTINE_CONTENTS = ROUTINE_CONTENTS;
	}
	
	public int getROUTINE_TOKEN() {
		return ROUTINE_TOKEN;
	}
	
	public void setROUTINE_TOKEN(int ROUTINE_TOKEN) {
		this.ROUTINE_TOKEN = ROUTINE_TOKEN;
	}
}
