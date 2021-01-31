import { TIMEOUT } from "./apiConstants";
import api from './api';

const endpoint = "regions"

const getEveryRegion = async () => {
    try {
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

const RegionService = {
    getEveryRegion : getEveryRegion
}

export default RegionService;