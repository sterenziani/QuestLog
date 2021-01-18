import { Redirect } from "react-router-dom";
import React from 'react';
import AuthService from '../../../services/api/authService';

const LogOutPage = () => {
    AuthService.logOut()
    return <Redirect to="/"/>
}

export default LogOutPage;