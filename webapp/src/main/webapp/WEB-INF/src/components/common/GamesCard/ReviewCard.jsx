import React, { Component } from 'react';
import {Button, Card, Form, Row, Col, Container, Modal} from "react-bootstrap";
import { CREATED } from '../../../services/api/apiConstants';
import {Translation} from "react-i18next";
import "../../../../src/index.scss";
import { withTranslation } from 'react-i18next';
import withQuery from "../../hoc/withQuery";
import withRedirect from '../../hoc/withRedirect';
import withUser from "../../hoc/withUser";
import {Slider} from '@material-ui/core';
import BacklogService from "../../../services/api/backlogService";
import ReviewService from "../../../services/api/reviewService";
import ScoreService from "../../../services/api/scoreService";

class ReviewCard extends Component {
    state = {
        game : this.props.game,
        params : {score: null, platform: this.props.platforms[0].id, body: ""},
        platforms : this.props.platforms,
        userId : this.props.userId,
        showModal: false,
        published: false,
    };

    componentWillMount() {
        const score = ScoreService.getUserGameScore(this.state.userId, this.state.game.id);
        Promise.all([ score ]).then((responses) => {
            let paramsCopy = Object.assign({}, this.state.params);
            paramsCopy.score = responses[0].score;
            this.setState({params: paramsCopy, loading:false});
        });
        this.props.addRedirection("gameProfile", `/games/${this.state.game.id}`);
    }

    onChangePlatforms(e){
        let paramsCopy = Object.assign({}, this.state.params);
        paramsCopy.platform = e.target.value;
        this.setState({params: paramsCopy});
    }

    onChangeBody(e){
        let paramsCopy = Object.assign({}, this.state.params);
        paramsCopy.body = e.target.value;
        this.setState({params: paramsCopy});
    }

    handleSliderChange(e, newValue) {
        let paramsCopy = Object.assign({}, this.state.params);
        paramsCopy.score = newValue;
        this.setState({params: paramsCopy});
    }

    getUserScore() {
        if(this.state.params.score || this.state.params.score === 0) {
            return parseInt(this.state.params.score);
        }
        else {
            return '-';
        }
    }

    getUserScoreNumerical() {
        if(this.state.params.score || this.state.params.score === 0) {
            return parseInt(this.state.params.score);
        }
        else {
            return 50;
        }
    }

    switchModal(){
        this.setState({showModal: !this.state.showModal});
    }

    publishReviewHandler(){
        if(this.props.userIsLoggedIn)
        {
            if(this.props.game.in_backlog){
                this.switchModal();
            }
            else{
                this.publishReview();
            }
        }
        else
            this.props.activateRedirect("login");
    }

    publishAndRemoveFromBacklog = () => {
        BacklogService.removeGameFromBacklog(this.props.game.id);
        this.publishReview();
        if(this.state.showModal)
            this.switchModal();
    }

    publishAndKeepInBacklog = () => {
        this.publishReview();
        if(this.state.showModal)
            this.switchModal();
    }

    publishReview = () => {
        this.setState({published:true});
        ReviewService.addReview(this.state.game.id, this.state.params.score, this.state.params.platform, this.state.params.body)
        .then(data => {
            if(data.status === CREATED){
                this.props.activateRedirect("gameProfile");
            }
            else{
                this.setState({published: false});
            }
        });
    }

    render() {
        return (
            <Card style={{borderBottomLeftRadius: 30, borderBottomRightRadius: 30 }} className="m-5 bg-light-grey right-wave left-wave" bordered>
                <Card.Header className="bg-very-dark text-white px-3 d-flex">
                    <h2 className="share-tech-mono">
                        <Translation>{t => t("reviews.addingReview", {value: this.state.game.title})}</Translation>
                    </h2>
                </Card.Header>
                <Card.Body className="card-body d-flex flex-wrap justify-content-center align-items-center">
                    <Col class="p-5">
                        <Form>
                            <Form.Group controlId="formScore">
                                <Form.Label className="text-center">
                                    <h5 className="text-center"><strong><Translation>{t => t("score.your")}</Translation></strong></h5>
                                </Form.Label>
                                <Container>
                                <Row>
                                    <Col>
                                        <Row>
                                            <Col className="col-sm-7 my-auto">
                                                <Slider className="my-3" onChange={(e, newValue) => {this.handleSliderChange(e, newValue)}}
                                                    style={{root:{color: 'light', height: 10}, thumb:{height: 24, width:24, backgroundColor: "primary"}}}
                                                        min={0} max={100} step={1} value={this.getUserScoreNumerical()}/>
                                            </Col>
                                            <Col>
                                                <div className="score-display text-center px-5"> <p class="badge badge-success">{this.getUserScore()}</p></div>
                                            </Col>
                                        </Row>
                                    </Col>
                                </Row>
                                </Container>
                            </Form.Group>
                            <Form.Group controlId="formPlatforms">
                                <div>
                                    <Form.Label>
                                        <h5>
                                            <strong>
                                                <Translation>{t => t("reviews.platform")}</Translation>
                                            </strong>
                                        </h5>
                                    </Form.Label>
                                </div>
                                <div className="text-center">
                                    <Form.Control className="text-center" as="select" value={this.state.params.platforms} onChange={this.onChangePlatforms.bind(this)} style={{padding: "5px"}}>
                                    {
                                        this.state.platforms.map(p => (<option key={p.id} value={p.id}>{p.name}</option>))
                                    }
                                    </Form.Control>
                                </div>
                            </Form.Group>
                            <Form.Group controlId="formText">
                                <Form.Label><h5><strong><Translation>{t => t("reviews.body")}</Translation></strong></h5></Form.Label>
                                <Form.Control onChange={this.onChangeBody.bind(this)} as="textarea" rows={6} />
                            </Form.Group>
                            <div className="text-center">
                            {
                                (this.state.params.score && this.state.params.body.length > 0)? [<Button className="btn btn-dark mt-3" onClick={(e) => {this.publishReviewHandler(e)}}><Translation>{t => t("submit")}</Translation></Button>]
                                : [<Button disabled className="btn btn-dark mt-3"><Translation>{t => t("submit")}</Translation></Button>]
                            }
                            </div>
                        </Form>
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
                    </Col>
                </Card.Body>
            </Card>
        );
    }
}



export default withTranslation() (withUser(withQuery(withRedirect(ReviewCard, {login: "/login"}))));
