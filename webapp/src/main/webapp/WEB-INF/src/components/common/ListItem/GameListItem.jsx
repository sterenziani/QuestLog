import React, { Component } from 'react';
import { Card } from 'react-bootstrap';
import BacklogButton from '../../common/BacklogButton/BacklogButton';
import GameCover from '../../common/GameCover/GameCover';
import { LinkContainer } from 'react-router-bootstrap';

class GameListItem extends Component {
    state = {
        enabled: true,
        game: this.props.game
    };

    updateGameBacklogStatus = status => {
        let gameCopy = Object.assign({}, this.state.game);
        gameCopy.in_backlog = status;
        this.setState({game: gameCopy});
    };

    deleteGame = status => {
        this.setState({enabled: false});
    };

    render() {
        if(this.state.enabled){
            return (
                <Card className="m-3 d-flex bg-transparent" style={{width: '250px', border: '0'}}>
                    <BacklogButton game={this.state.game} onUpdate={this.updateGameBacklogStatus} onDelete={this.deleteGame}/>
                    <LinkContainer to={`/games/` + this.state.game.id}>
                        <a className="d-flex flex-column flex-grow-1 text-white">
                            <GameCover code={"cover-"+this.state.game.id} cover={this.state.game.cover} resize='true'/>
                            <div className="card-body bg-primary flex-grow-1">
                                <h5>{this.state.game.title}</h5>
                            </div>
                        </a>
                    </LinkContainer>
                </Card>
            )
        }
        return (<></>)
    }
}

export default GameListItem;
