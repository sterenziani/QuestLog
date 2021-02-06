package ar.edu.itba.paw.model.compositekeys;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ScoreKey implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Column(name = "game")
	private Long gameId;
	@Column(name = "user_id")
	private Long userId;

    public ScoreKey() {
    }
 
    public ScoreKey(Long gameId, Long userId) {
        this.gameId = gameId;
        this.userId = userId;
    }
 
    public Long getGameId() {
        return gameId;
    }
 
    public Long getUserId() {
        return userId;
    }
    
    public void setGameId(long gameId) {
        this.gameId = gameId;
    }
 
    public void setUserId(long userId) {
        this.userId = userId;
    }
 
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScoreKey)) return false;
        ScoreKey that = (ScoreKey) o;
        return Objects.equals(getGameId(), that.getGameId()) &&
                Objects.equals(getUserId(), that.getUserId());
    }
 
	@Override
	public int hashCode()
	{
		int hashCode = 1;
		hashCode = (int) (31 * hashCode + userId.hashCode());
		hashCode = (int) (31 * hashCode + gameId.hashCode());
		return hashCode;
	}
}
