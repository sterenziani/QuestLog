import React from 'react';
import { render, fireEvent, screen } from '@testing-library/react';
import { Link, BrowserRouter as Router } from 'react-router-dom';
import Pagination from '../components/common/Pagination/Pagination';

const searchParams = {platforms: [8, 4], genres: [2], scoreLeft: 10, minsRight: 30}

const setPage = () => {}

test('Pagination Current Page Test', () => {
      render(
        <Router>
          <Pagination url={'list'} page={3} totalPages={5} setPage={setPage} queryParams={searchParams}/>
        </Router>
      )
      const page2 = screen.getByText('2')
      const page3 = screen.getByText('3')
      const page4 = screen.getByText('4')
      expect(page2.className).toBe('btn btn-dark')
      expect(page3.className).toBe('btn btn-light disabled')
      expect(page4.className).toEqual(page2.className)
});

test('Pagination Redirection Test', () => {
    const mockSetPage = jest.fn();
    render(
        <Router>
            <Pagination url={'list'} page={3} totalPages={5} setPage={mockSetPage} queryParams={searchParams}/>
        </Router>
    )
    const page2 = screen.getByText('2')
    const page3 = screen.getByText('3')
    const page4 = screen.getByText('4')
    fireEvent.click(page2)
    const path = window.location.pathname + window.location.search
    expect(path).toBe('/list?page=2&platforms=8&platforms=4&genres=2&scoreLeft=10&minsRight=30')
    expect(mockSetPage).toHaveBeenCalledTimes(1);
});

test('Pagination Button Blocking Test', () => {
      render(
        <Router>
          <Pagination url={'list'} page={1} totalPages={1} setPage={setPage} queryParams={searchParams}/>
        </Router>
      )
      const prevPage = screen.getByText('navigation.pagination.prev')
      const page1 = screen.getByText('1')
      const nextPage = screen.getByText('navigation.pagination.next')
      expect(prevPage.className).toBe('disabled btn btn-light')
      expect(page1.className).toBe('btn btn-light disabled')
      expect(nextPage.className).toEqual(prevPage.className)
});

test('Pagination Button Enabling Test', () => {
      render(
        <Router>
          <Pagination url={'list'} page={1} totalPages={2} setPage={setPage} queryParams={searchParams}/>
        </Router>
      )
      const prevPage = screen.getByText('navigation.pagination.prev')
      const page1 = screen.getByText('1')
      const nextPage = screen.getByText('navigation.pagination.next')
      expect(prevPage.className).toBe('disabled btn btn-light')
      expect(page1.className).toBe('btn btn-light disabled')
      expect(nextPage.className).toBe('btn btn-dark')
});
