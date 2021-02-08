import api from './api';
import AuthService from "./authService";
import Cookies from 'universal-cookie';
import PaginationService from './paginationService';
import { BAD_REQUEST, CONFLICT, TIMEOUT } from './apiConstants';

const gameServiceEndpoint   = 'games';

const register        = async (title, description, image, trailer, platforms, developers, publishers, genres, releaseDates) => {
    try {
        const new_game = {
            'title'        : title,
            'description'  : description,
            'cover'        : image,
            'trailer'      : trailer,
            'platforms'    : platforms,
            'developers'   : developers,
            'publishers'   : publishers,
            'genres'       : genres,
            'releaseDates' : releaseDates
        }
        const endpoint = gameServiceEndpoint + '/new_game';
        const response = await api.post(endpoint, new_game, {
            headers : {
                authorization  : AuthService.getToken(),
                'Content-Type' : 'application/json'
            }
        })
        return { status : response.status, data : response.data }
    } catch(err){
        if(err.response){
            if(err.response.status === BAD_REQUEST){
                return { status : err.response.status, errors : err.response.data.errors.map(e => e.split(" : ")[0]) }
            } else if (err.response.status === CONFLICT){
                return { status : err.response.status, errors : [err.response.data.field] }
            }
            return { status : err.response.status }
        } else {
            return { status : TIMEOUT }
        }
    }
}

const editGame        = async (id, title, description, image, trailer, platforms, developers, publishers, genres, releaseDates) => {
    try {
        const new_game_values = {
            'title'        : title,
            'description'  : description,
            'cover'        : image,
            'trailer'      : trailer,
            'platforms'    : platforms,
            'developers'   : developers,
            'publishers'   : publishers,
            'genres'       : genres,
            'releaseDates' : releaseDates
        }
        const endpoint = gameServiceEndpoint + `/${id}`;
        const response = await api.put(endpoint, new_game_values, {
            headers : {
                authorization  : AuthService.getToken(),
                'Content-Type' : 'application/json'
            }
        })
        return { status : response.status, data : response.data }
    } catch(err){
        if(err.response){
            if(err.response.status === BAD_REQUEST){
                return { status : err.response.status, errors : err.response.data.errors.map(e => e.split(" : ")[0]) }
            } else if (err.response.status === CONFLICT){
                return { status : err.response.status, errors : [err.response.data.field] }
            }
            return { status : err.response.status }
        } else {
            return { status : TIMEOUT }
        }
    }
}

const getPopularGames = async () => {
    try {
        const cookies = new Cookies();
        let currentBacklog = cookies.get('backlog')? cookies.get('backlog') : '';
        const endpoint = `${gameServiceEndpoint}/popular?backlog=${currentBacklog}`;
        const response = await api.get(endpoint, { headers: { 'Content-Type': 'application/json' , authorization: AuthService.getToken()}});
        return response.data;
    } catch(err) {
        if(err.response) {
            return { status : err.response.status };
        } else {
            return { status : TIMEOUT }
        }
    }
}

const searchGames = async(searchParams) => {
    return searchGamesPage(searchParams, 1);
}

const buildQueryParams = (searchParams) => {
    let params = "";
    // Parse searchParams
    if(searchParams){
        for (var key of Object.keys(searchParams)) {
            if((key === "platforms" || key === "genres") && searchParams[key]){
                for(var item of searchParams[key]){
                    params += "&"+key+"="+item;
                }
            }
            else if(searchParams[key]){
                params += "&"+key+"="+searchParams[key];
            }
        }
    }
    return params;
}

const searchGamesPage = async(searchParams, page) => {
  try {
        const cookies = new Cookies();
        let currentBacklog = cookies.get('backlog')? cookies.get('backlog') : '';
        if(searchParams.searchTerm == null){
            searchParams.searchTerm = '';
        }
        let params = buildQueryParams(searchParams) + `&backlog=${currentBacklog}`;
        const endpoint = `${gameServiceEndpoint}?page=${page}`+params;
        const response = await api.get(endpoint, { headers: { 'Content-Type': 'application/json' , authorization: AuthService.getToken()}});
        return PaginationService.parseResponsePaginationHeaders(response);
  } catch(err) {
    if(err.response) {
      return { status : err.response.status };
    } else {
      return { status : TIMEOUT }
    }
  }
}

const getUpcomingGames = async() => {
    try {
        const cookies = new Cookies();
        let currentBacklog = cookies.get('backlog')? cookies.get('backlog') : '';
        const endpoint = `${gameServiceEndpoint}/upcoming?backlog=${currentBacklog}`;
        const response = await api.get(endpoint, { headers: { 'Content-Type': 'application/json' , authorization: AuthService.getToken()}});
        return response.data;
    } catch(err) {
        if(err.response) {
            return { status : err.response.status };
        } else {
            return { status : TIMEOUT }
        }
    }
}

const getGameById = async(gameId)  => {
    try {
        const cookies = new Cookies();
        let currentBacklog = cookies.get('backlog')? cookies.get('backlog') : '';
        const endpoint = `${gameServiceEndpoint}/${gameId}?backlog=${currentBacklog}`;
        const response = await api.get(endpoint, { headers: { 'Content-Type': 'application/json' , authorization: AuthService.getToken()}});
        return response.data;
    } catch(err) {
        if(err.response) {
            return { status : err.response.status };
        } else {
            return { status : TIMEOUT }
        }
    }
}

const getGameReleaseDates = async(gameId) => {
  try {
      const cookies = new Cookies();
      let currentBacklog = cookies.get('backlog')? cookies.get('backlog') : '';
      const endpoint = `${gameServiceEndpoint}/${gameId}/release_dates?backlog=${currentBacklog}`;
      const response = await api.get(endpoint);
      return response.data;
  } catch(err) {
    if(err.response) {
      return { status : err.response.status };
    } else {
      return { status : TIMEOUT }
    }
  }
}

const deleteGame = async(gameId) => {
  try {
    const endpoint = `games/${gameId}`;
    const response = await api.delete(endpoint, { headers: { 'Content-Type': 'application/json' , authorization: AuthService.getToken()}});
    return response.data;
  } catch(err) {
    if(err.response) {
      return { status : err.response.status };
    } else {
      return { status : TIMEOUT }
    }
  }
}

const GameService = {
    register            : register,
    editGame            : editGame,
    getPopularGames     : getPopularGames,
    getUpcomingGames    : getUpcomingGames,
    getGameById         : getGameById,
    getGameReleaseDates : getGameReleaseDates,
    searchGames         : searchGames,
    searchGamesPage     : searchGamesPage,
    buildQueryParams    : buildQueryParams,
    deleteGame          : deleteGame
}

export default GameService;
