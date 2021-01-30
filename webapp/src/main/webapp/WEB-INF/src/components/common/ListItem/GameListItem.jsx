import React, { Component } from 'react';
import { Card } from 'react-bootstrap';
import BacklogButton from '../../common/BacklogButton/BacklogButton';
import GameCover from '../../common/GameCover/GameCover';

class GameListItem extends Component {
    state = {
        enabled: true,
        game: this.props.game
    };

    updateGameBacklogStatus = status => {
        this.state.game.in_backlog = status;
        this.setState({game: this.state.game});
    };

    deleteGame = status => {
        this.setState({enabled: false});
    };

    render() {
        if(this.state.enabled){
            return (
                <Card className="m-3 d-flex bg-transparent" style={{width: '250px', border: '0'}}>
                    <BacklogButton game={this.state.game} onUpdate={this.updateGameBacklogStatus} onDelete={this.deleteGame}/>
                    <a className="d-flex flex-column flex-grow-1 text-white" href={`${process.env.PUBLIC_URL}/games/` + this.state.game.id}>
                        <GameCover cover={this.state.game.cover} resize='true'/>
                        <div className="card-body bg-primary flex-grow-1">
                            <h5>{this.state.game.title}</h5>
                        </div>
                    </a>
                </Card>
            )
        }
        return (<></>)
    }
}

export default GameListItem;
