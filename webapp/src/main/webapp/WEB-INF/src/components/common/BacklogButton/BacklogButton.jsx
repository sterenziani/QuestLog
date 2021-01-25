import React, { Component } from 'react';
import {Button, Col, Row} from 'react-bootstrap';
import {Translation} from 'react-i18next';
import withUser from '../../hoc/withUser';
import BacklogService from "../../../services/api/backlogService";
import GameService from "../../../services/api/gameService";

class BacklogButton extends Component {
    state = {
        game: this.props.game? this.props.game : null,
        added: this.props.game? this.props.game.in_backlog : false
    };

    render() {
        if(this.props.userIsLoggedIn && this.props.userIsAdmin){
            return(
                <>
                    <Row className="m-0 w-auto">
                        <Col className="p-0 w-auto">
                            <Button onClick={this.editHandler} variant="outline-warning" className="btn-block" style={{borderTopLeftRadius: '0.5rem', borderTopRightRadius: '0', borderBottomLeftRadius: '0', borderBottomRightRadius: '0', borderBottomWidth: '0'}}>
                                <Translation>{t => t("games.profile.edit")}</Translation>
                            </Button>
                        </Col>
                        <Col className="p-0 w-auto">
                            <Button onClick={this.deleteHandler} variant="outline-danger" className="btn-block" style={{borderTopLeftRadius: '0', borderTopRightRadius: '0.5rem', borderBottomLeftRadius: '0', borderBottomRightRadius: '0', borderBottomWidth: '0', borderLeftWidth: '0'}}>
                                <Translation>{t => t("games.profile.delete")}</Translation>
                            </Button>
                        </Col>
                    </Row>
                    <Button onClick={this.backlogHandler} style={{borderRadius: '0'}} variant={this.getButtonType()}>
                        <Translation>{t => t(this.getTranslation())}</Translation>
                    </Button>
                </>
            );
        }
        return(
            <Button onClick={this.backlogHandler} variant={this.getButtonType()} style={{borderBottomLeftRadius: '0', borderBottomRightRadius: '0'}}>
                <Translation>{t => t(this.getTranslation())}</Translation>
            </Button>
        );
    }

    backlogHandler = () => {
        if(this.state.added){
            BacklogService.removeGameFromBacklog(this.state.game.id);
        }
        else{
            BacklogService.addGameToBacklog(this.state.game.id);
        }
        this.setState({added : !this.state.added});
    }

    editHandler = () => {
        console.log("Implementame");
    }

    deleteHandler = () => {
        GameService.deleteGame(this.state.game.id);
        window.location.reload();
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

export default withUser(BacklogButton);
