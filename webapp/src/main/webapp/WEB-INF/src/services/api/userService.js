import api from './api';
import AuthService from "./authService";
import { CONFLICT, TIMEOUT, CREATED, OK } from './apiConstants';
import PaginationService from './paginationService';

const endpoint    = '/users'

const register    = async(username, password, email, locale) => {
  try {
    const registerEndpoint = endpoint + '/register';
    const newUser          = {
      "username" : username,
      "password" : password,
      "email"    : email,
      "locale"   : locale
    }
    const response         = await api.post(registerEndpoint, newUser, {
      headers : {
        contentType : "application/json"
      }
    });
    return { status : response.status }
  } catch(e) {
    if (e.response){
      if(e.response.status === CONFLICT){
        return {
          status    : CONFLICT,
          conflicts : e.response.data
        }
      }
      return { status : e.response.status }
    } else {
      return { status : TIMEOUT }
    }
  }
}

const searchUsers = async(term) => {
    if(term == null){
        term = '';
    }
    return searchUsersPage(term, 1);
}

const searchUsersPage = async(term, page) => {
  try {
        if(term == null){
            term = '';
        }
        const endpoint = `users?page=${page}&searchTerm=${term}`;
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

const getUserById = async(userId) => {
  try {
        const endpoint = `users/${userId}`;
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

const makeAdmin = async(userId) => {
  try {
        const endpoint = `users/${userId}/admin`;
        const response = await api.put(endpoint, {}, { headers: { 'Content-Type': 'application/json' , authorization: AuthService.getToken()}});
        return response.data;
  } catch(err) {
    if(err.response) {
      return { status : err.response.status };
    } else {
      /* timeout */
    }
  }
}

const removeAdmin = async(userId) => {
  try {
        const endpoint = `users/${userId}/admin`;
        const response = await api.delete(endpoint, { headers: { 'Content-Type': 'application/json' , authorization: AuthService.getToken()}});
        return response.data;
  } catch(err) {
    if(err.response) {
      return { status : err.response.status };
    } else {
      /* timeout */
    }
  }
}

const requestPasswordChangeToken = async(email) => {
    try {
          const endpoint = `users/tokens`;
          const response = await api.post(endpoint, {'email': email}, { headers: { 'Content-Type': 'application/json' , authorization: AuthService.getToken()}});
          return response;
    } catch(err) {
      if(err.response) {
        return { status : err.response.status };
      } else {
        /* timeout */
      }
    }
}

const getToken = async(token) => {
    try {
          const endpoint = `users/tokens/${token}`;
          const response = await api.get(endpoint, { headers: { 'Content-Type': 'application/json' , authorization: AuthService.getToken()}});
          return response;
    } catch(err) {
      if(err.response) {
        return { status : err.response.status };
      } else {
        /* timeout */
      }
    }
}

const changePassword = async(userId, token, newPassword) => {
    try {
          const endpoint = `users/${userId}/password`;
          const response = await api.put(endpoint, {'token': token, 'password': newPassword}, { headers: { 'Content-Type': 'application/json' , authorization: AuthService.getToken()}});
          return response;
    } catch(err) {
      if(err.response) {
        return { status : err.response.status };
      } else {
        /* timeout */
      }
    }
}

const UserService = {
  register      : register,
  getUserById   : getUserById,
  searchUsers   : searchUsers,
  searchUsersPage : searchUsersPage,
  makeAdmin     : makeAdmin,
  removeAdmin   : removeAdmin,
  requestPasswordChangeToken : requestPasswordChangeToken,
  getToken : getToken,
  changePassword : changePassword
}

export default UserService;
