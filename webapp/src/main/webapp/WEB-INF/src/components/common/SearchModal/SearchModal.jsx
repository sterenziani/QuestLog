import React, { Component , useState} from 'react';
import Spinner from 'react-bootstrap/Spinner';
import {Button, Modal, Form, Col, Row} from "react-bootstrap";
import {Translation} from "react-i18next";
import GenreService from "../../../services/api/genreService";
import PlatformService from "../../../services/api/platformService";
import GameService from "../../../services/api/gameService";

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

    render() {
        if (this.state.loading === true) {
            return <div style={{
                position: 'absolute', left: '50%', top: '50%',
                transform: 'translate(-50%, -50%)'}}>
                <Spinner animation="border" variant="primary" />
            </div>
        }
        console.log("Estado actual:");
        console.log(this.state.searchParams);
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
                            <Row><Form.Text className="text-muted"><Translation>{t => t("search.multipleSelectHint")}</Translation></Form.Text></Row>
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
