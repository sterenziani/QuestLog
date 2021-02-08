import api from './api';
import AuthService from "./authService";
import PaginationService from './paginationService';
import {TIMEOUT} from './apiConstants';

const getGameRuns = async(gameId) => {
  try {
    const endpoint = `games/${gameId}/runs`;
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

const getUserRuns = async(userId, limit) => {
  return getUserRunsPage(userId, 1, limit);
}

const getUserRunsPage = async(userId, page, limit) => {
  if(userId == null) {
    return [];
  }
  try {
    const endpoint = `users/${userId}/runs?page=${page}&page_size=${limit}`;
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

const getGameTimes = async(gameId) => {
  try {
    const endpoint = `games/${gameId}/average_times`;
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

const getGameTopRuns = async(gameId) => {
  try {
    const endpoint = `games/${gameId}/top_runs`;
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
      return { status : TIMEOUT }
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
      return { status : TIMEOUT }
    }
  }
}

const addRun = async(gameId, hours, mins, secs, ps, plat) => {
  try {
    const endpoint = `games/${gameId}/new_run`;
    const time = secs + mins*60 + hours*3600;
    const newRun          = {
      "time" : time,
      "playstyle" : ps,
      "platform" : plat,
    }
    const response = await api.post(endpoint, newRun, { headers: { 'Content-Type': 'application/json' , authorization: AuthService.getToken()}});
    return response;
  } catch(err) {
    if(err.response) {
      return { status : err.response.status };
    } else {
      return { status : TIMEOUT }
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
  addRun        : addRun,
}

export default RunService;
