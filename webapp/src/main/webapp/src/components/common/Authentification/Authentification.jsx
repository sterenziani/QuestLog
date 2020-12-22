import React, { Component } from 'react';
import {
    Button
} from 'react-bootstrap';

class Authentification extends Component {
    state = {  }
    render() { 
        return ( 
            <React.Fragment>
                <Button
                    variant="outline-secondary"
                    className="mr-3"
                >
                    Log In
                </Button>
                <Button
                    variant="outline-secondary"
                >
                    Sign Up
                </Button>
            </React.Fragment>
        );
    }
}
 
export default Authentification;