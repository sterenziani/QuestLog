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
import withUser from '../../hoc/withUser';
import ScoreService from "../../../services/api/scoreService";
import Spinner from "react-bootstrap/Spinner";

class GameProfile extends Component {
    state = {
        game : this.props.game,
        userScore : null,
        myReviews: [],
        user: this.props.user ? this.props.user : null,
        loggedIn: this.props.userIsLoggedIn,
        displayedReviews: [],
        pagination: [],
        loading: true,
    };

    componentWillMount() {
        if(this.state.user)
        {
            const userReviews = ReviewService.getUserGameReviews(this.state.user.id, this.state.game.id);
            const score = ScoreService.getUserGameScore(this.state.user.id, this.state.game.id);

            //TODO: Handle no response (404)
            Promise.all([ userReviews, score ]).then((responses) => {
                this.setState({
                    myReviews: responses[0],
                    userScore : responses[1].score,
                    loading: false,
                });
            });
        }
        const gameReviews = ReviewService.getGameReviews(this.state.game.id, 5);
        //TODO: Handle no response (404)
        Promise.all([gameReviews]).then((responses) => {
            this.setState({
                displayedReviews: responses[0].content,
                pagination: responses[0].pagination,
                loading: false,
            });
        });
    };

    updateGameBacklogStatus = status => {
        let gameCopy = Object.assign({}, this.state.game);
        gameCopy.in_backlog = status;
        this.setState({game: gameCopy});
    };

    render() {
        if (this.state.loading === true) {
            return <div style={{
                position: 'absolute', left: '50%', top: '50%',
                transform: 'translate(-50%, -50%)'}}>
                <Spinner animation="border" variant="primary" />
            </div>
        }
        return (
            <Card className="m-5 bg-light-grey right-wave left-wave" bordered style={{ borderBottomLeftRadius: 30, borderBottomRightRadius: 30 }}>
                <div className="card-header bg-very-dark text-white">
                    <h2 className="share-tech-mono"> {this.state.game.title} </h2>
                </div>
                <Card.Body className="d-flex px-5">
                    {
                        <Grid style={{width: "100%"}}>
                            <Row>
                                <div><GameDetailsCard game={this.state.game} onUpdate={this.updateGameBacklogStatus}/></div>
                                <Col className="p-3">
                                    {this.state.game.released? [
                                        <div>
                                            <ScoreSlider game={this.state.game} userScore={this.state.userScore} user={this.state.user} onBacklogUpdate={this.updateGameBacklogStatus}/>
                                            <Tabs className="mt-5 mx-3 bg-dark" defaultActiveKey="runs" id="uncontrolled-tab-example">
                                                <Tab className="bg-very-light" eventKey="runs" title={<Translation>{t => t("games.profile.runs")}</Translation>}>
                                                    <RunsTab game={this.state.game} user={this.state.user} loggedIn={this.state.loggedIn}/>
                                                </Tab>
                                                {
                                                    this.state.displayedReviews.length > 0 ? [
                                                    <Tab className="bg-very-light" eventKey="reviews" title={<Translation>{t => t("games.profile.reviews")}</Translation>}>
                                                        <ReviewsTab className="p-5" key="1" game={this.state.game} user={this.state.user} loggedIn={this.state.loggedIn} reviews={this.state.displayedReviews} pagination={this.state.pagination} seeAll={true}/>
                                                    </Tab>] : [
                                                    <Tab className="bg-very-light" eventKey="reviews2" title={<Translation>{t => t("games.profile.reviews")}</Translation>}>
                                                        <ReviewsTab className="p-5" key="1b" game={this.state.game} user={this.state.user} loggedIn={this.state.loggedIn} reviews={[]} pagination={[]} seeAll={false}/>
                                                    </Tab>]
                                                }
                                                {this.state.myReviews.length > 0? [
                                                    <Tab className="bg-very-light" eventKey="my-reviews" title={<Translation>{t => t("games.profile.myReviews")}</Translation>}>
                                                        <ReviewsTab className="p-5" key="2" game={this.state.game} reviews={this.state.myReviews} label="reviews.myReviews" seeAll={false}/>
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

export default withUser(GameProfile);
