import React, { Component } from 'react';
import {Card, Row, Col, Badge, Button} from "react-bootstrap";
import {Grid, Slider} from '@material-ui/core';
import {Translation} from "react-i18next";
import "../../../../src/index.scss";
import GameDetailsCard from "../GameDetailsCard/GameDetailsCard";

class DetailCard extends Component {
    state = {
        game : this.props.game,
        userScore : 87,
    };

    render() {
        return (
            <Card className="m-5 bg-light-grey" bordered style={{ borderBottomLeftRadius: 30, borderBottomRightRadius: 30 }}>
                <div className="card-header bg-very-dark text-white px-3">
                    <h2 className="share-tech-mono">
                        {this.state.game.title}
                    </h2>
                </div>
                <div className="card-body d-flex flex-wrap justify-content-center">
                    {
                        <Grid>
                            <Row>
                            <Col><GameDetailsCard game={this.state.game} /></Col>
                        <Col>
                            <Row>
                                <Col>
                                    <Row>
                                        <h6 style={{fontWeight: "bold"}}>
                                        <Translation>
                                            {
                                                t => t("score.average")
                                            }
                                        </Translation>
                                        </h6>
                                    </Row>
                                    <Row>
                                        <h1>
                                        <Badge className="badge-dark">{this.getScore()}</Badge>
                                        </h1>

                                    </Row>

                                </Col>
                                <Col>
                                    <Row>
                                        <h6 style={{fontWeight: "bold"}}>

                                        <Translation >
                                            {
                                                t => t("score.your")
                                            }
                                        </Translation>
                                        </h6>
                                    </Row>
                                    <Row>
                                        <Slider  onChange={(e) => {this.handleChange(e)}}
                                                style={{root:{color: 'light', height: 10}, thumb:{height: 24, width:24, backgroundColor: "primary"}}}
                                                min={0} max={100} step={1} value={this.getUserScore()}/>
                                    </Row>
                                </Col>
                                <Col>
                                    <Row>
                                        <h1>
                                        <Badge  className="badge-success">{this.getUserScore()}</Badge>
                                        </h1>
                                    </Row>
                                    <Row>
                                        <Button variant={"primary"}>
                                            <Translation>
                                                {
                                                    t => t("score.rate")
                                                }
                                            </Translation>
                                        </Button>
                                    </Row>
                                </Col>
                            </Row>
                        </Col>
                            </Row></Grid>
                    }
                </div>
            </Card>
        );
    }

    getScore() {
        if(this.state.game.score) {
            return this.state.game.score;
        }
        else {
            return "-";
        }
    }

    handleChange = (e) => {
        this.setState({userScore : Number(e.target.value)});
    }

    getUserScore() {
        if(this.state.userScore) {
            return this.state.userScore;
        }
        else {
            return "-";
        }
    }
}

export default DetailCard;