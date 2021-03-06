import {useLocation} from "react-router-dom";
import {useContext, useEffect} from "react";
import {AuthContext} from "../context/AuthProvider";
import styled from "styled-components/macro";

export default function OAuthGitHubRedirect() {

    const queryParameter = new URLSearchParams(useLocation().search)
    const code = queryParameter.get("code")
    const {loginWithGitHub} = useContext(AuthContext)

    useEffect(() => {
        loginWithGitHub(code)
        //eslint-disable-next-line
    }, [code])

    return (
        <StyledDiv>
            <p>Logging in...</p>
            <p>{`The params are: ${queryParameter}`}</p>
            <p>{`The code is: ${code}`}</p>
        </StyledDiv>
    )
}

const StyledDiv = styled.div`
  text-align: center;
  font-family: sans-serif;
`