import React, { Component } from 'react';
import ImageService from "../../../services/api/imageService";
import {
    Card,
} from 'react-bootstrap';
import defaultGameCover from './images/default_game_cover.png';

class GameCover extends Component {
    state = {
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
              }).then((data) =>  {});
    }

    render() {
        let resize = this.props.resize;
        return (
            <Card.Img className={resize? 'cover' : ''} variant="top" src={this.state.cover} />
        )
    }
}

export default GameCover;
