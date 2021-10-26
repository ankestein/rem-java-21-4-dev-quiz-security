import {createContext, useState} from "react";
import {useHistory} from "react-router-dom";
import axios from "axios";

export const AuthContext = createContext({})

export default function AuthProvider({children}) {
    const [token, setToken] = useState()
    const history = useHistory()

    const login = (credentials) => {
        return axios.post('/auth/login', credentials)
            .then(response => response.data)
            .then(token => setToken(token))
            .then(() => history.push("/"))
            .catch(error => console.error(error.message))
    }

    const loginWithGitHub = (code) => {
        return axios.post('/auth/github/login', code)
            .then(response => response.data)
            .then(token => setToken(token))
            .then(() => history.push("/"))
            .catch(error => console.error(error.message))

    }

    return (
        <AuthContext.Provider value={{token, login, loginWithGitHub}}>
            {children}
        </AuthContext.Provider>
    )
}