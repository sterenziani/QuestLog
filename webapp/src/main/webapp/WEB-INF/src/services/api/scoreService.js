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

const ScoreService = {
  rateGame     : rateGame,
}

export default ScoreService;
