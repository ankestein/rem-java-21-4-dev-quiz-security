import { addQuestion, getQuestions } from '../service/devQuizApiService'
import { useEffect, useState } from 'react'

export default function useQuestions(token) {
  const [questions, setQuestions] = useState([])

  const getAllQuestions = (token) => {
    getQuestions(token).then(result => setQuestions(result))
        .catch(err => console.error(err))
  }

  useEffect(() => {
    getAllQuestions(token)
  }, [token])

  const saveQuestion = newQuestion => {
    addQuestion(newQuestion, token).then(getAllQuestions(token))
  }
  return {
    getAllQuestions,
    saveQuestion,
    questions,
  }
}
