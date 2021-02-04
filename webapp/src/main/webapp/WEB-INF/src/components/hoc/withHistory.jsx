import React from 'react';
import { useHistory } from 'react-router-dom';
import { useLocation } from 'react-router-dom';

const withHistory = (WrappedComponent) => {
    return (props) => {
        const history = useHistory();
        const location = useLocation();
        const addCurrentLocationToHistory = () => {
            history.push(location.pathname)
        }
        return (
            <WrappedComponent history={ history } location={ location } addCurrentLocationToHistory={ addCurrentLocationToHistory } {...props} />
        )
    }
}

export default withHistory;