import React, { Component } from 'react';
import {
    Button
} from 'react-bootstrap';
import {
    LinkContainer
} from 'react-router-bootstrap';

class Explore extends Component {
    state = {  }
    render() { 
        return ( 
            <LinkContainer to="/explore">
                <Button 
                    variant="dark"
                >
                    Explore
                </Button>
            </LinkContainer>
        );
    }
}
 
export default Explore;