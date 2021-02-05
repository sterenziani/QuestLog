import React, { Component } from 'react';
import { Translation } from 'react-i18next';
import { Formik } from 'formik';
import * as Yup from 'yup';

import FormikTextField from '../../common/Forms/FormikTextField';
import AuthForm from '../../common/Forms/AuthForm';
import AnyButton from '../../common/AnyButton/AnyButton';
import AuthService from '../../../services/api/authService';
import withRedirect from '../../hoc/withRedirect';
import { CREATED, OK, CONFLICT } from '../../../services/api/apiConstants';
import UserService from '../../../services/api/userService';
import i18n from '../../../services/i18n';
import withUser from '../../hoc/withUser';
import { withTranslation } from 'react-i18next';
import {Helmet, HelmetProvider} from "react-helmet-async";

const SignUpSchema = Yup.object().shape({
    username        : Yup
        .string()
        .matches(/^[a-zA-Z0-9]+$/, 'signup.username.errors.illegal_characters')
        .min(6, 'signup.username.errors.required_length')
        .max(100, 'signup.username.errors.max_length')
        .required('login.username.errors.is_required'),
    password        : Yup
        .string()
        .min(6, 'signup.password.errors.required_length')
        .max(100, 'signup.password.errors.max_length')
        .required('login.password.errors.is_required'),
    repeat_password : Yup
        .string()
        .when('password', (password, schema) => {
            return schema.test({
              test: repeat_password => !!password && repeat_password === password,
              message: 'signup.repeat_password.errors.passwords_dont_match'
            })
          })
        .required('signup.repeat_password.errors.is_required'),
    email           : Yup
        .string()
        .max(100, 'signup.email.errors.max_length')
        .email('signup.email.errors.invalid_email')
        .required('signup.email.errors.is_required')
})

class SignUpPage extends Component {
    state = {
        bad_connection      : false
    }
    register     = async (values, setSubmitting, setFieldError) => {
        const { status, conflicts } = await UserService.register(values.username, values.password, values.email, i18n.language)

        switch(status){

            case CREATED:
                this.authenticate(values)
                break;

            case CONFLICT:
                setSubmitting(false)
                conflicts.forEach(conflict => {
                    setFieldError(conflict.field, conflict.i18Key)
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
    authenticate = async (values) => {
        const { status } = await AuthService.logIn(values.username, values.password)

        switch(status){

            case OK:
                this.props.activateGoBack()
                break;

            default:
                this.props.loginFailed()
                this.props.activateRedirect("login")
                break;
        }

    }
    onSubmit = (values, { setSubmitting, setFieldError }) => {
        setSubmitting(true);
        this.register(values, setSubmitting, setFieldError);
    }

    componentDidMount() {
        const { t } = this.props
        document.title = t(`signup.title`) +" - QuestLog";
    }

    render() {
        const { t } = this.props
        return (
            <React.Fragment>
                <HelmetProvider>
                    <Helmet>
                        <title>{t(`signup.title`)} - QuestLog</title>
                    </Helmet>
                </HelmetProvider>
            <Formik
                initialValues = {{
                        username        : '',
                        password        : '',
                        repeat_password : '',
                        email           : ''
                    }
                }
                validationSchema={ SignUpSchema }
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
                    titleKey="signup.title"
                    onSubmit={ handleSubmit }
                >
                    {
                        this.state.bad_connection &&
                            <p className="form-error">
                                <Translation>
                                {
                                    t => t("signup.bad_connection")
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
                    <FormikTextField
                        type="password"
                        label="signup.repeat_password.label"
                        name="repeat_password"
                        placeholder="signup.repeat_password.placeholder"
                        value={ values.repeat_password }
                        error={ errors.repeat_password }
                        touched={ touched.repeat_password }
                        onChange={ handleChange }
                        onBlur={ handleBlur }
                    />
                    <FormikTextField
                        label="signup.email.label"
                        name="email"
                        placeholder="signup.email.placeholder"
                        value={ values.email }
                        error={ errors.email }
                        touched={ touched.email }
                        onChange={ handleChange }
                        onBlur={ handleBlur }
                    />
                    <AnyButton
                        variant="dark"
                        type="submit"
                        textKey="signup.signup"
                        disabled={ isSubmitting }
                    />
                    <AnyButton
                        variant="dark"
                        textKey="signup.login"
                        href="/login"
                    />
                </AuthForm>
            )}
            </Formik>
            </React.Fragment>
        );
    }
}

export default withTranslation() (withUser(withRedirect(SignUpPage, { login : "/login" }), { visibility : "anonymousOnly" }));
