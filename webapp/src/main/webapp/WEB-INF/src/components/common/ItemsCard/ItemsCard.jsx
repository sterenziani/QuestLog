import React, { Component } from 'react';
import GameListItem from "../../common/GameListItem/GameListItem";
import {Card, Container, Row} from "react-bootstrap";
import {Translation} from "react-i18next";
import "../../../../src/index.scss";

class ItemsCard extends Component {
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
                                t => t(this.state.label)
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
                    [<Container> <Row>{this.state.items.map(g =>
                        <GameListItem value={g.id} game={g}/>)}</Row></Container>]

                    }
                </div>
            </Card>
        );
    }
}

export default ItemsCard;