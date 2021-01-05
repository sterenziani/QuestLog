import React, { Component } from 'react';
import {
    Form,
    FormControl,
    Button
} from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSearch } from '@fortawesome/free-solid-svg-icons';
import { withTranslation } from 'react-i18next';

class Search extends Component {
    
    state = { 
        className   : this.props.className + " d-flex no-lineheight",
        searchTypes : [
            { id : 1, trans : "game", urlParam : "game" },
            { id : 2, trans : "user", urlParam : "user" }
        ]
    }

    render() {
        const { t } = this.props
        return (  
            <Form className={ this.state.className } inline>
                <FormControl type="text" placeholder="Search" className="mr-sm-2 flex-grow-1" />
                <Form.Control className="btn btn-dark mr-sm-2" as="select">
                {
                    this.state.searchTypes.map((s) =>
                        <option key={ s.id }>
                            {t(`navigation.search.options.${s.trans}`)}
                        </option>
                    )
                }
                </Form.Control>
                <Button variant="dark">
                    <FontAwesomeIcon className="mr-sm-2" icon={ faSearch }/>
                    {
                        t('navigation.search.btn')
                    }
                </Button>
            </Form>
        );
    }
}
 
export default withTranslation()(Search);