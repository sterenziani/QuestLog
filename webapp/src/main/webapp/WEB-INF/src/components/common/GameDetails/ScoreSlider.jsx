import React, { Component } from 'react';
import { withRouter } from "react-router-dom";
import {Row, Col, Badge, Button, Container} from "react-bootstrap";
import {Slider} from '@material-ui/core';
import {Translation} from "react-i18next";
import "../../../../src/index.scss";
import ScoreService from "../../../services/api/scoreService";

class ScoreSlider extends Component {
    state = {
        game: this.props.game,
        userScore : this.props.userScore,
        published : false,
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
        if(this.props.userId) {
            ScoreService.rateGame(this.state.game.id, this.state.userScore).then(data => {
                                                                                            if(data.status === '201'){
                                                                                                this.setState({published:true})
                                                                                            }
                                                                                            else{
                                                                                                // TODO: Force login or try again
                                                                                                console.log("Try again!");
                                                                                            }
                                                                                        });
        }
        else
            window.location.href = `${process.env.PUBLIC_URL}/login`;
    }

    getUserScore() {
        if(this.state.userScore || this.state.userScore === 0) {
            return parseInt(this.state.userScore);
        }
        else {
            return '-';
        }
    }

    getUserScoreNumerical() {
        if(this.state.userScore || this.state.userScore === 0) {
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
            </Row>
            </Container>
        )
    }
}

export default withRouter(ScoreSlider);
