import React from 'react';
import { Form } from 'react-bootstrap';
import { Translation } from 'react-i18next';

const AuthForm = props => {
    const { titleKey, children, ...other } = props
    return (
        <div className="container text-center align-middle">
            <div className="my-5 py-5 bg-light border-bottom border-primary rounded-lg">
                <Form.Group>
                    <h2 className="share-tech-mono">
                        <Translation>
                        {
                            t => t(`${titleKey}`)
                        }
                        </Translation>
                    </h2>
                </Form.Group>

                <Form {...other} >
                {
                    React.Children.map(children, child => <Form.Group>{child}</Form.Group>)
                }
                </Form>
            </div>
        </div>
    )
}

export default AuthForm;
