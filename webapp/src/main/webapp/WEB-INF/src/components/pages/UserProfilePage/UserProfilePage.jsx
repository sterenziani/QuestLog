import React, { Component } from 'react';
import { Helmet, HelmetProvider } from 'react-helmet-async';
import Spinner from 'react-bootstrap/Spinner';
import UserService from "../../../services/api/userService";
import UserProfile from "../../common/UserDetails/UserProfile";

class GameDetailsPage extends Component {
    state = {
        user: null,
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
                <UserProfile user={this.state.user} />
            </React.Fragment>
        );
    }
}

export default GameDetailsPage;
