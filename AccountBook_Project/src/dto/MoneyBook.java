//자바에서 가계부 테이블의 데이터를 얻고 보내줄 클래스
package dto;

//라이브러리 클래스 : 싱글톤, get, set
public class MoneyBook {
	private int MONEY_SEQ;
	private String MONEY_DATE;
	private int CATEGORY_SEQ;
	private String MONEY_INOUT;
	private int MONEY_WON;
	
	//필드
	private static MoneyBook singleton = new MoneyBook();
	
	//생성자
	private MoneyBook() {}
	
	//메소드
	public static MoneyBook getInstance() {
		return singleton;
	}
	
	public int getMONEY_SEQ() {
		return MONEY_SEQ;
	}
	
	public void setMONEY_SEQ(int MONEY_SEQ) {
		this.MONEY_SEQ = MONEY_SEQ;
	}
	
	public String getMONEY_DATE() {
		return MONEY_DATE;
	}
	
	public void setMONEY_DATE(String MONEY_DATE) {
		if(MONEY_DATE.contains("/")) {
			this.MONEY_DATE = MONEY_DATE.replace("/","-");
		}else if(MONEY_DATE.contains("-")) {
			this.MONEY_DATE = MONEY_DATE;
		} else if(MONEY_DATE.contains(".")) {
			this.MONEY_DATE = MONEY_DATE.replace(".","-");
		} else {
			this.MONEY_DATE = MONEY_DATE.replace(" ","-");
		}
	}
	
	public int getCATEGORY_SEQ() {
		return CATEGORY_SEQ;
	}
	
	public void setCATEGORY_SEQ(int CATEGORY_SEQ) {
		this.CATEGORY_SEQ = CATEGORY_SEQ;
	}
	
	public String getMONEY_INOUT() {
		return MONEY_INOUT;
	}
	
	public void setMONEY_INOUT(String MONEY_INOUT) {
		this.MONEY_INOUT = MONEY_INOUT;
	}
	
	public int getMONEY_WON() {
		
		return MONEY_WON;
	}
	
	public void setMONEY_WON(int MONEY_WON) {
		this.MONEY_WON = MONEY_WON;
	}
}

