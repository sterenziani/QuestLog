package ar.edu.itba.paw.model.compositekeys;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ReleaseKey implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Column(name = "game")
    private Long gameId;
    @Column(name = "region")
    private Long regionId;

    public ReleaseKey() {
    }

    public ReleaseKey(Long gameId, Long regionId) {
        this.gameId = gameId;
        this.regionId = regionId;
    }

    public Long getGameId() {
        return gameId;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReleaseKey that = (ReleaseKey) o;
        return Objects.equals(gameId, that.gameId) &&
                Objects.equals(regionId, that.regionId);
    }

    @Override
    public int hashCode()
    {
        int hashCode = 1;
        hashCode = (int) (31 * hashCode + gameId);
        hashCode = (int) (31 * hashCode + regionId);
        return hashCode;
    }
}
