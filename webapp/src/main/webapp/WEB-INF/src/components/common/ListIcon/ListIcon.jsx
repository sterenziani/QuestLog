import React, { Component } from 'react';
import ImageService from "../../../services/api/imageService";
import {
    Card,
} from 'react-bootstrap';
import defaultGameCover from './images/default_game_cover.png';
import Spinner from "react-bootstrap/Spinner";

class ListIcon extends Component {
    state = {
        icon: defaultGameCover,
        loading: true,
    };

	componentWillMount() {
      ImageService.getImageLink(this.props.icon)
          .then((data) => {
              this.setState({
                  icon: data,
                  loading: false,
              });
          }).then((data) =>  {});
    }

    render() {
        if (this.state.loading === true) {
            return <div style={{
                position: 'absolute', left: '50%', top: '50%',
                transform: 'translate(-50%, -50%)'}}>
                <Spinner animation="border" variant="primary" />
            </div>
        }
	    return (
            <Card.Img className={resize? 'cover' : ''} variant="top" src={this.state.cover} />
        )
    }
}

export default ListIcon;
