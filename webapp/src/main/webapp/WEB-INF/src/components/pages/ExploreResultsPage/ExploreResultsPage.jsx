import React, { Component } from 'react';
import { Helmet, HelmetProvider } from 'react-helmet-async';
import Spinner from 'react-bootstrap/Spinner';
import GamesCard from "../../common/GamesCard/GamesCard";
import PaginationService from "../../../services/api/paginationService";
import Pagination from "../../common/Pagination/Pagination";
import GameService from "../../../services/api/gameService";

class ExploreResultsPage extends Component {
    state = {
        path : window.location.pathname.substring(1 + (`${process.env.PUBLIC_URL}`).length),
        pagination: [],
        content : [],
        data : null,
        loading : true,
    };

    componentWillMount() {
        const fetchContent = PaginationService.getGenericContent(this.state.path + "/games");
        const fetchData = PaginationService.getGenericContent(this.state.path);

        //TODO: Handle no response (404)
        Promise.all([ fetchContent, fetchData ]).then((responses) => {
            this.setState({
                loading: false,
                content: responses[0].content,
                pagination: responses[0].pagination,
                data : responses[1].content,
            });
        });
    }

    render() {
        console.log("En ExploreResultsPage el path es: " +this.state.path)
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
                <GamesCard label={this.state.data.name} items={this.state.content} />
                <Pagination url={this.state.path} page={1} totalPages={5}/>
            </React.Fragment>
        );
    }
}
export default ExploreResultsPage;
