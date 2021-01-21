import React, { Component } from 'react';
import {Button, Card, Form, Row, Col, Container} from "react-bootstrap";
import {Translation} from "react-i18next";
import "../../../../src/index.scss";
import GameCover from "../GameCover/GameCover";
import NumericInput from "react-numeric-input";
import withQuery from "../../hoc/withQuery";
import ReviewService from "../../../services/api/reviewService";
import { withTranslation } from 'react-i18next';
import ScoreService from "../../../services/api/scoreService";
import {Slider} from '@material-ui/core';


class ReviewCard extends Component {
    state = {
        game : this.props.game,
        params : {score: null, platform: this.props.platforms[0].id, body: ""},
        platforms : this.props.platforms,
        userId : this.props.userId,
    };

    componentWillMount() {
        const score = ScoreService.getUserGameScore(this.state.userId, this.state.game.id);
        Promise.all([ score ]).then((responses) => {
            this.state.params.score = responses[0].score;
            this.setState({
                loading: false,
            });
        });
    }

    onChangePlatforms(e){
        this.state.params.platform = e.target.value;
        this.setState({});
    }

    onChangeBody(e){
        this.state.params.body = e.target.value;
        this.setState({});
    }

    handleSliderChange(e, newValue) {
        this.state.params.score = newValue;
        this.setState({});
    }

    getUserScore() {
        if(this.state.params.score || this.state.params.score == 0) {
            return parseInt(this.state.params.score);
        }
        else {
            return '-';
        }
    }

    getUserScoreNumerical() {
        if(this.state.params.score || this.state.params.score == 0) {
            return parseInt(this.state.params.score);
        }
        else {
            return 50;
        }
    }

    handleSubmit = () =>{
        if(this.props.userId) {
            ReviewService.addReview(this.state.game.id, this.state.params.score, this.state.params.platform, this.state.params.body);
            window.location.href = `${process.env.PUBLIC_URL}/games/${this.state.game.id}`;
        }
        else
            window.location.href = `${process.env.PUBLIC_URL}/login`;
    }

    render() {
        const { t } = this.props
        return (
            <Card style={{borderBottomLeftRadius: 30, borderBottomRightRadius: 30 }} className="m-5 bg-light-grey right-wave left-wave" bordered>
                <Card.Header className="bg-very-dark text-white px-3 d-flex">
                    <h2 className="share-tech-mono">
                        <Translation>{t => t("reviews.addingReview", {value: this.state.game.title})}</Translation>
                    </h2>
                </Card.Header>
                <Card.Body className="card-body d-flex flex-wrap justify-content-center align-items-center">
                    <Col class="p-5">
                        <Form onSubmit={this.handleSubmit}>
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
                                (this.state.params.score && this.state.params.body.length > 0)? [<Button className="btn btn-dark mt-3" onClick={this.handleSubmit}><Translation>{t => t("submit")}</Translation></Button>]
                                : [<Button disabled className="btn btn-dark mt-3" onClick={this.handleSubmit}><Translation>{t => t("submit")}</Translation></Button>]
                            }
                            </div>
                        </Form>
                    </Col>
                </Card.Body>
            </Card>
        );
    }
}



export default withTranslation() (withQuery(ReviewCard));
