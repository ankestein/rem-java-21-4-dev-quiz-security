import Header from './components/Header'
import './App.css'
import {Route, Switch, useHistory} from 'react-router-dom'
import Homepage from './pages/Homepage'
import AddQuestion from './pages/Add-Question'
import useQuestions from './hooks/useQuestions'
import Play from "./pages/Play";
import {useEffect, useState} from "react";
import {getQuestion} from "./service/devQuizApiService";
import LoginPage from "./pages/LoginPage";
import axios from "axios";


function App() {

    const [playQuestion, setPlayQuestion] = useState()
    const [token, setToken] = useState()
    const {questions, saveQuestion} = useQuestions(token)
    const history = useHistory();

    const getNextQuestion = () => {
        getQuestion().then(result => {
            setPlayQuestion(result)
        })
    }

    useEffect(() => {
        getNextQuestion();
    }, []);


    const login = (credentials) => {
        return axios.post('/auth/login', credentials)
            .then(response => response.data)
            .then(token => setToken(token))
            .then(() => history.push("/"))
            .catch(error => console.error(error.message))
    }

     return (
        <div className="App">
            <Header/>
            <Switch>
                <Route path="/login">
                    <LoginPage login={login}/>
                </Route>
                <Route exact path="/">
                    <Homepage questions={questions}/>
                </Route>
                <Route exact path="/add-question">
                    <AddQuestion saveQuestion={saveQuestion}/>
                </Route>
                <Route path="/play">
                    {playQuestion && <Play question={playQuestion} playNext={getNextQuestion}/>}
                </Route>
            </Switch>
        </div>
    )
}

export default App
