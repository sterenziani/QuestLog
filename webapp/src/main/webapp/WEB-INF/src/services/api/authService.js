import api from './api';

const endpoint      = '/users';

const TokenStore    = {
    setToken    : token => localStorage.setItem('token', token),
    getToken    : ()    => localStorage.getItem('token'),
    removeToken : ()    => localStorage.removeItem('token')
}

const UserStore     = {
    setUser     : user  => localStorage.setItem('user', user),
    getUser     : ()    => localStorage.getItem('user'),
    removeUser  : ()    => localStorage.removeItem('user')
}

let user;
const getUser   = () => {
    if(!user){
        user = UserStore.getUser();
    }
    return user;
}
const deleteUser = () => {
    if(user){
        user = undefined;
    }
    UserStore.removeUser()
}

let token;
const getToken  = () => {
    if(!token){
        token = TokenStore.getToken();
    }
    return token;
}
const deleteToken = () => {
    if(token){
        token = undefined;
    }
    TokenStore.removeToken();
}

const logIn = async (username, password) => {
    try {
        const logInEndpoint = endpoint + '/login';

        const response      = await api.get(logInEndpoint, {
            headers : {
                authorization : 'Basic ' + btoa(username + ":" + password)
            }
        })
        
        token         = response.headers.authorization;
        user          = response.data;

        TokenStore.setToken(token);
        UserStore.setUser(user);

        return { status: response.status }
    } catch(e) {
        if (e.response) {
            return { status: e.response.status }
        } else {
            return { status: 408 }
        }
    }
}

const logOut = async () => {
    deleteUser();
    deleteToken();
    return { status: 200 }
}

const AuthService   = {
    logIn    : logIn,
    logOut   : logOut,
    getUser  : getUser,
    getToken : getToken
}

export default AuthService;