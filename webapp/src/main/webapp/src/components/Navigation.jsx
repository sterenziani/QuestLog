import React, { Component } from 'react';
import { 
    Navbar, 
    Nav
} from 'react-bootstrap';
import questlogLogo from '../images/questlog-logo.png';

class Navigation extends Component {
    state = {  }
    render() { 
        return (
            <Navbar 
                bg="dark" 
                variant="dark"
                sticky="top"
            >
                <Navbar.Brand href="#home">
                <img
                    src={questlogLogo}
                    className="d-inline-block align-top"
                    alt="QuestLog"
                />
                </Navbar.Brand>
                <Nav className="mr-auto">
                    
                </Nav>
            </Navbar>
         );
    }
}
 
export default Navigation;