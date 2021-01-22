import React, { Component } from 'react';
import {Card, Row, Col, Badge, Button, Container} from "react-bootstrap";
import {Grid} from '@material-ui/core';
import {Translation} from "react-i18next";
import "../../../../src/index.scss";
import ReviewService from "../../../services/api/reviewService";
import ReviewCard from "../GameDetails/ReviewCard";
import Spinner from "react-bootstrap/Spinner";

class UserReviewsTab extends Component {
    state = {
        user: this.props.user,
        loggedInUser: this.props.loggedInUser,
        loggedIn: this.props.loggedIn,
        reviewsDisplayed: [],
        reviewsPagination: [],
        loading: true,
    };

    componentWillMount() {
        ReviewService.getUserReviews(this.props.user.id)
              .then((data) => {
                  this.setState({
                      reviewsDisplayed: data.content,
                      reviewsPagination: data.pagination,
                      loading: false,
                  });
              }).then((data) =>  {});
    };

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
                <Card className="m-5 text-center bg-very-light right-wave left-wave" bordered style={{ borderBottomLeftRadius: 30, borderBottomRightRadius: 30 }}>
                    <Card.Header className="bg-very-dark text-white d-flex">
                        <div><h2 className="share-tech-mono"><Translation>{t => t("users.userReviews", {value: this.state.user.username})}</Translation></h2></div>
                        {
                            this.state.reviewsPagination.next? [
                                <div className="ml-auto">
                                    <Button variant="link" className="text-white" href={`${process.env.PUBLIC_URL}/users/` +this.state.user.id +'/reviews'}><Translation>{t => t("navigation.seeAll")}</Translation></Button>
                                </div>
                            ] : []
                        }
                    </Card.Header>
                    <Card.Body class="mt-2 d-flex flex-wrap justify-content-center padding-left-wave padding-right-wave">
                    {(this.state.reviewsDisplayed.length > 0)? [
                        <Col>
                            {this.state.reviewsDisplayed.map(r => (
                                    <ReviewCard review={r} user={this.state.loggedInUser} loggedIn={this.state.loggedIn}/>
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
