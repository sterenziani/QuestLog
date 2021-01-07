import React, { Component } from 'react';


import {
    Card, Container,
} from 'react-bootstrap';


import BacklogButton from '../../common/BacklogButton/BacklogButton';

import defaultGameCover from './images/default_game_cover.png';

class GameListItem extends Component {
    state = {
        id: this.props.id,
        game: this.props.game,
        cover: defaultGameCover
    };
    
    render() {
        return (
            <Card bg="light-grey" text="white" style={{
                width: '18rem',
                paddingLeft: 10,
                paddingRight: 10,
                paddingTop: 10,
                paddingBottom: 10}}>
                <BacklogButton/>
                <Card.Img variant="top" src={this.state.game.cover == null ? defaultGameCover : this.state.game.cover} />
                    <Container bg="primary">
                        {this.state.game.title}
                    </Container>
            </Card>
        )
    }
}

export default GameListItem;