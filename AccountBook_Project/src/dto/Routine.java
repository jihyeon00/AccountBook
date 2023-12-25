//루틴 테이블에 값을 입력하고 저장할때 도와줄 클래스
package dto;

//라이브러리 클래스 : 싱글톤, get, set
public class Routine {
	private int ROUTINE_SEQ;
    private String ROUTINE_DIV;
    private String ROUTINE_CONTENTS;
    private int ROUTINE_TOKEN;
    
    //필드
    private static Routine singleton = new Routine(); 
    
    //생성자
    private Routine() {}
    
    //메소드
    public static Routine getInstance() {
    	return singleton;
    }
    
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
