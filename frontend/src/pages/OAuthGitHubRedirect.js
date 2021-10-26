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
    }, [code])

    console.log(`The params are: ${queryParameter}`)
    console.log(`The code is: ${code}`)
    console.log("test")

    return (
        <StyledDiv>
            <p>You are being redirected...</p>
            <p>{`The params are: ${queryParameter}`}</p>
            <p>{`The code is: ${code}`}</p>
        </StyledDiv>
    )
}

const StyledDiv = styled.div`
  text-align: center;
  font-family: sans-serif;
`