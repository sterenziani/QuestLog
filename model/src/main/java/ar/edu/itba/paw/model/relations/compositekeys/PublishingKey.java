package ar.edu.itba.paw.model.relations.compositekeys;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PublishingKey implements Serializable {

    @Column(name = "game")
    private Long gameId;
    @Column(name = "publisher")
    private Long publisherId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PublishingKey that = (PublishingKey) o;
        return Objects.equals(gameId, that.gameId) &&
                Objects.equals(publisherId, that.publisherId);
    }

    @Override
    public int hashCode()
    {
        int hashCode = 1;
        hashCode = (int) (31 * hashCode + gameId);
        hashCode = (int) (31 * hashCode + publisherId);
        return hashCode;
    }
}
