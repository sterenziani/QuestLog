import React, { Component } from 'react';
import { Container, Button } from 'react-bootstrap';
import { Helmet, HelmetProvider } from 'react-helmet-async';
import GameService from "../../../services/api/gameService";
import Spinner from 'react-bootstrap/Spinner';
import GamesCard from "../../common/GamesCard/GamesCard";
import BacklogService from "../../../services/api/backlogService";
import {Translation} from "react-i18next";
import withUser from '../../hoc/withUser';

class IndexPage extends Component {
    state = {
        anonBacklogEmpty: BacklogService.isAnonBacklogEmpty(),
        backlogGames: [],
        popularGames : [],
        upcomingGames : [],
        loading : true,
    };

    eraseBacklogHandler = () => {
        BacklogService.wipeAnonBacklog();
        this.setState({});
    }

    importHandler = async () => {
        await BacklogService.transferBacklog();
        //this.setState({})
        // TODO: Try to update state and put new games in backlog card. Could be async issues
        window.location.reload();
    }

    render() {
        const fetchBacklog = BacklogService.getCurrentUserBacklogPreview(10);
        const fetchPop = GameService.getPopularGames();
        const fetchUp = GameService.getUpcomingGames();

        //TODO: Handle no response (404)
        Promise.all([ fetchBacklog, fetchPop, fetchUp ]).then((responses) => {
            this.setState({
                loading: false,
                backlogGames: responses[0].content,
                backlogPagination: responses[0].pagination,
                popularGames: responses[1],
                upcomingGames : responses[2],
                anonBacklogEmpty: BacklogService.isAnonBacklogEmpty()
            });
        });

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
                        <title>QuestLog</title>
                    </Helmet>
                </HelmetProvider>
                { (this.props.userIsLoggedIn && !this.state.anonBacklogEmpty)?
                    [<Container className="text-center align-middle mt-3">
                            <div class="my-1 py-4 bg-light border-bottom border-primary rounded-lg">
                                <p><Translation>{t => t("index.anonBacklogWarning")}</Translation></p>
                                <div class="">
                                    <Button variant="dark" className="m-1 px-3" onClick={this.eraseBacklogHandler}><Translation>{t => t("index.importNo")}</Translation></Button>
                                    <Button variant="dark" className="m-1 px-3" onClick={this.importHandler}><Translation>{t => t("index.importYes")}</Translation></Button>
                                </div>
                                <p class="my-1"><Translation>{t => t("index.ignoreThis")}</Translation></p>
                            </div>
                        </Container>] : []
                }
                <div>
                    <GamesCard items={this.state.backlogGames} label={"games.lists.backlogGames"} search={false} pagination={this.state.backlogPagination} seeAllLink="backlog"/>
                </div>
                <div>
                    <GamesCard items={this.state.popularGames} label={"games.lists.popularGames"} search={false}/>
                </div>
                <div>
                    <GamesCard items={this.state.upcomingGames} label={"games.lists.upcomingGames"} search={false}/>
                </div>
            </React.Fragment>
        );
    }
}

export default withUser(IndexPage);
