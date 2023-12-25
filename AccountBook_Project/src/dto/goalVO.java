package dto;

// Goal 클래스의 정보를 DB에 저장 및 로드하기 위한 매개 클래스
public class goalVO {
	private int goal_seq;
	private String goal_contents;
	private int goal_token;
	
	public int getGoalSeq() {
		return goal_seq;
	}
	public void setGoalSeq(int goal_seq) {
		this.goal_seq = goal_seq;
	}
	public String getGoalContents() {
		return goal_contents;
	}
	public void setGoalContents(String goal_contents) {
		this.goal_contents = goal_contents;
	}
	public int getGoalToken() {
		return goal_token;
	}
	public void setGoalToken(int goal_token) {
		this.goal_token = goal_token;
	}
	
	@Override
	public String toString() {
		return "goalVO [goal_seq=" + goal_seq + ", goal_contents=" + goal_contents + ", goal_token=" + goal_token;
	}
	
	
	
	
}
