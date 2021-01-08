import React, { Component } from 'react';
import ImageService from "../../../services/api/imageService";
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
    
	componentWillMount() {
      if(this.state.game.cover == null) 
        this.setState({ cover: defaultGameCover, }); 
      else 
        ImageService.getImageLink(this.state.game.cover) 
              .then((data) => { 
                  this.setState({ 
                      cover: data 
                  }); 
              }).then((data) =>  {console.log(this.state.game.cover)}); 
    }
    
    render() {
        return (
            <Card className="m-3 d-flex bg-transparent" style={{width: '250px',}}>
                <BacklogButton/>
                <a className="d-flex flex-column flex-grow-1 text-white" href={'/games/' + this.state.id}>
	                <Card.Img className="cover" variant="top" src={this.state.cover} />
			        <div className="card-body bg-primary flex-grow-1">
			            <h5>{this.state.game.title}</h5>
			        </div>
			    </a>
            </Card>
        )
    }
}

export default GameListItem;