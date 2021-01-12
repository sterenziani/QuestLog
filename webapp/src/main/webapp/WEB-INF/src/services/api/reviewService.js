import api from './api';

const getGameReviews = async(gameId) => {
  try {
        const endpoint = `games/${gameId}/reviews`;
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
        return ret;
  } catch(err) {
    if(err.response) {
      return { status : err.response.status };
    } else {
      /* timeout */
    }
  }
}

const getGameReviewsPage = async(gameId, page) => {
  try {
    const endpoint = `games/${gameId}/reviews?page=${page}`;
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

const getUserGameReviews = async(userId, gameId) => {
  try {
    const endpoint = `users/${userId}/reviews/${gameId}`;
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

const ReviewService = {
  getGameReviews : getGameReviews,
  getGameReviewsPage : getGameReviewsPage,
  getUserGameReviews : getUserGameReviews
}

export default ReviewService;
