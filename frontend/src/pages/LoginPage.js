import {useState} from "react";
import {postUser} from "../service/devQuizApiService";
import styled from "styled-components/macro";

export default function LoginPage() {
    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')
    //const [token, setToken] = useState('')

    const handleClick = event => {
        event.preventDefault()
        const userData = {username, password}
        if (!userData) {
            return
        }
        postUser(userData)
            .then(token => console.log(token))
            //.then(token => setToken(token))
            //.then(console.log(token))

        setUsername('')
        setPassword('')
    }
//console.log(`token: ${token}`)

    return (
            <Form onSubmit={handleClick}>
                <Input type="text"
                       value={username}
                       placeholder="Enter username"
                       required="required"
                       onChange={event => setUsername(event.target.value)}/>
                <Input type="text"
                       value={password}
                       placeholder="Enter password"
                       required="required"
                       onChange={event => setPassword(event.target.value)}/>
                <button>Sign in</button>
            </Form>
    )


}

const Form = styled.form`
  width: 200px;
  margin: 40px auto;
  display: flex;
  flex-direction: column;
  gap: 40px;
  text-align: center;
`

const Input = styled.input`
display: flex;
`