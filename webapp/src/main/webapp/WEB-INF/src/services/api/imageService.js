import api from './api';
import {TIMEOUT} from './apiConstants';

const imageServiceEndpoint   = 'images';

const getImage = async(imageName)  => {
    try {
        const endpoint = `${imageServiceEndpoint}/${imageName}`;
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

const getImageLink = async(imageName)  => {
    try {
        const endpoint = `${process.env.PUBLIC_URL}/api/${imageServiceEndpoint}/${imageName}`;
        return endpoint;
    } catch(err) {
        if(err.response) {
            return { status : err.response.status };
        } else {
            return { status : TIMEOUT }
        }
    }
}

const ImageService = {
    getImage     : getImage,
    getImageLink : getImageLink,
}

export default ImageService;