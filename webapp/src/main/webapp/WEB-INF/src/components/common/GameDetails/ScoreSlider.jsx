import React, { Component } from 'react';
import { withRouter } from "react-router-dom";
import {Row, Col, Badge, Button, Container, Modal} from "react-bootstrap";
import {Slider} from '@material-ui/core';
import {Translation} from "react-i18next";
import "../../../../src/index.scss";
import withUser from '../../hoc/withUser';
import ScoreService from "../../../services/api/scoreService";
import BacklogService from "../../../services/api/backlogService";
import withRedirect from '../../hoc/withRedirect';

class ScoreSlider extends Component {
    state = {
        userScore : this.props.userScore,
        published : false,
        showModal: false,
    };

    switchModal(){
        this.setState({showModal: !this.state.showModal});
    }

    getScore() {
        if(this.props.game.votes > 0) {
            return this.props.game.score;
        }
        else {
            return (<Translation>{t => t("score.notAvailable")}</Translation>);
        }
    }

    handleSliderChange(e, newValue) {
        this.setState({userScore : newValue});
    }

    publishScoreHandler() {
        if(this.props.userIsLoggedIn)
        {
            if(this.props.game.in_backlog)
                this.switchModal();
            else
                this.publishScore();
        }
        else{
            this.props.activateRedirect("login")
        }
    }

    publishAndRemoveFromBacklog = () => {
        this.props.onBacklogUpdate(false);
        BacklogService.removeGameFromBacklog(this.props.game.id);
        this.publishScore();
        if(this.state.showModal)
            this.switchModal();
    }

    publishAndKeepInBacklog = () => {
        this.publishScore();
        if(this.state.showModal)
            this.switchModal();
    }

    publishScore = () => {
        ScoreService.rateGame(this.props.game.id, this.state.userScore)
        .then(data => {
            if(data.status == '201'){
                this.setState({published:true});
            }
            else{
                // TODO: Force login or try again
                console.log("Try again!");
            }
        });
    }

    getUserScore() {
        if(this.state.userScore || this.state.userScore == 0) {
            return parseInt(this.state.userScore);
        }
        else {
            return '-';
        }
    }

    getUserScoreNumerical() {
        if(this.state.userScore || this.state.userScore == 0) {
            return parseInt(this.state.userScore);
        }
        else {
            return 50;
        }
    }

    render() {
        return (
            <Container>
            <Row>
                <Col className="col-sm-3">
                    <Row> <h6 style={{fontWeight: "bold"}}> <Translation>{t => t("score.average")}</Translation> </h6> </Row>
                    <Row> <h1> <Badge className="badge-dark">{this.getScore()}</Badge> </h1> </Row>
                </Col>

                <Col className="col-sm-9">
                    <Row>
                        <Col className="col-sm-7 my-auto">
                            <Row className="m-auto"> <h6 style={{fontWeight: "bold"}}> <Translation>{t => t("score.your")}</Translation> </h6></Row>
                            <Slider className="my-3" onChange={(e, newValue) => {this.handleSliderChange(e, newValue)}}
                                style={{root:{color: 'light', height: 10}, thumb:{height: 24, width:24, backgroundColor: "primary"}}}
                                    min={0} max={100} step={1} value={this.getUserScoreNumerical()}/>
                        </Col>
                        <Col>
                            <div className="score-display text-center px-5"> <p class="badge badge-success">{this.getUserScore()}</p></div>
                            <div className="px-5">
                                {
                                    this.state.published?
                                        [<Button disabled className="btn-block" variant={"primary"}> <Translation>{t => t("score.rate")}</Translation> </Button>
                                        ] : [<Button className="btn-block" variant={"primary"} onClick={(e) => {this.publishScoreHandler(e)}}> <Translation>{t => t("score.rate")}</Translation> </Button>]
                                }
                            </div>
                        </Col>
                    </Row>
                </Col>
                <Modal show={this.state.showModal} onHide={() => this.switchModal()}>
                    <Modal.Header closeButton><Modal.Title><Translation>{t => t("games.profile.removeFromBacklogPrompt", {value: this.props.game.title})}</Translation></Modal.Title></Modal.Header>
                    <Modal.Body>
                        <Translation>{t => t("games.profile.removeFromBacklogPromptExplain")}</Translation>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="light" onClick={this.publishAndKeepInBacklog}><Translation>{t => t("games.profile.keepInBacklogAndSend")}</Translation></Button>
                        <Button variant="primary" onClick={this.publishAndRemoveFromBacklog}><Translation>{t => t("games.profile.removeFromBacklogAndSend")}</Translation></Button>
                    </Modal.Footer>
                </Modal>
            </Row>
            </Container>
        )
    }
}

export default withRouter(withUser(withRedirect(ScoreSlider, {login: "/login"})));
