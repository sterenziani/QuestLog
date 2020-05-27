package ar.edu.itba.paw.model.relations;

import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.Genre;
import ar.edu.itba.paw.model.relations.compositekeys.ClassificationKey;

import javax.persistence.*;

@Entity
@Table(name = "classifications")
public class Classification {

    @EmbeddedId
    private ClassificationKey id;

    @ManyToOne
    @MapsId("game")
    @JoinColumn(name = "game")
    private Game game;

    @ManyToOne
    @MapsId("genre")
    @JoinColumn(name = "genre")
    private Genre genre;


    public Classification(){
        //For Hibernate
    }

    public Game getGame() {
        return game;
    }

    public Genre getGenre() {
        return genre;
    }
}
