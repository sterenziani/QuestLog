import React, { Component } from 'react';
import {Form, Button, Container} from 'react-bootstrap';
import {Translation} from 'react-i18next';
import UserService from '../../../services/api/userService';
import { CREATED, NOT_FOUND } from '../../../services/api/apiConstants';
import withRedirect from '../../hoc/withRedirect';
import withUser from '../../hoc/withUser';
import { withTranslation } from 'react-i18next';
import {Helmet, HelmetProvider} from "react-helmet-async";

class RequestTokenPage extends Component {
    state = {
        email: '',
        correct: true,
        submitting: false,
        finished: false,
        bad_connection : false,
        email_not_found: false,
    }

    onKeyUp = (e) => {
        if (e.charCode === 13) {
            this.submitHandler(e);
        }
    }

    submitHandler = (e) => {
        const res = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        if(!res.test(String(this.state.email).toLowerCase())) {
            this.setState({correct: false});
            return;
        }
        this.setState({correct: true, submitting: true, email_not_found: false, bad_connection : false});
        UserService.requestPasswordChangeToken(this.state.email).then((data) => {
            if(data && data.status == CREATED){
                this.setState({correct: true, submitting: true, email_not_found: false, bad_connection : false, finished: true});
            }
            else if(data && data.status == NOT_FOUND){
                this.setState({correct: true, submitting: false, email_not_found: true, bad_connection : false});
            }
            else{
                this.setState({correct: true, submitting: false, email_not_found: false, bad_connection : true});
            }
        });
    }

    handleChange = (e) => {
        this.setState({email: e.target.value});
    }

    render() {
        const { t } = this.props
        return (
            <React.Fragment>
                <HelmetProvider>
                    <Helmet>
                        <title>{t(`forgotPassword.helmet`)} - QuestLog</title>
                    </Helmet>
                </HelmetProvider>

                <Container className="text-center align-middle">
                    <div className="my-5 py-5 bg-light border-bottom border-primary rounded-lg">
                        <h2 className="share-tech-mono">
                            <Translation>{t => t("forgotPassword.title")}</Translation>
                        </h2>
                        {
                            this.state.finished? [<p className="px-5 pt-3"><Translation>{t => t("forgotPassword.emailSent")}</Translation></p>] : [
                                <>
                                <p><Translation>{t => t("forgotPassword.subtitle")}</Translation></p>
                                {
                                    !this.state.correct &&
                                        <p className="form-error">
                                            <Translation>{t => t("forgotPassword.wrongEmail")}</Translation>
                                        </p>
                                }
                                {
                                    this.state.email_not_found &&
                                        <p className="form-error">
                                            <Translation>{t => t("forgotPassword.emailNotFound")}</Translation>
                                        </p>
                                }
                                {
                                    this.state.bad_connection &&
                                        <p className="form-error">
                                            <Translation>{t => t("login.bad_connection")}</Translation>
                                        </p>
                                }
                                <Form.Group className="w-50 m-auto">
                                    <Form.Label><Translation>{t => t("forgotPassword.fieldName")}</Translation></Form.Label>
                                    <Form.Control onKeyPress={this.onKeyUp.bind(this)} onChange={(e) => this.handleChange(e)} type="email" placeholder={"example@questlog.com"} />
                                </Form.Group>
                                <Button className="mt-3" variant="dark" onClick={(e) => this.submitHandler(e)} disabled={this.state.submitting? 'disabled' : ''}>
                                    <Translation>{t => t("forgotPassword.send")}</Translation>
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

export default withTranslation() (withUser(withRedirect(RequestTokenPage, { home : "/" }), { visibility : "anonymousOnly"}));
