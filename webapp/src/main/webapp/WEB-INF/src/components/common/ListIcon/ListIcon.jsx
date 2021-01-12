import React, { Component } from 'react';
import ImageService from "../../../services/api/imageService";
import {
    Card,
} from 'react-bootstrap';
import defaultGameCover from './images/default_game_cover.png';

class ListIcon extends Component {
    state = {
        icon: defaultGameCover
    };

	componentWillMount() {
      ImageService.getImageLink(this.props.icon)
          .then((data) => {
              this.setState({
                  icon: data
              });
          }).then((data) =>  {});
    }

    render() {
        return (
            <Card.Img className={resize? 'cover' : ''} variant="top" src={this.state.cover} />
        )
    }
}

export default ListIcon;
