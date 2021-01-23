import React, { Component } from 'react';
import { withTranslation } from 'react-i18next';

import withUser from '../../hoc/withUser';
import {Helmet, HelmetProvider} from "react-helmet-async";

class NewGamePage extends Component {
    state = {  }
    render() { 
        return (
            <React.Fragment>
                <HelmetProvider>
                    <Helmet>
                        <title>QuestLog</title>
                    </Helmet>
                </HelmetProvider>
            </React.Fragment>
        );
    }
}
 
export default withTranslation() (withUser(NewGamePage, { visibility: "adminOnly" }));