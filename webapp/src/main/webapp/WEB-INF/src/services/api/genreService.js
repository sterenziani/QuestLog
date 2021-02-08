import api from './api';
import { TIMEOUT } from './apiConstants';

import PaginationService from './paginationService';

const getAllGenres       = async () => {
  try {
    const endpoint = `genres?page_size=9999`;
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

const getGenres = async() => {
  return getGenresPage(1);
}

const getGenresPage = async(page) => {
  try {
        const endpoint = `genres?page=${page}`;
        const response = await api.get(endpoint);
        return PaginationService.parseResponsePaginationHeaders(response);
  } catch(err) {
    if(err.response) {
      return { status : err.response.status };
    } else {
      return { status : TIMEOUT }
    }
  }
}

const getGameGenres     = async(gameId) => {
  try {
    const endpoint = `games/${gameId}/genres`;
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

const GenreService = {
  getAllGenres     : getAllGenres,
  getGameGenres   : getGameGenres,
  getGenres : getGenres,
  getGenresPage : getGenresPage
}

export default GenreService;
