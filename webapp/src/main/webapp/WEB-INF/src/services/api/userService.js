import api from './api';

const getAllUsers = async(userId) => {
  try {
        const endpoint = `users`;
        const response = await api.get(endpoint);
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

const UserService = {
  getUserById   : getUserById,
  getAllUsers   : getAllUsers,
}

export default UserService;
