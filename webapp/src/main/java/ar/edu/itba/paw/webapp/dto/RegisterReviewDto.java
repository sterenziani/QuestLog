package ar.edu.itba.paw.webapp.dto;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class RegisterReviewDto {
	public RegisterReviewDto() {}
	
	private Long platform;
	
    @Max(100)
    @Min(0)
    private int score;

	private String body;

	public Long getPlatform() {
		return platform;
	}

	public void setPlatform(Long platform) {
		this.platform = platform;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
}
