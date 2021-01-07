import React, { Component } from 'react';
import { Helmet, HelmetProvider } from 'react-helmet-async';
import GameService from "../../../services/api/gameService";
import Spinner from 'react-bootstrap/Spinner';
import GameListItem from '../../common/GameListItem/GameListItem';


class IndexPage extends Component {
    state = {
        popularGames : [],
        upcomingGames : [],
        loading : true,
    };

    componentWillMount() {
        const fetchPop = GameService.getPopularGames();
        const fetchUp = GameService.getUpcomingGames();

        Promise.all([ fetchPop, fetchUp ]).then((responses) => {
            this.setState({
                loading: false,
                popularGames: responses[0],
                upcomingGames : responses[1],
            });
        });
        console.log(this.state.upcomingGames);
    }

    render() {
        if (this.state.loading === true) {
            return <Spinner animation="border" variant="primary" />;
        }
        return (  
            <React.Fragment>
                <HelmetProvider>
                    <Helmet>
                        <title>QuestLog</title>
                    </Helmet>
                </HelmetProvider>
                <ul>
                    {this.state.upcomingGames.map(g =>
                        <GameListItem value={g.id} game={g}  />)}
                </ul>
            </React.Fragment>
        );
    }
}
 
export default IndexPage;