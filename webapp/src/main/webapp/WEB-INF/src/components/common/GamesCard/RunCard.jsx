import React, { Component } from 'react';
import {Button, Card, Form, Row, Modal} from "react-bootstrap";
import {Translation} from "react-i18next";
import "../../../../src/index.scss";
import GameCover from "../GameCover/GameCover";
import NumericInput from "react-numeric-input";
import withQuery from "../../hoc/withQuery";
import withUser from "../../hoc/withUser";
import RunService from "../../../services/api/runService";
import BacklogService from "../../../services/api/backlogService";
import { withTranslation } from 'react-i18next';
import withRedirect from '../../hoc/withRedirect';

class RunCard extends Component {
    state = {
        game : this.props.game,
        params : {hours: this.props.query.get("hours"), mins: this.props.query.get("mins"), secs: this.props.query.get("secs"),
                    platform: this.props.platforms[0].id, playstyle: this.props.playstyles[0].id},
        platforms : this.props.platforms,
        playstyles: this.props.playstyles,
        showModal: false,
        published: false,
    };

    componentWillMount() {
        if (!this.state.params.hours) {
            this.state.params.hours = 0;
        }
        if (!this.state.params.mins) {
            this.state.params.mins = 0;
        }
        if (!this.state.params.secs) {
            this.state.params.secs = 0;
        }
        this.props.addRedirection("gameProfile", `/games/${this.state.game.id}`);
    }

    handleHourChange(e) {
        if(e > 9999)
            e = 9999;
        if(e < 0)
            e = 0;
        this.state.params.hours = e;
        this.setState({});
    }

    handleMinsChange(e) {
        if(e >= 60)
            e = 59;
        if(e < 0)
            e = 0;
        this.state.params.mins = e;
        this.setState({});
    }

    handleSecsChange(e) {
        if(e >= 60)
            e = 59;
        if(e < 0)
            e = 0;
        this.state.params.secs = e;
        this.setState({});
    }

    onChangePlatforms(e){
        this.state.params.platform = e.target.value;
        this.setState({});
    }

    onChangePlaystyles(e){
        this.state.params.playstyle = e.target.value;
        this.setState({});
    }

    switchModal(){
        this.setState({showModal: !this.state.showModal});
    }

    publishRunHandler(){
        if(this.props.userIsLoggedIn)
        {
            if(this.props.game.in_backlog){
                this.switchModal();
            }
            else{
                this.publishRun();
            }
        }
        else
            this.props.activateRedirect("login");
    }

    publishAndRemoveFromBacklog = () => {
        BacklogService.removeGameFromBacklog(this.props.game.id);
        this.publishRun();
        if(this.state.showModal)
            this.switchModal();
    }

    publishAndKeepInBacklog = () => {
        this.publishRun();
        if(this.state.showModal)
            this.switchModal();
    }

    publishRun = () => {
        this.setState({published:true});
        RunService.addRun(this.state.game.id, this.state.params.hours, this.state.params.mins, this.state.params.secs, this.state.params.playstyle, this.state.params.platform)
        .then(data => {
            if(data.status == '201'){
                this.props.activateRedirect("gameProfile");
            }
            else{
                // TODO: Force login or try again
                this.setState({published: false});
            }
        });
    }

    render() {
        const { t } = this.props
        return (
            <Card style={{borderBottomLeftRadius: 30, borderBottomRightRadius: 30 }} className="m-5 bg-light-grey right-wave left-wave" bordered>
                <Card.Header className="bg-very-dark text-white px-3 d-flex">
                    <h2 className="share-tech-mono">
                        <Translation>{t => t("runs.addingRun", {value: this.state.game.title})}</Translation>
                    </h2>
                </Card.Header>
                <Card.Body className="card-body d-flex flex-wrap justify-content-center align-items-center">
                    <div>
                        <GameCover cover={this.state.game.cover}/>
                    </div>
                    <div class="p-5">
                        <Form>
                            <Form.Group controlId="formPlatforms">
                                <div className="text-center">
                                    <Form.Label className="text-center">
                                        <h5 className="text-center">
                                            <strong>
                                                <Translation>{t => t("reviews.platform")}</Translation>
                                            </strong>
                                        </h5>
                                    </Form.Label>
                                </div>
                                <div className="text-center">
                                    <Form.Control className="text-center" as="select" value={this.state.params.platform} onChange={this.onChangePlatforms.bind(this)}
                                                  style={{padding: "5px"}}>
                                        {
                                            this.state.platforms.map(p => (
                                                <option key={p.id} value={p.id}>{p.name}</option>))
                                        }
                                    </Form.Control>
                                </div>
                            </Form.Group>
                            <Form.Group controlId="formPlaystyles">
                                <div className="text-center">
                                <Form.Label className="text-center">
                                    <h5>
                                        <strong>
                                            <Translation>{t => t("runs.playstyle")}</Translation>
                                        </strong>
                                    </h5>
                                </Form.Label>
                                </div>
                                <div className="text-center">
                                <Form.Control className="text-center" as="select" value={this.state.params.playstyle} onChange={this.onChangePlaystyles.bind(this)} style={{padding: "5px"}}>
                                {
                                    this.state.playstyles.map(p => (<option key={p.id} value={p.id}>{t(`runs.playstyles.${p.name}`)}</option>))
                                }
                                </Form.Control>
                                </div>
                            </Form.Group>
                                <div className="text-center">
                                <Form.Label>
                                    <h5 className="text-center">
                                        <strong>
                                            <Translation>{t => t("runs.time")}</Translation>
                                        </strong>
                                    </h5>
                                </Form.Label>
                                </div>
                                <Form.Group controlId="formTime">
                                    <Row>
                                        <NumericInput value={this.state.params.hours} min={0} max={9999} step={1} onChange={(e) => {this.handleHourChange(e)}}/>
                                        <strong> : </strong>
                                        <NumericInput value={this.state.params.mins} min={0} max={59} step={1} onChange={(e) => {this.handleMinsChange(e)}}/>
                                        <strong> : </strong>
                                        <NumericInput value={this.state.params.secs} min={0} max={59} step={1} onChange={(e) => {this.handleSecsChange(e)}}/>
                                    </Row>
                                </Form.Group>
                            <div className="text-center">
                            {
                                this.state.published? [<Button disabled className="btn btn-dark mt-3">
                                                        <Translation>{t => t("submit")}</Translation>
                                                    </Button>
                                                ] : [<Button className="btn btn-dark mt-3" onClick={(e) => {this.publishRunHandler(e)}}>
                                                        <Translation>{t => t("submit")}</Translation>
                                                    </Button>]
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
                    </div>
                </Card.Body>
            </Card>
        );
    }
}



export default withTranslation() (withUser(withQuery(withRedirect(RunCard, {login: "/login"}))));
