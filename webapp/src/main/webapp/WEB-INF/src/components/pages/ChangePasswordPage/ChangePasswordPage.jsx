import React, { Component } from 'react';
import {Form, Button, Container} from 'react-bootstrap';
import {Translation} from 'react-i18next';
import AuthService from '../../../services/api/authService';
import UserService from '../../../services/api/userService';
import { OK, UNAUTHORIZED, NOT_FOUND, FORBIDDEN } from '../../../services/api/apiConstants';
import withRedirect from '../../hoc/withRedirect';
import withUser from '../../hoc/withUser';
import withQuery from '../../hoc/withQuery';
import { withTranslation } from 'react-i18next';
import {Helmet, HelmetProvider} from "react-helmet-async";

class ChangePasswordPage extends Component {
    state = {
        pass1: '',
        pass2: '',
        correct: true,
        match: true,
        submitting: false,
        bad_connection : false,
        invalid_token: false,
        token: null
    }

    onKeyUp = (e) => {
        if (e.charCode === 13) {
            this.submitHandler(e);
            return false;
        }
    }

    authenticate = async() => {
        const { status } = await AuthService.logIn(this.state.token.user.username, this.state.pass1)
        switch(status){
            case OK:
                this.props.activateRedirect("home")
                break;

            case UNAUTHORIZED:
                this.setState({
                    unknown_error : true
                })
                break;

            default:
                this.setState({
                    bad_connection : true
                })
                break;
        }
    }

    submitHandler = (e) => {
        this.setState({correct: true, match: true, submitting: false, bad_connection : false, invalid_token: false});
        if(this.state.pass1.length < 6 || this.state.pass1.length < 6) {
            this.setState({correct: false});
            return;
        }
        if(this.state.pass1 != this.state.pass2) {
            this.setState({match: false});
            return;
        }
        this.setState({submitting: true});
        UserService.changePassword(this.state.token.user.id, this.state.token.token, this.state.pass1).then((data) => {
            if(data && data.status == OK){
                this.authenticate();
            }
            else if(data && data.status == NOT_FOUND){
                this.setState({correct: true, submitting: false, invalid_token: true, bad_connection : false});
            }
            else if(data && data.status == FORBIDDEN){
                this.setState({correct: true, submitting: false, invalid_token: true, bad_connection : false});
            }
            else{
                this.setState({correct: true, submitting: false, invalid_token: false, bad_connection : true});
            }
        });
    }

    handleChange = (e, field) => {
        if(field == 1)
            this.setState({pass1: e.target.value});
        else
            this.setState({pass2: e.target.value});
    }

    componentWillMount() {
        let token = this.props.query.get("token");
        if(!token){
            this.setState({token: null, invalid_token: true});
            return;
        }
        UserService.getToken(token).then((resp) => {
            if(resp.status != OK){
                this.setState({token: resp.data, invalid_token: true});
            }
            else{
                this.setState({token: resp.data});
            }
        });
    }

    render() {
        const { t } = this.props
        return (
            <React.Fragment>
                <HelmetProvider>
                    <Helmet>
                        <title>{t(`forgotPassword.changePassword`)} - QuestLog</title>
                    </Helmet>
                </HelmetProvider>

                <Container className="text-center align-middle">
                    <div className="my-5 py-5 bg-light border-bottom border-primary rounded-lg">
                        <h2 className="share-tech-mono">
                            <Translation>{t => t("forgotPassword.changePassword")}</Translation>
                        </h2>
                        {
                            this.state.invalid_token? [
                                <p className="mx-5 mt-4"><Translation>{t => t("forgotPassword.invalidToken")}</Translation></p>] : [
                                    <>
                                    <p><Translation>{t => t("forgotPassword.changePasswordSubtitle")}</Translation></p>
                                    {
                                        !this.state.correct &&
                                            <p className="form-error my-3">
                                                <Translation>{t => t("forgotPassword.passwordTooShort")}</Translation>
                                            </p>
                                    }
                                    {
                                        !this.state.match &&
                                            <p className="form-error my-3">
                                                <Translation>{t => t("forgotPassword.passwordsDontMatch")}</Translation>
                                            </p>
                                    }
                                    {
                                        this.state.bad_connection &&
                                            <p className="form-error my-3">
                                                <Translation>{t => t("login.bad_connection")}</Translation>
                                            </p>
                                    }
                                    {
                                        this.state.unknown_error &&
                                            <p className="form-error my-3">
                                                <Translation>{t => t("AyuUUUUUda")}</Translation>
                                            </p>
                                    }
                                    <Form.Group className="w-50 m-auto">
                                        <Form.Label><Translation>{t => t("forgotPassword.password")}</Translation></Form.Label>
                                        <Form.Control onKeyPress={this.onKeyUp.bind(this)} type="password" onChange={(e) => this.handleChange(e, 1)} placeholder={t => t("forgotPassword.password")} />
                                    </Form.Group>
                                    <div class="m-3"></div>
                                    <Form.Group className="w-50 m-auto">
                                        <Form.Label><Translation>{t => t("forgotPassword.repeatPassword")}</Translation></Form.Label>
                                        <Form.Control onKeyPress={this.onKeyUp.bind(this)} type="password" onChange={(e) => this.handleChange(e, 2)} placeholder={t => t("forgotPassword.repeatPassword")} />
                                    </Form.Group>
                                    <Button className="mt-4" variant="dark" onClick={(e) => this.submitHandler(e)} disabled={this.state.submitting? 'disabled' : ''}>
                                        <Translation>{t => t("forgotPassword.save")}</Translation>
                                    </Button>
                                    </>
                                ]
                        }
                    </div>
                </Container>
            </React.Fragment>
        );
    }
}

export default withTranslation() (withQuery(withUser(withRedirect(ChangePasswordPage, { home : "/" }), { visibility : "anonymousOnly"})));
