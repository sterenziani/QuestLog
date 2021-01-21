import GameService from "../../../services/api/gameService";
import Spinner from "react-bootstrap/Spinner";
import React, { Component } from 'react';
import {Helmet, HelmetProvider} from "react-helmet-async";
import PlatformService from "../../../services/api/platformService";
import RunService from "../../../services/api/runService";
import RunCard from "../../common/GamesCard/RunCard";
import withUser from '../../hoc/withUser';

class AddRunPage extends Component {
    state = {
        game: null,
        loading: true,
        userId: this.props.user ? this.props.user.id : null,
    };

    componentWillMount() {
        const fetchGame = GameService.getGameById(this.props.match.params.id);
        const fetchPlat = PlatformService.getGamePlatforms(this.props.match.params.id);
        const fetchPlay = RunService.getAllPlaystyles();
        Promise.all([ fetchGame, fetchPlat, fetchPlay ]).then((responses) => {
            this.setState({
                loading: false,
                game : responses[0],
                platforms : responses[1],
                playstyles : responses[2],
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

        return (
            <React.Fragment>
                <HelmetProvider>
                    <Helmet>
                        <title>{this.state.game.title}</title>
                    </Helmet>
                </HelmetProvider>
                <RunCard game={this.state.game} platforms={this.state.platforms} playstyles={this.state.playstyles} userId={this.state.userId}/>
            </React.Fragment>
        );
    }
}

export default withUser(AddRunPage);
