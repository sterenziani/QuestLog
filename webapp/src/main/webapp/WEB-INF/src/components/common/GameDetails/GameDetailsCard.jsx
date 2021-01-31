import React, { Component } from 'react';
import { Card } from 'react-bootstrap';
import GameService from "../../../services/api/gameService";
import DeveloperService from "../../../services/api/devService";
import PublisherService from "../../../services/api/publisherService";
import GenreService from "../../../services/api/genreService";
import PlatformService from "../../../services/api/platformService";
import BacklogButton from '../../common/BacklogButton/BacklogButton';
import GameDetailsInfoItem from './GameDetailsInfoItem'
import GameCover from '../../common/GameCover/GameCover';
import {Translation} from "react-i18next";
import "../../../../src/index.scss";
import Spinner from "react-bootstrap/Spinner";

class GameDetailsCard extends Component {
    state = {
        releaseDates: [],
        developers: [],
        publishers: [],
        genres: [],
        platforms: [],
        loading: true,
    };

    componentWillMount() {
        const fetchDates = GameService.getGameReleaseDates(this.props.game.id);
        const fetchDev = DeveloperService.getGameDevelopers(this.props.game.id);
        const fetchGen = GenreService.getGameGenres(this.props.game.id);
        const fetchPlat = PlatformService.getGamePlatforms(this.props.game.id);
        const fetchPub = PublisherService.getGamePublishers(this.props.game.id);

        //TODO: Handle no response (404)
        Promise.all([ fetchDates, fetchDev, fetchGen, fetchPlat, fetchPub ]).then((responses) => {
            this.setState({
                loading: false,
                releaseDates : responses[0],
                developers: responses[1],
                genres : responses[2],
                platforms : responses[3],
                publishers : responses[4],
            });
        });
    }

    render() {
        let trailerAvailable = this.props.game.trailer;
        if (this.state.loading === true) {
            return <div style={{
                position: 'absolute', left: '50%', top: '50%',
                transform: 'translate(-50%, -50%)'}}>
                <Spinner animation="border" variant="primary" />
            </div>
        }
        return (
            <Card className="m-3 d-flex bg-transparent" style={{width: '18rem', border: '0'}}>
              <BacklogButton game={this.props.game} onUpdate={this.props.onUpdate}/>
              <div className="d-flex flex-column flex-grow-1 text-white">
                <GameCover cover={this.props.game.cover}/>

                <div className="card-body bg-primary flex-grow-1">
                    <dl>
                        <div className="game-details-release-dates">
                            <GameDetailsInfoItem title={ <Translation>{t => t("games.profile.releaseDates")}</Translation> } items={this.state.releaseDates.map(r => (r.region.shortName +': ' +r.date))}/>
                        </div>
                    </dl>
                    <dl>
                        <div className="game-details-genres">
                            <GameDetailsInfoItem title={ <Translation>{t => t("games.profile.genres")}</Translation> } items={this.state.genres.map(g => (<Translation>{t => t("genres." +g.name)}</Translation>))}/>
                        </div>
                    </dl>
                    <dl>
                        <div className="game-details-platforms">
                            <GameDetailsInfoItem title={ <Translation>{t => t("games.profile.platforms")}</Translation> } items={this.state.platforms.map(p => (p.shortName))}/>
                        </div>
                    </dl>
                    <dl>
                        <div className="game-details-developers">
                            <GameDetailsInfoItem title={ <Translation>{t => t("games.profile.developers")}</Translation> } items={this.state.developers.map(dev => (dev.name))}/>
                        </div>
                    </dl>
                    <dl className="p-0 m-0">
                        <div className="game-details-publishers">
                            <GameDetailsInfoItem title={ <Translation>{t => t("games.profile.publishers")}</Translation> } items={this.state.publishers.map(pub => (pub.name))}/>
                        </div>
                    </dl>

                </div>
                { trailerAvailable? [<iframe width="286" height="161" src={'https://www.youtube.com/embed/' +this.props.game.trailer} frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>] : []}
              </div>
            </Card>
        )
    }
}

export default GameDetailsCard;
