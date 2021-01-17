import React, { Component } from 'react';
import {Card, Row, Col, Badge, Button, Container} from "react-bootstrap";
import {Grid} from '@material-ui/core';
import {Translation} from "react-i18next";
import "../../../../src/index.scss";
import ScoreService from "../../../services/api/scoreService";

class UserScoresTab extends Component {
    state = {
        user: this.props.user,
        userId: 22,
        loggedIn: true,
        scoresDisplayed: [],
        scoresPagination: []
    };

    componentWillMount() {
        ScoreService.getUserScores(this.props.user.id)
              .then((data) => {
                  this.setState({
                      scoresDisplayed: data.content,
                      scoresPagination: data.pagination
                  });
              }).then((data) =>  {});
    };

    render() {
        return (
            <Grid>
            {
                    <Card className="m-5 bg-very-light right-wave left-wave" bordered style={{ borderBottomLeftRadius: 30, borderBottomRightRadius: 30 }}>
                        <Card.Header className="bg-very-dark text-white d-flex">
                            <div><h2 className="share-tech-mono"><Translation>{t => t("users.userScores", {value: this.state.user.username})}</Translation></h2></div>
                            {
                                this.state.scoresPagination.next? [
                                    <div className="ml-auto">
                                        <Button variant="link" className="text-white" href={`${process.env.PUBLIC_URL}/users/` +this.state.user.id +'/scores'}><Translation>{t => t("navigation.seeAll")}</Translation></Button>
                                    </div>
                                ] : []
                            }
                        </Card.Header>
                        <Card.Body class="d-flex flex-wrap justify-content-center padding-left-wave padding-right-wave">
                        {(this.state.scoresDisplayed.length > 0)? [
                            <Col>
                                {this.state.scoresDisplayed.map(r => (
                                    <Row className="m-1">
                                        <Col className="text-right"> <a href={`${process.env.PUBLIC_URL}/games/` + r.game.id}>{r.game.title}</a></Col>
                                        <Col> {r.score} </Col>
                                    </Row>
                                ))}
                            </Col>
                            ] : [<Container className="text-center mt-5"> <p> <Translation>{t => t("users.noScoresSubmitted")}</Translation> </p> </Container>]
                        }
                        </Card.Body>
                    </Card>
            }
            </Grid>
        );
    }
}

export default UserScoresTab;
