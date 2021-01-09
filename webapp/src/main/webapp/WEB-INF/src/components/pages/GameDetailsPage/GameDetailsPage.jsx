import React, { Component } from 'react';
import { Helmet, HelmetProvider } from 'react-helmet-async';
import Spinner from 'react-bootstrap/Spinner';
import GameService from "../../../services/api/gameService";
import DetailCard from "../../common/ItemsCard/DetailCard";

class GameDetailsPage extends Component {
    state = {
        game: null,
        loading: true,
    };

    componentWillMount() {
        GameService.getGameById(this.props.match.params.id)
            .then((data) => {
                this.setState({
                    game: data,
                    loading: false,
                });
            });
    }

    render() {
        if (this.state.loading === true) {
            return <Spinner animation="border" variant="primary" />;
        }

        return (
            <React.Fragment>
                <HelmetProvider>
                    <Helmet>
                        <title>{this.state.game.title}</title>
                    </Helmet>
                </HelmetProvider>
                <DetailCard game={this.state.game} />
            </React.Fragment>
        );
    }
}

export default GameDetailsPage;
