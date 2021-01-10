import React, { Component } from 'react';
import {Card, Row, Col, Container, Tabs, Tab} from "react-bootstrap";
import {Grid} from '@material-ui/core';
import {Translation} from "react-i18next";
import "../../../../src/index.scss";
import GameDetailsCard from "./GameDetailsCard";
import ScoreSlider from "./ScoreSlider";
import RunsTab from "./RunsTab";
import ReviewsTab from "./ReviewsTab";

class GameProfile extends Component {
    state = {
        game : this.props.game,
        userScore : 87,
    };

    render() {
        return (
            <Card className="m-5 bg-light-grey bg-very-light right-wave left-wave" bordered style={{ borderBottomLeftRadius: 30, borderBottomRightRadius: 30 }}>
                <div className="card-header bg-very-dark text-white">
                    <h2 className="share-tech-mono"> {this.state.game.title} </h2>
                </div>
                <Card.Body className="d-flex px-5">
                    {
                        <Grid style={{width: "100%"}}>
                            <Row>
                                <Col className=""><GameDetailsCard game={this.state.game}/></Col>
                                <Col className="p-3 col-9">
                                    {this.state.game.released? [
                                        <div>
                                            <ScoreSlider game={this.state.game}/>
                                            <Tabs className="mt-5 mx-3 bg-dark" defaultActiveKey="runs" id="uncontrolled-tab-example">
                                                <Tab className="bg-very-light-grey" eventKey="runs" title={<Translation>{t => t("games.profile.runs")}</Translation>}>
                                                    <RunsTab game={this.state.game}/>
                                                </Tab>
                                                <Tab className="bg-very-light-grey" eventKey="reviews" title=<Translation>{t => t("games.profile.reviews")}</Translation>>
                                                    <ReviewsTab className="p-5" game={this.state.game} />
                                                </Tab>
                                            </Tabs>
                                        </div>
                                    ] : [ <Container className="text-center mt-5"> <p> <Translation>{t => t("games.profile.unreleased")}</Translation> </p> </Container> ]}
                                </Col>
                            </Row>
                        </Grid>
                    }
                </Card.Body>
            </Card>
        );
    }
}

export default GameProfile;
