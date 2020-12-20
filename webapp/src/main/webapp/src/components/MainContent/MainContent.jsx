import React, { 
    Component, 
    Suspense 
} from 'react';
import { Translation } from 'react-i18next';

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