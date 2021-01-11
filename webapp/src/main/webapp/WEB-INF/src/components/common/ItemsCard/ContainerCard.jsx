import React, { Component } from 'react';
import GenericListItem from "../ListItem/GenericListItem";
import {Card, Container, Row} from "react-bootstrap";
import {Translation} from "react-i18next";
import "../../../../src/index.scss";

class ItemsCard extends Component {
    state = {
        label : this.props.label,
        items : this.props.items,
        limit : 0,
    };
spacing
    render() {
        let lower = this.state.label.toLowerCase();
        return (
            <Card style={{width: "100%"}} className="m-5 bg-light-grey right-wave left-wave" bordered style={{ borderBottomLeftRadius: 30, borderBottomRightRadius: 30 }}>
                <div className="card-header bg-very-dark text-white px-3 d-flex">
                    <h2 className="share-tech-mono">
                        <Translation>
                            {
                                t => t(this.state.label)
                            }
                        </Translation>
                    </h2>
                    <div className="ml-auto">
                        <div className="ml-auto">
                            <a className="btn btn-link text-white" href={`${process.env.PUBLIC_URL}/${lower}`}>
                                <Translation>
                                    {
                                        t => t("navigation.seeAll")
                                    }
                                </Translation>
                            </a>
                        </div>
                    </div>
                </div>
                <div className="card-body d-flex flex-wrap justify-content-center align-items-center">
                    <Container> <Row>{this.state.items.slice(0,10).map(p =>
                            <GenericListItem value={p.id} label={this.props.label} item={p}/>)}</Row></Container>
                </div>
            </Card>
        );
    }
}

export default ItemsCard;