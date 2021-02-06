import React, { Component } from 'react';
import {Helmet, HelmetProvider} from "react-helmet-async";
import { Translation } from 'react-i18next';
import { Formik } from 'formik';
import * as Yup from 'yup';
import FormikTextField from '../../common/Forms/FormikTextField';
import AuthForm from '../../common/Forms/AuthForm';
import AnyButton from '../../common/AnyButton/AnyButton';
import AuthService from '../../../services/api/authService';
import { OK, UNAUTHORIZED } from '../../../services/api/apiConstants';
import withRedirect from '../../hoc/withRedirect';
import withUser from '../../hoc/withUser';
import { withTranslation } from 'react-i18next';


const LogInSchema = Yup.object().shape({
    username : Yup
        .string()
        .required('login.username.errors.is_required'),
    password : Yup
        .string()
        .required('login.password.errors.is_required')
})

class LogInPage extends Component {
    state = {
        correct        : true,
        bad_connection : false,
        failed_external_login : this.props.loginFailed
    }
    authenticate = async (values, setSubmitting) => {
        const { status } = await AuthService.logIn(values.username, values.password)

        switch(status){

            case OK:
                this.props.activateGoBack()
                break;

            case UNAUTHORIZED:
                setSubmitting(false)
                this.setState({
                    correct : false
                })
                break;

            default:
                setSubmitting(false)
                this.setState({
                    bad_connection : true
                })
                break;
        }

    }
    onSubmit = (values, { setSubmitting }) => {
        if(this.state.failed_external_login){
            this.props.loginFailedProcessed()
            this.setState({
                failed_external_login : false
            })
        }

        setSubmitting(true);
        this.authenticate(values, setSubmitting);
    }

    componentDidMount() {
        const { t } = this.props
        document.title = t(`login.title`) +" - QuestLog";
    }

    render() {
        const { t } = this.props
        return (
            <React.Fragment>
                <HelmetProvider>
                    <Helmet>
                        <title>{t(`login.title`)} - QuestLog</title>
                    </Helmet>
                </HelmetProvider>
            <Formik
                initialValues = {{
                        username : '',
                        password : ''
                    }
                }
                validationSchema={ LogInSchema }
                onSubmit = { this.onSubmit }
            >
            {({
                values,
                errors,
                touched,
                handleChange,
                handleBlur,
                handleSubmit,
                isSubmitting
            }) => (
                <AuthForm
                    titleKey="login.title"
                    onSubmit={ handleSubmit }
                >
                    {
                        !this.state.correct &&
                            <p className="form-error">
                                <Translation>
                                {
                                    t => t("login.error")
                                }
                                </Translation>
                            </p>
                    }
                    {
                        this.state.bad_connection &&
                            <p className="form-error">
                                <Translation>
                                {
                                    t => t("login.bad_connection")
                                }
                                </Translation>
                            </p>
                    }
                    {
                        this.state.failed_external_login &&
                            <p className="form-error">
                                <Translation>
                                {
                                    t => t("login.external_login_failed")
                                }
                                </Translation>
                            </p>
                    }
                    <FormikTextField
                        label="login.username.label"
                        name="username"
                        placeholder="login.username.placeholder"
                        value={ values.username }
                        error={ errors.username }
                        touched={ touched.username }
                        onChange={ handleChange }
                        onBlur={ handleBlur }
                    />
                    <FormikTextField
                        type="password"
                        label="login.password.label"
                        name="password"
                        placeholder="login.password.placeholder"
                        value={ values.password }
                        error={ errors.password }
                        touched={ touched.password }
                        onChange={ handleChange }
                        onBlur={ handleBlur }
                    />
                    <AnyButton
                        variant="dark"
                        type="submit"
                        textKey="login.login"
                        disabled={ isSubmitting }
                    />
                    <AnyButton
                        variant="dark"
                        textKey="login.signup"
                        href="/signup"
                    />
                    <AnyButton
                        variant="link"
                        textKey="login.forgot"
                        href="/forgotPassword"
                    />
                </AuthForm>
            )}
            </Formik>
            </React.Fragment>
        );
    }
}

export default withTranslation() (withUser(withRedirect(LogInPage, { home : "/" }), { visibility : "anonymousOnly"}));
