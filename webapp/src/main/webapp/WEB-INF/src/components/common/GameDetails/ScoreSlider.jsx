import React, { Component } from 'react';
import {Row, Col, Badge, Button, Container} from "react-bootstrap";
import {Slider} from '@material-ui/core';
import {Translation} from "react-i18next";
import "../../../../src/index.scss";
import ScoreService from "../../../services/api/scoreService";

class ScoreSlider extends Component {
    state = {
        game: this.props.game,
        userScore: null,
    };

    getScore() {
        if(this.state.game.votes > 0) {
            return this.state.game.score;
        }
        else {
            return (<Translation>{t => t("score.notAvailable")}</Translation>);
        }
    }

    handleSliderChange(e, newValue) {
        this.setState({userScore : newValue});
    }

    publishScoreHandler(e) {
        console.log("Publishing score");
        ScoreService.rateGame(this.state.game.id, this.state.userScore);
        console.log("I sent the request to rate game");
    }

    getUserScore() {
        if(this.state.userScore != null) {
            return this.state.userScore;
        }
        else {
            return '-';
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
                                defaultValue={50} min={0} max={100} step={1} value={this.getUserScore()}/>
                        </Col>
                        <Col>
                            <div className="score-display text-center px-5"> <p class="badge badge-success">{this.getUserScore()}</p></div>
                            <div className="px-5"> <Button className="btn-block" variant={"primary"} onClick={(e) => {this.publishScoreHandler(e)}}> <Translation>{t => t("score.rate")}</Translation> </Button> </div>
                        </Col>
                    </Row>
                </Col>
            </Row>
            </Container>
        )
    }
}

export default ScoreSlider;