import api from './api';

const getGameRuns = async(gameId) => {
  try {
    const endpoint = `games/${gameId}/runs`;
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

const getGameTimes = async(gameId) => {
  try {
    const endpoint = `games/${gameId}/average_times`;
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

const getGameTopRuns = async(gameId) => {
  try {
    const endpoint = `games/${gameId}/top_runs`;
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

const getUserGameRuns = async(userId, gameId) => {
  try {
    const endpoint = `users/${userId}/runs/${gameId}`;
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

const RunService = {
  getGameRuns  : getGameRuns,
  getGameTimes : getGameTimes,
  getGameTopRuns : getGameTopRuns,
  getUserGameRuns : getUserGameRuns
}

export default RunService;
