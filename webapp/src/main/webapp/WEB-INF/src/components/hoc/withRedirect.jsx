import React from 'react';
import { Redirect } from 'react-router-dom';

const withRedirect = (WrappedComponent, redirections) => {
    return class extends React.Component {
        constructor(props){
            super(props);
            this.state = {
                redirect : redirections,
                active   : false,
                key      : undefined
            }
        }
        activateRedirect = (key) => {
            this.setState({
                active : true,
                key    : key
            })
        }
        render(){
            return this.state.active ? (
                <Redirect to={ this.state.redirect[this.state.key] }/>
            ) : (
                <WrappedComponent activateRedirect={ this.activateRedirect } {...this.props} />
            )
        }
    };
}

export default withRedirect;