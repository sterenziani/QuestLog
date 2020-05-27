package ar.edu.itba.paw.model.relations.compositekeys;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ClassificationKey implements Serializable {

    @Column(name = "game")
    private Long gameId;
    @Column(name = "genre")
    private Long genreId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassificationKey that = (ClassificationKey) o;
        return Objects.equals(gameId, that.gameId) &&
                Objects.equals(genreId, that.genreId);
    }

    @Override
    public int hashCode()
    {
        int hashCode = 1;
        hashCode = (int) (31 * hashCode + gameId);
        hashCode = (int) (31 * hashCode + genreId);
        return hashCode;
    }
}
