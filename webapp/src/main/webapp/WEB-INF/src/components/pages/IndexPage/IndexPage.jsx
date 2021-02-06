import React, { Component } from 'react';
import {CREATED, OK, NOT_FOUND} from '../../../services/api/apiConstants';
import { Container, Button } from 'react-bootstrap';
import { Helmet, HelmetProvider } from 'react-helmet-async';
import GameService from "../../../services/api/gameService";
import Spinner from 'react-bootstrap/Spinner';
import GamesCard from "../../common/GamesCard/GamesCard";
import BacklogService from "../../../services/api/backlogService";
import {Translation} from "react-i18next";
import withUser from '../../hoc/withUser';
import ErrorContent from "../../common/ErrorContent/ErrorContent";

class IndexPage extends Component {
    state = {
        anonBacklogEmpty: BacklogService.isAnonBacklogEmpty(),
        backlogGames: [],
        popularGames : [],
        upcomingGames : [],
        backlogPagination: [],
        loading : true,
        importing: false,
        error : false,
    };

    eraseBacklogHandler = () => {
        BacklogService.wipeAnonBacklog();
        this.setState({});
    }

    importHandler = async () => {
        this.setState({ importing: true });
        let resp = await BacklogService.transferBacklog();
        if(resp.status === OK){
            this.setState({ backlogGames: [], anonBacklogEmpty: true });
            BacklogService.getCurrentUserBacklogPreview(10).then((data) => {
                this.setState({backlogGames: data.content, backlogPagination: data.pagination});
            })
        }
        else{
            this.setState({ importing: false });
        }
    }

    updateBacklog = () => {
        const fetchBacklog = BacklogService.getCurrentUserBacklogPreview(10);
        Promise.all([ fetchBacklog ]).then((responses) => {
            let findError = null;
            for(let i = 0; i < responses.length; i++) {
                if (responses[i].status && responses[i].status !== OK && responses[i].status !== CREATED) {
                    findError = responses[i].status;
                }
            }
            if(findError) {
                if(findError === NOT_FOUND)
                    findError = "whoops";
                this.setState({
                    loading: false,
                    error: true,
                    status: findError,
                });
            }
            else {
                let upcomingGames = [...this.state.upcomingGames];
                let popularGames = [...this.state.popularGames];
                let backlogGamesIds = new Set();
                responses[0].content.forEach(g => {
                    backlogGamesIds.add(g.id)
                });
                for(let i=0; i < upcomingGames.length; i++){
                    if(backlogGamesIds.has(upcomingGames[i].id)){
                        upcomingGames[i].in_backlog = true;
                    } else {
                        upcomingGames[i].in_backlog = false;
                    }
                }
                for(let i=0; i < popularGames.length; i++){
                    if(backlogGamesIds.has(popularGames[i].id)){
                        popularGames[i].in_backlog = true;
                    } else {
                        popularGames[i].in_backlog = false;
                    }
                }
                this.setState({
                    loading: false,
                    backlogGames: responses[0].content,
                    upcomingGames : upcomingGames,
                    popularGames : popularGames,
                    backlogPagination: responses[0].pagination,
                    anonBacklogEmpty: BacklogService.isAnonBacklogEmpty()
                });
            }
        });
    }

    componentWillMount() {
        const fetchBacklog = BacklogService.getCurrentUserBacklogPreview(10);
        const fetchPop = GameService.getPopularGames();
        const fetchUp = GameService.getUpcomingGames();

        Promise.all([ fetchBacklog, fetchPop, fetchUp ]).then((responses) => {
            let findError = null;
            for(let i = 0; i < responses.length; i++) {
                if (responses[i].status && responses[i].status !== OK && responses[i].status !== CREATED) {
                    findError = responses[i].status;
                }
            }
            if(findError) {
                if(findError === NOT_FOUND)
                    findError = "whoops";
                this.setState({
                    loading: false,
                    error: true,
                    status: findError,
                });
            }
            else {
                this.setState({
                    loading: false,
                    backlogGames: responses[0].content,
                    backlogPagination: responses[0].pagination,
                    popularGames: responses[1],
                    upcomingGames: responses[2],
                    anonBacklogEmpty: BacklogService.isAnonBacklogEmpty()
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
                        <title>QuestLog</title>
                    </Helmet>
                </HelmetProvider>
                { (this.props.userIsLoggedIn && !this.state.anonBacklogEmpty)?
                    [<Container className="text-center align-middle mt-3">
                            <div class="my-1 py-4 bg-light border-bottom border-primary rounded-lg">
                                <p><Translation>{t => t("index.anonBacklogWarning")}</Translation></p>
                                <div class="">
                                    <Button variant="dark" className="m-1 px-3" onClick={this.eraseBacklogHandler}><Translation>{t => t("index.importNo")}</Translation></Button>
                                    <Button variant="dark" disabled={this.state.importing} className="m-1 px-3" onClick={this.importHandler}><Translation>{t => t("index.importYes")}</Translation></Button>
                                </div>
                                <p class="my-1"><Translation>{t => t("index.ignoreThis")}</Translation></p>
                            </div>
                        </Container>] : []
                }
                <div>
                    <GamesCard items={this.state.backlogGames} label={"games.lists.backlogGames"} search={false} pagination={this.state.backlogPagination} seeAllLink="backlog" updateBacklog={ this.updateBacklog }/>
                </div>
                <div>
                    <GamesCard items={this.state.popularGames} label={"games.lists.popularGames"} search={false} updateBacklog={ this.updateBacklog }/>
                </div>
                <div>
                    <GamesCard items={this.state.upcomingGames} label={"games.lists.upcomingGames"} search={false} updateBacklog={ this.updateBacklog }/>
                </div>
            </React.Fragment>
        );
    }
}

export default withUser(IndexPage);
