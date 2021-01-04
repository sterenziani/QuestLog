//Libraries
import React, { Component } from 'react';
import { 
    Navbar, 
    Nav
} from 'react-bootstrap';
import {
    Link
} from 'react-router-dom';

//Component resources
import questlogLogo from './images/questlog-logo.png';

//Child components
import Search from '../Search/Search';
import Explore from '../Explore/Explore';
import Authentification from '../Authentification/Authentification';


class Navigation extends Component {
    state = {  }
    render() { 
        return (
            <Navbar 
                bg="primary" 
                variant="dark"
                sticky="top"
                className="d-flex no-lineheight"
            >
                <Navbar.Brand as={ Link } to="/">
                    <img
                        src={ questlogLogo }
                        className="d-inline-block align-top mr-4 mb-2"
                        alt="QuestLog"
                    />
                </Navbar.Brand>
                <Nav className="mr-auto flex-grow-1 d-flex my-3">
                    <Search className="flex-grow-1 mr-4"/>
                    <Explore className="mr-5" />
                    <Authentification />
                </Nav>
            </Navbar>
         );
    }
}
 
export default Navigation;