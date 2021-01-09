import api from './api';


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
  getGamePublishers   : getGamePublishers,
}

export default PublisherService;
