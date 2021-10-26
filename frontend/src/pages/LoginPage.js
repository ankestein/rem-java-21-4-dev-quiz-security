import {useContext, useState} from "react";
import styled from "styled-components/macro";
import {AuthContext} from "../context/AuthProvider";
import {useHistory} from "react-router-dom";
import {sendClientId} from "../service/devQuizApiService";

const initialCredentials = {
    username: "",
    password: ""
}

export default function LoginPage() {

    const [credentials, setCredentials] = useState(initialCredentials)
    const {login} = useContext(AuthContext)
    const history = useHistory();

    const handleChange = event => {
        setCredentials({...credentials, [event.target.name]: event.target.value})
    }

    const handleSubmit = event => {
        event.preventDefault()
        login(credentials)
    }

    const clientId = "20bd6360403e3e1711a8"

    const handleClick = () => {
        window.open("https://github.com/login/oauth/authorize?client_id=" + clientId)
    }

    return (
        <>
            <Form onSubmit={handleSubmit}>
                <label>Username
                    <input type="text"
                           value={credentials.username}
                           placeholder="Enter username"
                           required="required"
                           name="username"
                           onChange={handleChange}/>
                </label>

                <label>Password
                    <input type="password"
                           value={credentials.password}
                           placeholder="Enter password"
                           required="required"
                           name="password"
                           onChange={handleChange}/>
                </label>

                <button>Sign in</button>
            </Form>

            <button onClick={handleClick}>Log in with GitHub</button>

        </>


    )


}

const Form = styled.form`
  width: 175px;
  margin: 40px auto;
  display: flex;
  flex-direction: column;
  gap: 40px;
  text-align: center;
  font-family: sans-serif;
  padding: 0;
`