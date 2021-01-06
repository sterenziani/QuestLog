import api from './api';

const gameServiceEndpoint   = 'games';

const getPopularGames       = async () => {
    try {
        const endpoint = `${gameServiceEndpoint}/popular`;
        const response = await api.get(endpoint);
        return response.data;
    } catch(err) {
        if(err.response) {
            return { status : err.response.status };
        } else {
            /* timeout */
        }
    }
}

const getUpcomingGames     = async() => {
    try {
        const endpoint = `${gameServiceEndpoint}/upcoming`;
        const response = await api.get(endpoint);
        return response.data;
    } catch(err) {
        if(err.response) {
            return { status : err.response.status };
        } else {
            /* timeout */
        }
    }
}

const getGameById    = async(gameId)  => {
    try {
        const endpoint = `${gameServiceEndpoint}/${gameId}`;
        const response = await api.get(endpoint);
        return response.data;
    } catch(err) {
        if(err.response) {
            return { status : err.response.status };
        } else {
            /* timeout */
        }
    }
}

const GameService = {
    getPopularGames     : getPopularGames,
    getUpcomingGames   : getUpcomingGames,
    getGameById         : getGameById,
}

export default GameService;