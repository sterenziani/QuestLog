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
        console.log(this.state.game);
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
                <GameListItem value={this.props.match.params.id} />
                <p>{this.props.match.params.id}</p>
                <p>{this.state.game.title}</p>
            </React.Fragment>
        );
    }
}
 
export default GameDetailsPage;