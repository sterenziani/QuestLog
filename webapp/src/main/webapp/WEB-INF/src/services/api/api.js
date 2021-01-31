import { create } from 'axios';

let api = create({
    baseURL : `${process.env.PUBLIC_URL}/api/`,
    timeout : 10000
})

const errorHandler = (error) => {
    return Promise.reject({ ...error })
}
  
const successHandler = (response) => response;

// Intercepting responses
api.interceptors.response.use(response => successHandler(response),
                                error => errorHandler(error));

export default api;