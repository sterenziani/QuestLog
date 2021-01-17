import api from './api';


const rateGame = async(gameId, score) => {
  try {
    const endpoint = `games/${gameId}/new_score`;
    const json = JSON.stringify({"score": score});
    const response = await api.post(endpoint, json, { headers: { 'Content-Type': 'application/json' }});
    return response.data;
  } catch(err) {
    if(err.response) {
      return { status : err.response.status };
    } else {
      /* timeout */
    }
  }
}

const getGameScores = async(gameId) => {
    return getGameScoresPage(gameId, 1);
}

const getUserScores = async(userId) => {
    return getUserScoresPage(userId, 1);
}

const getGameScoresPage = async(gameId, page) => {
  try {
    const endpoint = `games/${gameId}/scores?page=${page}`;
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

const getUserScoresPage = async(userId, page) => {
  try {
    const endpoint = `users/${userId}/scores?page=${page}`;
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

const ScoreService = {
  rateGame     : rateGame,
  getGameScores : getGameScores,
  getUserScores : getUserScores,
  getGameScoresPage : getGameScoresPage,
  getUserScoresPage : getUserScoresPage
}

export default ScoreService;
