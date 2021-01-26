//Libraries
import React, {
    Component
} from 'react';
import {
    Route,
    Switch
} from 'react-router-dom';

//Child components
import IndexPage from '../../pages/IndexPage/IndexPage';
import GameDetailsPage from '../../pages/GameDetailsPage/GameDetailsPage';
import NewGamePage from '../../pages/NewGamePage/NewGamePage';
import LogInPage from '../../pages/LogInPage/LogInPage';
import SignUpPage from '../../pages/SignUpPage/SignUpPage';
import ExplorePage from '../../pages/ExplorePage/ExplorePage';
import ExploreResultsPage from '../../pages/ExploreResultsPage/ExploreResultsPage';
import LogOutPage from '../../pages/LogOutPage/LogOutPage';
import SeeAllPage from "../../pages/SeeAllPage/SeeAllPage";
import UserProfilePage from '../../pages/UserProfilePage/UserProfilePage';
import SearchUserResults from '../../pages/SearchUserResults/SearchUserResults';
import SearchGameResults from '../../pages/SearchGameResults/SearchGameResults';
import AddRunPage from "../../pages/AddRunPage/AddRunPage";
import AddReviewPage from "../../pages/AddReviewPage/AddReviewPage";
import UserBacklogPage from "../../pages/UserBacklogPage/UserBacklogPage";
import UserScoresPage from "../../pages/UserScoresPage/UserScoresPage";
import UserReviewsPage from "../../pages/UserReviewsPage/UserReviewsPage";
import UserRunsPage from "../../pages/UserRunsPage/UserRunsPage";


class ContentSwitch extends Component {
    state = {
        loginFailed : false
    }
    userCouldNotLoginNeedsProcessing = () => {
        this.setState({
            loginFailed : true
        })
    }
    userCouldNotLoginWasProcessed = () => {
        this.setState({
            loginFailed : false
        })
    }
    render() {
        return (
            <main>
                <Switch>
                    <Route
                        exact path="/"
                        component={ IndexPage }
                    />
                    <Route
                        exact path="/admin/game/new"
                        component={ NewGamePage }
                    />
                    <Route
                        exact path="/login"
                        render={ (props) => <LogInPage {...props} loginFailed={ this.state.loginFailed } loginFailedProcessed={ this.userCouldNotLoginWasProcessed }/> }
                    />
                    <Route
                        exact path="/logout"
                        component={ LogOutPage }
                    />
                    <Route
                        exact path="/signup"
                        render={ (props) => <SignUpPage {...props} loginFailed={ this.userCouldNotLoginNeedsProcessing }/> }
                    />
                    <Route
                        exact path="/search"
                        component={ IndexPage }
                    />
                    <Route
                        exact path="/explore"
                        component={ ExplorePage }
                    />
                    {
                        /*
                         * You can pass down URL parameters
                         * by prepending ':' to a parameter name.
                         * You can then specify a component and
                         * will be able to access it via
                         * { this.props.match.params.my_parameter }
                         */
                    }
                    <Route
                        exact path="/games/:id"
                        component={ GameDetailsPage }
                    />
                    <Route
                        exact path="/genres/:id"
                        component={ ExploreResultsPage }
                    />
                    <Route
                        exact path="/platforms/:id"
                        component={ ExploreResultsPage }
                    />
                    <Route
                        exact path="/developers/:id"
                        component={ ExploreResultsPage }
                    />
                    <Route
                        exact path="/publishers/:id"
                        component={ ExploreResultsPage }
                    />
                    <Route
                        exact path="/genres"
                        component={ SeeAllPage }
                    />
                    <Route
                        exact path="/platforms"
                        component={ SeeAllPage }
                    />
                    <Route
                        exact path="/developers"
                        component={ SeeAllPage }
                    />
                    <Route
                        exact path="/publishers"
                        component={ SeeAllPage }
                    />
                    <Route
                        exact path="/users/:id"
                        component={ UserProfilePage }
                    />
                    <Route
                        exact path="/gameSearch"
                        component={ SearchGameResults }
                    />
                    <Route
                        exact path="/userSearch"
                        component={ SearchUserResults }
                    />
                    <Route
                        exact path="/createRun/:id"
                        component={ AddRunPage }
                    />
                    <Route
                        exact path="/reviews/create/:id"
                        component={ AddReviewPage }
                    />
                    <Route
                        exact path="/users/:id/backlog"
                        component={ UserBacklogPage }
                    />
                    <Route
                        exact path="/users/:id/scores"
                        component={ UserScoresPage }
                    />
                    <Route
                        exact path="/users/:id/runs"
                        component={ UserRunsPage }
                    />
                    <Route
                        exact path="/users/:id/reviews"
                        component={ UserReviewsPage }
                    />
                    <Route
                        exact path="/backlog"
                        component={ UserBacklogPage }
                    />
                </Switch>
            </main>
        );
    }
}

export default ContentSwitch;
