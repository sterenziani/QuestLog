import React, { Component } from 'react';
import { Helmet, HelmetProvider } from 'react-helmet-async';
import Spinner from 'react-bootstrap/Spinner';
import GenreService from "../../../services/api/genreService";
import DeveloperService from "../../../services/api/devService";
import PlatformService from "../../../services/api/platformService";
import PublisherService from "../../../services/api/publisherService";
import {Col, Row} from "react-bootstrap";
import {Grid} from '@material-ui/core';
import {Translation} from "react-i18next";
import ContainerCard from "../../common/ItemsCard/ContainerCard";

class ExplorePage extends Component {
    state = {
        loading: true,
        genres: [],
        platforms: [],
        developers: [],
        publishers: [],

    };

    componentWillMount() {
        const fetchDev = DeveloperService.getAllDevelopers();
        const fetchGen = GenreService.getAllGenres();
        const fetchPlat = PlatformService.getAllPlatforms();
        const fetchPub = PublisherService.getAllPublishers();

        //TODO: Handle no response (404)
        Promise.all([ fetchDev, fetchGen, fetchPlat, fetchPub ]).then((responses) => {
            this.setState({
                loading: false,
                developers: responses[0],
                genres : responses[1],
                platforms : responses[2],
                publishers : responses[3],
            });
        });
        console.log(this.state.developers);
        console.log(this.state.genres);
        console.log(this.state.platforms);
        console.log(this.state.publishers);
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
                        <title>Questlog</title>
                    </Helmet>
                </HelmetProvider>
                <Grid style={{width: "100%"}} justify="space-evenly" alignItems="center">
                    <Col>
                        <Row>
                            <ContainerCard items={this.state.genres} label="Genres"/>
                        </Row>
                        <Row>
                            <ContainerCard items={this.state.platforms} label="Platforms"/>
                        </Row>
                        <Row>
                            <ContainerCard items={this.state.developers} label="Developers"/>
                        </Row>
                        <Row>
                            <ContainerCard items={this.state.publishers} label="Publishers"/>
                        </Row>
                    </Col>
                </Grid>
            </React.Fragment>
        );
    }

}

export default ExplorePage;
