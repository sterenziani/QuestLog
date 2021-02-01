import React from 'react';
import { render, fireEvent, screen } from '@testing-library/react';
import { BrowserRouter as Router } from 'react-router-dom';
import ReviewCard from '../components/common/GameDetails/ReviewCard';

const r1={enabled: true, body: ["It's a good game"], game: {id: 1, title: 'Super Mario Bros.'}, score: 85, id: 99,
        platform: {id: 15, shortName: "NES"}, postDate: "2021-01-21", user: {username: "QuestLogPAW", id: 1}}

const r2={enabled: false, body: ["I paid $60 for this", "I kinda regret it now"], game: {id: 2, title: 'Tetris'}, score: 46, id: 98,
        platform: {id: 15, shortName: "NES"}, postDate: "2021-01-20", user: {username: "QuestLogPAW", id: 1}}

test('Review Card Links Redirect Test', () => {
    render(<Router><ReviewCard review={r1}/></Router>)
    const userLink = screen.getByText('QuestLogPAW')
    const gameLink = screen.getByText('Super Mario Bros.')
    fireEvent.click(gameLink)
    expect(window.location.pathname).toBe('/games/'+r1.game.id)

    fireEvent.click(userLink)
    expect(window.location.pathname).toBe('/users/'+r1.user.id)
})

test('Review Card Display Test', () => {
    render(<Router><ReviewCard review={r1}/><ReviewCard review={r2}/></Router>)
    const gameLink1 = screen.getByText('Super Mario Bros.')
    const gameLink2 = screen.queryByText('Tetris')
    expect(gameLink1).toBeDefined()
    expect(gameLink2).toBeNull()
})
