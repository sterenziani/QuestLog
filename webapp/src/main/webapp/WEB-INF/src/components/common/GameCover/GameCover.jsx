import React, { Component } from 'react';
import ImageService from "../../../services/api/imageService";
import { Card, Image } from 'react-bootstrap';
import defaultGameCover from './images/default_game_cover.png';
import "../../../../src/index.scss";

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
        let mini = this.props.mini;
        return (
            <div>
            {mini? [<Image className={'cover-mini'} src={this.state.cover}/>] : [<Card.Img className={resize? 'cover' : ''} variant="top" src={this.state.cover} />]}
            </div>
        )
    }
}

export default GameCover;
