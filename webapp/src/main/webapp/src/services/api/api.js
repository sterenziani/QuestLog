import { create } from 'axios';

const api = create({
    baseURL : 'http://localhost:8080/webapp_war/api/',

})

const errorHandler = (error) => {
    return Promise.reject({ ...error })
}
  
const successHandler = (response) => response;

// Intercepting responses
api.interceptors.response.use(response => successHandler(response),
                                error => errorHandler(error));

export default api;