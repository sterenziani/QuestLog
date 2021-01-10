import React, { Component } from 'react';
import {Card, Row, Col, Badge, Button, Container} from "react-bootstrap";
import {Grid} from '@material-ui/core';
import {Translation} from "react-i18next";
import "../../../../src/index.scss";
import RunService from "../../../services/api/runService";

class ReviewsTab extends Component {
    state = {
        game: this.props.game,
        userId: 22,
        loggedIn: true,
        myRuns: [],
        myReviews: [],
        displayedReviews: [],
    };

    componentWillMount() {
        RunService.getGameTimes(this.props.game.gameId)
              .then((data) => {
                  this.setState({
                      avgTimes: data
                  });
              }).then((data) =>  {});
    };

    render() {
        return (
            <Grid>
                <Col className="text-center m-4">
                    <Button variant={"success"}> <Translation>{t => t("reviews.addReview")}</Translation> </Button>
                </Col>
                {
                    (this.state.loggedIn && this.state.myReviews.length > 0)? [
                        <p>"Coming soon"</p>
                    ] : []
                }
            </Grid>
        );
    }
}

export default ReviewsTab;
