//자바에서 가계부 테이블의 데이터를 얻고 보내줄 클래스

package accountbook;

public class MoneyBook {
	private int MONEY_SEQ;
	private String MONEY_DATE;
	private String MONEY_CATEGORY;
	private String MONEY_INOUT;
	private int MONEY_WON;
	
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
		this.MONEY_DATE = MONEY_DATE;
	}
	
	public String getMONEY_CATEGORY() {
		return MONEY_CATEGORY;
	}
	
	public void setMONEY_CATEGORY(String MONEY_CATEGORY) {
		this.MONEY_CATEGORY = MONEY_CATEGORY;
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

