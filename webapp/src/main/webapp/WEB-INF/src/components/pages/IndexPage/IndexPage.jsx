import React, { Component } from 'react';
import { Helmet, HelmetProvider } from 'react-helmet-async';
import GameService from "../../../services/api/gameService";
import GameListItem from '../../common/GameListItem/GameListItem';

class IndexPage extends Component {
    state = {
        popularGames : [],
        upcomingGames : []
    };

    componentDidMount() {
        const popularRes = GameService.getPopularGames();
        const upcomingRes = GameService.getUpcomingGames();
        this.setState({
            popularGames : popularRes.data,
            upcomingGames : upcomingRes.data
        })
        console.log(popularRes);
        console.log(this.popularGames);
    }

    render() { 
        return (  
            <React.Fragment>
                <HelmetProvider>
                    <Helmet>
                        <title>QuestLog</title>
                    </Helmet>
                </HelmetProvider>
                <GameListItem>
                    
                </GameListItem>
            </React.Fragment>
        );
    }
}
 
export default IndexPage;