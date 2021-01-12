import React, { Component } from 'react';
import {Card, Row, Col, Container, Tabs, Tab} from "react-bootstrap";
import {Grid} from '@material-ui/core';
import {Translation} from "react-i18next";
import "../../../../src/index.scss";
import GameDetailsCard from "./GameDetailsCard";
import ScoreSlider from "./ScoreSlider";
import RunsTab from "./RunsTab";
import ReviewsTab from "./ReviewsTab";
import ReviewService from "../../../services/api/reviewService";

class GameProfile extends Component {
    state = {
        game : this.props.game,
        userScore : 87,
        myReviews: [],
        userId: 22,
        loggedIn: true,
        displayedReviews: [],
        pagination: []
    };

    componentWillMount() {
        ReviewService.getUserGameReviews(this.state.userId, this.state.game.id)
              .then((data) => {
                  this.setState({
                      myReviews: data
                  });
              }).then((data) =>  {});
      ReviewService.getGameReviews(this.props.game.id)
          .then((data) => {
              this.setState({
                  displayedReviews: data.content,
                  pagination: data.pagination
              });
          }).then((data) =>  {});
    };

    render() {
        return (
            <Card className="m-5 bg-very-light right-wave left-wave" bordered style={{ borderBottomLeftRadius: 30, borderBottomRightRadius: 30 }}>
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
                                                <Tab className="bg-very-light" eventKey="runs" title={<Translation>{t => t("games.profile.runs")}</Translation>}>
                                                    <RunsTab game={this.state.game}/>
                                                </Tab>
                                                {
                                                    this.state.displayedReviews.length > 0? [
                                                    <Tab className="bg-very-light" eventKey="reviews" title=<Translation>{t => t("games.profile.reviews")}</Translation>>
                                                        <ReviewsTab className="p-5" key="1" game={this.state.game} reviews={this.state.displayedReviews} pagination={this.state.pagination} />
                                                    </Tab>] : [
                                                    <Tab className="bg-very-light" eventKey="reviews2" title=<Translation>{t => t("games.profile.reviews")}</Translation>>
                                                        <ReviewsTab className="p-5" key="1b" game={this.state.game} reviews={[]} pagination={[]} />
                                                    </Tab>]
                                                }
                                                {this.state.myReviews.length > 0? [
                                                    <Tab className="bg-very-light" eventKey="my-reviews" title=<Translation>{t => t("games.profile.myReviews")}</Translation>>
                                                        <ReviewsTab className="p-5" key="2" game={this.state.game} reviews={this.state.myReviews} label="reviews.myReviews"/>
                                                    </Tab>] : []
                                                }
                                            </Tabs>
                                        </div>
                                    ] : [ <Container className="text-center mt-2"> <p> <Translation>{t => t("games.profile.unreleased")}</Translation> </p> </Container> ]}
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
