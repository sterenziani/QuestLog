import React, { Component } from 'react';
import { Form, FormControl, Button } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSearch } from '@fortawesome/free-solid-svg-icons';
import { withTranslation } from 'react-i18next';
import withRedirect from '../../hoc/withRedirect';
import { LinkContainer } from 'react-router-bootstrap';

class Search extends Component {

    state = {
        searchTerm : "",
        category : "1",
        className   : this.props.className + " d-flex no-lineheight",
        searchTypes : [
            { id : "1", trans : "game", urlParam : "game" },
            { id : "2", trans : "user", urlParam : "user" }
        ]
    }

    componentWillMount() {
        this.setState({searchTerm: ""});
    }

    getPath() {
        if(this.state.category === "1"){
            return "/gameSearch?searchTerm="+this.state.searchTerm
        }
        else if(this.state.category === "2"){
            return "/userSearch?searchTerm="+this.state.searchTerm
        }
    }

    onKeyUp = (e) => {
        if (e.charCode === 13) {
            e.preventDefault();
            this.btn.click();
        }
    }

    render() {
        const { t } = this.props
        return (
            <Form className={ this.state.className } inline>
                <Form.Control type="text" placeholder={t('search.search')} className="mr-sm-2 flex-grow-1" onKeyPress={this.onKeyUp.bind(this)} onChange={e => this.setState({ searchTerm: e.target.value })}/>
                <Form.Control className="btn btn-dark mr-sm-2" as="select" onChange={e => this.setState({ category: e.target.value })}>
                {
                    this.state.searchTypes.map((s) =>
                        <option value={s.id} key={ s.id }>{t(`navigation.search.options.${s.trans}`)}</option>)
                }
                </Form.Control>
                <LinkContainer to={ this.getPath() }>
                    <Button ref={node => (this.btn = node)} variant="dark">
                        <FontAwesomeIcon className="mr-sm-2" icon={ faSearch }/>{t('navigation.search.btn')}
                    </Button>
                </LinkContainer>
            </Form>
        );
    }
}

export default withTranslation() (withRedirect(Search));
