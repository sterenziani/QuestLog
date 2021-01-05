import React, { Component } from 'react';
import { Helmet } from 'react-helmet';
import GameListItem from '../../common/GameListItem/GameListItem';

class IndexPage extends Component {
    state = {  }
    render() { 
        return (  
            <React.Fragment>
                <Helmet>
                    <title>QuestLog</title>
                </Helmet>
                <GameListItem>
                    
                </GameListItem>
            </React.Fragment>
        );
    }
}
 
export default IndexPage;