import api from './api';
import AuthService from "./authService";
import Cookies from 'universal-cookie';

const getGenericContent = async (endpoint) => {
    try {
        const cookies = new Cookies();
        let currentBacklog = cookies.get('backlog')? cookies.get('backlog') : '';
        const response = await api.get(endpoint + `?backlog=${currentBacklog}`, { headers: { 'Content-Type': 'application/json' , authorization: AuthService.getToken()}});
        const data = response.headers.link;
        let ret = {};
        if(data){
            let parsed_data = {};
            let arrData = data.split(",");
            arrData.forEach(element => {
                let linkInfo = /<([^>]+)>;\s+rel="([^"]+)"/ig.exec(element);
                parsed_data[linkInfo[2]]=linkInfo[1];
            });
            ret['pagination'] = parsed_data;
        }
        ret['content'] = response.data;
        ret['pageCount'] = response.headers["page-count"];
        ret['totalCount'] = response.headers["total-count"];
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
        const cookies = new Cookies();
        let currentBacklog = cookies.get('backlog')? cookies.get('backlog') : '';
        const response = await api.get(endpoint +"?page=" +page + `&backlog=${currentBacklog}`, { headers: { 'Content-Type': 'application/json' , authorization: AuthService.getToken()}});
        const data = response.headers.link;
        let ret = {};
        if(data){
            let parsed_data = {};
            let arrData = data.split(",");
            arrData.forEach(element => {
                let linkInfo = /<([^>]+)>;\s+rel="([^"]+)"/ig.exec(element);
                parsed_data[linkInfo[2]]=linkInfo[1];
            });
            ret['pagination'] = parsed_data;
        }
        ret['content'] = response.data;
        ret['pageCount'] = response.headers["page-count"];
        ret['totalCount'] = response.headers["total-count"];
        return ret;
    } catch(err) {
        if(err.response) {
            return { status : err.response.status };
        } else {
            /* timeout */
        }
    }
}

const parseResponsePaginationHeaders = (response) => {
    const data = response.headers.link;
    let ret = {};
    if(data){
        let parsed_data = {};
        let arrData = data.split(",");
        arrData.forEach(element => {
            let linkInfo = /<([^>]+)>;\s+rel="([^"]+)"/ig.exec(element);
            parsed_data[linkInfo[2]]=linkInfo[1];
        });
                ret['pagination'] = parsed_data;
    }
    ret['content'] = response.data;
    ret['pageCount'] = response.headers["page-count"];
    ret['totalCount'] = response.headers["total-count"];
    return ret;
}


const PaginationService = {
    getGenericContent     : getGenericContent,
    getGenericContentPage : getGenericContentPage,
    parseResponsePaginationHeaders : parseResponsePaginationHeaders
}

export default PaginationService;
