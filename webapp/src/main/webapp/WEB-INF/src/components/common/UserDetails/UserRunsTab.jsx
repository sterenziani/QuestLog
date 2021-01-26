import React, { Component } from 'react';
import {Card, Row, Col, Badge, Button, Container} from "react-bootstrap";
import {Grid} from '@material-ui/core';
import {Translation} from "react-i18next";
import "../../../../src/index.scss";

class UserRunsTab extends Component {
    state = {
        visitedUser: this.props.visitedUser,
        runsDisplayed: this.props.runsDisplayed,
        runsPagination: this.props.runsPagination,
        loggedUser: this.props.loggedUser,
    };

    convertTime(seconds) {
        const hours = Math.floor(seconds / 3600);
        seconds = seconds % 3600;
        const minutes = ("0" + seconds/60).slice(-2);
        seconds = ("0" + seconds%60).slice(-2);
        return "" +hours + " : " +minutes +" : " +seconds;
    };

    render() {
        return (
            <Grid>
                {
                    <Card className="m-5 bg-very-light right-wave left-wave" bordered style={{ borderBottomLeftRadius: 30, borderBottomRightRadius: 30 }}>
                        <Card.Header className="bg-very-dark text-white d-flex">
                            <div><h2 className="share-tech-mono"><Translation>{t => t("users.userRuns", {value: this.state.visitedUser.username})}</Translation></h2></div>
                            {
                                this.state.runsPagination.next && this.props.seeAll? [
                                    <div className="ml-auto">
                                        <Button variant="link" className="text-white" href={`${process.env.PUBLIC_URL}/users/` +this.state.visitedUser.id +'/runs'}><Translation>{t => t("navigation.seeAll")}</Translation></Button>
                                    </div>
                                ] : []
                            }
                        </Card.Header>
                        <Card.Body class="d-flex flex-wrap justify-content-center padding-left-wave padding-right-wave">
                            {
                                (this.state.runsDisplayed.length > 0)? [
                                    <Col>
                                        <Row>
                                            <Col className="text-right bg-primary text-white"><strong><Translation>{t => t("runs.game")}</Translation></strong></Col>
                                            <Col className="text-center bg-primary text-white"><strong><Translation>{t => t("runs.playstyle")}</Translation></strong></Col>
                                            <Col className="bg-primary text-white"><strong><Translation>{t => t("runs.time")}</Translation></strong></Col>
                                        </Row>

                                        {this.state.runsDisplayed.map(r => (
                                            <Row className="m-1">
                                                <Col className="text-right"> <a href={`${process.env.PUBLIC_URL}/games/` + r.game.id}>{r.game.title}</a> ({r.platform.shortName})</Col>
                                                <Col className="text-center"> <Translation>{t => t("runs.playstyles." +r.playstyle.name)}</Translation> </Col>
                                                <Col> {this.convertTime(r.time)} </Col>
                                            </Row>
                                        ))}
                                    </Col>] : [<Container className="text-center mt-5"> <p> <Translation>{t => t("users.noRunsSubmitted")}</Translation> </p> </Container>]
                            }
                        </Card.Body>
                    </Card>
                }
            </Grid>
        )
    }
}

export default UserRunsTab;
