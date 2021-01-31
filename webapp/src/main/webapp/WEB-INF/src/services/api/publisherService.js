import api from './api';
import { TIMEOUT } from './apiConstants';


const getAllPublishers       = async () => {
  try {
    const endpoint = `publishers`;
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

const getBiggestPublishers       = async () => {
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
  getAllPublishers     : getAllPublishers,
  getEveryPublisher    : getEveryPublisher,
  getGamePublishers    : getGamePublishers,
  getBiggestPublishers : getBiggestPublishers,
}

export default PublisherService;
