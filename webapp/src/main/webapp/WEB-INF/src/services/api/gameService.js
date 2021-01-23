import api from './api';
import AuthService from "./authService";

const gameServiceEndpoint   = 'games';

const getPopularGames = async () => {
    try {
        const endpoint = `${gameServiceEndpoint}/popular`;
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
        if(searchParams.searchTerm == null){
            searchParams.searchTerm = '';
        }
        let params = buildQueryParams(searchParams);
        const endpoint = `${gameServiceEndpoint}?page=${page}`+params;
        const response = await api.get(endpoint, { headers: { 'Content-Type': 'application/json' , authorization: AuthService.getToken()}});
        // Parse links
        const data = response.headers.link;
        let parsed_data = {};
        let arrData = data.split(",");
        arrData.forEach(element => {
            let linkInfo = /<([^>]+)>;\s+rel="([^"]+)"/ig.exec(element);
            parsed_data[linkInfo[2]]=linkInfo[1];
        });
        const ret = {};
        ret['pagination'] = parsed_data;
        ret['content'] = response.data;
        ret['pageCount'] = response.headers["page-count"];
        return ret;
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
        const endpoint = `${gameServiceEndpoint}/upcoming`;
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
        const endpoint = `${gameServiceEndpoint}/${gameId}`;
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
    const endpoint = `${gameServiceEndpoint}/${gameId}/release_dates`;
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
