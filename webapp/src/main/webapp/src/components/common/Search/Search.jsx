import React, { Component } from 'react';
import {
    Form,
    FormControl,
    Button
} from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSearch } from '@fortawesome/free-solid-svg-icons';

class Search extends Component {
    
    state = { 
        className : this.props.className + " d-flex no-lineheight"
    }

    render() {
        return (  
            <Form className={ this.state.className } inline>
                <FormControl type="text" placeholder="Search" className="mr-sm-2 flex-grow-1" />
                <Form.Control className="btn btn-dark mr-sm-2" as="select" defaultValue="Game">
                    <option>Game</option>
                    <option>User</option>
                </Form.Control>
                <Button variant="dark">
                    <FontAwesomeIcon className="mr-sm-2" icon={ faSearch }/>
                    Search
                </Button>
            </Form>
        );
    }
}
 
export default Search;