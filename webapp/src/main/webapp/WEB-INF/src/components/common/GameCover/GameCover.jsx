import React, { Component } from 'react';
import ImageService from "../../../services/api/imageService";
import { Card, Image } from 'react-bootstrap';
import defaultGameCover from './images/default_game_cover.png';
import "../../../../src/index.scss";
import Spinner from "react-bootstrap/Spinner";

class GameCover extends Component {
    state = {
        cover: defaultGameCover,
        loading: true,
    };

	componentWillMount() {
      if(this.props.cover == null) {
          this.setState(
              {cover: defaultGameCover,
                  loading: false,});
      }
      else
        ImageService.getImageLink(this.props.cover)
              .then((data) => {
                  this.setState({
                      cover: data,
                      loading: false,
                  });
              }).then((data) =>  {});
    }

    componentWillReceiveProps(newProps) {
        if(newProps.cover == null) {
            this.setState(
                {cover: defaultGameCover,
                    loading: false,});
        }
        else
          ImageService.getImageLink(newProps.cover)
                .then((data) => {
                    this.setState({
                        cover: data,
                        loading: false,
                    });
                }).then((data) =>  {});
    }

    render() {
        let resize = this.props.resize;
        let mini = this.props.mini;
        if (this.state.loading === true) {
            return <div style={{
                position: 'absolute', left: '50%', top: '50%',
                transform: 'translate(-50%, -50%)'}}>
                <Spinner animation="border" variant="primary" />
            </div>
        }
        return (
            <div>
            {mini? [<Image key={this.props.code} className={'cover-mini'} src={this.state.cover}/>] : [<Card.Img key={this.props.code} className={resize? 'cover' : ''} variant="top" src={this.state.cover} />]}
            </div>
        )
    }
}

export default GameCover;
