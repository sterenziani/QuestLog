import React, { Component } from 'react';
import { Helmet, HelmetProvider } from 'react-helmet-async';
import axios from "axios";
import GameService from "../../../services/api/gameService";
import GameListItem from "../../common/GameListItem/GameListItem";

class GameDetailsPage extends Component {
    state = {
        game: null,
        loading: 'true',
    };

    componentWillMount() {
        GameService.getGameById(this.props.match.params.id)
            .then((data) => {
                this.setState({
                    game: data,
                    loading: 'false',
                });
            });
    }

    render() {
        if (this.state.loading === 'true') {
            return <h2>Loading...</h2>;
        }

        return (
            <React.Fragment>
                <HelmetProvider>
                    <Helmet>
                        <title></title>
                    </Helmet>
                </HelmetProvider>
                <GameListItem value={this.props.match.params.id} title={this.state.game.title} cover={this.state.game.cover} />
            </React.Fragment>
        );
    }
}
 
export default GameDetailsPage;