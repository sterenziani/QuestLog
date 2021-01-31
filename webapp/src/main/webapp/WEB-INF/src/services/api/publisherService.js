import api from './api';
import { TIMEOUT } from './apiConstants';
import PaginationService from './paginationService';

const getPublishers = async() => {
  return getPublishersPage(1);
}

const getPublishersPage = async(page) => {
  try {
        const endpoint = `publishers?page=${page}`;
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

const getEveryPublisher = async () => {
  try {
    const endpoint = `publishers?page_size=9999`;
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

const getBiggestPublishers = async () => {
  try {
    const endpoint = `publishers/biggest`;
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

const getGamePublishers     = async(gameId) => {
  try {
    const endpoint = `games/${gameId}/publishers`;
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

const PublisherService = {
  getEveryPublisher    : getEveryPublisher,
  getGamePublishers    : getGamePublishers,
  getBiggestPublishers : getBiggestPublishers,
  getPublishers     : getPublishers,
  getPublishersPage : getPublishersPage
}

export default PublisherService;
