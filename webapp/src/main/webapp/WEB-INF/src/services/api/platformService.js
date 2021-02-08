import api from './api';
import { TIMEOUT } from './apiConstants';

import PaginationService from './paginationService';

const getAllPlatforms = async () => {
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

const getPlatforms = async() => {
  return getPlatformsPage(1);
}

const getPlatformsPage = async(page) => {
  try {
        const endpoint = `platforms?page=${page}`;
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

const getBiggestPlatforms       = async () => {
  try {
    const endpoint = `platforms/biggest`;
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

const getGamePlatforms     = async(gameId) => {
  try {
    const endpoint = `games/${gameId}/platforms`;
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

const PlatformService = {
  getAllPlatforms       : getAllPlatforms,
  getGamePlatforms      : getGamePlatforms,
  getBiggestPlatforms   : getBiggestPlatforms,
  getPlatforms          : getPlatforms,
  getPlatformsPage      : getPlatformsPage
}

export default PlatformService;
