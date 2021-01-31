import React, { Component } from 'react';
import {Card, Row, Col, Button, Container} from "react-bootstrap";
import {Grid} from '@material-ui/core';
import {Translation} from "react-i18next";
import "../../../../src/index.scss";
import AnyButton from "../AnyButton/AnyButton";

class UserScoresTab extends Component {
    state = {
        visitedUser: this.props.visitedUser,
        scoresDisplayed: this.props.scoresDisplayed,
        scoresPagination: this.props.scoresPagination,
    };

    render() {
        return (
            <Grid>
            {
                    <Card className="m-5 bg-very-light right-wave left-wave" bordered style={{ borderBottomLeftRadius: 30, borderBottomRightRadius: 30 }}>
                        <Card.Header className="bg-very-dark text-white d-flex">
                            <div><h2 className="share-tech-mono"><Translation>{t => t("users.userScores", {value: this.state.visitedUser.username})}</Translation></h2></div>
                            {
                                this.state.scoresPagination.next && this.props.seeAll? [
                                    <div className="ml-auto">
                                        <AnyButton variant="link" className="text-white" href={`/users/` +this.state.visitedUser.id +'/scores'} textKey="navigation.seeAll"/>
                                    </div>
                                ] : []
                            }
                        </Card.Header>
                        <Card.Body class="d-flex flex-wrap justify-content-center padding-left-wave padding-right-wave">
                        {(this.state.scoresDisplayed.length > 0)? [
                            <Col>
                                {this.state.scoresDisplayed.map(r => (
                                    <Row className="m-1">
                                        <Col className="text-right">
                                            <AnyButton variant="link" className="p-0 m-0 font-weight-bold" href={ "/games/" +r.game.id } text={r.game.title}/>
                                        </Col>
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
