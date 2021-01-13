import api from './api';

const getGenericContent = async (endpoint) => {
    try {
        const response = await api.get(endpoint);
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

const getGenericContentPage = async (endpoint, page) => {
    try {
        const response = await api.get(endpoint +"?page=" +page);
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
