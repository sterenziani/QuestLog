import React, { Component } from 'react';
import { Helmet, HelmetProvider } from 'react-helmet-async';
import Spinner from 'react-bootstrap/Spinner';
import UserService from "../../../services/api/userService";
import UserProfile from "../../common/UserDetails/UserProfile";
import withUser from '../../hoc/withUser';
import {CREATED, OK} from "../../../services/api/apiConstants";
import ErrorContent from "../../common/ErrorContent/ErrorContent";


class GameDetailsPage extends Component {
    state = {
        visitedUser: null,
        loading: true,
        error: false,
        status: null,
    };

    componentWillMount() {
        UserService.getUserById(this.props.match.params.id)
            .then((data) => {
                let findError = null;
                if (data.status && data.status !== OK && data.status !== CREATED) {
                    findError = data.status;
                }
                if(findError) {
                    this.setState({
                        loading: false,
                        error: true,
                        status: findError,
                    });
                }
                else {
                    this.setState({
                        visitedUser: data,
                        loading: false,
                    });
                }
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
        if(this.state.error) {
            return <ErrorContent status={this.state.status}/>
        }
        return (
            <React.Fragment>
                <HelmetProvider>
                    <Helmet>
                        <title>{this.state.visitedUser.username} - QuestLog</title>
                    </Helmet>
                </HelmetProvider>
                <UserProfile visitedUser={this.state.visitedUser}/>
            </React.Fragment>
        );
    }
}

export default withUser(GameDetailsPage);
