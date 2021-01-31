import React, { Component } from 'react';
import {Card, Col, Button, Container} from "react-bootstrap";
import {Grid} from '@material-ui/core';
import {Translation} from "react-i18next";
import "../../../../src/index.scss";
import ReviewCard from "../GameDetails/ReviewCard";
import AnyButton from "../AnyButton/AnyButton";

class UserReviewsTab extends Component {
    state = {
        visitedUser: this.props.visitedUser,
        reviewsDisplayed: this.props.reviewsDisplayed,
        reviewsPagination: this.props.reviewsPagination,
        loggedUser: this.props.loggedUser,
    };

    render() {
        return (
            <Grid>
                <Card className="m-5 text-center bg-very-light right-wave left-wave" bordered style={{ borderBottomLeftRadius: 30, borderBottomRightRadius: 30 }}>
                    <Card.Header className="bg-very-dark text-white d-flex">
                        <div><h2 className="share-tech-mono"><Translation>{t => t("users.userReviews", {value: this.state.visitedUser.username})}</Translation></h2></div>
                        {
                            this.state.reviewsPagination.next && this.props.seeAll? [
                                <div className="ml-auto">
                                    <AnyButton variant="link" className="text-white" href={`/users/` +this.state.visitedUser.id +'/reviews'} textKey="navigation.seeAll"/>
                                </div>
                            ] : []
                        }
                    </Card.Header>
                    <Card.Body class="mt-2 d-flex flex-wrap justify-content-center padding-left-wave padding-right-wave">
                    {(this.state.reviewsDisplayed.length > 0)? [
                        <Col>
                            {this.state.reviewsDisplayed.map(r => (
                                    <ReviewCard review={r} user={this.state.loggedUser} loggedIn={this.state.loggedIn}/>
                                ))}
                        </Col>
                    ] : [<Container className="text-center mt-5"> <p> <Translation>{t => t("users.noReviewsSubmitted")}</Translation> </p> </Container>]
                    }
                    </Card.Body>
                </Card>
            </Grid>
        );
    }
}

export default UserReviewsTab;
