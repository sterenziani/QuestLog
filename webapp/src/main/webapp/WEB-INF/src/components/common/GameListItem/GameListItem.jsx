import React, { Component } from 'react';
import {
    Card,
    Button
} from 'react-bootstrap';

import axios from 'axios';

import BacklogButton from '../../common/BacklogButton/BacklogButton';

import defaultGameCover from './images/default_game_cover.png';

class GameListItem extends Component {
    state = {
        id: this.props.id
    };
    render() {
        return (
            <Card style={{ width: '18rem' }}>
                <BacklogButton/>
                <Card.Img variant="top" src={ defaultGameCover } />
                <Card.Body>
                    hola
                </Card.Body>
            </Card>
        )
    }
}

export default GameListItem;