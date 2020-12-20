import React, { Component } from 'react';
import { Translation } from 'react-i18next';
import i18n from '../services/i18n/i18n';
import Navigation from './Navigation/Navigation';
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
    console.log(i18n.language)
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

      <React.Fragment>
        <Navigation />
        <main>
          <Translation>
            {
              t => <h1>{t('welcome')}</h1>
            }
          </Translation>
        </main>
      </React.Fragment>
     );
  }
}
 
export default App;