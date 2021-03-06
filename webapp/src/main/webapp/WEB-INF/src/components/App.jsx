//Libraries
import React, { Component, Suspense } from 'react';
import { BrowserRouter as Router } from 'react-router-dom';
import { LastLocationProvider } from 'react-router-last-location';
import Spinner from 'react-bootstrap/Spinner';

//Child components
import ContentSwitch from './common/ContentSwitch/ContentSwitch';
import Navigation from './common/Navigation/Navigation';
import AuthService from '../services/api/authService';

/*
 * There are two ways to declare components:
 *  - Functions
 *  - Classes that extend Component
 * 
 * Functions are considered stateless and Classes have
 * a state property. If state is not needed, it should
 * be declared as a stateless (function) component.
 */

class App extends Component {
  state = { 
    is_logging_in : true
  }

  autoLogin = async () => {
    await AuthService.logInWithStore()
    this.setState({
      is_logging_in : false
    })
  }

  componentDidMount() {
    this.autoLogin()
  }
  render() {
    if(this.state.is_logging_in){
      return <div style={{
        position: 'absolute', left: '50%', top: '50%',
        transform: 'translate(-50%, -50%)'}}>
            <Spinner animation="border" variant="primary" />
        </div>
    }
    return (
      
      /* 
       * React components can have only one route element.
       * Thus, a <React.Fragment> is needed when you wish
       * to have many.
       */

      /*
       * This is JSX, it may look like HTML but is actually
       * syntax sugar for JavaScript.
       */
      <Router basename={process.env.PUBLIC_URL}>
        <LastLocationProvider>
          <Suspense fallback="Loading...">
            <Navigation />
            <ContentSwitch />
          </Suspense>
        </LastLocationProvider>
      </Router>
     );
  }
}

export default App;