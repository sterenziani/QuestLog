import React, { Component } from 'react';
import { Helmet, HelmetProvider } from 'react-helmet-async';
<<<<<<< HEAD
import Spinner from 'react-bootstrap/Spinner';
=======
//import axios from "axios";
>>>>>>> 42d4002c28226ba0242d6d5f055044ff1713e22e
import GameService from "../../../services/api/gameService";
import GameListItem from "../../common/GameListItem/GameListItem";

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
                        <title></title>
                    </Helmet>
                </HelmetProvider>
                <GameListItem value={this.props.match.params.id} game={this.state.game} />
            </React.Fragment>
        );
    }
}
 
export default GameDetailsPage;