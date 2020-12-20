import React, { Component } from 'react';
import { Translation } from 'react-i18next';
import { Suspense } from 'react';

class MainContent extends Component {
    state = {  }
    render() { 
        return ( 
            <main>
                <Suspense fallback={"Loading..."}>
                    <Translation>
                        {
                            t => <h1>{t('welcome')}</h1>
                        }
                    </Translation>
                </Suspense>
            </main>
        );
    }
}
 
export default MainContent;