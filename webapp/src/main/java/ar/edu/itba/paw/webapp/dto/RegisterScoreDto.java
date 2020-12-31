package ar.edu.itba.paw.webapp.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

public class RegisterScoreDto {
    public RegisterScoreDto() {}

    private String           username;

    @Max(100)
    @Min(0)
    private int              score;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

}
