import React, { Component } from 'react';
import {Card, Row, Col, Container} from "react-bootstrap";
import {Grid} from '@material-ui/core';
import {Translation} from "react-i18next";
import "../../../../src/index.scss";
import RunService from "../../../services/api/runService";
import Spinner from "react-bootstrap/Spinner";
import withUser from '../../hoc/withUser';
import AnyButton from "../AnyButton/AnyButton";

class RunsTab extends Component {
    state = {
        game: this.props.game,
        avgTimes: [],
        fastestRuns: [],
        myRuns: [],
        loading: true,
        user: this.props.user ? this.props.user : null,
        loggedIn: this.props.userIsLoggedIn,
    };

    convertTime(seconds) {
        const hours = Math.floor(seconds / 3600);
        seconds = seconds % 3600;

        let minutes = Math.floor(seconds/60);
        if(minutes < 10)
            minutes = "0" + minutes;
        seconds = seconds % 60;
        if(seconds < 10)
            seconds = "0" + seconds;
        return "" +hours + " : " +minutes +" : " +seconds;
    };

    componentWillMount() {
        if(this.state.user){
            const fetchUsers = RunService.getUserGameRuns(this.state.user.id, this.props.game.id);
            
            Promise.all([ fetchUsers ]).then((responses) => {
                this.setState({
                    myRuns : responses[0],
                });
            });
        }
        const fetchAvg = RunService.getGameTimes(this.props.game.id);
        const fetchFastest = RunService.getGameTopRuns(this.props.game.id);

        Promise.all([ fetchAvg, fetchFastest ]).then((responses) => {
            this.setState({
                loading: false,
                avgTimes: responses[0],
                fastestRuns : responses[1]
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
            <Grid>
                <div className="text-center m-4 my-5">
                    <AnyButton variant="success" href={`/createRun/${this.state.game.id}`} textKey="runs.addRun"/>
                </div>

                {
                    (this.state.loggedIn && this.state.myRuns.length > 0)? [
                        <Card className="m-5 bg-very-light right-wave left-wave" bordered style={{ borderBottomLeftRadius: 30, borderBottomRightRadius: 30 }}>
                            <Card.Header className="bg-very-dark text-white d-flex">
                                <h2 className="share-tech-mono"><Translation>{t => t("runs.myRuns")}</Translation></h2>
                            </Card.Header>
                            <Card.Body class="d-flex flex-wrap justify-content-center">
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
                	<Card.Body class="d-flex flex-wrap justify-content-center">
                		<Col>
                			<Row>
                				<Col className="text-right bg-primary text-white"><strong><Translation>{t => t("runs.playstyle")}</Translation></strong></Col>
                				<Col className="bg-primary text-white"><strong><Translation>{t => t("runs.avgTime")}</Translation></strong></Col>
                			</Row>

                            {this.state.avgTimes.map(p => (
                                <Row className="m-1">
                                    <Col className="text-right"> <Translation>{t => t("runs.playstyles." +p.playstyle.name)}</Translation> </Col>
                                    <Col> {p.time !== "0 : 00 : 00" ? [p.time] : [<Translation>{t => t("runs.notAvailable")}</Translation>]} </Col>
                                </Row>
                            ))}
                        </Col>
                    </Card.Body>
                </Card>

                <Card className="m-5 bg-very-light right-wave left-wave" bordered style={{ borderBottomLeftRadius: 30, borderBottomRightRadius: 30 }}>
                    <Card.Header className="bg-very-dark text-white d-flex">
                        <h2 className="share-tech-mono"><Translation>{t => t("runs.fastestRuns")}</Translation></h2>
                    </Card.Header>
                    <Card.Body class="mt-2 d-flex flex-wrap justify-content-center">
                        <Col>
                            {this.state.fastestRuns.length > 0 ? [
                                this.state.fastestRuns.map(r => (
                                    <Row className="m-1">
                                        <Col className="text-right">
                                            <AnyButton variant="link" className="p-0 m-0 font-weight-bold text-primary" href={ "/users/" +r.user.id } text={r.user.username}/>
                                        </Col>
                                        <Col className="text-center"> {r.platform.shortName} </Col>
                                        <Col> {this.convertTime(r.time)} </Col>
                                    </Row>
                                ))
                            ] : [ <Container className="text-center mt-5"> <p> <Translation>{t => t("runs.noRunsSubmitted")}</Translation> </p> </Container> ]}

                        </Col>
                    </Card.Body>
                </Card>


            </Grid>
        )
    }
}

export default withUser(RunsTab);
