import React, { Component } from 'react';
import {
    Form,
    Button
} from 'react-bootstrap';
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
                        <h2>Log In</h2>
                        <Form onSubmit={ handleSubmit }>
                            <Form.Group controlId="formUsername">
                                <Form.Label>Username</Form.Label>
                                <Form.Control
                                    placeholder="Username" 
                                    name="username"
                                    onChange={ handleChange }
                                    onBlur={ handleBlur }
                                    value={ values.username }
                                />
                            </Form.Group>
                            { errors.username && touched.username && errors.username && (
                                <Translation>
                                {
                                    t => (
                                        <p className="form-error">{t(`${errors.username}`)}</p>
                                    )
                                }
                                </Translation>
                                
                            )}
                            
                            <Form.Group controlId="formPassword">
                                <Form.Label>Password</Form.Label>
                                <Form.Control 
                                    type="password" 
                                    placeholder="Password" 
                                    name="password"
                                    onChange={ handleChange }
                                    onBlur={ handleBlur }
                                    value={ values.password }
                                />
                            </Form.Group>
                            { errors.password && touched.password && errors.password && (
                                <Translation>
                                {
                                    t => (
                                        <p className="form-error">{t(`${errors.password}`)}</p>
                                    )
                                }
                                </Translation>
                                
                            )}
                            <Form.Group controlId="formRememberMe">
                                <Form.Check type="checkbox" label="Remember Me" />
                            </Form.Group>
                            <Button 
                                variant="primary" 
                                type="submit"
                                disabled={ !touched.username || 
                                    !touched.password || 
                                    errors.username || 
                                    errors.password || 
                                    isSubmitting 
                                }
                            >
                                Submit
                            </Button>
                        </Form>
                    </div>
                </div>
            )} 
            </Formik>
        );
    }
}
 
export default LogInPage;