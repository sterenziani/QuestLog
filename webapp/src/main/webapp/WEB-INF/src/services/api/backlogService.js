import api from './api';
import AuthService from "./authService";
import Cookies from 'universal-cookie';
import PaginationService from './paginationService';

const getUserBacklog = async(userId) => {
    return getUserBacklogPage(userId, 1);
}

const getUserBacklogPage = async(userId, page) => {
  try {
        const cookies = new Cookies();
        let currentBacklog = cookies.get('backlog')? cookies.get('backlog') : '';
        const endpoint = `users/${userId}/backlog?page=${page}&backlog=${currentBacklog}`;
        const response = await api.get(endpoint, { headers: { 'Content-Type': 'application/json' , authorization: AuthService.getToken()}});
        return PaginationService.parseResponsePaginationHeaders(response);
  } catch(err) {
    if(err.response) {
      return { status : err.response.status };
    } else {
      /* timeout */
    }
  }
}

const getCurrentUserBacklogPreview = async(page_size) => {
    try {
        const cookies = new Cookies();
        let currentBacklog = cookies.get('backlog')? cookies.get('backlog') : '';
        const endpoint = `backlog?page_size=${page_size}&backlog=${currentBacklog}`;
        const response = await api.get(endpoint, { headers: { 'Content-Type': 'application/json' , authorization: AuthService.getToken()}});
        return PaginationService.parseResponsePaginationHeaders(response);
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
      return PaginationService.parseResponsePaginationHeaders(response);
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

const wipeAnonBacklog = () => {
    const cookies = new Cookies();
    cookies.set('backlog', '', {path: '/'});
}

const transferBacklog = async () => {
    const cookies = new Cookies();
    const currentBacklog = cookies.get('backlog')? cookies.get('backlog') : '';
    const endpoint = `backlog?backlog=${currentBacklog}`;
    const response = await api.put(endpoint, {}, { headers: { 'Content-Type': 'application/json' , authorization: AuthService.getToken()}});
    cookies.set('backlog', '', {path: '/'});
}

const isAnonBacklogEmpty = () => {
    const cookies = new Cookies();
    const currentBacklog = cookies.get('backlog')? cookies.get('backlog') : '';
    return (currentBacklog.length > 0)? false:true;
}

const BacklogService = {
    getUserBacklog : getUserBacklog,
    getUserBacklogPage : getUserBacklogPage,
    addGameToBacklog : addGameToBacklog,
    removeGameFromBacklog : removeGameFromBacklog,
    getCurrentUserBacklog : getCurrentUserBacklog,
    getCurrentUserBacklogPage : getCurrentUserBacklogPage,
    wipeAnonBacklog : wipeAnonBacklog,
    transferBacklog : transferBacklog,
    isAnonBacklogEmpty : isAnonBacklogEmpty,
    getCurrentUserBacklogPreview : getCurrentUserBacklogPreview
}

export default BacklogService;
