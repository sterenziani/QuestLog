import React from 'react';
import { Redirect } from 'react-router-dom';
import { withLastLocation } from 'react-router-last-location';

import withHistory from './withHistory';

const withRedirect = (WrappedComponent, redirections) => {
    return withHistory(withLastLocation(class extends React.Component {
        constructor(props){
            super(props);
            this.state = {
                redirect : redirections,
                active   : false,
                key      : undefined,
                goBack   : false
            }
        }
        activateRedirect = (key) => {
            this.setState({
                active : true,
                key    : key
            })
        }
        activateGoBack = () => {
            if(this.props.lastLocation)
                this.props.history.goBack()
            else
                this.setState({
                    active : true,
                    goBack : true
                })
        }
        addRedirection = (key, value) => {
            let newRedirect = {
                ...this.state.redirect
            }
            newRedirect[key] = value
            this.setState({
                redirect: newRedirect
            });
        }
        render(){
            return this.state.active ? (
                this.state.goBack ? (
                    <Redirect to="/"/>
                ) : (
                    <Redirect to={ this.state.redirect[this.state.key] }/>
                )
            ) : (
                <WrappedComponent addRedirection={this.addRedirection} activateRedirect={ this.activateRedirect } activateGoBack={ this.activateGoBack } {...this.props} />
            )
        }
    }));
}

export default withRedirect;
