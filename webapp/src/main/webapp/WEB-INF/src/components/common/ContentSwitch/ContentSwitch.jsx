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
                        component={ IndexPage }
                    />
                    <Route 
                        exact path="/signup"
                        component={ IndexPage }
                    />
                    <Route 
                        exact path="/search"
                        component={ IndexPage }
                    />
                    <Route 
                        exact path="/explore"
                        component={ IndexPage }
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
                </Switch>
            </main>
        );
    }
}
 
export default ContentSwitch;