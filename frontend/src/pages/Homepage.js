import * as React from 'react'
import styled from 'styled-components'
import QuestionOverview from "../components/QuestionOverview";
import {Card} from "@mui/material";

function Homepage({ questions }) {

  const sendChosenId = () => {}

  return (
    <StyledCard variant="outlined">
      {questions.map(question => (
        <QuestionOverview question={question} key={question.id} sendChosenId={sendChosenId} />
      ))}
    </StyledCard>
  )
}
export default Homepage


const StyledCard = styled(Card) `
  display: flex;
  gap: 15px;
  flex-wrap: wrap;
  justify-content: center;
  && {
    color: steelblue;
  }
`

