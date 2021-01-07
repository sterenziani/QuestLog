import React, { Component } from 'react';
import {
    Button
} from 'react-bootstrap';
import {
    Translation
} from 'react-i18next';

class BacklogButton extends Component {
    state = {
        added: true
    };

    render() {
        return (
            <Button bg="light-grey" onClick={this.backlogHandler} variant={this.getButtonType()}>
                <Translation>
                    {
                        t => t(this.getTranslation())
                    }
                </Translation>
            </Button>
        )
    }

    backlogHandler = () => {
        this.setState({added : this.state.added ? false : true});
        console.log(this.state.added);

    }

    getButtonType() {
        let btype = "btn btn-outline-";
        btype += this.state.added ? "danger" : "success";
        return btype;
    }

    getTranslation() {
        let translate = 'games.profile.';
        translate += this.state.added ? 'removeFromBacklog' : 'addToBacklog';
        return translate;
    }
}
export default BacklogButton;