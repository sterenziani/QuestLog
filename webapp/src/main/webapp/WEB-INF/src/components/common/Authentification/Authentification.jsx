import React, { Component } from 'react';

import AnyButton from '../AnyButton/AnyButton';
import withUser from '../../hoc/withUser';

class Authentification extends Component {    
    render() {
        return this.props.userIsLoggedIn ? (
            <React.Fragment>
                <AnyButton 
                    variant="link"
                    text={ this.props.user.username }
                    href="/profile"
                    className="mr-3 color-white"
                />
                <AnyButton 
                    textKey="navigation.auth.logout"
                    href="/logout"
                    variant="outline-secondary"
                />
            </React.Fragment>
        ) : ( 
            <React.Fragment>
                <AnyButton 
                    textKey="navigation.auth.login"
                    href="/login"
                    variant="outline-secondary"
                    className="mr-3"
                />
                <AnyButton 
                    textKey="navigation.auth.signup"
                    href="/signup"
                    variant="outline-secondary"
                />
            </React.Fragment>
        );
    }
}
 
export default withUser(Authentification);