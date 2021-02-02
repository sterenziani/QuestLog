import React from 'react';
import { render, fireEvent, screen } from '@testing-library/react';
import { BrowserRouter as Router } from 'react-router-dom';
import ScoreSlider from '../components/common/GameDetails/ScoreSlider';

const g1 = {id: 1, title: "Super Mario Bros.", in_backlog: true, votes: 5, score: 88}
const g2 = {id: 2, title: "Tetris", in_backlog: false, votes: 0, score: 79}
const u1 = {username: "QuestLogPAW", id: 1}

test('Score Slider Scores Display Test', () => {
    const prevScore = 94
    const mockUpdateGameBacklogStatus = jest.fn();
    render(<Router>
                <ScoreSlider game={g1} userScore={prevScore} user={u1} onBacklogUpdate={mockUpdateGameBacklogStatus} userIsLoggedIn={true}/>
            </Router>)
    const avgBadge = screen.getByTestId('avg-score-badge')
    const userBadge = screen.getByTestId('user-score-badge')
    expect(parseInt(avgBadge.lastElementChild.innerHTML)).toBe(g1.score)
    expect(parseInt(userBadge.innerHTML)).toBe(prevScore)
})

test('Score Slider No-Scores Display Test', () => {
    const mockUpdateGameBacklogStatus = jest.fn();
    render(<Router>
                <ScoreSlider game={g2} user={u1} onBacklogUpdate={mockUpdateGameBacklogStatus} userIsLoggedIn={true}/>
            </Router>)
    const avgBadge = screen.getByTestId('avg-score-badge')
    const userBadge = screen.getByTestId('user-score-badge')
    expect(avgBadge.lastElementChild.innerHTML).toBe("score.notAvailable")
    expect(userBadge.innerHTML).toBe("-")
})


test('Score Slider Modal Opening Test', () => {
    const prevScore = 94
    const mockUpdateGameBacklogStatus = jest.fn();
    render(<Router>
                <ScoreSlider game={g1} userScore={prevScore} user={u1} onBacklogUpdate={mockUpdateGameBacklogStatus} userIsLoggedIn={true}/>
            </Router>)

    const button = screen.getByRole('button')
    fireEvent.click(button)
    const buttons = screen.queryAllByRole('button')
    expect(buttons.length).toBe(3) // El botón disabled no cuenta, solo los del modal
    expect(buttons[0].className).toBe("close")
    expect(buttons[1].className).toBe("btn btn-light")
    expect(buttons[2].className).toBe("btn btn-primary")
})

test('Score Slider Modal Skipping Test', () => {
    const prevScore = 94
    const mockUpdateGameBacklogStatus = jest.fn();
    render(<Router>
                <ScoreSlider game={g2} userScore={prevScore} user={u1} onBacklogUpdate={mockUpdateGameBacklogStatus} userIsLoggedIn={true}/>
            </Router>)

    const button = screen.getByRole('button')
    fireEvent.click(button)
    const buttons = screen.queryAllByRole('button')
    expect(buttons.length).toBe(1)
})
