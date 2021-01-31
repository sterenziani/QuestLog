import React, { Component } from 'react';
import { Helmet, HelmetProvider } from 'react-helmet-async';
import Spinner from 'react-bootstrap/Spinner';
import UserService from "../../../services/api/userService";
import withUser from '../../hoc/withUser';
import withQuery from '../../hoc/withQuery';
import { withTranslation } from 'react-i18next';
import Pagination from "../../common/Pagination/Pagination";
import RunService from "../../../services/api/runService";
import UserRunsTab from "../../common/UserDetails/UserRunsTab";


class UserRunsPage extends Component {
    state = {
        path : window.location.pathname.substring(1 + (`${process.env.PUBLIC_URL}`).length),
        loading: true,
        userId: this.props.user ? this.props.user.id : null,
        visitedUser: this.props.user,
        pagination: [],
        content : [],
        pageCount : null,
        page : null,
    };

    componentWillMount() {
        this.setPage();
    }

    setPage() {
        let page = this.props.query.get("page");
        if(!page) {
            page = 1;
        }
        if(this.props.match.params.id != this.state.userId)
        {
            UserService.getUserById(this.props.match.params.id).then((response) => {
                this.setState({
                    visitedUser : response,
                })
            });
        }
        RunService.getUserRunsPage(this.props.match.params.id, page, 25).then((response) => {
                this.setState({
                    loading: false,
                    content: response.content,
                    pagination: response.pagination,
                    pageCount : response.pageCount,
                    page : page,
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
        console.log(this.state.pagination)
        const { t } = this.props;
        return (
            <React.Fragment>
                <HelmetProvider>
                    <Helmet>
                        <title>{t("users.userRuns", {value: this.state.visitedUser.username})} - QuestLog</title>
                    </Helmet>
                </HelmetProvider>
                <UserRunsTab visitedUser={this.state.visitedUser} loggedUser={this.props.user} runsDisplayed={this.state.content} runsPagination={this.state.pagination} seeAll={false}/>
                <Pagination url={this.state.path} page={this.state.page} totalPages={this.state.pageCount} setPage={this.setPage} queryParams={this.state.searchParams}/>
            </React.Fragment>
        );
    }
}

export default withTranslation() (withUser(withQuery(UserRunsPage)));
