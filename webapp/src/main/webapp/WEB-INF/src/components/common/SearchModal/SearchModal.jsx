import React, { Component , useState} from 'react';
import Spinner from 'react-bootstrap/Spinner';
import {Button, Modal, Form, Col, Row} from "react-bootstrap";
import {Translation} from "react-i18next";
import {Slider} from '@material-ui/core';
import GenreService from "../../../services/api/genreService";
import PlatformService from "../../../services/api/platformService";
import GameService from "../../../services/api/gameService";
import NumericInput from 'react-numeric-input';

class SearchModal extends Component {
    state = {
        loading: true,
        show: false,
        genres: [],
        platforms: [],
        searchParams: this.props.searchParams,
        path : this.props.path
    };

    componentWillMount() {
        const fetchGen = GenreService.getEveryGenre();
        const fetchPlat = PlatformService.getEveryPlatform();

        //TODO: Handle no response (404)
        Promise.all([ fetchGen, fetchPlat ]).then((responses) => {
            this.setState({
                loading: false,
                genres : responses[0],
                platforms : responses[1]
            });
        });
        if(!this.state.searchParams.scoreLeft){
            this.state.searchParams.scoreLeft = 0;
        }
        if(!this.state.searchParams.scoreRight){
            this.state.searchParams.scoreRight = 100;
        }
        if(!this.state.searchParams.hoursLeft){
            this.state.searchParams.hoursLeft = 0;
        }
        if(!this.state.searchParams.hoursRight){
            this.state.searchParams.hoursRight = 9999;
        }
        if(!this.state.searchParams.minsLeft){
            this.state.searchParams.minsLeft = 0;
        }
        if(!this.state.searchParams.minsRight){
            this.state.searchParams.minsRight = 59;
        }
    }

    switchModal(){
        this.setState({show: !this.state.show});
    }

    onChangePlatforms(e){
       let selected=[];
       let selected_opt=(e.target.selectedOptions);
       for (let i = 0; i < selected_opt.length; i++){
           selected = selected.concat([selected_opt.item(i).value]);
       }
       this.state.searchParams.platforms = selected;
       this.setState({});
    }

    onChangeGenres(e){
       let selected=[];
       let selected_opt=(e.target.selectedOptions);
       for (let i = 0; i < selected_opt.length; i++){
           selected = selected.concat([selected_opt.item(i).value]);
       }
       this.state.searchParams.genres = selected;
       this.setState({});
    }

    handleSliderChange(e, newValue) {
        this.state.searchParams.scoreLeft = newValue[0];
        this.state.searchParams.scoreRight = newValue[1];
        this.setState({});
    }

    handleHourChange(e, side){
        if(side == "left"){
            this.state.searchParams.hoursLeft = e;
        }
        else{
            this.state.searchParams.hoursRight = e;
        }
        this.setState({});
    }

    handleMinsChange(e, side){
        if(side == "left"){
            this.state.searchParams.minsLeft = e;
        }
        else{
            this.state.searchParams.minsRight = e;
        }
        this.setState({});
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
            <div class="text-center mt-5">
                <Button variant="secondary" onClick={() => {this.switchModal()}}><Translation>{t => t("search.filterResults")}</Translation></Button>
                <Modal show={this.state.show} onHide={() => this.switchModal()}>
                    <Modal.Header closeButton><Modal.Title><Translation>{t => t("search.filterResults")}</Translation></Modal.Title></Modal.Header>
                    <Modal.Body>
                        <Form>
                            <Row>
                                <Col>
                                    <Form.Group controlId="formPlatforms">
                                        <Form.Label><Translation>{t => t("games.profile.platforms")}</Translation></Form.Label>
                                        <Form.Control as="select" multiple value={this.state.searchParams.platforms} onChange={this.onChangePlatforms.bind(this)} style={{height: '15rem'}}>
                                        {
                                            this.state.platforms.map(p => (<option key={p.id} value={p.id}>{p.name}</option>))
                                        }
                                        </Form.Control>
                                    </Form.Group>
                                </Col>
                                <Col>
                                    <Form.Group controlId="formGenres">
                                        <Form.Label><Translation>{t => t("games.profile.genres")}</Translation></Form.Label>
                                        <Form.Control as="select" multiple value={this.state.searchParams.genres} onChange={this.onChangeGenres.bind(this)} style={{height: '15rem'}}>
                                        {
                                            this.state.genres.map(g => (<option key={g.id} value={g.id}>{g.name}</option>))
                                        }
                                        </Form.Control>
                                    </Form.Group>
                                </Col>
                            </Row>
                            <Row className="my-3">
                                <Col className="px-5 color-primary">
                                    <Form.Group controlId="formScores">
                                        <Form.Label><Translation>{t => t("search.scoreRange")}</Translation></Form.Label>
                                        <Slider value={[this.state.searchParams.scoreLeft, this.state.searchParams.scoreRight]}
                                                onChange={(e, newValue) => {this.handleSliderChange(e, newValue)}} valueLabelDisplay="auto"
                                                min={0} max={100} step={1} aria-labelledby="range-slider"/>
                                    </Form.Group>
                                </Col>
                            </Row>
                            <Row>
                                <Form.Label><Translation>{t => t("search.minTime")}</Translation></Form.Label>
                                <Form.Group controlId="formTimeLeft">
                                    <Row>
                                        <NumericInput value={this.state.searchParams.hoursLeft} min={0} max={9999} step={1} onChange={(e) => {this.handleHourChange(e, "left")}} style={{width: '5rem'}}/>
                                        <Translation>{t => t("search.hours")}</Translation>
                                        <NumericInput value={this.state.searchParams.minsLeft} min={0} max={59} step={1} onChange={(e) => {this.handleMinsChange(e, "left")}} style={{width: '5rem'}}/>
                                        <Translation>{t => t("search.mins")}</Translation>
                                    </Row>
                                </Form.Group>
                            </Row>
                            <Row>
                                <Form.Label><Translation>{t => t("search.maxTime")}</Translation></Form.Label>
                                <Form.Group controlId="formTimeRight">
                                    <Row>
                                        <NumericInput value={this.state.searchParams.hoursRight} min={0} max={9999} step={1} onChange={(e) => {this.handleHourChange(e, "right")}}/>
                                        <Translation>{t => t("search.hours")}</Translation>
                                        <NumericInput value={this.state.searchParams.minsRight} min={0} max={59} step={1} onChange={(e) => {this.handleMinsChange(e, "right")}}/>
                                        <Translation>{t => t("search.mins")}</Translation>
                                    </Row>
                                </Form.Group>
                            </Row>
                        </Form>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="light" onClick={() => {this.switchModal()}}><Translation>{t => t("search.closeModal")}</Translation></Button>
                        <Button variant="primary" href={this.state.path +"?page=1" +GameService.buildQueryParams(this.state.searchParams)}><Translation>{t => t("search.search")}</Translation></Button>
                    </Modal.Footer>
                </Modal>
            </div>
        )
    }
}

export default SearchModal;
