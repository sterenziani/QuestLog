import api from './api';

const getGameRuns = async(gameId) => {
  try {
    const endpoint = `games/${gameId}/runs`;
    const response = await api.get(endpoint);
    // Parse links
    const data = response.headers.link;
    let parsed_data = {};
    let arrData = data.split(",");
    arrData.forEach(element => {
        let linkInfo = /<([^>]+)>;\s+rel="([^"]+)"/ig.exec(element);
        parsed_data[linkInfo[2]]=linkInfo[1];
    });
    const ret = {};
    ret['pagination'] = parsed_data;
    ret['content'] = response.data;
    ret['pageCount'] = response.headers["page-count"];
    return ret;
  } catch(err) {
    if(err.response) {
      return { status : err.response.status };
    } else {
      /* timeout */
    }
  }
}

const getUserRuns = async(userId) => {
  return getUserRunsPage(userId, 1);
}

const getUserRunsPage = async(userId, page) => {
  if(userId == null) {
    return [];
  }
  try {
    const endpoint = `users/${userId}/runs?page=${page}`;
    const response = await api.get(endpoint);
    // Parse links
    const data = response.headers.link;
    let parsed_data = {};
    let arrData = data.split(",");
    arrData.forEach(element => {
        let linkInfo = /<([^>]+)>;\s+rel="([^"]+)"/ig.exec(element);
        parsed_data[linkInfo[2]]=linkInfo[1];
    });
    const ret = {};
    ret['pagination'] = parsed_data;
    ret['content'] = response.data;
    ret['pageCount'] = response.headers["page-count"];
    return ret;
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
  if(userId == null) {
    return [];
  }
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

const getAllPlaystyles       = async () => {
  try {
    const endpoint = `games/runs/playstyles`;
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
  getGameTopRuns  : getGameTopRuns,
  getUserGameRuns : getUserGameRuns,
  getUserRuns     : getUserRuns,
  getUserRunsPage : getUserRunsPage,
  getAllPlaystyles : getAllPlaystyles,
}

export default RunService;
