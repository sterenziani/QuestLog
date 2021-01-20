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

class UserProfile extends Component {
    state = {
        loading: true,
        user : this.props.user,
        loggedIn: this.props.loggedIn,
        loggedInId: this.props.loggedInId,
        backlog: [],
        backlogPagination: []
    };

    componentWillMount() {
        BacklogService.getUserBacklog(this.props.user.id)
              .then((data) => {
                  this.setState({
                      backlog: data.content,
                      backlogPagination: data.pagination,
                      loading: false,
                  });
              }).then((data) =>  {});
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
            <div>
                <div class="mx-5 align-middle">
                    <div class="mb-4 mt-5 text-center">
                        <h1 class="align-middle share-tech-mono">{this.state.user.username}</h1>
                        {(this.state.loggedIn && this.state.loggedInId === this.state.user.id)? [<h5 class="align-middle">{this.state.user.email}</h5>] : []}
                    </div>
                    <div class="d-flex text-left flex-wrap">
                        <div class="mb-0 m-3 bg-dark border-bottom border-primary rounded-lg text-white flex-grow-1 d-flex justify-content-center">
                            <div class="pl-3 d-flex justify-content-center flex-column">
                                <i class="fa fa-4x fa-star d-block"></i>
                            </div>
                            <div class="pt-5 pb-5 pr-5 pl-3">
                                <h5><Translation>{t => t("users.gamesRated", {value: this.state.user.score_total})}</Translation></h5>
                                <h5><Translation>{t => t("users.scoreAverage", {value: this.state.user.score_average})}</Translation></h5>
                            </div>
                        </div>
                        <div class="mb-0 m-3 bg-dark border-bottom border-primary rounded-lg text-white flex-grow-1 d-flex justify-content-center">
                            <div class="pl-3 d-flex justify-content-center flex-column">
                                <i class="fa fa-4x fa-gamepad d-block"></i>
                            </div>
                            <div class="pt-5 pb-5 pr-5 pl-3">
                                <h5><Translation>{t => t("users.runsCreated", {value: this.state.user.runs_total})}</Translation></h5>
                                <h5><Translation>{t => t("users.hoursPlayed", {value: this.state.user.runs_hours_played})}</Translation></h5>
                            </div>
                        </div>
                        {this.state.user.favorite_game? [
                            <div class="mb-0 m-3 py-3 px-5 bg-dark border-bottom border-primary rounded-lg text-white flex-grow-1 d-flex justify-content-center align-items-center">
                                <h5 class="pr-3"><Translation>{t => t("users.favoriteGame", {value: this.state.user.favorite_game})}</Translation></h5>
                                <a href={`${process.env.PUBLIC_URL}/games/` + this.state.user.favorite_game.id} class="text-white">
                                    <div class="bg-primary d-flex flex-row align-items-center">
                                        <GameCover mini='true' cover={this.state.user.favorite_game.cover}/>
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
                                <div><h2 className="share-tech-mono"> <Translation>{t => t("users.userBacklog", {value: this.state.user.username})}</Translation></h2></div>
                                {
                                    this.state.backlogPagination.next? [
                                        <div className="ml-auto">
                                            <Button variant="link" className="text-white" href={`${process.env.PUBLIC_URL}/users/` +this.state.user.id +'/backlog'}><Translation>{t => t("navigation.seeAll")}</Translation></Button>
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
                            <UserScoresTab user={this.state.user} loggedIn={this.state.loggedIn} loggedInId={this.state.loggedInId}/>
                        </Tab>
                        <Tab className="bg-very-light" eventKey="runs" title={<Translation>{t => t("users.runs")}</Translation>}>
                            <UserRunsTab user={this.state.user} loggedIn={this.state.loggedIn} loggedInId={this.state.loggedInId}/>
                        </Tab>
                        <Tab className="bg-very-light" eventKey="reviews" title={<Translation>{t => t("users.reviews")}</Translation>}>
                            <UserReviewsTab user={this.state.user} loggedIn={this.state.loggedIn} loggedInId={this.state.loggedInId}/>
                        </Tab>
                    </Tabs>
                </div>
            </div>
        );
    }
}

export default UserProfile;
