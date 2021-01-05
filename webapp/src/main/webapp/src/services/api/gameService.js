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

const getUpcommingGames     = async() => {
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

const GameService = {
    getPopularGames     : getPopularGames,
    getUpcommingGames   : getUpcommingGames
}

export default GameService;