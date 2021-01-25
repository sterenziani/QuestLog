import React, { Component } from 'react';
import {
    Card,
} from 'react-bootstrap';
import BacklogButton from '../../common/BacklogButton/BacklogButton';
import GameCover from '../../common/GameCover/GameCover';

class GameListItem extends Component {
    state = {
        game: this.props.game
    };

    render() {
        return (
            <Card className="m-3 d-flex bg-transparent" style={{width: '250px', border: '0'}}>
                <BacklogButton game={this.state.game}/>
                <a className="d-flex flex-column flex-grow-1 text-white" href={`${process.env.PUBLIC_URL}/games/` + this.state.game.id}>
	                <GameCover cover={this.state.game.cover} resize='true'/>
			        <div className="card-body bg-primary flex-grow-1">
			            <h5>{this.state.game.title}</h5>
			        </div>
			    </a>
            </Card>
        )
    }
}

export default GameListItem;
