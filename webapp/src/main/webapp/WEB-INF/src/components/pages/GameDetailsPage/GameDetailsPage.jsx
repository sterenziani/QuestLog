import React, { Component } from 'react';
import { Helmet, HelmetProvider } from 'react-helmet-async';
import Spinner from 'react-bootstrap/Spinner';
import GameService from "../../../services/api/gameService";
import GameProfile from "../../common/GameDetails/GameProfile";
import {CREATED, OK} from "../../../services/api/apiConstants";
import ErrorContent from "../../common/ErrorContent/ErrorContent";

class GameDetailsPage extends Component {
    state = {
        game: null,
        loading: true,
        status : null,
        error : false,
    };

    componentWillMount() {
        this.updateGame();
    }
    
    updateGame = () => {
        GameService.getGameById(this.props.match.params.id)
            .then((data) => {
                let findError = null;
                if (data.status && data.status !== OK && data.status !== CREATED) {
                        findError = data.status;
                }
                if(findError) {
                    this.setState({
                        loading: false,
                        error: true,
                        status: findError,
                    });
                }
                else {
                    this.setState({
                        game: data,
                        loading: false,
                    });
                }
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
        if(this.state.error) {
            return <ErrorContent status={this.state.status}/>
        }
        return (
            <React.Fragment>
                <HelmetProvider>
                    <Helmet>
                        <title>{this.state.game.title} - QuestLog</title>
                    </Helmet>
                </HelmetProvider>
                <GameProfile game={this.state.game} updateGame={this.updateGame} />
            </React.Fragment>
        );
    }
}

export default GameDetailsPage;
