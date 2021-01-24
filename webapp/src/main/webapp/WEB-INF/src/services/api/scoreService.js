import api from './api';
import {OK, TIMEOUT} from "./apiConstants";
import AuthService from "./authService";
import PaginationService from './paginationService';

const rateGame = async(gameId, score) => {
  try {
    const endpoint = `games/${gameId}/new_score`;
    const json = JSON.stringify({"score": score});
    const response = await api.post(endpoint, json, { headers: { 'Content-Type': 'application/json' , authorization: AuthService.getToken()}});
    return response;
  } catch(err) {
    if(err.response) {
      return { status : err.response.status };
    } else {
      /* timeout */
    }
  }
}

const getUserGameScore       = async (userId, gameId) => {
  if(userId == null) {
    return [];
  }
  try {
    const endpoint = `users/${userId}/scores/${gameId}`;
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
    return PaginationService.parseResponsePaginationHeaders(response);
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
    return PaginationService.parseResponsePaginationHeaders(response);
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
  getUserGameScore : getUserGameScore,
  getGameScores : getGameScores,
  getUserScores : getUserScores,
  getGameScoresPage : getGameScoresPage,
  getUserScoresPage : getUserScoresPage
}

export default ScoreService;
