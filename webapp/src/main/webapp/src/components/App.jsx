//Libraries
import React, { Component } from 'react';
import { 
  BrowserRouter as Router 
} from 'react-router-dom';

//Child components
import ContentSwitch from './common/ContentSwitch/ContentSwitch';
import Navigation from './common/Navigation/Navigation';

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
  state = {  }
  render() {
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

      <Router>
        <Navigation />
        <ContentSwitch />
      </Router>
     );
  }
}
 
export default App;