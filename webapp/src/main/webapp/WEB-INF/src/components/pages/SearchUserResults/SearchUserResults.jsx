import React, { Component } from 'react';
import { Helmet, HelmetProvider } from 'react-helmet-async';
import Spinner from 'react-bootstrap/Spinner';
import {Card, Row, Col, Button} from "react-bootstrap";
import UserService from "../../../services/api/userService";
import ContainerCard from "../../common/GamesCard/ContainerCard";
import PaginationService from "../../../services/api/paginationService";
import Pagination from "../../common/Pagination/Pagination";
import withQuery from '../../hoc/withQuery';
import {Translation} from "react-i18next";
import GenericListItem from "../../common/ListItem/GenericListItem";

class SearchUserResults extends Component {
    state = {
        path : window.location.pathname.substring(1 + (`${process.env.PUBLIC_URL}`).length),
        loading: true,
        pagination: [],
        content : [],
        page : null,
        term: null,
        pageCount : null,
        adminLoggedIn: false,
    };

    componentWillMount() {
        this.setPage()
    }

    setPage() {
        let page = this.props.query.get("page");
        let term = this.props.query.get("searchTerm");
        if(!page) {
            page = 1;
        }
        if(!term) {
            term = '';
        }
        UserService.searchUsersPage(term, page).then((response) => {
            this.setState({
                loading: false,
                content: response.content,
                pagination: response.pagination,
                pageCount : response.pageCount,
                page : page,
                term : term,
            });
        });
    }

    render() {
        let label = "Buscando usuarios :)";
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
                        <title>sEaRCh ReSuLtS</title>
                    </Helmet>
                </HelmetProvider>
                <Card style={{width: "100%"}} className="m-5 bg-light-grey right-wave left-wave" bordered style={{ borderBottomLeftRadius: 30, borderBottomRightRadius: 30 }}>
                    <Card.Header className="bg-very-dark text-white px-3 d-flex">
                        <h2 className="share-tech-mono">
                            <Translation>{t => t("search.userResults", {value: this.state.term})}</Translation>
                        </h2>
                    </Card.Header>
                    <Card.Body className="d-flex flex-wrap justify-content-center align-items-center align-items-stretch">
                        <Col>
                             {this.state.content.length > 0? [this.state.content.map(u =>
                                <Row>
                                    <Col style={{verticalAlign: "middle", padding:"10px"}} className={this.state.adminLoggedIn? 'text-right':'text-center'}>
                                        <a href={u.id} style={{fontSize: "25px"}}>{u.username}</a>
                         			</Col>
                                    {
                                        this.state.adminLoggedIn? [
                                            <Col style={{verticalAlign: "middle", padding:"10px"}}>
                                                <Button href="">ADD/REMOVE ADMIN</Button>
                                            </Col>] : []
                                    }
                     			</Row>
                            )] : [<Translation>{t => t("search.noResults")}</Translation>] }
                        </Col>
                    </Card.Body>
                </Card>
                <Pagination url={this.state.path} page={this.state.page} totalPages={this.state.pageCount} setPage={this.setPage}/>
            </React.Fragment>
        );
    }
}

export default withQuery(SearchUserResults);
