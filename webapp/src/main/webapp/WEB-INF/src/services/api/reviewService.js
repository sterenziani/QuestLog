import api from './api';
import AuthService from "./authService";

const getGameReviews = async(gameId) => {
  return getGameReviewsPage(gameId, 1);
}

const getUserReviews = async(userId) => {
  return getUserReviewsPage(userId, 1);
}

const getGameReviewsPage = async(gameId, page) => {
  try {
    const endpoint = `games/${gameId}/reviews?page=${page}`;
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

const getUserReviewsPage = async(userId, page) => {
  if(userId == null) {
    return [];
  }
  try {
    const endpoint = `users/${userId}/reviews?page=${page}`;
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

const getUserGameReviews = async(userId, gameId) => {
  if(userId == null) {
    return [];
  }
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

const addReview = async(gameId, score, platform, body) => {
  try {
    const endpoint = `games/${gameId}/new_review`;
    const newReview = {
      "platform" : platform,
      "score" : score,
      "body" : body,
    }
    const response = await api.post(endpoint, newReview, { headers: { 'Content-Type': 'application/json' , authorization: AuthService.getToken()}});
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
  getGameReviews     : getGameReviews,
  getGameReviewsPage : getGameReviewsPage,
  getUserGameReviews : getUserGameReviews,
  getUserReviews     : getUserReviews,
  getUserReviewsPage : getUserReviewsPage,
  addReview : addReview
}

export default ReviewService;
