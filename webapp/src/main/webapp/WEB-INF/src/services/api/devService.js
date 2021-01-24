import api from './api';
import PaginationService from './paginationService';

const getDevelopers = async() => {
  return getDevelopersPage(1);
}

const getDevelopersPage = async(page) => {
  try {
        const endpoint = `developers?page=${page}`;
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

const getBiggestDevelopers       = async () => {
  try {
    const endpoint = `developers/biggest`;
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

const getGameDevelopers     = async(gameId) => {
  try {
    const endpoint = `games/${gameId}/developers`;
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

const DeveloperService = {
    getDevelopers       : getDevelopers,
    getDevelopersPage   : getDevelopersPage,
    getGameDevelopers   : getGameDevelopers,
    getBiggestDevelopers : getBiggestDevelopers,
}

export default DeveloperService;
