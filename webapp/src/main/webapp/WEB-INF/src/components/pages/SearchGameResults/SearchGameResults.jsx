import React, { Component } from 'react';
import { Helmet, HelmetProvider } from 'react-helmet-async';
import Spinner from 'react-bootstrap/Spinner';
import GameService from "../../../services/api/gameService";
import ContainerCard from "../../common/GamesCard/ContainerCard";
import PaginationService from "../../../services/api/paginationService";
import Pagination from "../../common/Pagination/Pagination";
import withQuery from '../../hoc/withQuery';
import {Translation} from "react-i18next";
import GenericListItem from "../../common/ListItem/GenericListItem";
import GamesCard from "../../common/GamesCard/GamesCard";
import SearchModal from "../../common/SearchModal/SearchModal"

class SearchGameResults extends Component {
    state = {
        path : window.location.pathname.substring(1 + (`${process.env.PUBLIC_URL}`).length),
        pagination: [],
        content : [],
        data : null,
        page : null,
        searchParams: {},
        pageCount : null,
        loading : true,
    };

    componentWillMount() {
        this.setPage();
    }

    setPage() {
        let page = this.props.query.get("page");
        let searchParams = {hoursLeft: this.props.query.get("hoursLeft"), minsLeft: this.props.query.get("minsLeft"), secsLeft: this.props.query.get("secsLeft"),
                            hoursRight: this.props.query.get("hoursRight"), minsRight: this.props.query.get("minsRight"), secsRight: this.props.query.get("secsRight"),
                            scoreLeft: this.props.query.get("scoreLeft"), scoreRight: this.props.query.get("scoreRight"),
                            platforms: this.props.query.getAll("platforms"), genres: this.props.query.getAll("genres"), searchTerm: this.props.query.get("searchTerm")}
        if(!page) {
            page = 1;
        }
        if(!searchParams.searchTerm) {
            searchParams.searchTerm = '';
        }
        GameService.searchGamesPage(searchParams, page).then((response) => {
            this.setState({
                loading: false,
                content: response.content,
                pagination: response.pagination,
                pageCount : response.pageCount,
                page : page,
                searchParams : searchParams,
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
        let params = GameService.buildQueryParams(this.state.searchParams).replace(0,"?");

        return (
            <React.Fragment>
                <HelmetProvider>
                    <Helmet>
                        <title>QuestLog - Game Search Results</title>
                    </Helmet>
                </HelmetProvider>
                <SearchModal searchParams={this.state.searchParams} path={this.state.path}/>
                <GamesCard label={"search.gameResults"} labelArgs={this.state.searchParams.searchTerm} items={this.state.content} />
                <Pagination url={this.state.path} page={this.state.page} totalPages={this.state.pageCount} setPage={this.setPage} queryParams={this.state.searchParams}/>
            </React.Fragment>
        );
    }
}
export default withQuery(SearchGameResults);
