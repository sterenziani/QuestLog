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
import LogInPage from '../../pages/LogInPage/LogInPage';
import SignUpPage from '../../pages/SignUpPage/SignUpPage';
import ExplorePage from '../../pages/ExplorePage/ExplorePage';
import ExploreResultsPage from '../../pages/ExploreResultsPage/ExploreResultsPage';
import LogOutPage from '../../pages/LogOutPage/LogOutPage';
import SeeAllPage from "../../pages/SeeAllPage/SeeAllPage";
import UserProfilePage from '../../pages/UserProfilePage/UserProfilePage';


class ContentSwitch extends Component {
    state = { }
    render() {
        return (
            <main>
                <Switch>
                    <Route
                        exact path="/"
                        component={ IndexPage }
                    />
                    <Route
                        exact path="/login"
                        component={ LogInPage }
                    />
                    <Route 
                        exact path="/logout"
                        component={ LogOutPage }
                    />
                    <Route
                        exact path="/signup"
                        component={ SignUpPage }
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
                </Switch>
            </main>
        );
    }
}

export default ContentSwitch;
