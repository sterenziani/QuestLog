import GameService from "../../../services/api/gameService";
import Spinner from "react-bootstrap/Spinner";
import React, { Component } from 'react';
import {Helmet, HelmetProvider} from "react-helmet-async";
import PlatformService from "../../../services/api/platformService";
import RunService from "../../../services/api/runService";
import RunCard from "../../common/GamesCard/RunCard";
import withUser from '../../hoc/withUser';
import { withTranslation } from 'react-i18next';
import {CREATED, OK} from "../../../services/api/apiConstants";
import ErrorContent from "../../common/ErrorContent/ErrorContent";


class AddRunPage extends Component {
    state = {
        game: null,
        loading: true,
        userId: this.props.user ? this.props.user.id : null,
        platforms: [],
        playstyles: [],
        error : false,
        status : null,
    };

    componentWillMount() {
        const fetchGame = GameService.getGameById(this.props.match.params.id);
        const fetchPlat = PlatformService.getGamePlatforms(this.props.match.params.id);
        const fetchPlay = RunService.getAllPlaystyles();
        Promise.all([ fetchGame, fetchPlat, fetchPlay ]).then((responses) => {
            let findError = null;
            for(let i = 0; i < responses.length; i++) {
                if (responses[i].status && responses[i].status !== OK && responses[i].status !== CREATED) {
                    findError = responses[i].status;
                }
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
                    loading: false,
                    game: responses[0],
                    platforms: responses[1],
                    playstyles: responses[2],
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
        if (this.state.error) {
            return <ErrorContent status={this.state.status}/>
        }
        const { t } = this.props
        return (
            <React.Fragment>
                <HelmetProvider>
                    <Helmet>
                        <title>{t(`runs.addingRun`)} {this.state.game.title} - QuestLog</title>
                    </Helmet>
                </HelmetProvider>
                <RunCard game={this.state.game} platforms={this.state.platforms} playstyles={this.state.playstyles} userId={this.state.userId}/>
            </React.Fragment>
        );
    }
}

export default withTranslation() (withUser(AddRunPage,{visibility : "usersOnly"}));
