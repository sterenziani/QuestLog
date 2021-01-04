import React, { Component } from 'react';
import {
    Button
} from 'react-bootstrap';
import { Translation } from 'react-i18next';

class Authentification extends Component {
    state = {  }
    render() { 
        return ( 
            <React.Fragment>
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
                <Button
                    variant="outline-secondary"
                >
                    <Translation>
                    {
                        t => t('navigation.auth.signup')
                    }
                    </Translation>
                </Button>
            </React.Fragment>
        );
    }
}
 
export default Authentification;