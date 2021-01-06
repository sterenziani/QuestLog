import React, { Component } from 'react';
import { Helmet, HelmetProvider } from 'react-helmet-async';
import axios from "axios";

class GameDetailsPage extends Component {
    state = {
        id : null,
        game: null,
        loading : true,
    };

    componentDidMount = () => {
        let id = this.props.match.params.id;

        axios.get('http://localhost:8080/webapp/api/games/' + id)
            .then(response => {
                const game = response.data;
                console.log("HOLAAA")
                console.log(response.data)
                console.log("CHAU")

                this.setState({
                    game,
                    loading : false
                });
            })
    }

    render() {
        return (
            <React.Fragment>
                <HelmetProvider>
                    <Helmet>
                        <title>QuestLog - Game Details</title>
                    </Helmet>
                </HelmetProvider>
                <p>{this.props.match.params.id}</p>
            </React.Fragment>
        );
    }
}
 
export default GameDetailsPage;