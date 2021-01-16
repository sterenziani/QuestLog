import React, { Component } from 'react';
import { Helmet, HelmetProvider } from 'react-helmet-async';
import Spinner from 'react-bootstrap/Spinner';
import ContainerCard from "../../common/GamesCard/ContainerCard";
import PaginationService from "../../../services/api/paginationService";
import Pagination from "../../common/Pagination/Pagination";
import withQuery from '../../hoc/withQuery';

class SeeAllPage extends Component {
    state = {
        path : window.location.pathname.substring(1 + (`${process.env.PUBLIC_URL}`).length),
        content: [],
        page : null,
        pageCount : null,
        loading: true,
    };

    componentWillMount() {
        this.setPage()
    }

    setPage() {
        let page = this.props.query.get("page");
        if(!page) {
            page = 1;
        }
        PaginationService.getGenericContent(this.state.path + "?page=" + page)
            .then((data) => {
                this.setState({
                    loading: false,
                    content: data.content,
                    page : page,
                    pageCount : data.pageCount,
            });
        });
    }

    render() {
        console.log(this.state.path)
        console.log(this.state.content)
        console.log(this.state.pageCount)
        let label = this.state.path.charAt(0).toUpperCase() + this.state.path.substring(1);
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
                <ContainerCard items={this.state.content} label={label} limit={this.state.content.length}/>
                <Pagination url={this.state.path} page={this.state.page} totalPages={this.state.pageCount} setPage={this.setPage}/>
            </React.Fragment>
        );
    }

}

export default withQuery(SeeAllPage);
