import React, { Component } from 'react';
import {Row, Col, Button} from "react-bootstrap";
import {Translation} from "react-i18next";
import "../../../../src/index.scss";

class ReviewCard extends Component {
    state = {
        review: this.props.review,
        userId: this.props.userId,
        loggedIn: this.props.loggedIn,
    };

    render() {
        return (
            <div className="panel panel-default bg-dark-white rounded-lg my-3">
              <div className="panel-heading py-2 px-3 bg-light">
            	<Row>
            		<Col className="text-left font-weight-bold">
            			<a href={`${process.env.PUBLIC_URL}/users/` + this.state.review.user.id}>{this.state.review.user.username}</a>
            		</Col>
            		<Col className="text-right">
            			{this.state.review.postDate}
            		</Col>
            	</Row>
              </div>
              <div className="panel-body p-3">
            	<Row className="mb-3">
            		<Col className="text-center font-weight-bold">
            			<a href={`${process.env.PUBLIC_URL}/games/` + this.state.review.game.id}>{this.state.review.game.title}</a> {"("+this.state.review.platform.shortName+")"}
            		</Col>
            	</Row>
            	<Row>
            		<Col className="col-10 text-left">
                        {this.state.review.body.map(l => (
                            <div>
                                <p>{l}</p>
                            </div>
                        ))}
            		</Col>
            		<Col className="col-2 container">
            			<div className="score-display text-center px-5"> <p class="badge badge-primary">{this.state.review.score}</p></div>
            		</Col>
            	</Row>
                {(this.state.loggedIn && this.state.review.user.id === this.state.userId)? [
                    <Row className="mt-3 justify-content-center">
                        <Button variant="danger"><Translation>{t => t("reviews.delete")}</Translation>{this.state.label}</Button>
                    </Row>] : []}
              </div>
            </div>
        );
    }
}

export default ReviewCard;
