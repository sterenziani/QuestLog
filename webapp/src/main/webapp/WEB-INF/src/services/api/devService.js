import api from './api';
import PaginationService from './paginationService';
import { TIMEOUT } from './apiConstants';

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
      return { status : TIMEOUT }
    }
  }
}

const getEveryDeveloper = async () => {
  try {
    const endpoint = `developers?page_size=9999`;
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

const getBiggestDevelopers       = async () => {
  try {
    const endpoint = `developers/biggest`;
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

const getGameDevelopers     = async(gameId) => {
  try {
    const endpoint = `games/${gameId}/developers`;
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

const DeveloperService = {
    getEveryDeveloper    : getEveryDeveloper,
    getGameDevelopers    : getGameDevelopers,
    getBiggestDevelopers : getBiggestDevelopers,
    getDevelopers       : getDevelopers,
    getDevelopersPage   : getDevelopersPage
}

export default DeveloperService;
