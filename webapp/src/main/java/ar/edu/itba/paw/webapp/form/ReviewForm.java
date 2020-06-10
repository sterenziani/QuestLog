package ar.edu.itba.paw.webapp.form;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class ReviewForm {
	
    @Size(min=100, max = 15000)
    private String body;
    
    @Min(0)
    @Max(100)
    private int score;

    private long platform;

    public String getBody() {
        return body;
    }

    public long getPlatform() {
        return platform;
    }

    public int getScore() {
        return score;
    }
    
    public void setBody(String body) {
        this.body = body;
    }

    public void setPlatform(long platform) {
        this.platform = platform;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
