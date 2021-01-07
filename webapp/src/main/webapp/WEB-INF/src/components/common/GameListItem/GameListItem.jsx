import React, { Component } from 'react';
//import api from '../../../services/api/api';
import ImageService from "../../../services/api/imageService";
import {
    Card,
//    Button
} from 'react-bootstrap';

//import axios from 'axios';

import BacklogButton from '../../common/BacklogButton/BacklogButton';

import defaultGameCover from './images/default_game_cover.png';

class GameListItem extends Component {
    state = {
        id: this.props.id,
        title: this.props.title,
        cover: defaultGameCover
    };
    
    componentWillMount() {
        if(this.props.cover == null)
    		this.setState({ cover: defaultGameCover, });
    	else
	    	ImageService.getImageLink(this.props.cover)
	            .then((data) => {
	                this.setState({
	                    cover: data
	                });
	            }).then((data) =>  {console.log(this.state.cover)});
    }
    
    render() {
        return (
            <Card style={{ width: '18rem' }}>
                <BacklogButton/>
                <Card.Img variant="top" src={this.state.cover} />
                <Card.Body>
                    {this.state.title}
                </Card.Body>
            </Card>
        )
    }
}

export default GameListItem;