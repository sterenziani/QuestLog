import React, { Component } from 'react';
import {
    Form,
    Button
} from 'react-bootstrap';
import {
    LinkContainer
} from 'react-router-bootstrap';
import { Formik } from 'formik';
import * as Yup from 'yup';
import { Translation } from 'react-i18next';

const LogInSchema = Yup.object().shape({
    username : Yup.string()
        .required('login.username.errors.is_required'),
    password : Yup.string()
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
                <div
                    className="container text-center align-middle"
                >
                    <div
                        className="my-5 py-5 bg-light border-bottom border-primary rounded-lg"
                    >
                        <Form.Group>
                            <h2 className="share-tech-mono">
                                <Translation>
                                {
                                    t => t('login.title')
                                }
                                </Translation>
                            </h2>
                        </Form.Group>
                        
                        <Form onSubmit={ handleSubmit }>
                            <Form.Group controlId="formUsername">
                                <Form.Label>
                                    <Translation>
                                    {
                                        t => t('login.username.label')
                                    }
                                    </Translation>
                                    <Form.Control
                                        placeholder="Username" 
                                        name="username"
                                        onChange={ handleChange }
                                        onBlur={ handleBlur }
                                        value={ values.username }
                                    />
                                    <p className="form-error">
                                    { errors.username && touched.username && errors.username && (
                                        <Translation>
                                        {
                                            t => t(`${errors.username}`)
                                        }
                                        </Translation>
                                    )}
                                    </p>
                                </Form.Label>
                            </Form.Group>
                            <Form.Group controlId="formPassword">
                                <Form.Label>
                                    <Translation>
                                    {
                                        t => t('login.password.label')
                                    }
                                    </Translation>
                                    <Form.Control 
                                        type="password" 
                                        placeholder="Password" 
                                        name="password"
                                        onChange={ handleChange }
                                        onBlur={ handleBlur }
                                        value={ values.password }
                                    />
                                    <p className="form-error">
                                    { errors.password && touched.password && errors.password && (
                                        <Translation>
                                        {
                                            t => t(`${errors.password}`)
                                        }
                                        </Translation>
                                    )}
                                    </p>
                                </Form.Label>
                            </Form.Group>
                            <Form.Group controlId="formRememberMe">
                                <Translation>
                                {
                                    t => <Form.Check type="checkbox" label={t('login.remember-me')} />
                                }
                                </Translation>
                                
                            </Form.Group>
                            <Form.Group>
                                <Button 
                                    variant="dark" 
                                    type="submit"
                                    disabled={ 
                                        isSubmitting 
                                    }
                                >
                                    <Translation>
                                    {
                                        t => t('login.login')
                                    }
                                    </Translation>
                                </Button>
                            </Form.Group>
                            <Form.Group>
                                <LinkContainer to="/signup">
                                    <Button 
                                        variant="dark"
                                        disabled={ 
                                            isSubmitting 
                                        }
                                    >
                                        <Translation>
                                        {
                                            t => t('login.signup')
                                        }
                                        </Translation>
                                    </Button>
                                </LinkContainer>
                            </Form.Group>
                            <Form.Group>
                                <LinkContainer to="/forgotPassword">
                                    <Button 
                                        variant="link"
                                        disabled={ 
                                            isSubmitting 
                                        }
                                    >
                                        <Translation>
                                        {
                                            t => t('login.forgot')
                                        }
                                        </Translation>
                                    </Button>
                                </LinkContainer>
                            </Form.Group>
                        </Form>
                    </div>
                </div>
            )} 
            </Formik>
        );
    }
}
 
export default LogInPage;