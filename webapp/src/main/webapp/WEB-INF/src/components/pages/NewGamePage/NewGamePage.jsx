import React, { Component } from 'react';

import withUser from '../../hoc/withUser';

class NewGamePage extends Component {
    state = {  }
    render() { 
        return (  
            <p>New Game</p>
        );
    }
}
 
export default withUser(NewGamePage, { visibility: "adminOnly" });