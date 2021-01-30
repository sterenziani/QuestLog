import React, { Component } from 'react';
import { Helmet, HelmetProvider } from 'react-helmet-async';
import {Translation} from "react-i18next";
import skull from './images/skull.png';


class ErrorContent extends Component {
    state = {
        status: this.props.status,
    };

    render() {
        const body = "error." + this.state.status;
        return (
            <React.Fragment>
                <HelmetProvider>
                    <Helmet>
                        <title>Error - QuestLog</title>
                    </Helmet>
                </HelmetProvider>
                <div className="col justify-content-center align-items-center text-center">
                    <h1 style={{fontSize: "8em"}} className="share-tech-mono text-center">
                        {this.state.status}
                    </h1>
                    <img src={ skull } alt="Skull logo" className="text-center" style={{height: "30em", width:"30em", margin: "auto"}}/>
                    <h1 style={{fontSize: "4em"}} className="share-tech-mono text-center">
                        <Translation>{t => t(body)}</Translation>
                    </h1>
                </div>
            </React.Fragment>
        );
    }
}
export default ErrorContent;
