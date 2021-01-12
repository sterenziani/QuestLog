import api from './api';

const getGenericContent       = async (endpoint) => {
    try {
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


const PaginationService = {
    getGenericContent     : getGenericContent,

}

export default PaginationService;
