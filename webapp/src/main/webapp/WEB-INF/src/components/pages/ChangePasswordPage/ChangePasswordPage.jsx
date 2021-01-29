import React, { Component } from 'react';
import {Form, Button} from 'react-bootstrap';
import {Translation} from 'react-i18next';
import AuthForm from '../../common/Forms/AuthForm';
import AnyButton from '../../common/AnyButton/AnyButton';
import AuthService from '../../../services/api/authService';
import UserService from '../../../services/api/userService';
import { OK, UNAUTHORIZED, CREATED, NOT_FOUND, FORBIDDEN } from '../../../services/api/apiConstants';
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

    authenticate = async() => {
        const { status } = await AuthService.logIn(this.state.token.user.username, this.state.pass1)
        switch(status){
            case OK:
                window.location.href = "";
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
        const resp = UserService.changePassword(this.state.token.user.id, this.state.token.token, this.state.pass1).then((data) => {
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

                <AuthForm titleKey="forgotPassword.changePassword">
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
                                <Form.Control type="password" onChange={(e) => this.handleChange(e, 1)} placeholder={t => t("forgotPassword.password")} />
                            </Form.Group>
                            <div class="m-3"></div>
                            <Form.Group className="w-50 m-auto">
                                <Form.Label><Translation>{t => t("forgotPassword.repeatPassword")}</Translation></Form.Label>
                                <Form.Control type="password" onChange={(e) => this.handleChange(e, 2)} placeholder={t => t("forgotPassword.repeatPassword")} />
                            </Form.Group>
                            <Button className="mt-4" variant="dark" onClick={(e) => this.submitHandler(e)} disabled={this.state.submitting? 'disabled' : ''}>
                                <Translation>{t => t("forgotPassword.save")}</Translation>
                            </Button>
                            </>
                        ]
                }
                </AuthForm>
            </React.Fragment>
        );
    }
}

export default withTranslation() (withQuery(withUser(withRedirect(ChangePasswordPage, { home : "/" }), { visibility : "anonymousOnly"})));
