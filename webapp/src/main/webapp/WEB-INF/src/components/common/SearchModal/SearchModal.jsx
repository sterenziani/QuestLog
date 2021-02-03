import React, { Component } from 'react';
import Spinner from 'react-bootstrap/Spinner';
import {Button, Modal, Form, Col, Row} from "react-bootstrap";
import {Translation} from "react-i18next";
import {Slider} from '@material-ui/core';
import GenreService from "../../../services/api/genreService";
import PlatformService from "../../../services/api/platformService";
import GameService from "../../../services/api/gameService";
import NumericInput from 'react-numeric-input';
import { withTranslation } from 'react-i18next';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCog } from '@fortawesome/free-solid-svg-icons';
import AnyButton from "../AnyButton/AnyButton";


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
        const fetchGen = GenreService.getAllGenres();
        const fetchPlat = PlatformService.getAllPlatforms();
        let paramsCopy = Object.assign({}, this.state.searchParams);
        if(!paramsCopy.scoreLeft){
            paramsCopy.scoreLeft = 0;
        }
        if(!paramsCopy.scoreRight){
            paramsCopy.scoreRight = 100;
        }
        if(!paramsCopy.hoursLeft){
            paramsCopy.hoursLeft = 0;
        }
        if(!paramsCopy.hoursRight){
            paramsCopy.hoursRight = 9999;
        }
        if(!paramsCopy.minsLeft){
            paramsCopy.minsLeft = 0;
        }
        if(!paramsCopy.minsRight){
            paramsCopy.minsRight = 59;
        }
        //TODO: Handle no response (404)
        Promise.all([ fetchGen, fetchPlat ]).then((responses) => {
            this.setState({
                loading: false,
                genres : responses[0],
                platforms : responses[1],
                searchParams: paramsCopy
            });
        });
    }

    componentWillReceiveProps(newProps) {
        this.setState({
            show: false,
            searchParams: newProps.searchParams,
            path : newProps.path
        })
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
       let paramsCopy = Object.assign({}, this.state.searchParams);
       paramsCopy.platforms = selected;
       this.setState({searchParams: paramsCopy});
    }

    onChangeGenres(e){
       let selected=[];
       let selected_opt=(e.target.selectedOptions);
       for (let i = 0; i < selected_opt.length; i++){
           selected = selected.concat([selected_opt.item(i).value]);
       }
       let paramsCopy = Object.assign({}, this.state.searchParams);
       paramsCopy.genres = selected;
       this.setState({searchParams: paramsCopy});
    }

    handleSliderChange(e, newValue) {
        let paramsCopy = Object.assign({}, this.state.searchParams);
        paramsCopy.scoreLeft = newValue[0];
        paramsCopy.scoreRight = newValue[1];
        this.setState({searchParams: paramsCopy});
    }

    handleHourChange(e, side){
        if(e < 0)
            e = 0;
        else if(e > 9999)
            e = 9999;
        let paramsCopy = Object.assign({}, this.state.searchParams);
        if(side === "left"){
            paramsCopy.hoursLeft = e;
        }
        else{
            paramsCopy.hoursRight = e;
        }
        this.setState({searchParams: paramsCopy});
    }

    handleMinsChange(e, side){
        if(e < 0)
            e = 0;
        else if(e > 59)
            e = 59;
        let paramsCopy = Object.assign({}, this.state.searchParams);
        if(side === "left"){
            paramsCopy.minsLeft = e;
        }
        else{
            paramsCopy.minsRight = e;
        }
        this.setState({searchParams: paramsCopy});
    }

    render() {
        if (this.state.loading === true) {
            return <div style={{
                position: 'absolute', left: '50%', top: '50%',
                transform: 'translate(-50%, -50%)'}}>
                <Spinner animation="border" variant="primary" />
            </div>
        }
        const { t } = this.props
        return (
            <div className="text-center mt-5">
                <Button variant="outline-secondary" className="w-25" onClick={() => {this.switchModal()}}>
                    <FontAwesomeIcon className="mr-sm-2" icon={ faCog }/>{t("search.filterResults")}
                </Button>
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
                                            this.state.platforms.length > 0? [this.state.platforms.map(p => (<option key={p.id} value={p.id}>{p.name}</option>))] : []
                                        }
                                        </Form.Control>
                                    </Form.Group>
                                </Col>
                                <Col>
                                    <Form.Group controlId="formGenres">
                                        <Form.Label><Translation>{t => t("games.profile.genres")}</Translation></Form.Label>
                                        <Form.Control as="select" multiple value={this.state.searchParams.genres} onChange={this.onChangeGenres.bind(this)} style={{height: '15rem'}}>
                                        {
                                            this.state.genres.length > 0? [this.state.genres.map(g => (<option key={g.id} value={g.id}>{t(`genres.${g.name}`)}</option>))] : []
                                        }
                                        </Form.Control>
                                    </Form.Group>
                                </Col>
                            </Row>
                            <Form.Text muted className="mt-0 mb-4"><Translation>{t => t("search.multipleSelectHint")}</Translation></Form.Text>
                            <Form.Label><Translation>{t => t("search.scoreRange")}</Translation></Form.Label>
                            <Row className="my-3">
                                <Col className="px-5 color-primary">
                                    <Form.Group controlId="formScores">

                                        <Slider value={[this.state.searchParams.scoreLeft, this.state.searchParams.scoreRight]}
                                                onChange={(e, newValue) => {this.handleSliderChange(e, newValue)}} valueLabelDisplay="auto"
                                                min={0} max={100} step={1} aria-labelledby="range-slider"/>
                                    </Form.Group>
                                </Col>
                            </Row>

                            <Form.Group controlId="formTimeRight">
                                <Form.Label><Translation>{t => t("search.minTime")}</Translation></Form.Label>
                                <Col className="d-flex mb-0 text-center align-items-center">
                                    <NumericInput className="form-control" value={this.state.searchParams.hoursLeft} min={0} max={9999} step={1} onChange={(e) => {this.handleHourChange(e, "left")}} />
                                    <Col><Translation>{t => t("search.hours")}</Translation></Col>
                                    <Col></Col>
                                    <NumericInput className="form-control" value={this.state.searchParams.minsLeft} min={0} max={59} step={1} onChange={(e) => {this.handleMinsChange(e, "left")}} />
                                    <Col><Translation>{t => t("search.mins")}</Translation></Col>
                                </Col>
                            </Form.Group>

                            <Form.Group controlId="formTimeRight">
                                <Form.Label><Translation>{t => t("search.maxTime")}</Translation></Form.Label>
                                <Col className="d-flex mb-0 text-center align-items-center">
                                    <NumericInput className="form-control" value={this.state.searchParams.hoursRight} min={0} max={9999} step={1} onChange={(e) => {this.handleHourChange(e, "right")}}/>
                                    <Col><Translation>{t => t("search.hours")}</Translation></Col>
                                    <Col></Col>
                                    <NumericInput className="form-control" value={this.state.searchParams.minsRight} min={0} max={59} step={1} onChange={(e) => {this.handleMinsChange(e, "right")}}/>
                                    <Col><Translation>{t => t("search.mins")}</Translation></Col>
                                </Col>
                            </Form.Group>

                        </Form>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="light" onClick={() => {this.switchModal()}}><Translation>{t => t("search.closeModal")}</Translation></Button>
                        <AnyButton variant="primary" href={"/" +this.state.path +"?page=1" +GameService.buildQueryParams(this.state.searchParams)} textKey={"search.search"}/>
                    </Modal.Footer>
                </Modal>
            </div>
        )
    }
}

export default withTranslation()(SearchModal);
