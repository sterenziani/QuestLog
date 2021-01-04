import React, { Component } from 'react';
import { Helmet } from 'react-helmet';

class GameDetailsPage extends Component {
    state = {  }
    render() { 
        return ( 
            <React.Fragment>
                <Helmet>
                    <title>QuestLog - Game Details</title>
                </Helmet>
                <p>{this.props.match.params.id}</p>
            </React.Fragment>
        );
    }
}
 
export default GameDetailsPage;