import api from './api';
import { TIMEOUT } from './apiConstants';

const getAllPlatforms       = async () => {
  try {
    const endpoint = `platforms`;
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

const getEveryPlatform = async () => {
  try {
    const endpoint = `platforms?page_size=9999`;
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

const getBiggestPlatforms       = async () => {
  try {
    const endpoint = `platforms/biggest`;
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

const getGamePlatforms     = async(gameId) => {
  try {
    const endpoint = `games/${gameId}/platforms`;
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

const PlatformService = {
  getAllPlatforms     : getAllPlatforms,
  getGamePlatforms   : getGamePlatforms,
  getBiggestPlatforms : getBiggestPlatforms,
  getEveryPlatform : getEveryPlatform
}

export default PlatformService;
