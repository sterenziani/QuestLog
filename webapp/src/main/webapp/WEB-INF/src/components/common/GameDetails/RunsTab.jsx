import React, { Component } from 'react';
import {Card, Row, Col, Badge, Button, Container} from "react-bootstrap";
import {Grid} from '@material-ui/core';
import {Translation} from "react-i18next";
import "../../../../src/index.scss";
import RunService from "../../../services/api/runService";

class RunsTab extends Component {
    state = {
        game: this.props.game,
        userId: 22,
        loggedIn: true,
        avgTimes: [],
        fastestRuns: [],
        myRuns: []
    };

    convertTime(seconds) {
        const hours = Math.floor(seconds / 3600);
        seconds = seconds % 3600;
        const minutes = ("0" + seconds/60).slice(-2);
        seconds = ("0" + seconds%60).slice(-2);
        return "" +hours + " : " +minutes +" : " +seconds;
    };

    componentWillMount() {
        RunService.getGameTimes(this.props.game.gameId)
              .then((data) => {
                  this.setState({
                      avgTimes: data
                  });
              }).then((data) =>  {});
        RunService.getGameTopRuns(this.props.game.gameId)
            .then((data) => {
                this.setState({
                    fastestRuns: data
                });
            }).then((data) =>  {});
        RunService.getUserGameRuns(this.state.userId, this.props.game.gameId)
            .then((data) => {
                this.setState({
                    myRuns: data
                });
            }).then((data) =>  {});
    };

    render() {
        return (
            <Grid>
                <Col className="text-center m-4">
                    <Button variant={"success"}> <Translation>{t => t("runs.addRun")}</Translation> </Button>
                </Col>

                {
                    (this.state.loggedIn && this.state.myRuns.length > 0)? [
                        <Card className="m-5 bg-very-light right-wave left-wave" bordered style={{ borderBottomLeftRadius: 30, borderBottomRightRadius: 30 }}>
                            <Card.Header className="bg-very-dark text-white d-flex">
                                <h2 className="share-tech-mono"><Translation>{t => t("runs.myRuns")}</Translation></h2>
                            </Card.Header>
                            <Card.Body class="d-flex flex-wrap justify-content-center padding-left-wave padding-right-wave">
                            <Col>
                                <Row>
                                    <Col className="text-right bg-primary text-white"><strong><Translation>{t => t("runs.platform")}</Translation></strong></Col>
                                    <Col className="text-center bg-primary text-white"><strong><Translation>{t => t("runs.playstyle")}</Translation></strong></Col>
                                    <Col className="bg-primary text-white"><strong><Translation>{t => t("runs.time")}</Translation></strong></Col>
                                </Row>

                                {this.state.myRuns.map(r => (
                                    <Row className="m-1">
                                        <Col className="text-right"> {r.platform.shortName} </Col>
                                        <Col className="text-center"> <Translation>{t => t("runs.playstyles." +r.playstyle.name)}</Translation> </Col>
                                        <Col> {this.convertTime(r.time)} </Col>
                                    </Row>
                                ))}
                            </Col>
                            </Card.Body>
                        </Card>
                    ] : []
                }


                <Card className="m-5 bg-very-light right-wave left-wave" bordered style={{ borderBottomLeftRadius: 30, borderBottomRightRadius: 30 }}>
                    <Card.Header className="bg-very-dark text-white d-flex">
                    	<h2 className="share-tech-mono"><Translation>{t => t("runs.avgTimes")}</Translation></h2>
                	</Card.Header>
                	<Card.Body class="d-flex flex-wrap justify-content-center padding-left-wave padding-right-wave">
                		<Col>
                			<Row>
                				<Col className="text-right bg-primary text-white"><strong><Translation>{t => t("runs.playstyle")}</Translation></strong></Col>
                				<Col className="bg-primary text-white"><strong><Translation>{t => t("runs.avgTime")}</Translation></strong></Col>
                			</Row>

                            {this.state.avgTimes.map(p => (
                                <Row className="m-1">
                                    <Col className="text-right"> <Translation>{t => t("runs.playstyles." +p.playstyle.name)}</Translation> </Col>
                                    <Col> {p.time != "0 : 00 : 00" ? [p.time] : [<Translation>{t => t("runs.notAvailable")}</Translation>]} </Col>
                                </Row>
                            ))}
                        </Col>
                    </Card.Body>
                </Card>

                <Card className="m-5 bg-very-light right-wave left-wave" bordered style={{ borderBottomLeftRadius: 30, borderBottomRightRadius: 30 }}>
                    <Card.Header className="bg-very-dark text-white d-flex">
                        <h2 className="share-tech-mono"><Translation>{t => t("runs.fastestRuns")}</Translation></h2>
                    </Card.Header>
                    <Card.Body class="mt-2 d-flex flex-wrap justify-content-center padding-left-wave padding-right-wave">
                        <Col>
                            {this.state.fastestRuns.map(r => (
                                <Row className="m-1">
                                    <Col className="text-right"> {r.user.username} </Col>
                                    <Col className="text-center"> {r.platform.shortName} </Col>
                                    <Col> {this.convertTime(r.time)} </Col>
                                </Row>
                            ))}
                        </Col>
                    </Card.Body>
                </Card>


            </Grid>
        )
    }
}

export default RunsTab;
