import React, { Component } from 'react';
import { Helmet, HelmetProvider } from 'react-helmet-async';
import Spinner from 'react-bootstrap/Spinner';
import GameService from "../../../services/api/gameService";
import GameProfile from "../../common/GameDetails/GameProfile";

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
            return <div style={{
                position: 'absolute', left: '50%', top: '50%',
                transform: 'translate(-50%, -50%)'}}>
                <Spinner animation="border" variant="primary" />;
            </div>
        }

        return (
            <React.Fragment>
                <HelmetProvider>
                    <Helmet>
                        <title>{this.state.game.title}</title>
                    </Helmet>
                </HelmetProvider>
                <GameProfile game={this.state.game} />
            </React.Fragment>
        );
    }
}

export default GameDetailsPage;
