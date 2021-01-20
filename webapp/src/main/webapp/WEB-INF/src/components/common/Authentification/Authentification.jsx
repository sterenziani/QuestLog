import React, { Component } from 'react';

import AnyButton from '../AnyButton/AnyButton';
import withUser from '../../hoc/withUser';

class Authentification extends Component {    
    render() {
        const { userIsLoggedIn, user } = this.props;
        return userIsLoggedIn ? (
            <div className={ this.props.className }>
                <AnyButton 
                    variant="link"
                    text={ user.username }
                    href="/profile"
                    className="mr-3 color-white"
                />
                <AnyButton 
                    textKey="navigation.auth.logout"
                    href="/logout"
                    variant="outline-secondary"
                />
            </div>
        ) : ( 
            <div className={ this.props.className }>
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
            </div>
        );
    }
}
 
export default withUser(Authentification);