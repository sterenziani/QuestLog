import api from './api';


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
  getGameDevelopers   : getGameDevelopers,
  getBiggestDevelopers : getBiggestDevelopers,
}

export default DeveloperService;
