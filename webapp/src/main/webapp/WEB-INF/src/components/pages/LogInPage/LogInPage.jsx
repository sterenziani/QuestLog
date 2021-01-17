import React, { Component } from 'react';
import { 
    Form 
} from 'react-bootstrap'; 
import { 
    Translation 
} from 'react-i18next';
import { 
    Formik 
} from 'formik';
import * as Yup from 'yup';

import FormikTextField from '../../common/Forms/FormikTextField';
import AuthForm from '../../common/Forms/AuthForm';
import AnyButton from '../../common/AnyButton/AnyButton';
import AuthService from '../../../services/api/authService';
import withRedirect from '../../hoc/withRedirect';
import { OK, UNAUTHORIZED } from '../../../services/api/apiConstants';

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
        bad_connection : false
    }
    authenticate = async (values, setSubmitting) => {
        const { status } = await AuthService.logIn(values.username, values.password)

        console.log(status);

        switch(status){

            case OK:
                this.props.activateRedirect()
                break;

            case UNAUTHORIZED:
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
        setSubmitting(true);
        this.authenticate(values, setSubmitting);
    }
    render() {
        return ( 
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
                    <Translation>
                    {
                        t => <Form.Check type="checkbox" label={t('login.remember-me')} />
                    }
                    </Translation>
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
        );
    }
}

export default withRedirect(LogInPage, "/");