import React, { Component } from 'react';
import { Helmet, HelmetProvider } from 'react-helmet-async';
import {Col, Container} from "react-bootstrap";
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
                <div className="container text-center align-middle">
                    <div className="my-5 p-5 bg-light border-bottom border-primary rounded-lg">
                        <h2 style={{fontSize: "8em"}} className="share-tech-mono text-center">
                            {this.state.status}
                        </h2>
                        <img src={ skull } alt="Skull logo" className="text-center" style={{height: "10em", width:"10em", margin: "auto"}}/>
                        <h3 className="share-tech-mono text-center">
                            <Translation>{t => t(body)}</Translation>
                        </h3>
                    </div>
                </div>
            </React.Fragment>
        );
    }
}
export default ErrorContent;
