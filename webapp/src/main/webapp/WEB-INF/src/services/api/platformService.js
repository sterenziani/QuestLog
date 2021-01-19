import api from './api';


const getAllPlatforms       = async () => {
  try {
    const endpoint = `platforms`;
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

const getEveryPlatform = async () => {
  try {
    const endpoint = `platforms?page_size=9999`;
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
