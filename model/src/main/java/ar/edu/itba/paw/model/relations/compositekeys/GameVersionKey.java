package ar.edu.itba.paw.model.relations.compositekeys;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class GameVersionKey implements Serializable {

    @Column(name = "game")
    private Long gameId;
    @Column(name = "platform")
    private Long platformId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameVersionKey that = (GameVersionKey) o;
        return Objects.equals(gameId, that.gameId) &&
                Objects.equals(platformId, that.platformId);
    }

    @Override
    public int hashCode()
    {
        int hashCode = 1;
        hashCode = (int) (31 * hashCode + gameId);
        hashCode = (int) (31 * hashCode + platformId);
        return hashCode;
    }
}
