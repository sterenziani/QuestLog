import React, { Component, lazy, Suspense } from 'react';
import {Card} from "react-bootstrap";
import {Translation} from "react-i18next";
import "../../../../src/index.scss";
import AnyButton from "../AnyButton/AnyButton";
import Spinner from "react-bootstrap/Spinner";
const GenericListItem = lazy(() => import("../ListItem/GenericListItem"));

class ItemsCard extends Component {
    state = {
        label : this.props.label,
        items : this.props.items,
        limit : this.props.limit,
    };

    render() {
        let lower = this.state.label.toLowerCase();
        return (
            <Card className="m-5 bg-light-grey right-wave left-wave" bordered style={{ borderBottomLeftRadius: 30, borderBottomRightRadius: 30 }}>
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
                         <Suspense fallback={<div style={{
                             position: 'absolute', left: '50%', top: '50%',
                             transform: 'translate(-50%, -50%)'}}>
                             <Spinner animation="border" variant="primary" />
                         </div>}>
                         <GenericListItem value={p.id} category={this.props.label} item={p}/>
                         </Suspense>)
                     }
                </div>
            </Card>
        );
    }
}

export default ItemsCard;
