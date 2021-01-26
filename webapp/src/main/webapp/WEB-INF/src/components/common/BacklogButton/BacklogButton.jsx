import React, { Component } from 'react';
import {Button, Col, Row, Modal} from 'react-bootstrap';
import {Translation} from 'react-i18next';
import withUser from '../../hoc/withUser';
import BacklogService from "../../../services/api/backlogService";
import GameService from "../../../services/api/gameService";

class BacklogButton extends Component {
    state = {
        added: this.props.game? this.props.game.in_backlog : false,
        showModal: false,
    };

    backlogHandler = () => {
        if(this.state.added){
            BacklogService.removeGameFromBacklog(this.props.game.id);
            if(this.props.onUpdate)
                this.props.onUpdate(false);
        }
        else{
            BacklogService.addGameToBacklog(this.props.game.id);
            if(this.props.onUpdate)
                this.props.onUpdate(true);
        }
        this.setState({added : !this.state.added});
    }

    editHandler = () => {
        console.log("Implementame");
    }

    deleteHandler = () => {
        GameService.deleteGame(this.props.game.id);
        window.location.reload();
    }

    switchModal(){
        this.setState({showModal: !this.state.showModal});
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

    static getDerivedStateFromProps(nextProps, prevState) {
        return {
            added: nextProps.game? nextProps.game.in_backlog : false,
        };
    }

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
                            <Button onClick={() => {this.switchModal()}} variant="outline-danger" className="btn-block" style={{borderTopLeftRadius: '0', borderTopRightRadius: '0.5rem', borderBottomLeftRadius: '0', borderBottomRightRadius: '0', borderBottomWidth: '0', borderLeftWidth: '0'}}>
                                <Translation>{t => t("games.profile.delete")}</Translation>
                            </Button>
                            <Modal show={this.state.showModal} onHide={() => this.switchModal()}>
                                <Modal.Header closeButton><Modal.Title><Translation>{t => t("games.profile.deletingGame", {value: this.props.game.title})}</Translation></Modal.Title></Modal.Header>
                                <Modal.Body>
                                    <Translation>{t => t("games.profile.deleteSure")}</Translation>
                                </Modal.Body>
                                <Modal.Footer>
                                    <Button variant="light" onClick={() => {this.switchModal()}}><Translation>{t => t("games.profile.deleteNo")}</Translation></Button>
                                    <Button variant="danger" onClick={this.deleteHandler}><Translation>{t => t("games.profile.deleteYes")}</Translation></Button>
                                </Modal.Footer>
                            </Modal>
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
}

export default withUser(BacklogButton);
