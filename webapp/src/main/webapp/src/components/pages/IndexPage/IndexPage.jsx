import React, { Component } from 'react';
import { Helmet } from 'react-helmet';

class IndexPage extends Component {
    state = {  }
    render() { 
        return (  
            <React.Fragment>
                <Helmet>
                    <title>QuestLog</title>
                </Helmet>
                <h1>Welcome!</h1>
            </React.Fragment>
        );
    }
}
 
export default IndexPage;