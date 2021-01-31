import React, { Component } from 'react';
import GenericListItem from "../ListItem/GenericListItem";
import {Card} from "react-bootstrap";
import {Translation} from "react-i18next";
import "../../../../src/index.scss";
import AnyButton from "../AnyButton/AnyButton";

class ItemsCard extends Component {
    state = {
        label : this.props.label,
        items : this.props.items,
        limit : this.props.limit,
    };

    render() {
        let lower = this.state.label.toLowerCase();
        return (
            <Card style={{width: "100%"}} className="m-5 bg-light-grey right-wave left-wave" bordered style={{ borderBottomLeftRadius: 30, borderBottomRightRadius: 30 }}>
                <div className="card-header bg-very-dark text-white px-3 d-flex">
                    <h2 className="share-tech-mono">
                        <Translation>{t => t("navigation." +lower)}</Translation>
                    </h2>
                    {this.state.items.length > this.state.limit ?
                        [<div className="ml-auto">
                        <div className="ml-auto">
                            <AnyButton variant="link" className="text-white" href={`/${lower}`} textKey="navigation.seeAll"/>
                        </div>
                    </div>] : null}
                </div>
                <div className="card-body d-flex flex-wrap justify-content-center align-items-center align-items-stretch">
                     {this.state.items.slice(0,this.state.limit).map(p =>
                            <GenericListItem value={p.id} category={this.props.label} item={p}/>)}
                </div>
            </Card>
        );
    }
}

export default ItemsCard;
