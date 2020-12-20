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
                className="d-flex"
            >
                <Navbar.Brand as={ Link } to="/">
                    <img
                        src={ questlogLogo }
                        className="d-inline-block align-top"
                        alt="QuestLog"
                    />
                </Navbar.Brand>
                <Nav className="mr-auto flex-grow-1 d-flex">
                    <Search className="flex-grow-1"/>
                    <Explore />
                    <Authentification />
                </Nav>
            </Navbar>
         );
    }
}
 
export default Navigation;