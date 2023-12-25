package dto;

public class conAnalysisVO {
	private int money_seq;
	private String money_date;
	private String money_inout;
	private int money_won;
	private int category_seq;
	private String category_name;
	
	
	public int getMoneySeq() {
		return money_seq;
	}
	public void setMoneySeq(int money_seq) {
		this.money_seq = money_seq;
	}
	public String getMoneyDate() {
		return money_date;
	}
	public void setMoneyDate(String money_date) {
		this.money_date = money_date;
	}
	public String getMoneyInout() {
		return money_inout;
	}
	public void setMoneyInout(String money_inout) {
		this.money_inout = money_inout;
	}
	public int getMoneyWon() {
		return money_won;
	}
	public void setMoneyWon(int money_won) {
		this.money_won = money_won;
	}
	public int getCategorySeq() {
		return category_seq;
	}
	public void setCategorySeq(int category_seq) {
		this.category_seq = category_seq;
	}
	public String getCategoryName() {
		return category_name;
	}
	public void setCategoryName(String category_name) {
		this.category_name = category_name;
	}
	
}
