import api from './api';
import AuthService from "./authService";
import Cookies from 'universal-cookie';
import PaginationService from './paginationService';

const gameServiceEndpoint   = 'games';

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
            /* timeout */
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
            if((key == "platforms" || key == "genres") && searchParams[key]){
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
      /* timeout */
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
            /* timeout */
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
            /* timeout */
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
      /* timeout */
    }
  }
}

const GameService = {
    getPopularGames     : getPopularGames,
    getUpcomingGames    : getUpcomingGames,
    getGameById         : getGameById,
    getGameReleaseDates : getGameReleaseDates,
    searchGames         : searchGames,
    searchGamesPage     : searchGamesPage,
    buildQueryParams    : buildQueryParams
}

export default GameService;
