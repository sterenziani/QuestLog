import React, { Component } from 'react';
import { Helmet, HelmetProvider } from 'react-helmet-async';
import { withTranslation } from 'react-i18next';
import Spinner from 'react-bootstrap/Spinner';
import {Card, Row, Col, Button} from "react-bootstrap";
import UserService from "../../../services/api/userService";
import Pagination from "../../common/Pagination/Pagination";
import withQuery from '../../hoc/withQuery';
import {Translation} from "react-i18next";
import withUser from '../../hoc/withUser';
import { LinkContainer } from 'react-router-bootstrap';
import ErrorContent from "../../common/ErrorContent/ErrorContent";
import {CREATED, OK} from "../../../services/api/apiConstants";

class SearchUserResults extends Component {
    state = {
        path : window.location.pathname.substring(1 + (`${process.env.PUBLIC_URL}`).length),
        loading: true,
        pagination: [],
        content : [],
        page : null,
        term: null,
        pageCount : null,
        error : false,
        status : null
    };

    componentWillMount() {
        this.setPage(this.props);
    }

    componentWillReceiveProps(newProps) {
        this.setPage(newProps);
    }

    setPage(props) {
        let page = props.query.get("page");
        let term = props.query.get("searchTerm");
        if(!page) {
            page = 1;
        }
        if(!term) {
            term = '';
        }
        UserService.searchUsersPage(term, page).then((response) => {
            let findError = null;
            if (response.status && response.status !== OK && response.status !== CREATED) {
                findError = response.status;
            }
            if(findError) {
                this.setState({
                    loading: false,
                    error: true,
                    status: findError,
                });
            }
            else {
                this.setState({
                    loading: false,
                    content: response.content,
                    pagination: response.pagination,
                    pageCount: response.pageCount,
                    page: page,
                    term: term,
                });
            }
        });
    }

    makeAdminHandler = async (user) => {
        await UserService.makeAdmin(user.id);
        this.setPage(this.props)
    }

    removeAdminHandler = async (user) => {
        await UserService.removeAdmin(user.id);
        this.setPage(this.props)
    }

    render() {
        const { t } = this.props
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
                        <title>{t("search.helmet", {value: this.state.term})} - QuestLog</title>
                    </Helmet>
                </HelmetProvider>
                <Card className="m-5 bg-light-grey right-wave left-wave" bordered style={{ borderBottomLeftRadius: 30, borderBottomRightRadius: 30 }}>
                    <Card.Header className="bg-very-dark text-white px-3 d-flex">
                        <h2 className="share-tech-mono">
                            <Translation>{t => t("search.userResults", {value: this.state.term})}</Translation>
                        </h2>
                    </Card.Header>
                    <Card.Body className="d-flex flex-wrap justify-content-center align-items-center align-items-stretch">
                        <Col>
                             {this.state.content && this.state.content.length > 0? [this.state.content.map(u =>
                                <Row>
                                    <Col style={{verticalAlign: "middle", padding:"10px"}} className={this.props.userIsAdmin? 'text-right':'text-center'}>
                                        <div style={{fontSize: "25px"}}>
                                            <LinkContainer to={ "/users/" +u.id }>
                                                <a href={() => false} className="text-primary">{u.username}</a>
                                            </LinkContainer>
                                        </div>
                         			</Col>
                                    {
                                        (this.props.userIsAdmin)?
                                            u.id !== this.props.user.id? [
                                            <Col style={{verticalAlign: "middle", padding:"10px"}}>
                                                {
                                                    u.admin? [
                                                        <Button variant="danger" onClick={() => {this.removeAdminHandler(u)}}><Translation>{t => t("search.removeAdmin")}</Translation></Button>] : [
                                                        <Button variant="success" onClick={() => {this.makeAdminHandler(u)}}><Translation>{t => t("search.makeAdmin")}</Translation></Button>]
                                                }
                                            </Col>] : [<Col></Col>]
                                         : []
                                    }
                                </Row>)] : [<div class="text-center"><Translation>{t => t("search.noResults")}</Translation></div>] }
                        </Col>
                    </Card.Body>
                </Card>
                <Pagination url={this.state.path} page={this.state.page} totalPages={this.state.pageCount} setPage={this.setPage}/>
            </React.Fragment>
        );
    }
}

export default withTranslation() (withUser(withQuery(SearchUserResults)));
