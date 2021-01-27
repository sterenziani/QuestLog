import React, { Component } from 'react';
import {Card, Col, Button, Container} from "react-bootstrap";
import {Grid} from '@material-ui/core';
import {Translation} from "react-i18next";
import "../../../../src/index.scss";
import ReviewCard from "./ReviewCard";
import withUser from '../../hoc/withUser';

class ReviewsTab extends Component {
    state = {
        game: this.props.game,
        reviews: this.props.reviews? this.props.reviews : [],
        pagination: this.props.pagination? this.props.pagination : [],
        label: this.props.label? this.props.label : "reviews.reviews",
        user: this.props.user ? this.props.user : null,
        loggedIn: this.props.userIsLoggedIn,
    };

    render() {
        return (
            <Grid>
                <div className="text-center m-4">
                    <Button variant={"success"} href={`${process.env.PUBLIC_URL}/reviews/create/${this.state.game.id}`}> <Translation>{t => t("reviews.addReview")}</Translation> </Button>
                </div>
                <Card className="m-5 text-center bg-very-light right-wave left-wave" bordered style={{ borderBottomLeftRadius: 30, borderBottomRightRadius: 30 }}>
                    <Card.Header className="bg-very-dark text-white d-flex">
                    <div>
                        <h2 className="share-tech-mono"><Translation>{t => t(this.state.label)}</Translation></h2>
                    </div>
                    {
                        this.state.pagination.next && this.props.seeAll? [
                            <div className="ml-auto">
                                <Button variant="link" className="text-white" href={`${process.env.PUBLIC_URL}/games/` +this.state.game.id +'/reviews'}><Translation>{t => t("navigation.seeAll")}</Translation></Button>
                            </div>
                        ] : []
                    }
                    </Card.Header>
                    <Card.Body class="mt-2 d-flex flex-wrap justify-content-center padding-left-wave padding-right-wave">
                    {(this.state.reviews.length > 0)? [
                        <Col>
                            {this.state.reviews.map(r => (
                                    <ReviewCard review={r}/>
                                ))}
                        </Col>
                    ] : [<Container className="text-center mt-5"> <p> <Translation>{t => t("reviews.noReviewsSubmitted")}</Translation> </p> </Container>]
                    }
                    </Card.Body>
                </Card>
            </Grid>
        );
    }
}

export default withUser(ReviewsTab);
