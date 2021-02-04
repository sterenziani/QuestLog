import React, { Component } from 'react';
import { Helmet, HelmetProvider } from 'react-helmet-async';
import Spinner from 'react-bootstrap/Spinner';
import ContainerCard from "../../common/GamesCard/ContainerCard";
import PaginationService from "../../../services/api/paginationService";
import Pagination from "../../common/Pagination/Pagination";
import withQuery from '../../hoc/withQuery';
import { withTranslation } from 'react-i18next';
import {CREATED, OK, NOT_FOUND} from "../../../services/api/apiConstants";
import ErrorContent from "../../common/ErrorContent/ErrorContent";


class SeeAllPage extends Component {
    state = {
        path : window.location.pathname.substring(1 + (`${process.env.PUBLIC_URL}`).length),
        content: [],
        page : null,
        pageCount : null,
        loading: true,
        error : false,
        status : null,
    };

    componentWillMount() {
        this.setPage()
    }

    setPage() {
        let page = this.props.query.get("page");
        if(!page) {
            page = 1;
        }
        PaginationService.getGenericContentPage(this.state.path, page)
            .then((data) => {
                let findError = null;
                if (data.status && data.status !== OK && data.status !== CREATED) {
                    findError = data.status;
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
                        content: data.content,
                        page: page,
                        pageCount: data.pageCount,
                    });
                }
        });
    }

    render() {
        let label = this.state.path.charAt(0).toUpperCase() + this.state.path.substring(1);
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
                        <title>{t(`games.profile.${this.state.path}`)} - Questlog</title>
                    </Helmet>
                </HelmetProvider>
                <ContainerCard items={this.state.content} label={label} limit={this.state.content.length}/>
                <Pagination url={this.state.path} page={this.state.page} totalPages={this.state.pageCount} setPage={this.setPage}/>
            </React.Fragment>
        );
    }

}

export default withTranslation() (withQuery(SeeAllPage));
