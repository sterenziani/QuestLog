import React, { Component } from 'react';
import {Button, Card, Form, Row} from "react-bootstrap";
import {Translation} from "react-i18next";
import "../../../../src/index.scss";
import GameCover from "../GameCover/GameCover";
import NumericInput from "react-numeric-input";
import withQuery from "../../hoc/withQuery";

class RunCard extends Component {
    state = {
        game : this.props.game,
        params : {hours: this.props.query.get("hours"), mins: this.props.query.get("mins"), secs: this.props.query.get("secs"),
    platforms: this.props.query.getAll("platforms"), playstyles: this.props.query.getAll("playstyles")},
        platforms : this.props.platforms,
        playstyles: this.props.playstyles,

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
    }

    handleHourChange(e) {
        this.state.params.hours = e;
    }

    handleMinsChange(e) {
        this.state.params.mins = e;
    }

    handleSecsChange(e) {
        this.state.params.secs = e;
    }

    onChangePlatforms(e){
        console.log(e)
        this.state.params.platforms = e.target.value;
    }

    onChangePlaystyles(e){
        console.log(e)
        console.log(e.target)
        this.state.params.playstyles = e.target.value;
    }

    handleSubmit = () =>{
        console.log(this.state.params)
    }

    render() {
        return (
            <Card style={{width: "100%"}} className="m-5 bg-light-grey right-wave left-wave" bordered style={{ borderBottomLeftRadius: 30, borderBottomRightRadius: 30 }}>
                <div className="card-header bg-very-dark text-white px-3 d-flex">
                    <h2 className="share-tech-mono">
                        <Translation>{t => t("runs.addingRun")}</Translation> {this.state.game.title}
                    </h2>
                </div>
                <div className="card-body d-flex flex-wrap justify-content-center align-items-center align-items-stretch">
                    <div>
                        <GameCover cover={this.state.game.cover}/>
                    </div>
                    <div>
                        <Form onSubmit={this.handleSubmit}>
                            <Form.Group controlId="formPlatforms">
                                <Form.Label className="text-center">
                                    <h5 className="text-center">
                                        <strong>
                                            <Translation>{t => t("games.profile.platforms")}</Translation>
                                        </strong>
                                    </h5>
                                </Form.Label>
                                <Form.Control size="sm" className="text-center" as="select" value={this.state.params.platforms} onChange={this.onChangePlatforms.bind(this)}
                                              style={{padding: "5px"}}>
                                    {
                                        this.state.platforms.map(p => (
                                            <option key={p.id} value={p.id}>{p.name}</option>))
                                    }
                                </Form.Control>
                            </Form.Group>
                            <Form.Group controlId="formPlaystyles">
                                <Form.Label className="text-center">
                                    <h5>
                                        <strong>
                                            <Translation>{t => t("runs.playstyle")}</Translation>
                                        </strong>
                                    </h5>
                                </Form.Label>
                                <Form.Control size="lg" className="text-center" as="select" value={this.state.params.playstyles} onChange={this.onChangePlaystyles.bind(this)}
                                              style={{padding: "5px"}}>
                                    {
                                        this.state.playstyles.map(p => (
                                            <option key={p.id} value={p.id}>{p.name}</option>))
                                    }
                                </Form.Control>
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
                                <Form.Group controlId="formTimeRight">
                                    <Row>
                                        <NumericInput value={this.state.params.hours} min={0} max={9999} step={1} onChange={(e) => {this.handleHourChange(e)}}/>
                                        <strong> : </strong>
                                        <NumericInput value={this.state.params.mins} min={0} max={59} step={1} onChange={(e) => {this.handleMinsChange(e)}}/>
                                        <strong> : </strong>
                                        <NumericInput value={this.state.params.secs} min={0} max={59} step={1} onChange={(e) => {this.handleSecsChange(e)}}/>
                                    </Row>
                                </Form.Group>
                            <div className="text-center">
                            <Button className="btn btn-dark mt-3" type="submit" onClick={this.handleSubmit}>
                                <Translation>
                                    {
                                        t => t("submit")
                                    }
                                </Translation>
                            </Button>
                            </div>
                        </Form>

                    </div>
                </div>
            </Card>
        );
    }
}



export default withQuery(RunCard);