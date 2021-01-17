import React, { Component } from 'react';
import GameListItem from "../ListItem/GameListItem";
import {Card, Row} from "react-bootstrap";
import {Translation} from "react-i18next";
import "../../../../src/index.scss";

class GamesCard extends Component {
    state = {
        label : this.props.label,
        items : this.props.items,
    };

    render() {
        let empty = !this.state.items.length;
        return (
            <Card className="m-5 bg-light-grey" bordered style={{ borderBottomLeftRadius: 30, borderBottomRightRadius: 30 }}>
                <div className="card-header bg-very-dark text-white px-3">
                    <h2 className="share-tech-mono">
                        <Translation>
                            {
                                t => t(this.state.label, {value: this.props.labelArgs})
                            }
                        </Translation>
                    </h2>
                </div>
                <div className="card-body d-flex flex-wrap justify-content-center">
                    {empty
                        ? [<Translation>
                                    {
                                        t => t("games.lists.emptyList")
                                    }
                                </Translation>]
                                :
                    [<Row className="justify-content-center">{this.state.items.map(g =>
                        <GameListItem value={g.id} game={g}/>)}</Row>]
                    }
                </div>
            </Card>
        );
    }
}

export default GamesCard;
