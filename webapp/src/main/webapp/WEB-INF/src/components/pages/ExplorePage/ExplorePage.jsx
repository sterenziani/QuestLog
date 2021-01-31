import React, { Component } from 'react';
import { Helmet, HelmetProvider } from 'react-helmet-async';
import Spinner from 'react-bootstrap/Spinner';
import GenreService from "../../../services/api/genreService";
import DeveloperService from "../../../services/api/devService";
import PlatformService from "../../../services/api/platformService";
import PublisherService from "../../../services/api/publisherService";
import ContainerCard from "../../common/GamesCard/ContainerCard";
import { withTranslation } from 'react-i18next';
import {CREATED, OK} from "../../../services/api/apiConstants";
import ErrorContent from "../../common/ErrorContent/ErrorContent";

class ExplorePage extends Component {
    state = {
        loading: true,
        genres: [],
        platforms: [],
        developers: [],
        publishers: [],
        error : false,
        status : null,
    };

    componentWillMount() {
        const fetchDev = DeveloperService.getBiggestDevelopers();
        const fetchGen = GenreService.getAllGenres();
        const fetchPlat = PlatformService.getBiggestPlatforms();
        const fetchPub = PublisherService.getBiggestPublishers();

        Promise.all([ fetchDev, fetchGen, fetchPlat, fetchPub ]).then((responses) => {
            let findError = null;
            for(let i = 0; i < responses.length; i++) {
                if (responses[i].status && responses[i].status != OK && responses[i].status != CREATED) {
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
                    developers: responses[0],
                    genres: responses[1],
                    platforms: responses[2],
                    publishers: responses[3],
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
        const { t } = this.props
        return (
            <React.Fragment>
                <HelmetProvider>
                    <Helmet>
                        <title>{t(`navigation.explore`)} - QuestLog</title>
                    </Helmet>
                </HelmetProvider>
                <div>
                        <div>
                            <ContainerCard items={this.state.genres} label="Genres" limit={25}/>
                        </div>
                        <div>
                            <ContainerCard items={this.state.platforms} label="Platforms" limit={18}/>
                        </div>
                        <div>
                            <ContainerCard items={this.state.developers} label="Developers" limit={15}/>
                        </div>
                        <div>
                            <ContainerCard items={this.state.publishers} label="Publishers" limit={15}/>
                        </div>
                </div>
            </React.Fragment>
        );
    }

}

export default withTranslation() (ExplorePage);
