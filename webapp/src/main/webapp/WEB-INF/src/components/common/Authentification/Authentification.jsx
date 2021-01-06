import React, { Component } from 'react';
import {
    Button
} from 'react-bootstrap';
import {
    LinkContainer
} from 'react-router-bootstrap';
import { Translation } from 'react-i18next';

class Authentification extends Component {
    state = {  }
    render() { 
        return ( 
            <React.Fragment>
                <LinkContainer to="/login">
                    <Button
                        variant="outline-secondary"
                        className="mr-3"
                    >
                        <Translation>
                        {
                            t => t('navigation.auth.login')
                        }
                        </Translation>
                    </Button>
                </LinkContainer>
                <LinkContainer to="signup">
                    <Button
                        variant="outline-secondary"
                    >
                        <Translation>
                        {
                            t => t('navigation.auth.signup')
                        }
                        </Translation>
                    </Button>
                </LinkContainer>
            </React.Fragment>
        );
    }
}
 
export default Authentification;