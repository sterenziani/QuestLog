import React, { Component } from 'react';
import {Card, Row, Col, Badge, Button, Container} from "react-bootstrap";
import {Grid} from '@material-ui/core';
import {Translation} from "react-i18next";
import "../../../../src/index.scss";
import ReviewService from "../../../services/api/reviewService";
import ReviewCard from "./ReviewCard";

class ReviewsTab extends Component {
    state = {
        game: this.props.game,
        userId: 22,
        loggedIn: true,
        myReviews: [],
        displayedReviews: [],
        pagination: []
    };

    componentWillMount() {
        ReviewService.getUserGameReviews(this.state.userId, this.props.game.id)
              .then((data) => {
                  this.setState({
                      myReviews: data
                  });
              }).then((data) =>  {});
        ReviewService.getGameReviews(this.props.game.id)
            .then((data) => {
                this.setState({
                    displayedReviews: data.content,
                    pagination: data.pagination
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
                        <Card className="m-5 text-center bg-very-light right-wave left-wave" bordered style={{ borderBottomLeftRadius: 30, borderBottomRightRadius: 30 }}>
                            <Card.Header className="bg-very-dark text-white d-flex">
                                <h2 className="share-tech-mono"><Translation>{t => t("reviews.myReviews")}</Translation></h2>
                            </Card.Header>
                            <Card.Body class="mt-2 d-flex flex-wrap justify-content-center padding-left-wave padding-right-wave">
                                <Col>
                                    {this.state.myReviews.map(r => (
                                            <ReviewCard review={r}/>
                                        ))}
                                </Col>
                            </Card.Body>
                        </Card>
                    ] : []
                }
                <Card className="m-5 text-center bg-very-light right-wave left-wave" bordered style={{ borderBottomLeftRadius: 30, borderBottomRightRadius: 30 }}>
                    <Card.Header className="bg-very-dark text-white d-flex">

                        <div>
                            <h2 className="share-tech-mono"><Translation>{t => t("reviews.reviews")}</Translation></h2>
                        </div>
                        {
                            this.state.pagination.next? [
                                <div className="ml-auto">
                                    <Button variant="link" className="text-white" href={`${process.env.PUBLIC_URL}/games/` +this.state.game.id +'/reviews'}><Translation>{t => t("navigation.seeAll")}</Translation></Button>
                                </div>
                            ] : []
                        }
                    </Card.Header>
                    <Card.Body class="mt-2 d-flex flex-wrap justify-content-center padding-left-wave padding-right-wave">
                        <Col>
                            {
                                (this.state.displayedReviews.length > 0)? [
                                    this.state.displayedReviews.map(r => (<ReviewCard review={r}/>))
                                ] : [<Container className="text-center mt-5"> <p> <Translation>{t => t("reviews.noReviewsSubmitted")}</Translation> </p> </Container>]
                            }
                        </Col>
                    </Card.Body>
                </Card>
            </Grid>
        );
    }
}

export default ReviewsTab;
