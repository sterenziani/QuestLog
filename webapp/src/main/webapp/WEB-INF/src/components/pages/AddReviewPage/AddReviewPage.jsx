import GameService from "../../../services/api/gameService";
import Spinner from "react-bootstrap/Spinner";
import React, { Component } from 'react';
import {Helmet, HelmetProvider} from "react-helmet-async";
import PlatformService from "../../../services/api/platformService";
import { withTranslation } from 'react-i18next';
import ReviewCard from "../../common/GamesCard/ReviewCard";
import withUser from '../../hoc/withUser';

class AddReviewPage extends Component {
    state = {
        game: null,
        loading: true,
        userId: this.props.user ? this.props.user.id : null,
    };

    componentWillMount() {
        const fetchGame = GameService.getGameById(this.props.match.params.id);
        const fetchPlat = PlatformService.getGamePlatforms(this.props.match.params.id);
        Promise.all([ fetchGame, fetchPlat ]).then((responses) => {
            this.setState({
                loading: false,
                game : responses[0],
                platforms : responses[1]
            });
        });
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
            <React.Fragment>
                <HelmetProvider>
                    <Helmet>
                        <title>{t(`reviews.addingReview`)} {this.state.game.title} - QuestLog</title>
                    </Helmet>
                </HelmetProvider>
                <ReviewCard game={this.state.game} platforms={this.state.platforms} userId={this.state.userId}/>
            </React.Fragment>
        );
    }
}

export default withTranslation() (withUser(AddReviewPage,{visibility : "usersOnly"}));
