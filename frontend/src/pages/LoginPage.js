import {useState} from "react";
import styled from "styled-components/macro";

const initialCredentials = {
    username: "",
    password: ""
}

export default function LoginPage({login}) {

    const [credentials, setCredentials] = useState(initialCredentials)

    const handleChange = event => {
        setCredentials({...credentials, [event.target.name] : event.target.value})
    }

    const handleSubmit = event => {
        event.preventDefault()
        login(credentials)
    }


    return (
        <Form onSubmit={handleSubmit}>
            <label>Username
                <Input type="text"
                       value={credentials.username}
                       placeholder="Enter username"
                       required="required"
                       name="username"
                       onChange={handleChange}/>
            </label>

            <label>Password
                <Input type="text"
                       value={credentials.password}
                       placeholder="Enter password"
                       required="required"
                       name="password"
                       onChange={handleChange}/>
            </label>

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