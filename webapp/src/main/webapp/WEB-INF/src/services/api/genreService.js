import api from './api';
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
      /* timeout */
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
      /* timeout */
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
      /* timeout */
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
