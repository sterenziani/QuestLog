import React, { Component } from 'react';
import {reaction} from 'mobx';

import AnyButton from '../AnyButton/AnyButton';
import AuthService from '../../../services/api/authService';

class Authentification extends Component {
    constructor(props){
        super(props)

        const userStore = AuthService.getUserStore()

        this.state = {}
        
        this.state.userIsLoggedIn = userStore.isLoggedIn
        this.state.user           = userStore.user
        reaction(
            () => userStore.user,
            () => this.setState({
                userIsLoggedIn : userStore.isLoggedIn,
                user           : userStore.user
            })
        )
    }
    
    render() { 
        console.log("here")
        return this.state.userIsLoggedIn ? (
            <React.Fragment>
                <AnyButton 
                    variant="link"
                    text={ this.state.user.username }
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
 
export default Authentification;