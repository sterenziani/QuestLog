import React, { Component } from 'react';
import GameListItem from "../ListItem/GameListItem";
import {Card, Row, Button} from "react-bootstrap";
import {Translation} from "react-i18next";
import "../../../../src/index.scss";
import AnyButton from "../AnyButton/AnyButton";

class GamesCard extends Component {
    state = {
        label : this.props.label,
        items : this.props.items? this.props.items : [],
        search : this.props.search,
        pagination : this.props.pagination? this.props.pagination : [],
        seeAllLink: this.props.seeAllLink,
    };

    componentWillReceiveProps(newProps) {
        this.setState({ items: newProps.items? newProps.items : [],
                        pagination: newProps.pagination? newProps.pagination : []});
    }

    render() {
        let empty = !this.state.items.length;
        return (
            <Card className="m-5 bg-light-grey" bordered style={{ borderBottomLeftRadius: 30, borderBottomRightRadius: 30 }}>
                <Card.Header className="bg-very-dark text-white px-3">
                    <Row className="px-3">
                        <h2 className="share-tech-mono">
                            <Translation>{t => t(this.state.label, {value: this.props.labelArgs})}</Translation>
                        </h2>
                        {
                            this.state.pagination.next ?
                            [<div className="ml-auto">
                            <div className="ml-auto">
                                <AnyButton variant="link" className="text-white" href={"/"+this.state.seeAllLink} textKey="navigation.seeAll"/>
                            </div>
                        </div>] : null}
                    </Row>
                    {this.state.search ?[
                    <Row className="px-3">
                        <div className="row ml-1 mr-auto my-auto">
                            <Translation>{t => t("search.resultsLabel", {value: this.props.totalCount})}</Translation>
                        </div>
                    </Row>] : null}
                </Card.Header>
                <Card.Body className="d-flex flex-wrap justify-content-center">
                    {empty? [<Translation>{t => t("games.lists.emptyList")}</Translation>]
                                :
                    [<Row className="justify-content-center">{this.state.items.map(g =>
                        <GameListItem value={g.id} game={g}/>)}</Row>]
                    }
                </Card.Body>
            </Card>
        );
    }
}

export default GamesCard;
