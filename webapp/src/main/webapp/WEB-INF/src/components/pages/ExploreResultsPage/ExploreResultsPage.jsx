import React, { Component } from 'react';
import { Helmet, HelmetProvider } from 'react-helmet-async';
import Spinner from 'react-bootstrap/Spinner';
import GamesCard from "../../common/GamesCard/GamesCard";
import PaginationService from "../../../services/api/paginationService";
import Pagination from "../../common/Pagination";
import GameService from "../../../services/api/gameService";

class ExploreResultsPage extends Component {
    state = {
        path : window.location.pathname.substring(8),
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
                content: responses[0],
                data : responses[1],
            });
        });
    }

    render() {
        console.log(this.state.path)
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
                        <title>Questlog</title>
                    </Helmet>
                </HelmetProvider>
                <GamesCard label={this.state.data.name} items={this.state.content} />
                <Pagination total={this.state.content.length} page={1}/>
            </React.Fragment>
        );
    }
}
export default ExploreResultsPage;
