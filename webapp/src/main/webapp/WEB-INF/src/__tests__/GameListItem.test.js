import React from 'react';
import { render, fireEvent, screen } from '@testing-library/react';
import { BrowserRouter as Router } from 'react-router-dom';
import GameListItem from '../components/common/ListItem/GameListItem';

const g1 = {id: 1, title: "Super Mario Bros.", in_backlog: false}
const g2 = {id: 2, title: "Tetris", in_backlog: true}

test('Backlog Button Load Color Test', () => {
    render(<Router>
                <GameListItem key={g1.id} value={g1.id} game={g1}/>
                <GameListItem key={g2.id} value={g2.id} game={g2}/>
            </Router>);
    const [button1, button2] = screen.getAllByRole('button')
    const class1 = button1.className
    const class2 = button2.className
    expect(class1).toBe('btn btn-btn btn-outline-success')
    expect(class2).toBe('btn btn-btn btn-outline-danger')
});

test('Backlog Button Click Test', () => {
    render(<Router><GameListItem key={g1.id} value={g1.id} game={g1}/></Router>);
    const button1 = screen.getByRole('button')
    const class1 = button1.className
    expect(class1).toBe('btn btn-btn btn-outline-success')
    expect(g1.in_backlog).toBeFalsy()

    fireEvent.click(button1)
    const button2 = screen.getByRole('button')
    const class2 = button2.className
    expect(class2).toBe('btn btn-btn btn-outline-danger')
    expect(g1.in_backlog).toBeTruthy()
});

test('Game Card Click Test', () => {
    render(<Router><GameListItem key={g1.id} value={g1.id} game={g1}/></Router>);
    const card = screen.getByText('Super Mario Bros.')
    fireEvent.click(card.parentNode)
    expect(window.location.pathname).toBe('/games/'+g1.id)
});
