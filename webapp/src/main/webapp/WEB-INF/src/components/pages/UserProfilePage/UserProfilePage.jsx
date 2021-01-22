import React, { Component } from 'react';
import { Helmet, HelmetProvider } from 'react-helmet-async';
import Spinner from 'react-bootstrap/Spinner';
import UserService from "../../../services/api/userService";
import UserProfile from "../../common/UserDetails/UserProfile";
import withUser from '../../hoc/withUser';


class GameDetailsPage extends Component {
    state = {
        user: null,
        loggedInUser: this.props.user,
        loggedIn: this.props.userIsLoggedIn,
        loading: true,
    };

    componentWillMount() {
        UserService.getUserById(this.props.match.params.id)
            .then((data) => {
                this.setState({
                    user: data,
                    loading: false,
                });
            });
    }

    render() {
        if (this.state.loading === true) {
            return <div style={{
                position: 'absolute', left: '50%', top: '50%',
                transform: 'translate(-50%, -50%)'}}>
                <Spinner animation="border" variant="primary" />
            </div>
        }

        return (
            <React.Fragment>
                <HelmetProvider>
                    <Helmet>
                        <title>{this.state.user.username}</title>
                    </Helmet>
                </HelmetProvider>
                {console.log(this.state)}
                <UserProfile user={this.state.user} loggedIn={this.state.loggedIn} loggedInUser={this.state.loggedInUser}/>
            </React.Fragment>
        );
    }
}

export default withUser(GameDetailsPage);
