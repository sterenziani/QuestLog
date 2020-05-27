package ar.edu.itba.paw.model.relations;

import ar.edu.itba.paw.model.Developer;
import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.Platform;
import ar.edu.itba.paw.model.relations.compositekeys.DevelopmentKey;

import javax.persistence.*;

@Entity
@Table(name = "development")
public class Development {

    @EmbeddedId
    private DevelopmentKey id;

    @ManyToOne
    @MapsId("game")
    @JoinColumn(name = "game")
    private Game game;

    @ManyToOne
    @MapsId("developer")
    @JoinColumn(name = "developer")
    private Developer developer;


    public Development(){
        //For Hibernate
    }

    public Game getGame() {
        return game;
    }

    public Developer getDeveloper() {
        return developer;
    }
}
