import api from './api';
import { TIMEOUT } from './apiConstants';

const getAllDevelopers       = async () => {
  try {
    const endpoint = `developers`;
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
  getAllDevelopers     : getAllDevelopers,
  getEveryDeveloper    : getEveryDeveloper,
  getGameDevelopers    : getGameDevelopers,
  getBiggestDevelopers : getBiggestDevelopers,
}

export default DeveloperService;
