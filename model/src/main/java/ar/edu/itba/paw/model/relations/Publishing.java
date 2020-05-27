package ar.edu.itba.paw.model.relations;

import ar.edu.itba.paw.model.Developer;
import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.Publisher;
import ar.edu.itba.paw.model.relations.compositekeys.DevelopmentKey;
import ar.edu.itba.paw.model.relations.compositekeys.PublishingKey;

import javax.persistence.*;

@Entity
@Table(name = "publishing")
public class Publishing {

    @EmbeddedId
    private PublishingKey id;

    @ManyToOne
    @MapsId("game")
    @JoinColumn(name = "game")
    private Game game;

    @ManyToOne
    @MapsId("publisher")
    @JoinColumn(name = "publisher")
    private Publisher publisher;


    public Publishing(){
        //For Hibernate
    }

    public Game getGame() {
        return game;
    }

    public Publisher getPublisher() {
        return publisher;
    }
}
