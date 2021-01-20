import React, { Component } from 'react';
import { Translation } from 'react-i18next';
import { Button } from 'react-bootstrap';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPlus } from '@fortawesome/free-solid-svg-icons';
import { LinkContainer } from 'react-router-bootstrap';

class CreateGame extends Component {
    state = {  }
    render() {
        return (
            <Translation>
            {
                t => (
                    <LinkContainer to="/admin/game/new">
                        <Button variant="success" {...this.props}>
                            <FontAwesomeIcon className="mr-sm-2" icon={ faPlus }/>{t('navigation.addGame')}
                        </Button>
                    </LinkContainer>
                )
            }
            </Translation>
        );
    }
}
 
export default CreateGame;