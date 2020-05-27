package ar.edu.itba.paw.model.relations;

import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.Platform;
import ar.edu.itba.paw.model.relations.compositekeys.GameVersionKey;

import javax.persistence.*;

@Entity
@Table(name = "game_versions")
public class GameVersion {

    @EmbeddedId
    private GameVersionKey id;

    @ManyToOne
    @MapsId("game")
    @JoinColumn(name = "game")
    private Game game;

    @ManyToOne
    @MapsId("platform")
    @JoinColumn(name = "platform")
    private Platform platform;

    public GameVersion(){
        //For Hibernate
    }

    public Game getGame() {
        return game;
    }

    public Platform getPlatform() {
        return platform;
    }
}
