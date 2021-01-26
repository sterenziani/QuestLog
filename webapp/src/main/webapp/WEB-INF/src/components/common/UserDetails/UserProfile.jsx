import React, { Component } from 'react';
import {Card, Row, Tabs, Tab, Button} from "react-bootstrap";
import Spinner from 'react-bootstrap/Spinner';
import {Translation} from "react-i18next";
import "../../../../src/index.scss";
import BacklogService from "../../../services/api/backlogService";
import GameCover from "../GameCover/GameCover";
import UserScoresTab from "./UserScoresTab";
import UserRunsTab from "./UserRunsTab";
import UserReviewsTab from "./UserReviewsTab";
import GameListItem from "../ListItem/GameListItem";
import withUser from '../../hoc/withUser';
import ScoreService from "../../../services/api/scoreService";
import ReviewService from "../../../services/api/reviewService";
import RunService from "../../../services/api/runService";

class UserProfile extends Component {
    state = {
        loading: true,
        visitedUser : this.props.visitedUser,
        user: this.props.user ? this.props.user : null,
        loggedIn: this.props.userIsLoggedIn,
    };

    componentWillMount() {
        const fetchBacklog = BacklogService.getUserBacklog(this.props.visitedUser.id, 5);
        const fetchScore = ScoreService.getUserScores(this.props.visitedUser.id, 10);
        const fetchRev = ReviewService.getUserReviews(this.props.visitedUser.id, 5);
        const fetchRun = RunService.getUserRuns(this.props.visitedUser.id, 10);

        //TODO: Handle no response (404)
        Promise.all([ fetchBacklog, fetchScore, fetchRev, fetchRun ]).then((responses) => {
            this.setState({
                loading: false,
                backlog: responses[0].content,
                backlogPagination: responses[0].pagination,
                scoresDisplayed: responses[1].content,
                scoresPagination: responses[1].pagination,
                reviewsDisplayed: responses[2].content,
                reviewsPagination: responses[2].pagination,
                runsDisplayed: responses[3].content,
                runsPagination: responses[3].pagination,

            });
        });
    }

    render() {
        if (this.state.loading === true) {
            return <div style={{
                position: 'absolute', left: '50%', top: '50%',
                transform: 'translate(-50%, -50%)'}}>
                <Spinner animation="border" variant="primary" />
            </div>
        }
        return (
            <div>
                <div class="mx-5 align-middle">
                    <div class="mb-4 mt-5 text-center">
                        <h1 class="align-middle share-tech-mono">{this.state.visitedUser.username}</h1>
                        {(this.state.loggedIn && this.state.user.id === this.state.visitedUser.id)? [<h5 class="align-middle">{this.state.visitedUser.email}</h5>] : []}
                    </div>
                    <div class="d-flex text-left flex-wrap">
                        <div class="mb-0 m-3 bg-dark border-bottom border-primary rounded-lg text-white flex-grow-1 d-flex justify-content-center">
                            <div class="pl-3 d-flex justify-content-center flex-column">
                                <i class="fa fa-4x fa-star d-block"></i>
                            </div>
                            <div class="pt-5 pb-5 pr-5 pl-3">
                                <h5><Translation>{t => t("users.gamesRated", {value: this.state.visitedUser.score_total})}</Translation></h5>
                                <h5><Translation>{t => t("users.scoreAverage", {value: this.state.visitedUser.score_average})}</Translation></h5>
                            </div>
                        </div>
                        <div class="mb-0 m-3 bg-dark border-bottom border-primary rounded-lg text-white flex-grow-1 d-flex justify-content-center">
                            <div class="pl-3 d-flex justify-content-center flex-column">
                                <i class="fa fa-4x fa-gamepad d-block"></i>
                            </div>
                            <div class="pt-5 pb-5 pr-5 pl-3">
                                <h5><Translation>{t => t("users.runsCreated", {value: this.state.visitedUser.runs_total})}</Translation></h5>
                                <h5><Translation>{t => t("users.hoursPlayed", {value: this.state.visitedUser.runs_hours_played})}</Translation></h5>
                            </div>
                        </div>
                        {this.state.visitedUser.favorite_game? [
                            <div class="mb-0 m-3 py-3 px-5 bg-dark border-bottom border-primary rounded-lg text-white flex-grow-1 d-flex justify-content-center align-items-center">
                                <h5 class="pr-3"><Translation>{t => t("users.favoriteGame", {value: this.state.visitedUser.favorite_game})}</Translation></h5>
                                <a href={`${process.env.PUBLIC_URL}/games/` + this.state.visitedUser.favorite_game.id} class="text-white">
                                    <div class="bg-primary d-flex flex-row align-items-center">
                                        <GameCover mini='true' cover={this.state.visitedUser.favorite_game.cover}/>
                                    </div>
                                </a>
                            </div>
                        ] : []}
                    </div>
                </div>
                <div class="mx-5 align-middle">
                    <Tabs className="mt-5 mx-3 bg-dark" defaultActiveKey="backlog" id="uncontrolled-tab-example">
                        <Tab className="bg-very-light" eventKey="backlog" title={<Translation>{t => t("users.backlog")}</Translation>}>
                            <div>
                            <Card className="m-5 bg-light-grey" bordered style={{ borderBottomLeftRadius: 30, borderBottomRightRadius: 30 }}>
                                <Card.Header className="bg-very-dark text-white d-flex">
                                <div><h2 className="share-tech-mono"> <Translation>{t => t("users.userBacklog", {value: this.state.visitedUser.username})}</Translation></h2></div>
                                {
                                    this.state.backlogPagination.next? [
                                        <div className="ml-auto">
                                            <Button variant="link" className="text-white" href={`${process.env.PUBLIC_URL}/users/` +this.state.visitedUser.id +'/backlog'}><Translation>{t => t("navigation.seeAll")}</Translation></Button>
                                        </div>
                                    ] : []
                                }
                                </Card.Header>
                                <Card.Body className="card-body d-flex flex-wrap justify-content-center">
                                    {this.state.backlog.length === 0? [<Translation>{t => t("games.lists.emptyList")}</Translation>] : [<Row className="justify-content-center">{this.state.backlog.map(g => <GameListItem value={g.id} game={g}/>)}</Row>]}
                                </Card.Body>
                            </Card>
                            </div>
                        </Tab>
                        <Tab className="bg-very-light" eventKey="scores" title={<Translation>{t => t("users.scores")}</Translation>}>
                            <UserScoresTab seeAll={true} visitedUser={this.state.visitedUser} loggedUser={this.state.user} scoresDisplayed={this.state.scoresDisplayed} scoresPagination={this.state.scoresPagination}/>
                        </Tab>
                        <Tab className="bg-very-light" eventKey="runs" title={<Translation>{t => t("users.runs")}</Translation>}>
                            <UserRunsTab seeAll={true} visitedUser={this.state.visitedUser} loggedUser={this.state.user} runsDisplayed={this.state.runsDisplayed} runsPagination={this.state.runsPagination}/>
                        </Tab>
                        <Tab className="bg-very-light" eventKey="reviews" title={<Translation>{t => t("users.reviews")}</Translation>}>
                            <UserReviewsTab seeAll={true} visitedUser={this.state.visitedUser} loggedUser={this.state.user} reviewsDisplayed={this.state.reviewsDisplayed} reviewsPagination={this.state.reviewsPagination}/>
                        </Tab>
                    </Tabs>
                </div>
            </div>
        );
    }
}

export default withUser(UserProfile);
