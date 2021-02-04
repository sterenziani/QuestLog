import React, { Component } from 'react';
import { Helmet, HelmetProvider } from 'react-helmet-async';
import Spinner from 'react-bootstrap/Spinner';
import BacklogService from "../../../services/api/backlogService";
import UserService from "../../../services/api/userService";
import withUser from '../../hoc/withUser';
import withQuery from '../../hoc/withQuery';
import { withTranslation } from 'react-i18next';
import GamesCard from "../../common/GamesCard/GamesCard";
import Pagination from "../../common/Pagination/Pagination";
import {CREATED, OK, NOT_FOUND} from "../../../services/api/apiConstants";
import ErrorContent from "../../common/ErrorContent/ErrorContent";


class UserBacklogPage extends Component {
    state = {
        backlogOwner: this.props.user,
        path : window.location.pathname.substring(1 + (`${process.env.PUBLIC_URL}`).length),
        loading: true,
        pagination: [],
        content : [],
        page : null,
        pageCount : null,
        error : false,
        status : null,
    };

    componentWillMount() {
        this.setPage();
    }

    setPage() {
        let page = this.props.query.get("page");
        if(!page) {
            page = 1;
        }

        let findError = null;

        if(this.props.match.params.id)
        {
            // Visiting specified user
            UserService.getUserById(this.props.match.params.id).then((response) => {
                if (response.status && response.status !== OK && response.status !== CREATED) {
                    findError = response.status;
                }
                if(findError) {
                    this.setState({
                        loading: false,
                        error: true,
                        status: findError,
                    });
                    return;
                }
                else {
                    this.setState({
                        backlogOwner: response,
                    })
                }
            });
            BacklogService.getUserBacklogPage(this.props.match.params.id, page, 15).then((response) => {
                if (response.status && response.status !== OK && response.status !== CREATED) {
                    findError = response.status;
                }
                if(findError) {
                    this.setState({
                        loading: false,
                        error: true,
                        status: findError,
                    });
                    return;
                }
                else {
                    this.setState({
                        loading: false,
                        content: response.content,
                        pagination: response.pagination,
                        pageCount: response.pageCount,
                        page: page,
                    });
                }
            });
        }
        else
        {
            // Own backlog
            BacklogService.getCurrentUserBacklogPage(page).then((response) => {
                if (response.status && response.status !== OK && response.status !== CREATED) {
                    findError = response.status;
                }
                if(findError) {
                    if(findError === NOT_FOUND)
                        findError = "whoops";
                    this.setState({
                        loading: false,
                        error: true,
                        status: findError,
                    });
                    return;
                }
                else {
                    this.setState({
                        loading: false,
                        content: response.content,
                        pagination: response.pagination,
                        pageCount: response.pageCount,
                        page: page,
                    });
                }
            });
        }
    }

    render() {
        if (this.state.loading === true) {
            return <div style={{
                position: 'absolute', left: '50%', top: '50%',
                transform: 'translate(-50%, -50%)'}}>
                <Spinner animation="border" variant="primary" />
            </div>
        }
        let label = "games.lists.backlogGames";
        let param = "";
        if(this.state.backlogOwner){
            label = "users.userBacklog";
            param = {value: this.state.backlogOwner.username};
        }

        if(this.state.error) {
            return <ErrorContent status={this.state.status}/>
        }
        const { t } = this.props
        return (
            <React.Fragment>
                <HelmetProvider>
                    <Helmet>
                        <title>{t(label, param)} - QuestLog</title>
                    </Helmet>
                </HelmetProvider>
                <GamesCard label={t(label, param)} items={this.state.content} totalCount={this.state.totalCount}/>
                <Pagination url={this.state.path} page={this.state.page} totalPages={this.state.pageCount} setPage={this.setPage} queryParams={this.state.searchParams}/>
            </React.Fragment>
        );
    }
}

export default withTranslation() (withUser(withQuery(UserBacklogPage)));
