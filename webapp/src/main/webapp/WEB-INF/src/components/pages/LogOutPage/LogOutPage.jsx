import { Redirect } from "react-router-dom";
import React from 'react';
import AuthService from '../../../services/api/authService';

import withRedirect from '../../hoc/withRedirect';

const LogOutPage = (props) => {
    AuthService.logOut()
    props.activateGoBack()
    return <Redirect to="/"/>
}

export default withRedirect(LogOutPage);