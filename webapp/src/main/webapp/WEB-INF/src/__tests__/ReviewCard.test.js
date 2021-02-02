import React from 'react';
import { render, fireEvent, screen } from '@testing-library/react';
import { BrowserRouter as Router } from 'react-router-dom';
import ReviewCard from '../components/common/GameDetails/ReviewCard';

const r1={enabled: true, body: ["It's a good game"], game: {id: 1, title: 'Super Mario Bros.'}, score: 85, id: 99,
        platform: {id: 15, shortName: "NES"}, postDate: "2021-01-21", user: {username: "QuestLogPAW", id: 1}}

const r2={enabled: false, body: ["I paid $60 for this", "I kinda regret it now"], game: {id: 2, title: 'Tetris'}, score: 46, id: 98,
        platform: {id: 15, shortName: "NES"}, postDate: "2021-01-20", user: {username: "QuestLogPAW", id: 1}}

const r3={enabled: true, game: {id: 2, title: 'Tetris'}, score: 46, id: 98,
        body: ["This review is nothing but an example", "It is only being used to test this component", "I'm sure you have better things to do than be reading this..."],
        platform: {id: 15, shortName: "NES"}, postDate: "2021-01-20", user: {username: "QuestLogPAW", id: 1}}

const user = {username:"QuestLog", admin:true}

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

test('Review Card Remove Button Display Test', () => {
    render(<Router><ReviewCard user={user} userIsLoggedIn={true} review={r1}/></Router>)
    const deleteButton = screen.getByRole('button')
    expect(deleteButton).toBeDefined()
    expect(deleteButton.className).toBe('btn btn-danger')
    expect(deleteButton.innerHTML).toBe('reviews.delete')
})

test('Review Card Count Lines Test', () => {
    render(<Router><ReviewCard review={r3}/></Router>)
    const lines = screen.queryAllByTestId("review-line")
    expect(lines.length).toEqual(r3.body.length)
    expect(lines[0].innerHTML).toEqual('<p>'+r3.body[0]+'</p>')
    expect(lines[1].innerHTML).toEqual('<p>'+r3.body[1]+'</p>')
    expect(lines[2].innerHTML).toEqual('<p>'+r3.body[2]+'</p>')
})
