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

const LogInSchema = Yup.object().shape({
    username : Yup
        .string()
        .required('login.username.errors.is_required'),
    password : Yup
        .string()
        .required('login.password.errors.is_required')
})

class LogInPage extends Component {
    state = { }
    onSubmit = (values, { setSubmitting }) => {
        console.log(values);
        setSubmitting(false);
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

export default LogInPage;