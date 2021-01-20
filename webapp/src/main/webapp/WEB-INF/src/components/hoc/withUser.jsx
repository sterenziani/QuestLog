import React from 'react';

import AuthService from '../../services/api/authService';
import { reaction } from 'mobx';

import withRedirect from './withRedirect';

const withUser = (WrappedComponent, config) => {
    return withRedirect(class extends React.Component {
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
            if(config){
                const { visibility } = config

                switch(visibility){

                    case "anonymousOnly":
                        if(userStore.isLoggedIn){
                            this.props.activateGoBack()
                        }
                        break;

                    case "usersOnly":
                        if(!userStore.isLoggedIn){
                            this.props.activateRedirect("login")
                        }
                        break;

                    case "adminOnly":
                        break;
                }
            }
            
        }
        render(){
            return <WrappedComponent user={this.state.user} userIsLoggedIn={this.state.userIsLoggedIn} {...this.props}/>
        }
    }, { login : "/login" })
}

export default withUser;