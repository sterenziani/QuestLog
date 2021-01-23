import api from './api';
import AuthService from "./authService";
import Cookies from 'universal-cookie';

const getUserBacklog = async(userId) => {
    return getUserBacklogPage(userId, 1);
}

const getUserBacklogPage = async(userId, page) => {
  try {
        const cookies = new Cookies();
        let currentBacklog = cookies.get('backlog')? cookies.get('backlog') : '';
        const endpoint = `users/${userId}/backlog?page=${page}&backlog=${currentBacklog}`;
        const response = await api.get(endpoint, { headers: { 'Content-Type': 'application/json' , authorization: AuthService.getToken()}});
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

const getCurrentUserBacklog = async() => {
    return getCurrentUserBacklogPage(1);
}

const getCurrentUserBacklogPage = async(page) => {
  try {
      const cookies = new Cookies();
      let currentBacklog = cookies.get('backlog')? cookies.get('backlog') : '';
      const endpoint = `backlog?page=${page}&backlog=${currentBacklog}`;
      const response = await api.get(endpoint, { headers: { 'Content-Type': 'application/json' , authorization: AuthService.getToken()}});
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

const addGameToBacklog = async(gameId) => {
  try {
      const userData =  AuthService.getUserStore();
      if(userData.isLoggedIn){
          const endpoint = `backlog/${gameId}`;
          const response = await api.put(endpoint, {}, { headers: { 'Content-Type': 'application/json' , authorization: AuthService.getToken()}});
          return response;
      }
      const cookies = new Cookies();
      let currentBacklog = cookies.get('backlog')? cookies.get('backlog') : '';
      const endpoint = `backlog/${gameId}?backlog=${currentBacklog}`;
      const response = await api.put(endpoint, {}, { headers: { 'Content-Type': 'application/json' , authorization: AuthService.getToken()}});
      cookies.set('backlog', response.data.backlog, {path: '/'});
      return response;
  } catch(err) {
    if(err.response) {
      return { status : err.response.status };
    } else {
      /* timeout */
    }
  }
}

const removeGameFromBacklog = async(gameId) => {
    try {
        const userData =  AuthService.getUserStore();
        if(userData.isLoggedIn){
            const endpoint = `backlog/${gameId}`;
            const response = await api.delete(endpoint, { headers: { 'Content-Type': 'application/json' , authorization: AuthService.getToken()}});
            return response;
        }
        const cookies = new Cookies();
        let currentBacklog = cookies.get('backlog')? cookies.get('backlog') : '';
        const endpoint = `backlog/${gameId}?backlog=${currentBacklog}`;
        const response = await api.delete(endpoint, { headers: { 'Content-Type': 'application/json' , authorization: AuthService.getToken()}});
        cookies.set('backlog', response.data.backlog, {path: '/'});
        return response;
  } catch(err) {
    if(err.response) {
      return { status : err.response.status };
    } else {
      /* timeout */
    }
  }
}

const BacklogService = {
    getUserBacklog : getUserBacklog,
    getUserBacklogPage : getUserBacklogPage,
    addGameToBacklog : addGameToBacklog,
    removeGameFromBacklog : removeGameFromBacklog,
    getCurrentUserBacklog : getCurrentUserBacklog,
    getCurrentUserBacklogPage : getCurrentUserBacklogPage
}

export default BacklogService;
