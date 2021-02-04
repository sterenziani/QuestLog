import React, { Component } from 'react';
import { Helmet, HelmetProvider } from 'react-helmet-async';
import Spinner from 'react-bootstrap/Spinner';
import withUser from '../../hoc/withUser';
import withQuery from '../../hoc/withQuery';
import { withTranslation } from 'react-i18next';
import Pagination from "../../common/Pagination/Pagination";
import ReviewService from "../../../services/api/reviewService";
import ReviewsTab from "../../common/GameDetails/ReviewsTab";
import GameService from "../../../services/api/gameService";
import {CREATED, OK} from "../../../services/api/apiConstants";
import ErrorContent from "../../common/ErrorContent/ErrorContent";


class GameReviewsPage extends Component {
    state = {
        path : window.location.pathname.substring(1 + (`${process.env.PUBLIC_URL}`).length),
        loading: true,
        userId: this.props.user ? this.props.user.id : null,
        pagination: [],
        content : [],
        pageCount : null,
        page : null,
        error : false,
        status: null,
    };

    componentWillMount() {
        this.setPage();
    }

    setPage() {
        let page = this.props.query.get("page");
        if(!page) {
            page = 1;
        }
        console.log(page)
        const fetchReviews = ReviewService.getGameReviewsPage(this.props.match.params.id, page,10);
        const fetchGame = GameService.getGameById(this.props.match.params.id);
        Promise.all([ fetchReviews, fetchGame ]).then((responses) => {
            let findError = null;
            for(let i = 0; i < responses.length; i++) {
                if (responses[i].status && responses[i].status != OK && responses[i].status != CREATED) {
                    findError = responses[i].status;
                }
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
                    content: responses[0].content,
                    pagination: responses[0].pagination,
                    pageCount: responses[0].pageCount,
                    page: page,
                    game: responses[1],
                    loading: false,
                });
            }
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
        if(this.state.error) {
            return <ErrorContent status={this.state.status}/>
        }
        const { t } = this.props;
        return (
            <React.Fragment>
                <HelmetProvider>
                    <Helmet>
                        <title>{t("users.userReviews", {value: this.state.game.title})} - QuestLog</title>
                    </Helmet>
                </HelmetProvider>
                <ReviewsTab className="p-5" key="1" game={this.state.game} user={this.props.user} loggedIn={this.props.loggedIn} reviews={this.state.content} pagination={this.state.pagination} seeAll={false}/>
                <Pagination url={this.state.path} page={this.state.page} totalPages={this.state.pageCount} setPage={this.setPage} queryParams={this.state.searchParams}/>
            </React.Fragment>
        );
    }
}

export default withTranslation() (withUser(withQuery(GameReviewsPage)));
