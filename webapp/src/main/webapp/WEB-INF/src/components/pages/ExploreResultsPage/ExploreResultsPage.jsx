import React, { Component } from 'react';
import { Helmet, HelmetProvider } from 'react-helmet-async';
import Spinner from 'react-bootstrap/Spinner';
import GamesCard from "../../common/GamesCard/GamesCard";
import PaginationService from "../../../services/api/paginationService";
import Pagination from "../../common/Pagination/Pagination";
import withQuery from '../../hoc/withQuery';
import { withTranslation } from 'react-i18next';


class ExploreResultsPage extends Component {
    state = {
        path : window.location.pathname.substring(1 + (`${process.env.PUBLIC_URL}`).length),
        pagination: [],
        content : [],
        data : null,
        page : null,
        pageCount : null,
        loading : true,
    };

    componentWillMount() {
        this.setPage()
    }

    setPage() {
        let page = this.props.query.get("page");
        if(!page) {
            page = 1;
        }
        const fetchContent = PaginationService.getGenericContentPage(this.state.path + "/games", page);
        const fetchData = PaginationService.getGenericContent(this.state.path);

        //TODO: Handle no response (404)
        Promise.all([ fetchContent, fetchData ]).then((responses) => {
            this.setState({
                loading: false,
                content: responses[0].content,
                pagination: responses[0].pagination,
                data : responses[1].content,
                page : page,
                pageCount : responses[0].pageCount,
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

        let category = this.state.path.split("/")[0];
        let label = "";
        if(this.state.data)
            label = this.state.data.name;
        if(category.toLowerCase() === "genres" && this.state.data){
            label = "genres." +this.state.data.name;
        }
        const { t } = this.props

        return (
            <React.Fragment>
                <HelmetProvider>
                    <Helmet>
                        <title>{t(label)} - QuestLog</title>
                    </Helmet>
                </HelmetProvider>
                <GamesCard label={label} items={this.state.content} search={false} />
                <Pagination url={this.state.path} page={this.state.page} totalPages={this.state.pageCount} setPage={this.setPage}/>
            </React.Fragment>
        );
    }
}
export default withTranslation() (withQuery(ExploreResultsPage));
