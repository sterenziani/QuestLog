import React from 'react';
import { Redirect } from 'react-router-dom';

const withRedirect = (WrappedComponent, redirect_to) => {
    return class extends React.Component {
        constructor(props){
            super(props);
            this.state = {
                redirect : redirect_to,
                active   : false
            }
        }
        activateRedirect = () => {
            this.setState({
                active   : true
            })
        }
        render(){
            return this.state.active ? (
                <Redirect to={ this.state.redirect }/>
            ) : (
                <WrappedComponent activateRedirect={ this.activateRedirect } {...this.props} />
            )
        }
    };
}

export default withRedirect;