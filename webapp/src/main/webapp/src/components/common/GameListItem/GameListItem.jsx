import React, { Component } from 'react';
import {
    Card,
    Button
} from 'react-bootstrap';
import {
    Translation
} from 'react-i18next';

import defaultGameCover from './images/default_game_cover.png';

class GameListItem extends Component {
    state = {}
    render() {
        return (
            <Card style={{ width: '18rem' }}>
                <Button variant="primary">
                    <Translation>
                    {
                        t => t('games.profile.addToBacklog')
                    }
                    </Translation>
                </Button>
                <Card.Img variant="top" src={ defaultGameCover } />
                <Card.Body>
                    Algun jueguito
                </Card.Body>
            </Card>
        )
    }
}

export default GameListItem;