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
            <Button variant={this.getButtonType()}>
                <Translation>
                    {
                        t => t(this.getTranslation())
                    }
                </Translation>
            </Button>
        )
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