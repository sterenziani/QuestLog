import React, { Component } from 'react';
import {
    Button
} from 'react-bootstrap';
import {
    LinkContainer
} from 'react-router-bootstrap';
import { Translation } from 'react-i18next';

class Explore extends Component {
    state = {  }
    render() { 
        return ( 
            <LinkContainer to="/explore">
                <Button 
                    variant="dark"
                    className={ this.props.className }
                >
                    <Translation>
                    {
                        t => t('navigation.explore')
                    }
                    </Translation>
                </Button>
            </LinkContainer>
        );
    }
}
 
export default Explore;