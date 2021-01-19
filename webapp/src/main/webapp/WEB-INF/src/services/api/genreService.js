import api from './api';


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

const getEveryGenre = async () => {
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
  getEveryGenre : getEveryGenre
}

export default GenreService;
