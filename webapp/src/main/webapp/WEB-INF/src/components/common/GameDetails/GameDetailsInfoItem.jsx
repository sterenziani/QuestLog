import React, { Component } from 'react';

class GameDetailsInfoItem extends Component {
    state = {

    };

    render() {
        return (
            <div className="game-details-info-item">
                <div className="bg-dark text-center"><strong>{this.props.title}</strong></div>
                <div className="bg-primary text-center">
                    {this.props.items.map(item => (<ul className="px-0 m-0">{item}<br/></ul>))}
                </div>
            </div>
        )
    }
}

export default GameDetailsInfoItem;
