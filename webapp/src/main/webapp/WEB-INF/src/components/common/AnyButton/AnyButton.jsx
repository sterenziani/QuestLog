import React from 'react';
import {
    Button
} from 'react-bootstrap';
import {
    LinkContainer
} from 'react-router-bootstrap';
import { Translation } from 'react-i18next';

const AnyButton = props => {
    const { href, textKey, ...other } = props
    if(href){
        return (
            <LinkContainer to={ href }>
                <Button 
                    { ...other }
                >
                    <Translation>
                    {
                        t => t(`${textKey}`)
                    }
                    </Translation>
                </Button>
            </LinkContainer>
        )
    }
    return (
        <Button 
            { ...other }
        >
            <Translation>
            {
                t => t(`${textKey}`)
            }
            </Translation>
        </Button>
    );
}
 
export default AnyButton;