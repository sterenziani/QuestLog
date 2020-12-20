import React, { Component } from 'react';

class GameDetailsPage extends Component {
    state = {  }
    render() { 
        return ( 
            <p>{this.props.match.params.id}</p>
        );
    }
}
 
export default GameDetailsPage;