import api from './api';
import AuthService from "./authService";

const getGenericContent = async (endpoint) => {
    try {
        const response = await api.get(endpoint, { headers: { 'Content-Type': 'application/json' , authorization: AuthService.getToken()}});
        // Parse links
        const data = response.headers.link;
        let parsed_data = {};
        const ret = {};
        if(data)
        {
            let arrData = data.split(",");
            arrData.forEach(element => {
                let linkInfo = /<([^>]+)>;\s+rel="([^"]+)"/ig.exec(element);
                parsed_data[linkInfo[2]]=linkInfo[1];
            });
        }
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

const getGenericContentPage = async (endpoint, page) => {
    try {
        const response = await api.get(endpoint +"?page=" +page, { headers: { 'Content-Type': 'application/json' , authorization: AuthService.getToken()}});
        // Parse links
        const data = response.headers.link;
        let parsed_data = {};
        const ret = {};
        if(data)
        {
            let arrData = data.split(",");
            arrData.forEach(element => {
                let linkInfo = /<([^>]+)>;\s+rel="([^"]+)"/ig.exec(element);
                parsed_data[linkInfo[2]]=linkInfo[1];
            });
        }
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


const PaginationService = {
    getGenericContent     : getGenericContent,
    getGenericContentPage : getGenericContentPage

}

export default PaginationService;
