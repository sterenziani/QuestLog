import React, { Component } from 'react';
import {Row, Col, Button, Modal} from "react-bootstrap";
import {LinkContainer} from 'react-router-bootstrap';
import {Translation} from "react-i18next";
import "../../../../src/index.scss";
import ReviewService from "../../../services/api/reviewService";
import withUser from '../../hoc/withUser';
import AnyButton from "../AnyButton/AnyButton";

class ReviewCard extends Component {
    state = {
        review: this.props.review,
        user: this.props.user,
        loggedIn: this.props.userIsLoggedIn,
        enabled: this.props.review.enabled,
        showModal: false,
    };

    deleteHandler = () => {
        ReviewService.deleteReview(this.state.review.id);
        this.setState({enabled: false});
    }

    switchModal(){
        this.setState({showModal: !this.state.showModal});
    }

    render() {
        if(this.state.enabled){
            return (
                <div className="panel panel-default bg-dark-white rounded-lg my-3">
                  <div className="panel-heading py-2 px-3 bg-light">
                    <Row>
                        <Col className="text-left font-weight-bold">
                            <AnyButton variant="link" className="p-0 m-0 font-weight-bold" href={ "/users/" +this.state.review.user.id } text={this.state.review.user.username}/>
                        </Col>
                        <Col className="my-auto text-right">
                            {this.state.review.postDate}
                        </Col>
                    </Row>
                  </div>
                  <div className="panel-body p-3">
                    <Row className="mb-3">
                        <Col className="text-center font-weight-bold">
                            <AnyButton variant="link" className="p-0 m-0 font-weight-bold" href={ "/games/" +this.state.review.game.id } text={this.state.review.game.title}/>
                            {" ("+this.state.review.platform.shortName+")"}
                        </Col>
                    </Row>
                    <Row className="container-fluid">
                        <Col className="col-10 text-left">
                            {this.state.review.body.map((l, index) => (
                                <div key={index}>
                                    <p>{l}</p>
                                </div>
                            ))}
                        </Col>
                        <Col className="col-2 container">
                            <div className="score-display text-center px-5"> <p className="badge badge-primary">{this.state.review.score}</p></div>
                        </Col>
                    </Row>
                    {(this.state.loggedIn && (this.state.review.user.id === this.state.user.id || this.state.user.admin))? [
                        <Row className="mt-3 justify-content-center">
                            <Button variant="danger" onClick={() => {this.switchModal()}}><Translation>{t => t("reviews.delete")}</Translation>{this.state.label}</Button>
                            <Modal show={this.state.showModal} onHide={() => this.switchModal()}>
                                <Modal.Header closeButton><Modal.Title><Translation>{t => t("reviews.deletingReview")}</Translation></Modal.Title></Modal.Header>
                                <Modal.Body>
                                    <Translation>{t => t("reviews.deleteSure")}</Translation>
                                </Modal.Body>
                                <Modal.Footer>
                                    <Button variant="light" onClick={() => {this.switchModal()}}><Translation>{t => t("reviews.deleteNo")}</Translation></Button>
                                    <Button variant="danger" onClick={this.deleteHandler}><Translation>{t => t("reviews.deleteYes")}</Translation></Button>
                                </Modal.Footer>
                            </Modal>
                        </Row>] : []}
                  </div>
                </div>
            );
        }
        else{
            return(<></>);
        }
    }
}

export default withUser(ReviewCard);
