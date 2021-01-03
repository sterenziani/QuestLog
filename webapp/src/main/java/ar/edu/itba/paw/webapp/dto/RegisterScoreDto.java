package ar.edu.itba.paw.webapp.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class RegisterScoreDto {
    public RegisterScoreDto() {}

    @Max(100)
    @Min(0)
    private int score;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

}
