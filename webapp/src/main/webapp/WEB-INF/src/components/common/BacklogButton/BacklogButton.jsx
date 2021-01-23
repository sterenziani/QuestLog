import React, { Component } from 'react';
import {Button} from 'react-bootstrap';
import {Translation} from 'react-i18next';
import BacklogService from "../../../services/api/backlogService";

class BacklogButton extends Component {
    state = {
        game: this.props.game? this.props.game : null,
        added: this.props.game? this.props.game.in_backlog : false
    };

    render() {
        return (
            <Button bg="light-grey" onClick={this.backlogHandler} variant={this.getButtonType()}>
                <Translation>{t => t(this.getTranslation())}</Translation>
            </Button>
        )
    }

    backlogHandler = () => {
        if(this.state.added){
            BacklogService.removeGameFromBacklog(this.state.game.id).then((data) => console.log(data));
        }
        else{
            BacklogService.addGameToBacklog(this.state.game.id).then((data) => console.log(data));
        }
        this.setState({added : !this.state.added});
        console.log(this.state);
    }

    getButtonType() {
        let btype = "btn btn-outline-";
        btype += this.state.added ? "danger" : "success";
        return btype;
    }

    getTranslation() {
        let translate = 'games.profile.';
        translate += this.state.added ? 'removeFromBacklog' : 'addToBacklog';
        return translate;
    }
}

export default BacklogButton;
